"use strict";
const fs = require('fs');
const path = require('path');
const [oldRoot, newRoot, output] = process.argv.slice(2);
if (!oldRoot || !newRoot || !output) throw Error('Usage: old-json new-json report.json');
function files(root, relative = '') {
  return fs.readdirSync(path.join(root, relative), {withFileTypes:true}).flatMap(e =>
    e.isDirectory() ? files(root, path.join(relative,e.name)) : e.name.endsWith('.json') ? [path.join(relative,e.name)] : []);
}
function canonical(x) {
  if (Array.isArray(x)) return x.map(canonical);
  if (x && typeof x === 'object') return Object.fromEntries(Object.keys(x).sort().map(k => [k,canonical(x[k])]));
  return x;
}
const same = (a,b) => JSON.stringify(canonical(a)) === JSON.stringify(canonical(b));
const load = (root,f) => JSON.parse(fs.readFileSync(path.join(root,f),'utf8'));
const oldFiles = new Set(files(oldRoot)), newFiles = new Set(files(newRoot));
const report = {oldRoot,newRoot,unchanged:[],changed:[],added:[],removed:[],focus:[]};
for (const f of newFiles) {
  if (!oldFiles.has(f)) { report.added.push(f); continue; }
  const a=load(oldRoot,f), b=load(newRoot,f);
  (same(a,b) ? report.unchanged : report.changed).push(f);
}
report.removed = [...oldFiles].filter(f => !newFiles.has(f));
const focus = [
  ['mainstageconfig','Id',r=>r.Id>=10100101 && r.Id<=10300801],
  ['taskconfig','TaskId',r=>r.TaskId>=200001 && r.TaskId<=200111],
  ['powertowerconfig','Id',r=>r.Id<=19],
  ['powertowerchapterconfig','Id',()=>true], ['powertowerrewardconfig','Id',()=>true],
  ['reputationconfig','Id',r=>r.Id>=1001 && r.Id<=1003],
  ['playerlevelconfig','Level',r=>r.Level<=17],
  ['playerstagetaskconfig','Id',()=>true],
  ['exppoolbaseconfig','Key',()=>true], ['exppoolelixirconfig','Id',()=>true],
  ['exppoolqualityconfig','Quality',()=>true], ['exppoolaccumulateconfig','Key',()=>true],
  ['newskilllotterypoolconfig','Id',()=>true],
  ['lotterytypeconfig','Id',()=>true], ['monsterskillconfig','Id',()=>true],
];
for(const [table,key,filter] of focus) {
  const f=table+'.json';
  if(!oldFiles.has(f)||!newFiles.has(f)) { report.focus.push({table,missing:true});continue; }
  const a=load(oldRoot,f).rows.filter(filter), b=load(newRoot,f).rows.filter(filter);
  if([...a,...b].some(r=>r[key]===undefined)) { report.focus.push({table,key,invalidKey:true});continue; }
  const changes=[];
  for(const r of b) {
    const old=a.find(o=>o[key]===r[key]);
    if(!old) { changes.push({id:r[key],added:r});continue; }
    for(const k of new Set([...Object.keys(old),...Object.keys(r)])) {
      if(!same(old[k],r[k])) changes.push({id:r[key],field:k,before:old[k],after:r[k]});
    }
  }
  report.focus.push({table,key,oldRows:a.length,newRows:b.length,
    removed:a.filter(r=>!b.some(n=>n[key]===r[key])).map(r=>r[key]),changes});
}
// Follow the actual early main-stage monster references, not every monster in the game.
const earlyStages=load(newRoot,'mainstageconfig.json').rows.filter(r=>r.Id>=10100101&&r.Id<=10300705);
const monsterIds=new Set(earlyStages.flatMap(r=>r.MonsterGroup.flat()));
const attrs=load(newRoot,'monsterattrconfig.json').rows.filter(r=>monsterIds.has(r.Id));
const templates=new Set(attrs.map(r=>r.Monster));
report.earlyMonsters=[];
for(const [table,ids] of [['monsterattrconfig',monsterIds],['monsterskillconfig',templates]]) {
  const old=new Map(load(oldRoot,table+'.json').rows.map(r=>[r.Id,r]));
  const selected=load(newRoot,table+'.json').rows.filter(r=>ids.has(r.Id));
  const changes=[];
  for(const row of selected) for(const key of new Set([...Object.keys(old.get(row.Id)||{}),...Object.keys(row)])) {
    if(!same(old.get(row.Id)?.[key],row[key])) changes.push({id:row.Id,field:key,before:old.get(row.Id)?.[key],after:row[key]});
  }
  report.earlyMonsters.push({table,selected:selected.length,changes});
}
fs.writeFileSync(output,JSON.stringify(report,null,2));
console.log(JSON.stringify({unchanged:report.unchanged.length,changed:report.changed.length,
  added:report.added.length,removed:report.removed.length,
  focus:report.focus.map(({changes,...r})=>({...r,changedFields:changes?.length,
    fieldNames:[...new Set(changes?.map(c=>c.field))]}))},null,2));
