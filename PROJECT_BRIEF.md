# 斗帝（斗破苍穹）Java 私服项目简报

> 用途：给协作 AI / 新成员的分工交接卡。改代码前先读此页 + 铁律。

## 一句话

复刻《斗帝》手游服务端：本地起服务端，官方客户端（雷电模拟器）直连游玩。所有协议和数值以**官服抓包**为唯一依据，逐项还原。

## 环境与路径

| 项 | 位置 |
|---|---|
| 服务端源码 | `D:\doudi-resources\doupo-server`（Maven 多模块：dpcq-game-server / dpcq-protobuf） |
| 技术栈 | JDK8 + SpringBoot + Netty；HTTP 18080 + Socket 19090；protobuf |
| 数据库 | MySQL 8.4，端口 **3307**（非默认服务；配置 `D:\doudi-resources\doupo-config\mysql-local.ini`，凭据在 `database.db`） |
| 客户端 | 雷电模拟器（桥接 192.168.1.35），`adb reverse tcp:18080 tcp:18080` + `tcp:19090 tcp:19090`（掉线后需重设，否则"网络错误"） |
| 官服抓包 | `D:\we\xwechat_files\wxid_dz9kth4lpsq221_e5dc\msg\file\2026-08\第一次创角色.json`（约 9MB，官服全流程协议序列） |
| 只读参考 | `D:\server\fuwu`（**禁止修改**） |
| 配置表 | `D:\doudi-resources\client-analysis\config-json\6.9.263\`（702 张，使用同批 Configs DLL 完整解析；旧 `6.8.1` 保留作差异对照。基础属性在表内，最终战斗值仍需核对抓包 attributeList） |
| 客户端反编译 | `D:\doudi-resources\client-analysis\decompiled-gameplay-runtime-6.9.263\`（当前热更 C# 字段定义；无版本后缀目录保留作旧版参考） |
| 资源代理 | 本地镜像 `D:\doudi-resources\resource-mirror`；`application.properties` 中 `game.resources.upstream=https://imlj2-cn-resbak.lansors.com`（**不带任何路径前缀**，带前缀会双前缀 502/404） |

## 命令（重要）

2026-09-08 配置对齐记录：`docs/LUBAN-6.9-ALIGNMENT-20260908.md`。当前 APK 壳仍报 6.8.1，但热更资源为 6.9.263；不能仅凭 APK 版本或把目录改名判定表版本。下方早期玩法进度为历史记录，不能当成当前验收清单。

git-bash 里 `mvn` 脚本已坏（classworlds），**必须用 PowerShell 调用**：

```bash
powershell.exe -NoProfile -Command ". 'D:\doudi-resources\doupo-server\dev-env.ps1'; Set-Location 'D:\doudi-resources\doupo-server'; mvn test"
```

部署流程：

```bash
# 1. 停旧服：netstat -ano | grep ":18080" 取 PID → Stop-Process -Id <PID> -Force
# 2. 打包：powershell ... mvn package '-DskipTests' -q
# 3. 重启：java -jar dpcq-game-server/target/dpcq-game-server-0.0.1-SNAPSHOT.jar（后台）
# 4. 验证：日志出现 "Started DoupoGameServerApplication" + 18080/19090 LISTENING
```

## 铁律（必须遵守）

1. **抓包为唯一依据**：所有下发数值必须能追溯到抓包（PackUpdateResp 的 size、SkillActionResp 的 damage、ChangeFightValueActionVO 的 changeValue、attributeList 等），禁止推算/复制其他关卡的值；配置表只做静态核对，冲突以抓包为准。
2. 只改与当前问题直接相关的文件；不修改 `D:\server\fuwu`。
3. 每个改动先补失败测试，`mvn test` 全绿再部署。
4. 客户端 UI 未解锁 ≠ 服务端数值问题，先找该 UI 前后的官方协议序列。

## 协议约定

- 定义：`dpcq-protobuf/src/main/proto/scene.proto`；消息 ID = protoId（`context.write(protoId, msg, 0)`）。
- 常用 ID：LotteryDrawReq=77351、LotteryDrawResp=77352、LotteryInfoResp=77354、LotteryPoolUpReq=77363、LotteryPoolUpResp=77364、首充 RealFirstChargeUpdateResp=77006/InfoResp=77001、PackUpdateResp=50402、RewardResp=50406、TaskUpdateResp=50906、HeroStatUpdateResp=50455、AttributeActionVO=50801、PlayerFightForceResp=52351、HeroSkillActResp=75011。
- **PackUpdateResp(50402) 的 `operationType` 是内部操作码，与 protoId 无关**：LOTTERY_TEN_CONSUME=77351、LOTTERY_REWARD=77353、QUEST_MAIN_REWARD=50907、MAIN_STAGE_PASS_REWARD=61951。
- 客户端反编译 `.cs` 文件是字段结构的权威来源。

## 已实现（主线 1-8 关全通）

- **章节/战斗**：1-8 关每关三波小怪 + Boss（`ChapterConfig` 章节数值、`SceneHandler` 战斗流程、`CombatSession` Boss 攻击序列）；怪物攻/血/掉落/站位(dir)/Boss 反击（技能/伤害/怒气）均为抓包精确值。
- **任务链路**：第 1-5 关领奖（200003-200011 等）、第六关 200012 领奖（灵液+声望+模块开放+**首充弹窗推送**）、纳戒引导任务。
- **纳戒（抽卡系统，LotteryType=12 NEW_FIGHT_SKILL）**：
  - 引导抽取 1-4 次（固定技能序列，抓包值）。
  - 引导后通用抽取：**一次请求 = 按当前能量 ÷ 10 算本次抽几下 → 一次性扣 N×10 → 一次性返回 N 个奖励**（官服 idx 1937 语义；能量不足返回失败不扣）。十连固定 10 下扣 100。
  - 纳戒能量 100200：打 Boss +3/+10、领奖 +10/+2，状态化累加；引导抽取按官服链设值（6/8）。
  - 纳戒升级：LotteryPoolUp（77363/77364），showStage 1→2→3，阈值 `newskilllotterypoolconfig`（5/10/50/100/100/150/300）。
- **装配**：两技能槽（updateHeroSkillSchema + writeSecondSkillEquip）。
- **灵液**：writeAlchemyTaking 补 key=1（ExpPoolAccumulateConfig）。
- **资源代理**：upstream 去前缀（修复双前缀 502/404）；碎心掌图标 bundle 已缓存。
- **首充弹窗**：200012 领奖后下发 77006/77001（chargeId=1，未购买状态）；点击支付未实现。

## 待办 / 已知问题

- 纳戒二次升级（2→3）：引导后已可连抽，totalDrawTime 到 15 应可触发，**待实测**。
- 玩家通用伤害未按关精确化：第八关抓包有递增规律（336→402→406，同技能越打越高），未复现。
- 首充点击支付 / 购买回调未实现（用户明确暂不做）。
- `mvn test` 当前 **118/118**（SceneHandlerTest 73 个）。

## 关键文件

- `dpcq-game-server/src/main/java/com/doupo/server/module/scene/SceneHandler.java`（主逻辑 ~4100 行）
- `dpcq-game-server/src/main/java/com/doupo/server/module/scene/ChapterConfig.java`（章节/怪物数值）
- `dpcq-game-server/src/main/java/com/doupo/server/module/combat/CombatSession.java`（Boss 攻击序列）
- `dpcq-protobuf/src/main/proto/scene.proto`（协议定义）
- `dpcq-game-server/src/main/resources/application.properties`（端口/DB/资源代理上游）
- `dpcq-game-server/src/test/java/com/doupo/server/module/scene/SceneHandlerTest.java`（73 个协议级测试）

## 抓包提取技巧

大文件（9MB）用 Python `text.find()` + 小窗口正则，**不要**用慢速全文件正则（会超时）。按 `"idx": N` + `"name"` 定位消息，message 的 `decoded` 即协议内容。
