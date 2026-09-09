# 装备栏穿戴链路修复（2026-09-08）

## 原因和范围

原服务只有领取装备的 `75355 MainEquipBagAddResp`，没有保存已领取装备的背包状态，没有 `75351 MainEquipDressReq` 处理函数，也缺少 `MainEquipHeroVO.slotVOList`、`MainEquipSlotVO`、`75356 MainEquipPositionInfoResp`。因此能弹出“青岩护腿”的卡片，但点击穿戴不更新上方装备栏。

本次修的是领取后穿戴、换装和装备栏/背包状态同步，不宣称完成全部斗铠玩法。

## 证据

- 当前客户端：`D:\doudi-resources\client-analysis\decompiled-gameplay-runtime-6.9.263`。
- `SGEngine.GameplayCustom\MainEquipCompareView.cs` 点击穿戴先发 `61999`，再发 `75351`，设置 `main=true`。
- `MainEquipModel.OnRspMainEquipPositionInfoResp` 处理 `75356` 后更新英雄槽位；`main=true` 才分发快装弹窗的 `k_MainEquipQuickDressUpdate` 等事件。
- `D:\抓包数据\第二次抓包.json`：idx 9126–9128 是护腿穿戴；idx 10060–10063 是同槽换装。
- `6.9.263\mainequipconfig.json`：装备 15 青岩护腿，Type=5；装备 12 青岩盔，Type=2。使用表映射，不通过装备 ID 尾数猜槽位。

| 动作 | 官方协议顺序 | 本地实现 |
|---|---|---|
| 已全部领取后点击穿戴 | 61999 → 75351 | 61999 验证已领取/已穿戴对象，不重复发奖；75351 校验归属、英雄和槽位 |
| 空槽穿戴 | 75354(type=1) → 75356 | 从本玩家背包移除对应 objectId，保存槽位，回正确嵌套 VO 和 main 标记 |
| 同槽换装 | 75354 → 75355(flag=false) → 75356 | 旧物品同一个 objectId 和随机属性退回背包；旧装备等级归零，新装备继承槽位等级 |

没有另造 `MainEquipDressResp`，也不返回同请求 ID 的假确认包。

## 修改位置

- `dpcq-protobuf/src/main/proto/scene.proto`：补齐本次需要的穿戴、槽位、背包减少协议字段；没有重编号。
- `dpcq-game-server/src/main/java/com/doupo/server/module/scene/SceneHandler.java`：保存领取后的装备，增加穿戴/换装处理、完整背包/槽位快照、重复对象保护；引导重置时一起清理。
- `tools/export_ch17_support.js`：加入同批 6.9 `mainequipconfig` 全部 267 行及来源哈希，运行时配置现为 13 张源表；从原始 HAR 提取 7 个穿戴/换装二进制测试样本，避免大整数 objectId 被 JS 浮点截断。
- `dpcq-game-server/src/test/java/com/doupo/server/module/scene/MainEquipDressTest.java`：9 项测试，包含真实抓包消息对照、独立槽位、重复点击、外部对象、错误槽位、未解锁英雄、未领取对象、槽位等级继承、引导重置、重复奖励和 gaming 协议注册器调用。

沿用现有玩家 Actor 串行处理，不重构启动框架，不修改只读参考 `D:\server\fuwu`。

## 验证和部署

- 先以测试复现“75351 没有 PlayerCmd 处理函数”。另以测试复现重复奖励可能把已穿对象重新放回背包，随后修复。
- 全量 `mvn -pl dpcq-game-server -am test -q`、`package -q`：**270 tests，0 failures，0 errors，0 skipped**。没有跳过测试。
- 原源码/配置/JAR 备份：`D:\doudi-resources\backups\equip-wear-20260908`。
- 原 JAR SHA-256：`c5d16ba455e8991dd9262136dcacaab1e7925873dc1083abcd3b3c1b987fdaaf`。
- 新 JAR SHA-256：`258af962744be8b8293cbb416cf8944e7a046752b1c7dc45904e9889ac0bb173`。
- 18:39 启动 PID **15940**；18080/19090 同进程监听，`/health` 为 UP；ADB reverse 已恢复。
- 实读 JAR 配置为 6.9.263，mainEquips=267、护腿槽位=5；启动日志没有 ERROR。
- 日志：`dpcq-game-server/target/run-logs/server-20260908-183955.out.log`。

## 尚未验收/尚未实现

- 尚未操作实机点击穿戴；请重新登录、领取挂机装备，穿戴青岩盔和青岩护腿，确认对应图标进入第 2/5 槽，关掉再打开装备页后仍显示。若失败，记录点击时间，查 `MainEquip dressed` 或 `MainEquip dress ignored` 日志。
- 当前装备和关卡均为内存态，登录引导重置会清空，不承诺跨重启持久化。未清数据库、未修改 APK。
- 61999 本次仅覆盖抓包所见“全部领取后的重复确认”。尚未领取奖励时的独立单件领取分支未实现，不能用请求里的对象 ID 直接生成装备。
- 强化、分解、卸装、一键穿戴、石头/器核不在本次范围内。现有槽位等级在换装时保留，但没有凭空开放强化。
- 本次没有按猜测增加战力、气血或伤害。穿戴后的 `HeroOriginNodeStatResp`、`HeroStatUpdateResp`、战斗总属性刷新尚需单独结合抓包和属性公式接入；装备栏状态完成不等于战斗加成链完成。
