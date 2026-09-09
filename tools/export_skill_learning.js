"use strict";
// Reproducible offline exports from the 6.9.263 table and second capture.
const fs = require('fs');
const path = require('path');
const {extractWsMessages, framePayload, parseFrame, directionOf} = require('./har_decode');
const rows = JSON.parse(fs.readFileSync('D:/doudi-resources/client-analysis/config-json/6.9.263/newskillbaseconfig.json', 'utf8')).rows;
const skills = rows.filter(r => r.Id === r.BaseId).map(r => ({
    id:r.Id, quality:r.Quality, star:r.Star, activeNeed:r.ActiveNeed,
    skillUnlock:r.SkillUnlock, arcaneUnlock:r.ArcaneUnlock
}));
const configDir = path.resolve(__dirname, '../dpcq-game-server/src/main/resources/skill-learning');
fs.mkdirSync(configDir, {recursive:true});
fs.writeFileSync(path.join(configDir, 'base-config.json'), JSON.stringify({version:'6.9.263',skills},null,2)+'\n');
const fixtureDir = path.resolve(__dirname, '../dpcq-game-server/src/test/resources/skill-learning');
fs.mkdirSync(fixtureDir, {recursive:true});
const har = JSON.parse(fs.readFileSync('C:/Users/ppp/Desktop/imlj2-cn-gamewx.lansors.com_2026_09_07_19_03_22.har', 'utf8'));
const wanted = new Map([[1464,75011],[1465,75049],[1466,75039]]);
let authenticated = false, count = 0;
extractWsMessages(har).forEach((message,i) => {
    const [payload] = framePayload(message);
    if (!payload) return;
    const frame = parseFrame(payload, directionOf(message), authenticated);
    if (frame.protocolId === 50052) authenticated = true;
    if (!wanted.has(i)) return;
    if (frame.protocolId !== wanted.get(i)) throw Error('Unexpected protocol at '+i);
    fs.writeFileSync(path.join(fixtureDir, i+'-'+frame.protocolId+'.bin'), frame.body);
    count++;
});
if (count !== wanted.size) throw Error('Missing skill activation fixtures');
console.log('Exported '+skills.length+' base skill rows and '+count+' activation packets.');
