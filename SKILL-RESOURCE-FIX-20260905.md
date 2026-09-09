# 千焰剑罡特效资源加载修复（2026-09-05）

## 已确认原因

- Android logcat 13:22:25–13:22:28：`pre_eff_xiaoyan_skill13_01/02/03/04/05/07` 等实例化失败；依赖 bundle 返回 HTTP 502。
- 服务端 `dpcq-game-server/target/run-logs/server-20260905-131645.out.log` 对应请求抛出 `SSLHandshakeException: Remote host terminated the handshake`。
- 同机 Java 8 独立请求复现：备用源 `https://imlj2-cn-resbak.lansors.com` 握手失败，强制 TLS 1.2 仍失败；主源 `https://imlj2-cn-res.lansors.com` 返回 200。Windows curl 对两个源都能下载，不能据此认定 Java 下载链正常。
- 样例路径：`/imlj2_android_alpha1/Ljxs/ServerData/8b/249c68219161c865586743da8df73446_741619e2a6dc771f9541d605b652e48b.bundle`，200 响应长度 199624 字节。

## 修改与验证

- 仅将 `dpcq-game-server/src/main/resources/application.properties` 的 `game.resources.upstream` 改为主源。保留 HTTPS 验证、域名白名单、资源相对路径与缓存；没有更改战斗协议、数值、客户端包。
- `mvn -pl dpcq-game-server -am test -q`：158 项测试，0 失败、0 错误；随后 package 成功。
- 原服务 JAR 已备份至 `D:/doudi-resources/backups/skill-resource-fix-20260905-133337/before.jar`。
- 重启后进程 PID 25588，18080/19090 均监听；新日志 `server-20260905-133453.out.log`。
- 从旧服务日志提取 25 个不同的失败资源路径，逐一请求本地 18080 资源接口：25/25 返回 200 且正文非空，已缓存到 `D:/doudi-resources/resource-mirror`。下载由修复后的 Java 服务完成。
- 客户端已重新进入关卡。尚未得到本次修复后再次释放千焰剑罡的画面确认，不能将资源 HTTP 验证等同于最终特效/伤害飘字验收。

## 画面验收

退出并重新进入游戏，释放千焰剑罡，检查各段特效、受击和伤害数字；对照同一时间 logcat，确认不再出现该技能资源下载及实例化错误。若资源加载成功但飘字仍缺失，继续核对伤害动作与目标生命周期，不改动未验证数值。
