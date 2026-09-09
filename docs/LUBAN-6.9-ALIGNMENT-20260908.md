# 6.9.263 Luban 配置对齐记录

日期：2026-09-08。范围：当前本地镜像、对应热更 DLL、Luban 导出、服务端已接入的配置子集。不是一次“换表即完成全部玩法”的验收。

## 已完成

- 从本地当前 catalog 找到 722 个 RawData 条目，全部有对应缓存；没有从 CDN 下载，也没有用旧版同名文件补缺。
- 使用同批 `SGEngine.Configs.Runtime.dll` 的 Luban 反序列化逻辑重新导出：702 张表，missing=0、failed=0。RawData 条目数与 DLL 定义的表数不是同一统计口径。
- 新旧 JSON 按内容比较：旧 645 张、新 702 张；398 张相同、247 张变化、57 张新增、0 张删除。
- 服务端 `chapter17/config.json` 改为 6.9.263 来源，并记录 catalog、DLL、12 张源表和对应 RawData 的哈希；包含 50 条前期任务、59 条主线配置（到第 18 关首段）、塔 1～19 层、塔章节/奖励、声望 1001～1003、境界 1～17 级以及纳戒/灵液相关配置。
- 境界、早期声望/灵液等原实现经比对仍匹配的数值保留，不做比例放大或凭感觉调整。
- 当前导出工具显式读取 6.9.263 的反编译协议定义。此次重新生成的 `chapter17.proto` 与更新前逐字节相同；没有更换线上的封包格式。
- 登录返回的本地服务版本标签更新为 `local-6.9.263`，保留有抓包依据的登录 `resourceVersion=1`。
- 保留原 Maven / Spring / Netty / Actor 启动链，不修改只读架构参考 `D:\server\fuwu`，不改数据库和 APK。

## 版本字段不能互相代替

| 来源 | 实际值 | 处理 |
|---|---|---|
| 模拟器当前 APK `com.dpcq.doupo` | versionName=6.8.1，versionCode=26 | APK 壳版本，不等于热更数据版本；未改测试工具中的 APK clientVersion |
| 当前 PatchSetting | major=6、minor=9、resource=263、assembly=3、Vcs=80026 | 将这批导出命名为 6.9.263 |
| 当前 hotUpdateAssemblyInfo | version=234 | 与 PatchSetting 的 assembly=3 分别记录，未假设含义一致 |
| 第二次抓包 LoginAuthResp（idx 1） | version=online-33244、resourceVersion=1 | 保留登录资源字段 1，不强改成 263 |

当前 catalog 内嵌 hash 为全零，外部 `.hash` 文本也不能直接当作 bin 的 MD5 校验值。因此固定文件 SHA-256、条目映射及 DLL 实际解析结果，而不宣称仅靠 hash 文件证明“官方原件”。

## 文件位置与复现链路

| 内容 | 位置 |
|---|---|
| 本次固定快照及 manifest | `D:\doudi-resources\client-analysis\luban-6.9.263-20260908` |
| 新版原始配置 bytes | `D:\doudi-resources\client-analysis\config-data\6.9.263` |
| 新版完整 JSON | `D:\doudi-resources\client-analysis\config-json\6.9.263` |
| 新版 Gameplay 反编译代码 | `D:\doudi-resources\client-analysis\decompiled-gameplay-runtime-6.9.263` |
| 全量/前期差异报告 | `D:\doudi-resources\client-analysis\luban-6.9.263-20260908\table-diff.json` |
| 更新前源码、配置、JAR 备份 | `D:\doudi-resources\backups\align-6.9.263-20260908` |
| 服务端实际内置配置 | `D:\doudi-resources\doupo-server\dpcq-game-server\src\main\resources\chapter17\config.json` |

旧 `config-json\6.8.1`、`config-data\6.8.1` 和旧反编译目录没有覆盖。以后做当前版本分析，应明确选新目录；`tools/har_decode.js` 的默认旧路径继续服务旧版离线分析，新版导出通过 `new Schema(新版路径)` 显式选择，避免旧历史工具被整体换源。

复现顺序：

1. `tools/prepare_luban_snapshot.py`：从本地镜像读取 metadata/catalog；验证旧 APK 内 DLL 与旧明文 DLL 的全文件对应关系，再导出新 DLL 和 722 个 RawData 文件。输出目录中遇到不同内容会拒绝覆盖。
2. 既有 `D:\doudi-resources\client-analysis\luban-json-exporter\bin\Debug\net8.0\LubanJsonExporter.exe`，参数 `--dll <快照>\decoded\SGEngine.Configs.Runtime.dll --config-root <新版 config-data> --output <新版 config-json>`。使用 DLL 自带结构解析，不编造二进制字段。导出时的 MonoBehaviour/SimpleJSON 可选依赖提示未造成表加载失败。
3. `tools/compare_luban_versions.js <旧 JSON> <新 JSON> <table-diff.json>`：按表内容比较，并追踪前期主线的实际怪物引用。
4. `tools/export_ch17_support.js`：把服务端当前使用的子集写入资源，协议字段取新 DLL，二进制抓包样本仍原样保留。
5. PowerShell 加载 `dev-env.ps1`，执行 `mvn -pl dpcq-game-server -am test -q`，通过后才打包部署。

本次 DLL 解码方式是客户端原有重复 XOR；通过旧密文/明文全文件一致性验证后才用于新文件，新文件具有有效 PE/CLR 结构且完整导出成功。原镜像内 DLL 未替换。

关键 SHA-256：

- catalog：`4bd3910708836bc3462951cff465a2ddda1a61dfc0e1e70f146471479d2c0795`
- 新 Configs 明文 DLL：`58e6b889dfb4e787a74933e9b52ab0691dee8b283b42d64f69ea6169ca2fb754`
- 新 Gameplay 明文 DLL：`a71a24219703b10fa488d1ef9df972cc9ba35171295c590fe8a37c257da0faab`
- 第二次抓包 JSON：`60a486191887e0324773d625823c73f751f039547e18bdc65f4635f6f569e620`

## 已确认的实际变化

| 范围 | 差异 | 本次处理 |
|---|---|---|
| 前期主线配置 59 行 | 29 处 Power 改动；如第 15 关 Boss 243511→317782，第 17 关 Boss 250156→324428 | 更新表，不将 Power 当伤害或血量倍率 |
| 前期任务 50 行 | 新增 TaskFinish=0；任务条件和奖励不变 | 更新表，不额外发奖励 |
| 塔 1～19 层 | Power 变化；第 1 层 78240→57396；部分塔章节展示字段变化 | 更新表；塔奖励 402 行完全相同 |
| 声望 1001～1003 | 无变化 | 700、800 等已验证消耗不改 |
| 境界 1～17 级 | 无变化 | 单测逐级验证 Exp、NeedUpStage、基础属性、固定加成和前三阶段上限 |
| 灵液 | base/quality/elixir 表不变；accumulate 的 1103、1104 Exp 变为 550、800 | 完整导出包含变更；当前 1/1101/1102 消费路径不受影响，未擅自开放后续品质 |
| 纳戒池 | 7007/7008 的 Guarantee 和 7007/7008/8007/8008 的 LotteryTimes 有变化；升级 Times 不变 | 完整新版池表已保存；不把 LotteryTimes 混同为升级次数，早期发放/扣费不改 |
| 主线 1～17 怪物引用 | 210 条 monsterattr 配置不变；15 个模板中，100127 的 Skills 新增 208 | 记录下面的战斗核对事项，不编造新战斗事件 |

## 必须保留的待验证项

1. **Boss 技能 208**：模板 100127 被第 11～17 关 Boss 引用。新版 `skillfightconfig` 中名称为“缩短受控制时间”，SkillType=10。仅凭这两张表不能推出触发时点、持续时间和应下发的协议；需继续对照第二次抓包的战报和客户端处理逻辑。本次没有伪造普通攻击、伤害或控制事件。
2. 新版 DLL 相对旧版有增量协议字段，例如 `SceneHeroVo.puppetTotalPower`、`NewFightSkillLotteryVo.selectedLotteryTimes`、`LotteryDrawResp.hitPondVos`。已拿到新版定义，但未把新增玩法冒充成已实现；现有字段未发现删除/改号，本次 28 个导出消息定义不变。
3. 完整导出 702 张表不等于服务端已经使用全部 702 张表。当前内置的是已实现流程的 12 张表子集；硬编码的抓包战斗、未完成的机制和持久化仍需独立验证。
4. 本次没有操作客户端打一遍第 1～17 关，也没有实测所有纳戒阶段或塔模式。自动测试与启动健康检查不能代替实机验收。

## 测试和部署

- 先运行新配置测试，在旧资源上复现版本缺失/表项缺失失败；切换后通过。
- 新增 `LubanVersionAlignmentTest` 3 项、`LoginVersionAlignmentTest` 1 项。
- 全量 `test` 和 `package` 均通过：**261 tests，0 failures，0 errors，0 skipped**，未使用 `-DskipTests`。
- 更新前 JAR SHA-256：`4e2143c4f83a793f10136ca05545a50f9646842993b4b68b58826b732ac1acc6`，已保存到备份目录。
- 新 JAR SHA-256：`c5d16ba455e8991dd9262136dcacaab1e7925873dc1083abcd3b3c1b987fdaaf`。
- 2026-09-08 18:22 启动新服务：PID **4464**，18080/19090 由同一进程监听；`/health` 返回 UP。JAR 内嵌配置实读为 6.9.263、12 张源表索引。
- 本机 HTTP 获取 PatchSetting、catalog、hotUpdateAssemblyInfo 均为 200，响应内容与固定快照逐字节相同。
- `emulator-5554` 的 ADB reverse 18080/19090 已恢复。
- MySQL 原进程 17996 未重启，未清库。游戏重启会断开连接，现有内存态关卡/任务/塔状态可能丢失；客户端需重新登录。
- 运行日志：`D:\doudi-resources\doupo-server\dpcq-game-server\target\run-logs\server-20260908-182219.out.log`。

回退时先确认当前 PID 的完整 JAR 路径，仅停止该游戏进程，再用备份 JAR 替换对应 target 文件并执行原 `run-game-server.ps1`；不要清理数据库、客户端资源或整个工作目录。
