// Node 22+: node tools/ninth-guide-smoke.js --create-test-account
// Localhost only. Creates one isolated account; does not change the user's character.
const assert = require('node:assert/strict');
const { randomBytes } = require('node:crypto');
const { setTimeout: delay } = require('node:timers/promises');
if (!process.argv.includes('--create-test-account')) {
    throw new Error('Pass --create-test-account to authorize a new local test account.');
}

function varint(value) {
    let n = BigInt(value), bytes = [];
    do { bytes.push(Number(n & 127n) | (n > 127n ? 128 : 0)); n >>= 7n; } while (n);
    return Buffer.from(bytes);
}
const integer = (field, value) => Buffer.concat([varint(field * 8), varint(value)]);
const string = (field, value) => Buffer.concat([
    varint(field * 8 + 2), varint(Buffer.byteLength(value)), Buffer.from(value)]);
function float(field, value) {
    const b = Buffer.alloc(4); b.writeFloatLE(value);
    return Buffer.concat([varint(field * 8 + 5), b]);
}
// Decode only protobuf wire values; nested messages are decoded at their known fields.
function fields(buffer) {
    let pos = 0;
    const result = {};
    const read = () => {
        let value = 0n, shift = 0n, byte;
        do { byte = buffer[pos++]; value |= BigInt(byte & 127) << shift; shift += 7n; }
        while (byte & 128);
        return Number(value);
    };
    while (pos < buffer.length) {
        const tag = read(), field = tag >> 3, wire = tag & 7;
        let value;
        if (wire === 0) value = read();
        else if (wire === 1) { value = buffer.readDoubleLE(pos); pos += 8; }
        else if (wire === 5) { value = buffer.readFloatLE(pos); pos += 4; }
        else if (wire === 2) { const length = read(); value = buffer.subarray(pos, pos + length); pos += length; }
        else throw new Error(`Unsupported wire type ${wire}`);
        (result[field] ||= []).push(value);
    }
    return result;
}
async function main() {
    const account = '199' + String(Date.now()).slice(-8);
    const password = randomBytes(12).toString('hex');
    const registered = await fetch('http://127.0.0.1:18080/register', {
        method: 'POST', body: new URLSearchParams({ account, password, confirmPassword: password })
    }).then(r => r.text());
    assert(registered.includes('注册成功'), 'Local account registration failed');
    const login = await fetch('http://127.0.0.1:18080/login', {
        method: 'POST', headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ account, password })
    }).then(r => r.json());
    assert.equal(login.code, 0);
    const playerId = 100000000000 + Number(login.data.userId);
    console.log(`Created local smoke-test player ${playerId}`);
    const query = new URLSearchParams({ publisher: '37wan', gid: '1', pid: '1',
        clientVersion: '6.8.1', token: login.data.token, os: 'android' });
    const guide = await fetch('http://127.0.0.1:18080/login/v2/imlj2/37wan/client/login/guide?' + query)
        .then(r => r.json());
    assert.equal(guide.state, 0);
    const ws = new WebSocket('ws://127.0.0.1:19090/');
    ws.binaryType = 'arraybuffer';
    const packets = [];
    ws.addEventListener('message', event => {
        const b = Buffer.from(event.data);
        packets.push({ id: b.readInt32BE(4), data: fields(b.subarray(8)) });
    });
    const waitFor = async predicate => {
        const deadline = Date.now() + 45000;
        while (!predicate()) {
            if (Date.now() > deadline) throw new Error('Timed out waiting for captured protocol sequence');
            await delay(50);
        }
    };
    let authenticated = false;
    const send = (id, ...parts) => {
        const body = Buffer.concat(parts), header = Buffer.alloc(authenticated ? 12 : 8);
        header.writeInt32BE(header.length - 4 + body.length); header.writeInt32BE(id, 4);
        ws.send(Buffer.concat([header, body]));
    };
    try {
        await new Promise((resolve, reject) => {
            ws.addEventListener('open', resolve, { once: true });
            ws.addEventListener('error', reject, { once: true });
        });
        send(50051, string(1, account), integer(2, 1), string(5, guide.data.sign),
            integer(6, guide.data.time), integer(17, 3));
        await waitFor(() => packets.some(p => p.id === 50052));
        assert.equal(packets.find(p => p.id === 50052).data[1]?.[0] || 0, 0);
        authenticated = true;
        send(61973, integer(1, 10200405), float(4, 1299.3));
        await waitFor(() => packets.some(p => p.id === 50756));
        assert(!packets.some(p => p.id === 77066), 'Guide must not trigger on entry');
        send(61974, float(3, 1296.8));
        await waitFor(() => packets.some(p => p.id === 77066));
        const at = packets.findIndex(p => p.id === 77066);
        await waitFor(() => packets.length >= at + 3);
        assert.deepEqual(packets.slice(at - 2, at + 3).map(p => p.id),
            [50756, 50801, 77066, 50798, 50798]);
        assert.equal(packets[at].data[1][0], 10043);
        const unit = fields(packets[at - 2].data[2][0]);
        const attrs = fields(unit[2][0])[2].map(fields);
        assert.equal(attrs.find(a => a[1][0] === 103011)[2][0], 1);
        assert.equal(packets[at - 1].data[3][0], playerId * 1000 + 1);
        assert.equal(packets[at + 1].data[2]?.[0] || 0, 0);
        assert.equal(packets[at + 2].data[2][0], 80);
        const count = packets.length;
        await delay(2200);
        assert(!packets.slice(count).some(p => p.id === 50763), 'Boss attacked during dialogue pause');
        console.log('PASS: live Actor tick -> HP 1 -> visible refresh -> heal -> guide 10043 -> MP 0/80; paused');
        send(61975); // resume
        send(61972, integer(1, 10120110101), integer(2, playerId * 1000 + 505),
            float(5, 1296.8), float(9, 1299.3));
        await waitFor(() => packets.some(p => p.id === 50781));
        const finish = packets.findIndex(p => p.id === 50781);
        assert(packets.slice(at + 1, finish).some(p => p.id === 77066 && p.data[1][0] === 10044));
        assert.equal(packets.filter(p => p.id === 77066 && p.data[1][0] === 10043).length, 1);
        console.log('PASS: resume -> finisher -> guide 10044 -> fight result; guide 10043 sent once');
    } finally {
        // A failed probe must not leave a live test fight ticking indefinitely.
        if (authenticated && ws.readyState === WebSocket.OPEN) send(61975, integer(1, 1));
        ws.close();
    }
}
main().catch(error => { console.error(error); process.exitCode = 1; });
