# 第11关 Boss 双主角修复（2026-09-08）

## 根因与模式

第11关使用 `61971 MainMapStartFightResp → BattleLogResp(type=1001)` 的战报播放链，而非前期实时战斗。官服视频进入七彩吞天蟒后切换战斗界面，这个变化本身正常。

原实现仅平移抓包坐标，未转换内层身份：外层 PreBattleInfo 已是本地账号，内层 50804 仍保留官服主角。当前资源中本来只有一个主角，加上场景中已有的本地主角，客户端会按不同对象 ID 创建第二人。

| 身份 | 抓包 | 当前本地账号示例 |
| --- | --- | --- |
| playerId | 33318502768 | 100100000001 |
| 场景主角对象 ID | 767192978761449840 | 100100000001001 |

依据：

- `client-analysis/decompiled-gameplay-runtime/SGEngine.GameplayCustom/SceneComponentObjectMgrMainStage.cs`：CreateServerObject 按 baseInfoVo.Id 复用既有对象；非手动模式会移除主角实时战斗 AI。
- 同目录 `SceneComponentObjectMgr.cs`：CoreHero 判断依赖 heroVo.playerId 与本地 PlayerId 一致。
- 同目录 `BattleLog.cs`：按 FriendPlayerID 识别主角并初始化技能栏。
- `dpcq-game-server/src/main/resources/wutan-battlelog/10300105.bin`：同时包含主角、Boss 的技能、伤害、移动、飞行技能和战斗统计。

## 修改

1. 新增局部转换器 `WutanBattleLogIdentity`，为每次回包识别抓包中的单主角，转换为当前账号及既有本地场景对象 ID。转换结果不写回公共缓存。
2. 依据客户端 ProtoMember 补齐 scene.proto 的飞行技能、Buff、锁定、战报移除、战中统计及拉拽结束消息。生成 Java 消息后，按明确字段白名单转换对象引用，不全局替换 int64。
3. 同步转换攻击者、目标、锁定目标、技能冷却所属角色、内层 CrossHeroShortInfo、飞行技能来源/目标、CommonSkill 所属账号、Buff 关联、统计及移除引用。
4. 补上 `50844 BaoBuStartResp` 和 `50829 PullEndResp` 的坐标平移。
5. 畸形战报、未知协议或不支持的多主角战报明确报错，不静默发送未转换的官服身份。

遵循 doudi-server-reference 的 Java 8 与局部改动规范；没有修改 fuwu 参考工程、客户端 APK 或关卡数值。

## 验证

- 先补 SceneHandlerTest 的主角身份断言，原实现稳定失败：期望 `100000000214001`，实际 `767192978761449840`。
- 修复后该测试通过，包含原有 Boss 结算/任务断言。
- 新增 7 个回归测试：覆盖现有第11～14关 16 段战报，每段验证缺省坐标、原坐标和平移坐标；验证无旧身份残留、技能所属角色一致、双方出招及时间/伤害不变、缓存隔离、异常战报拒绝。
- `mvn -pl dpcq-game-server -am test -q`：239 项通过，0 失败、0 错误、0 跳过。
- `mvn -pl dpcq-game-server -am package -q`：成功，未跳过测试。

## 部署与回退资料

- 2026-09-08 16:23 启动修复版；服务 PID 17700，端口 18080/19090。
- `/health` 返回 `{"status":"UP","service":"dpcq-game-server"}`，启动错误日志为空。
- JAR SHA-256：`80BACA16B2B2EE85C77765917F0D88F5EEEEBAA285F15ACA0F10F0454C8E8911`。
- 日志：`dpcq-game-server/target/run-logs/server-20260908-162353.out.log`。
- 原源码、协议、原测试文件和旧 JAR 备份：`D:/doudi-resources/backups/ch11-battle-identity-20260908/`。未重置数据库或删除存档。

## 验收与尚存边界

ADB 当时没有在线设备，尚未完成真机画面验收。请彻底退出客户端后重新登录，进入第11关 Boss，确认只有一个主角、双方出招/受击/伤害数字正常、技能栏识别主角，结算后恢复场景并继续任务。只重新发送请求可能保留上一次错误创建的场景对象。

本次是战报身份与播放链修复。仍使用抓包中的战斗属性、出招与胜负，并没有实现根据当前装备、属性重新模拟战斗的完整引擎；此前缺失捕获资源时的简化回退也未在本次改动中扩展。不能将测试通过视为已完成所有玩法或真机视觉验收。
