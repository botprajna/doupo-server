# 斗者灵液修复（2026-09-08）

## 依据与范围

- 配置：`D:\doudi-resources\client-analysis\config-json\6.9.263\exppoolbaseconfig.json`、`exppoolaccumulateconfig.json`；运行时沿用已核验的 `chapter17/config.json`，没有重导或改动数值。
- 客户端：`decompiled-gameplay-runtime-6.9.263/SGEngine.GameplayCustom/ExpPoolModel.cs`、`ExpPoolUpLevelView.cs`。75088 更新会先清空材料计数字典，因此服务端必须发送全量计数快照。
- 第二次官服抓包：8986 首份 102 入库；9009 产 4 颗 1102；9010 返回 101=5、102=1；9015/9016 按真实 rid 统计服用；9717 在原槽位扣 102 并保留道具身份。
- 保留现有 Java 8 / PlayerCmd / 玩家 Actor 框架（服务端参考 skill 的架构约束）；未改其他玩法、开服日期、付费状态或材料奖励数量。

## 修复

1. `makeAlchemy` 从炼丹等级/境界选择 101→1101 或 102→1102，不再等任务 200036。
2. 接入普通与斗者灵液的实际奖励库存。奖励可叠加；消耗在真实槽位减一，归零删除；不再推算“第几次使用对应第几个槽位”。真实连接以已发送的背包状态为准。
3. `costItem2Nums` 按材料分别累计，`makeTimes` 仍为累计炼制次数。每天上海时区零点后，在线心跳清空当日材料计数并推 75088；库存、产物和累计服药次数不清空。
4. 已炼制产物保存完整 VO。`takingInfo` 按产物 rid 统计，不把斗者丹药累计到 1101；同一产物只能消费一次。旧的新手自动升二级逻辑只允许等级 1 触发。
5. 服务开服日随日期推进重新计算。配置不变：开服第一天普通材料限 5 份、斗者材料限 10 份；从第二天起这两种材料解除次数限制，仍需要真实库存。限制不作用于已经炼出的丹药，不添加每次服用冷却。

## 验证

- 先运行 3 个复现用例：空库存产药、提前切换失败、takingInfo 类型错误，旧代码均失败。
- `FighterElixirTest` 10 项覆盖：真实突破切换、200027 早期奖励、库存耗尽、普通/斗者分类快照、原槽位扣减、重复服用、上限 5/10、午夜心跳重置、第二天第 11 份斗者灵液可用、角色初始化清空。
- 使用 4 份原始协议正文做对照，包括真实 `PlayerConnectionContext` + Netty EmbeddedChannel 的 50402 扣减包。
- 老的突破测试使用显式材料前置条件；原数值断言保留。需要第六份普通灵液的三项旧用例明确设置为开服第二天。
- 测试材料仅在测试代码中准备，不给生产玩家额外发放材料。
- 复跑：`. .\dev-env.ps1` 后运行 `mvn -pl dpcq-game-server -am test`；测试抓包提取脚本为 `node tools/export_elixir_fixtures.js`，仅读本地 HAR。

## 部署与边界

- 最终 `mvn -pl dpcq-game-server -am package`：280 项测试通过，失败/错误/跳过均为 0。
- 新服务 PID `20104`，18080/19090 均监听，`/health` 返回 `UP`；启动错误日志为空。
- 新 JAR SHA-256：`8CA588B3F7E1DB40FFDDFB1ED88CC148E698A4E80865232BDD757565783439FA`；运行日志 `dpcq-game-server/target/run-logs/server-20260908-190347.out.log`。
- 回滚备份：`D:\doudi-resources\backups\fighter-elixir-20260908`（源文件、原配置、旧 JAR）。旧 JAR SHA-256：`258AF962744BE8B8293CBB416CF8944E7A046752B1C7DC45904E9889AC0BB173`。
- 开服时间保持 `2026-09-08T00:00:00+08:00`。
- 仍沿用已有内存试玩状态；未增加数据库持久化，重新初始化角色或重启不会保留该试玩进度。
- 本次只处理普通/斗者两档；更高境界、独立 source=2 产物及未提供证据的扩展上限/付费能力没有额外实现或伪造。
- 自动测试与服务健康检查不能代替手机画面验收，需要客户端重新登录后再验证服用界面。
