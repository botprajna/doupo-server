# 第 7～10 关后排索敌不攻击修复（2026-09-08）

## 范围与根因

修复魔兽山脉第 7～10 关每关三波及循环波次（10200201～10200503，仅尾号 01/02/03）。第 6 关保留原有前后排激活时序；Boss 和第 11 关起的战报播放链路不改。

原实现有三个相互关联的问题：

- `CombatSession.hasMonsterMovement()` 仅允许第 6 关。后续怪即使显示警戒，也没有进入追击移动；范围外的攻击一直被跳过。
- `SceneHandler` 首次出手的警戒分支仍按第 6 关三只怪处理，未覆盖后续每波六只怪；主动技能开场也可能没有激活它们。
- `CombatSessionFactory` 丢弃 `SceneMonsterVo.monsterId`，后续远程兵落入通用近战技能分支。该字段对应 MonsterSkillConfig.Id，不是波次 templateId。

## 证据

仅使用本地资料，不请求官方服务：

- 解码抓包：`D:\抓包数据\第二次抓包.json`。
- 对应原始 HAR：`C:\Users\ppp\Desktop\imlj2-cn-gamewx.lansors.com_2026_09_07_19_03_22.har`。
- Luban：`D:\doudi-resources\client-analysis\config-json\6.9.263\monsterskillconfig.json`、`skillfightconfig.json`。

| monsterId | 两个普攻技能 ID | CastRadius | 命中 actionId / special |
| --- | --- | --- | --- |
| 121001（前排） | 201110110101 / 201110210101 | 3 | 保留普通近战结构 |
| 121002（第 7/9/10 关后排） | 40310310101 / 40310410101 | 6 | 5123601 / 0 |
| 13（第 8 关后排） | 220310110101 / 220310210101 | 5 | 2200101 / 128 |

抓包索引（从 0 起）：第 7 关后排警戒 2342、停步 2371、发招 2372、命中 2403；第 8 关后排警戒 5425、追击 5511、停步 5512、发招 5513、命中 5554；第 9 关后排警戒 6755、追击 6768、停步 6789、发招 6790；第 10 关后排警戒 7624、发招 7689。追击速度 5；两类远程技能的 timeRatio 分别为 1.24839、1.11。

## 修改

- 保留现有 Actor、CombatTickProcessor、Netty 和生命周期结构，不新增调度链，不修改 `D:\server\fuwu`。
- 保留怪物配置 ID；第 7～10 关按实际怪物种类选择施法距离、技能和命中动作。
- 每波首次出手激活全部六只怪。进入范围前追击，停步后发招；移动 tick 不加速攻击冷却。
- 维持死亡停手、暂停冻结、旧会话 tick 隔离。
- 没有重写伤害/减伤/怒气公式，也没有新增真实弹道飞行延迟调度。本次验证的是警戒、追击与技能/命中包的结构，不代表所有战斗数值已对齐。

## 测试与复现

先加用例复现：后续波次未移动、六只怪警戒数为 0、远程兵输出近战技能。修复后新增 8 项测试，覆盖十二波、三波后循环、主动技能开场、距离边界、两类远程技能、暂停恢复、冷却、死亡与旧波次隔离。

`tools/export_later_monster_fixtures.js` 从上述本地资料提取四个原始技能包和三个 Luban 技能范围配置，存于 `dpcq-game-server/src/test/resources/later-monsters`。测试直接对比捕获的发招/命中包；命中包仅替换测试角色身份及本场伤害、血量，不改技能、actionId、special 或结构。

执行（项目根目录）：

```powershell
. .\dev-env.ps1
mvn -pl dpcq-game-server -am test -q
mvn -pl dpcq-game-server -am package -q
```

两次均成功；打包时未跳过测试。最终 20 个测试类、288 项通过，失败/错误/跳过均为 0。尚未进行手机画面验收。

## 部署与回退记录

- 2026-09-08 19:45:51 启动修复版。Java PID 20104（Windows 复用了旧 PID，已核对创建时间）；18080/19090 均由该进程监听。
- `/health` 返回 `UP`；启动错误日志为空，启动输出未发现 ERROR/Exception。
- MySQL PID 17996、3307 保持运行；未清理数据库。模拟器 emulator-5554 的 18080/19090 ADB reverse 已配置。
- 新 JAR SHA-256：`E71BD67264431C74B5F09D2BA0778EF6908704C449E782D0E2F4FA664D1E49EC`。
- 修改前源码、相关旧测试、配置及 JAR 备份：`D:\doudi-resources\backups\later-monster-movement-20260908`。
- 备份 JAR SHA-256：`8CA588B3F7E1DB40FFDDFB1ED88CC148E698A4E80865232BDD757565783439FA`。
- 启动日志：`dpcq-game-server/target/run-logs/server-20260908-194551.out.log`。

重启已使原有内存测试进度重新初始化。客户端需退出重登，重点确认第 7/8/9/10 关第二、三波及循环波次的后排能够走近、停步并播放对应远程攻击；第 11 关以后的战报问题需沿不同链路另查。
