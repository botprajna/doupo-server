"use strict";
// Extract only selected protocol bodies from the local official capture; no network access.
const fs = require('fs');
const path = require('path');
const {extractWsMessages, framePayload, parseFrame, directionOf} = require('./har_decode');
const har = JSON.parse(fs.readFileSync('C:/Users/ppp/Desktop/imlj2-cn-gamewx.lansors.com_2026_09_07_19_03_22.har', 'utf8'));
const wanted = new Map([[9009,75053],[9010,75088],[9015,75055],[9018,75067],[9019,75060],
    [9717,50402],[9733,75057],[9734,75060]]);
const out = path.resolve(__dirname, '../dpcq-game-server/src/test/resources/elixir');
fs.mkdirSync(out, {recursive:true});
let auth = false, count = 0;
extractWsMessages(har).forEach((message, i) => {
    const [payload] = framePayload(message);
    if (!payload) return;
    const frame = parseFrame(payload, directionOf(message), auth);
    if (frame.protocolId === 50052) auth = true;
    if (!wanted.has(i)) return;
    if (frame.protocolId !== wanted.get(i)) throw Error('Unexpected protocol at '+i);
    fs.writeFileSync(path.join(out, i+'-'+frame.protocolId+'.bin'), frame.body);
    count++;
});
if (count !== wanted.size) throw Error('Missing elixir fixtures');
console.log('Exported '+count+' elixir fixtures.');
