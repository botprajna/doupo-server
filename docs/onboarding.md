# 斗破苍穹：斗帝之路 服务端 —— 新人上手教学文档

> 目标读者：刚刚入职、第一次接触本项目的新人。
> 阅读方式：从上往下按顺序读，每一章是一个「模块」，每个模块里按「文件」逐个拆解。
> 文中的文件路径都可以直接点击跳转。

---

## 目录

1. [项目是什么](#1-项目是什么)
2. [配置文件 application.properties](#2-配置文件-applicationproperties)
3. [启动流程 foundation/starting](#3-启动流程-foundationstarting)
4. [协议层 dpcq-protobuf + dpcq-common](#4-协议层-dpcq-protobuf--dpcq-common)
5. [网络层 foundation/netty](#5-网络层-foundationnetty)
6. [玩家 Actor 层 foundation/player](#6-玩家-actor-层-foundationplayer)
7. [数据库层 foundation/database](#7-数据库层-foundationdatabase)
8. [数据层 dpcq-pojo](#8-数据层-dpcq-pojo)
9. [业务模块 module](#9-业务模块-module)
10. [HTTP 入口层](#10-http-入口层)
11. [一次登录的完整链路（10 个文件）](#11-一次登录的完整链路)
12. [响应号映射机制](#12-响应号映射机制)
13. [新人加一条协议的标准流程](#13-新人加一条协议的标准流程)
14. [已知坑与注意事项](#14-已知坑与注意事项)

---

## 1. 项目是什么

斗破苍穹：斗帝之路（dpcq）的**游戏服务端**。Maven 多模块项目，根 [pom.xml](../pom.xml) 下 4 个模块：

| 模块 | 作用 | 目前内容 |
|------|------|---------|
| [dpcq-common](../dpcq-common/) | 公共常量 | 只有 `ProtocolIds.java` |
| [dpcq-protobuf](../dpcq-protobuf/) | 通信协议定义 | 11 个 `.proto` 文件 |
| [dpcq-pojo](../dpcq-pojo/) | 数据实体 | 只有 `Account.java` |
| [dpcq-game-server](../dpcq-game-server/) | 游戏服务端主体 | 网络层、Actor、业务模块 |

**技术栈**：Java 8 + Spring Boot 2.1.0 + Netty（WebSocket 长连接）+ Protobuf（序列化）+ Akka（Actor 并发）+ MySQL + 内部框架（`gaming-ruler` / `gaming-fakecmd` / `gaming-db` / `gaming-prefab`）。

### 1.1 两套入口（重要概念）

服务端同时监听**两个端口、两套协议**：

```
HTTP   18080  ──►  Spring MVC  (@RestController)
                    账号注册登录 / 登录引导 / 资源镜像 / 健康检查

Netty  19090  ──►  WebSocket 长连接  (游戏实时消息)
                    登录 / 心跳 / 创建角色 / 场景 / 好友 ...
```

- `server.port=18080`：Spring Boot 自己读的 HTTP 端口。
- `game.netty.port=19090`：你自己代码读的游戏长连接端口。

---

## 2. 配置文件 application.properties

文件：[dpcq-game-server/src/main/resources/application.properties](../dpcq-game-server/src/main/resources/application.properties)

逐行解释：

| 行 | 配置 | 含义 |
|----|------|------|
| `spring.application.name=dpcq-game-server` | 应用名 | 日志/监控里标识用 |
| `server.port=18080` | HTTP 端口 | Web/管理端口 |
| `server.servlet.context-path=/` | Servlet 根路径 | `/` 即无前缀 |
| `spring.main.banner-mode=off` | 关闭启动 banner | 日志更干净 |
| `game.netty.port=19090` | 游戏 TCP 端口 | 客户端连的端口 |
| `game.netty.idle-seconds=60` | 空闲超时 | 60 秒没消息判断开 |
| `game.netty.boss-threads=1` | boss 线程 | 负责接收新连接 |
| `game.netty.worker-threads=4` | worker 线程 | 负责读写处理 |
| `game.server.id=1` | 服务器编号 | 以后多区服区分用 |
| `game.database.config-file=...` | 数据库配置文件路径 | 指向外部 db 配置 |
| `logging.level.org.gaming.db...DBManager=OFF` | 关掉某类日志 | 第三方组件不刷屏 |

> `game.*` 开头的都是**自定义配置**，通过 `@Value("${game.xxx}")` 注入到代码里，Spring Boot 不认识它们。

---

## 3. 启动流程 foundation/starting

包路径：`dpcq-game-server/src/main/java/com/doupo/server/foundation/starting/`

### 3.1 文件：DoupoGameServerApplication.java（入口）

[dpcq-game-server/.../DoupoGameServerApplication.java](../dpcq-game-server/src/main/java/com/doupo/server/DoupoGameServerApplication.java)

```java
@ComponentScan(basePackages = {"com.doupo", "org.gaming"})  // 扫描本项目 + 框架的 bean
@SpringBootApplication
public class DoupoGameServerApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication();
        application.addListeners(new ProjectBooter());   // 关键：挂启动监听器
        ...
    }
}
```

- `@ComponentScan` 扫 `com.doupo` 和 `org.gaming` 两个包（`org.gaming` 是内部框架）。
- `addListeners(new ProjectBooter())` 是最重要的一行，把游戏框架的启动逻辑挂到 Spring 生命周期上。

### 3.2 文件：ProjectBooter.java（框架总开关）

[dpcq-game-server/.../ProjectBooter.java](../dpcq-game-server/src/main/java/com/doupo/server/foundation/starting/ProjectBooter.java)

```java
if (event instanceof ContextRefreshedEvent) {
    Spring.setContext(event.getApplicationContext());
    LifecycleSupport.start();       // 启动所有 Lifecycle
} else if (event instanceof ContextClosedEvent) {
    LifecycleSupport.stop();        // 关闭所有 Lifecycle
}
```

- Spring 容器就绪 → 发 `ContextRefreshedEvent` → 调 `LifecycleSupport.start()`。
- 这一行会**按优先级启动后面所有的 `Lifecycle` 组件**（数据库、Actor 池、Netty）。

### 3.3 文件：BeanRegister.java（bean 注册分流）

[dpcq-game-server/.../BeanRegister.java](../dpcq-game-server/src/main/java/com/doupo/server/foundation/starting/BeanRegister.java)

它是一个 `BeanPostProcessor`，Spring 初始化**每一个 bean** 时都会经过它，按类型分流：

| bean 类型 | 注册到 | 作用 |
|-----------|--------|------|
| `Lifecycle` | `LifecycleSupport` | 统一启动/停止 |
| `@Controller` | `GameCmdManager` | **命令分发器** |
| `@Service` / `@Component` | `EventBus` | 事件总线 |

> **新人重点**：本项目里 `@Controller` 不是 Spring MVC 控制器，而是 `gaming-fakecmd` 框架的「命令处理器」标记。加了 `@Controller` 的类，里面的 `@PlayerCmd` 方法才会被注册进消息分发器。

### 3.4 文件：ServerFoundationLifecycle.java（占位）

[dpcq-game-server/.../ServerFoundationLifecycle.java](../dpcq-game-server/src/main/java/com/doupo/server/foundation/starting/ServerFoundationLifecycle.java)

一个空的 `Lifecycle`，`start()/stop()` 只打日志，属于「先占位、以后放初始化逻辑」的架子。

### 3.5 三个真正的 Lifecycle 的启动顺序

按 `Priority`（优先级）+ `Ordinal`（序数）排序：

| 顺序 | 类 | Priority/Ordinal | 职责 |
|------|-----|------------------|------|
| 1 | DatabaseLifecycle | INITIALIZATION / MIN | 加载数据库配置、建表 |
| 2 | PlayerActorPool | LOW / MIN | 创建玩家 Actor 池 |
| 3 | NettyLifecycle | LOW / _25 | 启动 Netty 监听 19090 |

---

## 4. 协议层 dpcq-protobuf + dpcq-common

### 4.1 Protobuf 原理（一句话）

你只写 `.proto` 定义文件，编译工具自动生成 Java 类。**只改 `.proto`，永远不要手改 `target/` 下的生成类**（会被覆盖）。

### 4.2 信封：wrapper.proto

[dpcq-protobuf/src/main/proto/wrapper.proto](../dpcq-protobuf/src/main/proto/wrapper.proto)

```proto
message MessageWrapper {
    int32 proto_id = 1;    // 协议号：里面装的是什么消息
    int64 request_id = 2;  // 请求编号：异步回包匹配用
    bytes  data     = 3;   // 内层消息序列化后的字节
    int32 code      = 4;   // 错误码
    string message  = 5;   // 错误信息
}
```

所有消息都装在这个信封里传输。`proto_id` 是信封上的编号，`data` 是里面装的信。

### 4.3 11 个 proto 文件全景

| 文件 | 领域 | 关键消息 |
|------|------|---------|
| [wrapper.proto](../dpcq-protobuf/src/main/proto/wrapper.proto) | 信封 | `MessageWrapper` |
| [heartbeat.proto](../dpcq-protobuf/src/main/proto/heartbeat.proto) | 未登录心跳 | `HeartbeatRequest`(10001) |
| [login.proto](../dpcq-protobuf/src/main/proto/login.proto) | 登录认证 | `LoginAuthReq`(50051) |
| [player_create.proto](../dpcq-protobuf/src/main/proto/player_create.proto) | 创建角色 | `PlayerCreateReq`(50351) |
| [player_init.proto](../dpcq-protobuf/src/main/proto/player_init.proto) | 初始化下推 | `PlayerInitBeginResp` |
| [player_runtime.proto](../dpcq-protobuf/src/main/proto/player_runtime.proto) | 运行时 | `HeartbeatReq`(50002) 等 |
| [hero_unlock.proto](../dpcq-protobuf/src/main/proto/hero_unlock.proto) | 英雄解锁下推 | `HeroUnlockIndexesResp` |
| [friend_init.proto](../dpcq-protobuf/src/main/proto/friend_init.proto) | 好友 | `FriendListReq`(51609) 等 |
| [server_group.proto](../dpcq-protobuf/src/main/proto/server_group.proto) | 服务器分组 | `ServerGroupInfoReq`(71601) |
| [main_stage.proto](../dpcq-protobuf/src/main/proto/main_stage.proto) | 主线章节下推 | `MainMapChapterInfoResp` |
| [scene.proto](../dpcq-protobuf/src/main/proto/scene.proto) | 场景（最大） | `ChangeSceneReq`、怪物等 |

### 4.4 协议号（proto_id）的两种声明方式

**① 请求类消息**：在 `.proto` 里用 `enum Proto { ID = xxx; }` 写死。看 [login.proto](../dpcq-protobuf/src/main/proto/login.proto)：

```proto
message LoginAuthReq {
    enum Proto {
        UNKNOWN = 0;
        ID = 50051;   // ← 登录请求的协议号
    }
    ...
}
```

**② 服务器主动下推类消息**：可能没有 enum，协议号以「魔法数字」直接写在 Java 代码里（见第 9 章的 `PlayerCreateHandler`）。

### 4.5 文件：dpcq-common 的 ProtocolIds.java

[dpcq-common/src/main/java/com/doupo/common/protocol/ProtocolIds.java](../dpcq-common/src/main/java/com/doupo/common/protocol/ProtocolIds.java)

```java
public final class ProtocolIds {
    public static final int HEARTBEAT = 10001;
    private ProtocolIds() {}
}
```

- 定位是「协议号集中管理」，但目前只定义了 `HEARTBEAT`，其它协议号都写在 proto enum 里，**这个类还没真正派上用场**（项目早期的痕迹）。
- `final class` + 私有构造器 = 只含常量的工具类，不可实例化、不可继承。

### 4.6 坑：两套心跳

| 心跳 | 协议号 | 定义处 | 处理类 |
|------|--------|--------|--------|
| 未登录心跳 | 10001 | heartbeat.proto | [HeartbeatHandler](../dpcq-game-server/src/main/java/com/doupo/server/module/system/HeartbeatHandler.java) |
| 已登录心跳 | 50002→50003 | player_runtime.proto | [PlayerRuntimeHandler](../dpcq-game-server/src/main/java/com/doupo/server/module/system/PlayerRuntimeHandler.java) |

区别：前者 `@PlayerCmd(needLogin = false)`（登录前），后者是登录后定时心跳。

---

## 5. 网络层 foundation/netty

包路径：`dpcq-game-server/src/main/java/com/doupo/server/foundation/netty/`

### 5.1 文件：NettyLifecycle.java（启动 Netty）

[dpcq-game-server/.../NettyLifecycle.java](../dpcq-game-server/src/main/java/com/doupo/server/foundation/netty/NettyLifecycle.java)

```java
@Value("${game.netty.port:19090}")   private int port;        // 从配置读端口
@Value("${game.netty.idle-seconds:60}") private int idleSeconds;
@Value("${game.netty.boss-threads:1}")  private int bossThreads;
@Value("${game.netty.worker-threads:4}") private int workerThreads;

public void start() {
    server = new NettySocketServer(bossThreads, workerThreads);
    server.startServer(port, new DoupoWebSocketChannelInitializer(idleSeconds, new GameNettyHandler()));
}
```

- `@Value` 注入配置，冒号后是默认值。
- `startServer` 内部调用 Netty 真正 `bind(19090)`，**真正的监听代码在外部库 `gaming-ruler` 的 `NettySocketServer` 里**，不在本项目源码。

### 5.2 文件：DoupoWebSocketChannelInitializer.java（装配流水线）

[dpcq-game-server/.../DoupoWebSocketChannelInitializer.java](../dpcq-game-server/src/main/java/com/doupo/server/foundation/netty/DoupoWebSocketChannelInitializer.java)

每个连接建立时，`initChannel` 给它装一串处理器（pipeline）：

```
HttpServerCodec → ChunkedWrite → HttpObjectAggregator
→ IdleStateHandler(60秒) → WebSocketServerProtocolHandler
→ BinaryFrameDecoder → BinaryFrameEncoder → GameNettyHandler
```

数据进来依次经过每个处理器，最后到 `GameNettyHandler`（业务入口）。

### 5.3 文件：GameFrameCodec.java（拆包/封包）

[dpcq-game-server/.../GameFrameCodec.java](../dpcq-game-server/src/main/java/com/doupo/server/foundation/netty/GameFrameCodec.java)

定义了一套**自定义二进制帧**格式：

```
[长度 4字节][协议号 4字节][校验和 4字节(登录后才有)][正文]
```

- `decode()` 按大端序读出 `protocolId / checksum / body`。
- `authenticated`（是否登录）决定有没有校验和段——登录前校验和是 0。
- `isGameFrame()` 判断一段字节是不是这个格式。

### 5.4 文件：GameNettyHandler.java（业务入口）

[dpcq-game-server/.../GameNettyHandler.java](../dpcq-game-server/src/main/java/com/doupo/server/foundation/netty/GameNettyHandler.java)

四个关键方法：

- **channelActive**（连接建立）：`new PlayerConnectionContext`，挂到 channel 的 attr 上，登记 `ONLINE` 表。
- **channelInactive**（断开）：从 `ONLINE` 移除，在线数 -1。
- **channelRead**（收数据）：先判断二进制帧还是 `MessageWrapper`，解出 `protoId + data` 后调 `context.tell()`。
- **userEventTriggered**（事件）：处理 60 秒空闲超时，关连接。

---

## 6. 玩家 Actor 层 foundation/player

包路径：`dpcq-game-server/src/main/java/com/doupo/server/foundation/player/`

这是整个架构**最核心、最难理解**的一层。

### 6.1 核心思想：为什么用 Actor

每个玩家的消息必须**串行处理**（避免并发改同一份玩家数据）。Akka 的 Actor 一个时刻只处理一条消息，天然保证串行。做法是：**把大量玩家分摊到固定数量的 Actor 上**，每个 Actor 轮流转多个玩家。

### 6.2 文件：PlayerConnectionContext.java（连接上下文）

[dpcq-game-server/.../PlayerConnectionContext.java](../dpcq-game-server/src/main/java/com/doupo/server/foundation/player/PlayerConnectionContext.java)

每个玩家连接一个实例，装状态：

| 字段 | 含义 |
|------|------|
| `channel` | Netty 通道，回包靠它 |
| `actor` | 被分配到的 Actor |
| `playerId` | 玩家 ID（登录后绑定） |
| `login` | 是否已登录 |
| `currentMessageId` | 当前消息编号 |
| `account` | 绑定的账号 |
| `serverZone` | 服务器区号（默认 1） |

关键方法：
- `tell(wrapper)`：把消息投递给 Actor。
- `write(wrapper)`：`channel.writeAndFlush` 回包。
- `bindAccount(account)`：绑定账号，并根据账号 ID 算 `playerId`，再按 `playerId` 重新分配 Actor。

### 6.3 文件：PlayerActorPool.java（Actor 池）

[dpcq-game-server/.../PlayerActorPool.java](../dpcq-game-server/src/main/java/com/doupo/server/foundation/player/PlayerActorPool.java)

- `start()`：创建 `PARALLELISM_MAX * 2` 个 `PlayerActor`，存数组。
- `nextActor()`：轮询分配（新连接用，负载均衡）。
- `actorFor(playerId)`：`playerId % 数组长度` 取模分配（登录后用，同一玩家固定 Actor）。

### 6.4 文件：PlayerActor.java（玩家 Actor）

[dpcq-game-server/.../PlayerActor.java](../dpcq-game-server/src/main/java/com/doupo/server/foundation/player/PlayerActor.java)

```java
public Receive createReceive() {
    return receiveBuilder()
        .match(PlayerProtocolMessage.class, PlayerCmdRegister.INS::handle)
        .build();
}
```

收到 `PlayerProtocolMessage` 就交给 `PlayerCmdRegister.handle`（框架内部），框架根据 `protoId` 找到对应的 `@PlayerCmd` 方法执行。

### 6.5 文件：PlayerProtocolMessage.java（消息载体 + 回包）

[dpcq-game-server/.../PlayerProtocolMessage.java](../dpcq-game-server/src/main/java/com/doupo/server/foundation/player/PlayerProtocolMessage.java)

实现 `IPlayerCmdMessage`，是 Actor 信箱里的消息体。两个关键方法：

- `getCmd()`：返回 `protoId`，供框架分发。
- `onResponse(response)`：handler 返回后，把「请求号」映射成「响应号」，调 `context.write` 回包（详见第 12 章）。

---

## 7. 数据库层 foundation/database

### 文件：DatabaseLifecycle.java

[dpcq-game-server/.../DatabaseLifecycle.java](../dpcq-game-server/src/main/java/com/doupo/server/foundation/database/DatabaseLifecycle.java)

`start()` 流程：
1. 从 `game.database.config-file` 加载数据库配置。
2. 逐个 `SELECT 1` 校验连接。
3. 收集所有 `@Table` / `@LogTable` 注解的实体类。
4. `SlimDao.build(configs, entityClasses)` 建表 + 初始化 DAO。

---

## 8. 数据层 dpcq-pojo

### 文件：Account.java

[dpcq-pojo/src/main/java/com/doupo/pojo/account/Account.java](../dpcq-pojo/src/main/java/com/doupo/pojo/account/Account.java)

用 `gaming-db` 框架的注解把 Java 类映射成数据库表：

```java
@Repository
@Table(name = "account", comment = "Game account", dbAlias = "game", indexs = {...})
public class Account extends AbstractEntity {
    @Id(strategy = Strategy.AUTO) private long id;
    @Column(name = "account_name", length = 64) private String accountName;
    @Column(name = "password_hash", length = 100) private String passwordHash;
    ...
}
```

- `@Table` 声明表，`@Column` 声明列，`@Id` 声明主键。
- 继承 `AbstractEntity`，由框架提供 CRUD 能力。
- 字段映射账号的账号名、密码哈希、会话 token 哈希、会话过期时间等。

---

## 9. 业务模块 module

包路径：`dpcq-game-server/src/main/java/com/doupo/server/module/`

### 9.1 module/system —— 系统类

**HeartbeatHandler.java**（未登录心跳，最小例子）：
[dpcq-game-server/.../HeartbeatHandler.java](../dpcq-game-server/src/main/java/com/doupo/server/module/system/HeartbeatHandler.java)

```java
@Controller
public class HeartbeatHandler {
    @PlayerCmd(needLogin = false)
    public HeartbeatResponse heartbeat(IPlayerContext context, HeartbeatRequest request) {
        return HeartbeatResponse.newBuilder().setServerTime(System.currentTimeMillis()).build();
    }
}
```

「收到请求 → 返回响应」的最小模板。

**PlayerCreateHandler.java**（创建角色，服务器连续下推）：
[dpcq-game-server/.../PlayerCreateHandler.java](../dpcq-game-server/src/main/java/com/doupo/server/module/system/PlayerCreateHandler.java)

收到 `PlayerCreateReq`(50351) 后，连续下推一串初始化消息：

| 协议号 | 消息 | 含义 |
|--------|------|------|
| 50360 | 空包 | 创建响应 |
| 50352 | PlayerInitBeginResp | 初始化开始 |
| 50453 | HeroUnlockResp | 解锁单个英雄 |
| 50451 | HeroUnlockIndexesResp | 解锁英雄索引列表 |
| 61951 | MainMapChapterInfoResp | 主线章节 |
| 50353 | PlayerInitEndResp | 初始化结束 |

> 这就是 `HeroUnlockIndexesResp` 的「跳转去向」：协议号 50451 在这里被推送。

**PlayerRuntimeHandler.java**（已登录后的运行时）：
[dpcq-game-server/.../PlayerRuntimeHandler.java](../dpcq-game-server/src/main/java/com/doupo/server/module/system/PlayerRuntimeHandler.java)

处理已登录心跳（50002）、IP 归属地（50371）、创建英雄职业（50502）。

**ServerGroupInfoHandler.java**（服务器分组）：
[dpcq-game-server/.../ServerGroupInfoHandler.java](../dpcq-game-server/src/main/java/com/doupo/server/module/system/ServerGroupInfoHandler.java)

返回服务器分组信息（71601）。

### 9.2 module/login —— 登录

**LoginAuthHandler.java**：
[dpcq-game-server/.../LoginAuthHandler.java](../dpcq-game-server/src/main/java/com/doupo/server/module/login/LoginAuthHandler.java)

`@PlayerCmd(needLogin = false)`，处理 `LoginAuthReq`(50051)：
1. 调 `AccountAuthService.authenticateByGuideSignature` 验签名。
2. 校验 `serverId == 1`。
3. 通过则 `bindAccount(account)`，返回 `LoginAuthResp`。

### 9.3 module/account —— 账号

**AccountAuthService.java**：
[dpcq-game-server/.../AccountAuthService.java](../dpcq-game-server/src/main/java/com/doupo/server/module/account/AccountAuthService.java)

账号核心逻辑：
- `register`：手机号注册，BCrypt 存密码哈希。
- `login`：密码登录，生成会话 token。
- `authenticateByToken`：按 token 认证。
- `authenticateByGuideSignature`：按 HMAC-SHA256 签名认证（引导登录用）。

**AccountApiController.java**：
[dpcq-game-server/.../AccountApiController.java](../dpcq-game-server/src/main/java/com/doupo/server/module/account/AccountApiController.java)

HTTP 接口：`POST /login`（JSON）、`GET/POST /register`（注册页 + 表单）。

### 9.4 module/social —— 社交

**FriendInitHandler.java**：
[dpcq-game-server/.../FriendInitHandler.java](../dpcq-game-server/src/main/java/com/doupo/server/module/social/FriendInitHandler.java)

好友列表（51609）、好友申请（51605）、黑名单（51607）、红点（62902），目前都是返回空 `DefaultInstance` 的占位实现。

### 9.5 module/scene —— 场景（最大业务模块）

**SceneHandler.java**：
[dpcq-game-server/.../SceneHandler.java](../dpcq-game-server/src/main/java/com/doupo/server/module/scene/SceneHandler.java)

处理切场景、新手引导打怪、主线章节、任务奖励等，含大量 `@PlayerCmd` 方法和怪物快照、玩家单位的构建逻辑。

### 9.6 module/resource —— 资源镜像

**ResourceMirrorService.java / ResourceMirrorController.java**：
[dpcq-game-server/.../ResourceMirrorController.java](../dpcq-game-server/src/main/java/com/doupo/server/module/resource/ResourceMirrorController.java)

提供游戏资源文件的本地镜像/缓存，请求 `/imlj2_android_alpha1/Ljxs/**` 时从上游下载并缓存。

---

## 10. HTTP 入口层

**HealthController.java**（健康检查）：
[dpcq-game-server/.../HealthController.java](../dpcq-game-server/src/main/java/com/doupo/server/health/HealthController.java)

`GET /health` 返回 `{"status":"UP","service":"dpcq-game-server"}`。

**LoginGuideController.java**（登录引导）：
[dpcq-game-server/.../LoginGuideController.java](../dpcq-game-server/src/main/java/com/doupo/server/module/login/LoginGuideController.java)

`GET /login/v2/imlj2/37wan/client/login/guide` 和 `/check`，给客户端返回游戏服的 IP + 端口 + 登录签名。

---

## 11. 一次登录的完整链路

玩家打开游戏 → 连上 → 登录 → 回包，10 个文件按执行顺序：

| # | 文件 | 阶段 | 做什么 |
|---|------|------|--------|
| 1 | DoupoGameServerApplication | 启动 | `main` 入口 |
| 2 | ProjectBooter | 启动 | 捕获事件，调 `LifecycleSupport.start()` |
| 3 | NettyLifecycle | 启动 | 监听 19090 |
| 4 | DoupoWebSocketChannelInitializer | 连接 | 装配 pipeline |
| 5 | GameNettyHandler(channelActive) | 连接 | 建 PlayerConnectionContext |
| 6 | PlayerConnectionContext(构造器) | 连接 | 绑定 channel + 分配 Actor |
| 7 | PlayerActorPool(nextActor) | 连接 | 轮询分配 Actor |
| 8 | GameNettyHandler(channelRead) | 请求 | 收到字节，判断帧格式 |
| 9 | GameFrameCodec | 请求 | 拆包，解出 protocolId + body |
| 10 | PlayerConnectionContext(tell) | 请求 | 投递给 Actor |

之后（业务收尾）：`PlayerActor` → `LoginAuthHandler` → `AccountAuthService` → `PlayerProtocolMessage.onResponse` → `context.write` 回包。

---

## 12. 响应号映射机制

框架只知道「请求号」，但回包常要回「响应号」。映射表在 [PlayerProtocolMessage.java](../dpcq-game-server/src/main/java/com/doupo/server/foundation/player/PlayerProtocolMessage.java) 的 `onResponse` 里：

| 请求号 | 响应号 | 含义 |
|--------|--------|------|
| 50002 | 50003 | 心跳 |
| 50051 | 50052 | 登录（特殊分支，还额外推 50354） |
| 50371 | 50365 | IP 归属地更新 |
| 50502 | 50503 | 创建英雄职业 |
| 50780 | 50753 | 切场景 |
| 50755 | 50756 | 场景加载完成 B |
| 51605 | 51606 | 好友申请列表 |
| 71601 | 71602 | 服务器分组 |

> 新人加协议时，若响应号与请求号不同，要在这里补 case。

---

## 13. 新人加一条协议的标准流程

1. 在 [dpcq-protobuf/src/main/proto/](../dpcq-protobuf/src/main/proto/) 里加 `.proto` 定义（含 `enum Proto { ID = xxx; }`），编译生成 Java 类。
2. 写一个 `@Controller` 类 + `@PlayerCmd` 方法（参考 `HeartbeatHandler`）。
3. 若响应号与请求号不同，在 `PlayerProtocolMessage.onResponse` 补映射。
4. 重启验证。

---

## 14. 已知坑与注意事项

1. **依赖缺失**：`gaming-fakecmd` / `gaming-db` / `gaming-prefab` 三个私有库要先 `mvn install` 进本地仓库，否则 IDE 一片红（`IPlayerContext`、`org.gaming.db.*` 解析不了）。
2. **不要手改生成类**：`dpcq-protobuf/target/generated-sources/` 下的类都是产物，改 `.proto` 才是正道。
3. **两个端口别搞混**：18080 HTTP / 19090 Netty 长连接。
4. **两套心跳别搞混**：10001 未登录 / 50002 已登录。
5. **`@Controller` 不是 Spring MVC**：它是 `gaming-fakecmd` 的命令处理器标记。
6. **真正的监听代码在外部库**：`gaming-ruler` 的 `NettySocketServer`，不在本项目源码。
