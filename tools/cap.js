// 鎶撳寘鏌ヨ灏忓伐鍏凤細node tools/cap.js <鍛戒护> [鍙傛暟]
const fs = require('fs');
const path = 'D:\\\u6293\u5305\u6570\u636e\\\u7b2c\u4e00\u6b21\u521b\u89d2\u8272.json';
const data = JSON.parse(fs.readFileSync(path, 'utf8'));
const msgs = data.messages;

const cmd = process.argv[2];

function brief(m) {
  return `${m.idx}\t${m.direction}\t${m.protocolId}\t${m.name || '?'}\t${JSON.stringify(m.decoded)}`;
}

function nameOf(m) {
  return m.name || '?';
}

if (cmd === 'range') {
  const a = Number(process.argv[3]);
  const b = Number(process.argv[4]);
  const filter = process.argv[5];
  for (const m of msgs) {
    if (m.idx < a || m.idx > b) continue;
    if (filter && !nameOf(m).includes(filter)) continue;
    console.log(brief(m));
  }
} else if (cmd === 'name') {
  const n = process.argv[3];
  const a = process.argv[4] ? Number(process.argv[4]) : 0;
  const b = process.argv[5] ? Number(process.argv[5]) : 1e9;
  for (const m of msgs) {
    if (m.idx < a || m.idx > b) continue;
    if (nameOf(m).includes(n)) console.log(brief(m));
  }
} else if (cmd === 'grep') {
  const n = process.argv[3];
  for (const m of msgs) {
    if (JSON.stringify(m.decoded || {}).includes(n)) console.log(brief(m));
  }
} else if (cmd === 'names') {
  const a = Number(process.argv[3]);
  const b = Number(process.argv[4]);
  for (const m of msgs) {
    if (m.idx < a || m.idx > b) continue;
    console.log(`${m.idx}\t${m.direction}\t${m.protocolId}\t${nameOf(m)}`);
  }
} else {
  console.log('usage: range a b [nameFilter] | name <name> [a b] | grep <text> | names a b');
}
