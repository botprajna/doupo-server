"use strict";
// Offline evidence for the two rear-monster skill families; no production config changes.
const fs = require('fs');
const path = require('path');
const {extractWsMessages, framePayload, parseFrame, directionOf} = require('./har_decode');
const har = JSON.parse(fs.readFileSync('C:/Users/ppp/Desktop/imlj2-cn-gamewx.lansors.com_2026_09_07_19_03_22.har', 'utf8'));
const wanted = new Map([[2372,50762],[2403,50763],[5513,50762],[5554,50763]]);
const out = path.resolve(__dirname, '../dpcq-game-server/src/test/resources/later-monsters');
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
if (count !== wanted.size) throw Error('Missing monster fixtures');
const config = 'D:/doudi-resources/client-analysis/config-json/6.9.263/';
const kinds = JSON.parse(fs.readFileSync(config+'monsterskillconfig.json', 'utf8')).rows;
const skills = JSON.parse(fs.readFileSync(config+'skillfightconfig.json', 'utf8')).rows;
const evidence = kinds.filter(r => [13,121001,121002].includes(r.Id)).map(r => ({
    monsterId:r.Id, skills:r.Skills.map(id => ({id, radius:skills.find(s=>s.Id===id).CastRadius}))
}));
fs.writeFileSync(path.join(out,'skill-ranges.json'), JSON.stringify({version:'6.9.263',evidence},null,2)+'\n');
console.log('Exported '+count+' monster packets and 3 Luban skill profiles.');
