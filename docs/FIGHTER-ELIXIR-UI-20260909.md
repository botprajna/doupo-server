# 斗者升星后灵液显示回退修复（2026-09-09）

## 范围与根因

只修斗者阶段的灵液/境界回包，不改挂机收益或第 14 关任务。

- `SceneHandler.breakHeroLevel` 的 75067 将境界写死为 stage=1。
- `breakHeroLevel`、`upgradeHeroLevel` 的 75060 将炼丹等级写死为 level=1。
- 客户端 `ExpPoolController.OnRspAlchemyNewExpChange` 会调用 `SetCurrentLevel`，所以服务端实际扣聚气灵液时，客户端却重新展示筑基灵液。
- 现在突破使用当前境界，两个升星入口的修为通知使用与炼制相同的炼丹等级来源。

## 对照证据

资料：`D:\抓包数据\第二次抓包.json`、6.9.263 Luban 表及同版本客户端反编译代码。

- `exppoolbaseconfig`：level=1 消耗 101（筑基），level=2 消耗 102（聚气）；产物分别为 1101 / 1102。
- idx 9018/9019：二星斗者的 75067 为 level=9、stage=2；75060 为 exp=380、level=2。
- idx 9733/9734：三星斗者的 75057 为 level=10、stage=2；75060 为 exp=730、level=2。
- 首次成为斗者的 idx 8212 先发送旧炼丹 level=1，随后 idx 8217 的 75065 切换至 2。这个过渡顺序保持原样，不应把所有 level=1 无差别替换成 2。

## 验证

- 新增 3 个用例，修复前均失败，修复后通过。
- 两个用例逐包比对官方原始正文；另一个覆盖斗者一星至九星的升星回包、聚气炼制与服用、筑基库存不被扣除、聚气用尽不回退筑基。
- `FighterElixirTest`：13 项通过。
- 全量 `mvn package -Dmaven.jar.forceCreation=true`：305 项通过，0 失败、0 错误、0 跳过，打包成功。
- 尚未替代真实客户端界面的人工复测。

## 部署

- 用户明确同意修复后立即重启并重新测试。
- 旧 PID 12700 已停止；新 PID 7156，端口 18080 / 19090，`/health` 返回 UP。
- MySQL 未重启、未重置数据库；原服务的内存测试进度随重启清空。
- 运行日志：`dpcq-game-server/target/run-logs/server-20260909-105147.out.log`。
- 新 JAR SHA-256：`5CCF93701A840A6A85F04E3A2DA66641CB0CD2B5EE49BA6A86943BA604D16243`。
- 修改前源码、测试、提取脚本及运行包备份：`D:\doudi-resources\backups\fighter-elixir-ui-20260909`。

## 挂机收益：只调查，未修改

- 第二次抓包有 3 次 61953 领取请求、3 次 61954 收益回复、11 次 61955 挂机收益更新。
- idx 9248 / 9425 / 9611 的累计时间为 60000 / 120000 / 180000 毫秒，装备条目为 3 / 6 / 9，证明存在持续累计。
- 本服 `pendingHangUpEquips` 目前只在声望解锁等节点预置批次，`takeHangUpReward` 领取后移除；没有完整的按时间持续累计和离线结算。
- 以上是主线挂机装备收益，不等同于炼气塔托管；这些抓包不能证明所有离线资源结算规则。
