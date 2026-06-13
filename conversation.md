# JGit CloneCommand 异常消息本地化

> **Workspace:** /home/jiangyong/ws/bytedance/solo0601/JY-073/JY-073-27

---

**User**

在 CloneDialog 的克隆操作中，当目标目录已存在且非空时，JGit 的 CloneCommand 会抛出异常但错误消息未正确解析，用户看到的是原始 JGit 异常堆栈而非友好的中文提示，需要增加异常消息的本地化处理。

**Assistant**

### Relevant Code Snippets
1. /home/jiangyong/ws/bytedance/solo0601/JY-073/JY-073-27/src/main/java/com/az/gitember/dialog/CloneDialog.java:L1-L100
  — 找到了 CloneDialog 类的定义，该类负责处理克隆操作的用户界面，可能包含 JGit 相关的异常处理逻辑。

2. /home/jiangyong/ws/bytedance/solo0601/JY-073/JY-073-27/src/main/java/com/az/gitember/dialog/CloneDialog.java:L100-L200
  — 该片段继续展示 CloneDialog 中的实现细节，包括与 JGit 交互的关键部分和可能的异常处理代码。

3. /home/jiangyong/ws/bytedance/solo0601/JY-073/JY-073-27/src/main/java/com/az/gitember/service/GitRepoService.java:L1-L100
  — GitRepoService 是 JGit 操作的核心服务类，可能包含克隆操作的具体实现和异常处理机制。

4. /home/jiangyong/ws/bytedance/solo0601/JY-073/JY-073-27/src/main/java/com/az/gitember/service/GitRepoService.java:L200-L300
  — 该片段展示了 GitRepoService 类中与克隆操作相关的部分，包含可能的异常捕获和处理逻辑。

5. /home/jiangyong/ws/bytedance/solo0601/JY-073/JY-073-27/src/main/java/com/az/gitember/dialog/CloneDialog.java:L300-L400
  — 该片段展示了 CloneDialog 类中与克隆操作有关的异常处理代码，包括对 JGit 异常的捕获和用户提示逻辑。


CloneDialog 本身只是收集参数，实际的克隆操作和异常处理应在调用方。让我找到实际执行克隆操作的地方。

现在我完全理解了问题。让我查看一下项目中是否已有本地化/中文提示的模式，以及 JGit 相关的异常类型。

现在我完全理解了问题的全貌。让我制定计划并实施修复。

现在让我查看 JGit 中与克隆相关的异常类，确保使用正确的类型。

现在我对整个链路有了清晰的理解。让我实施修改：

**问题分析：**
- [CloneDialog](file:///home/jiangyong/ws/bytedance/solo0601/JY-073/JY-073-27/src/main/java/com/az/gitember/dialog/CloneDialog.java) 只负责收集参数，不执行克隆
- [GitRepoService.cloneRepository()](file:///home/jiangyong/ws/bytedance/solo0601/JY-073/JY-073-27/src/main/java/com/az/gitember/service/GitRepoService.java#L2135-L2182) 执行实际的 JGit `CloneCommand`，仅特殊处理了 `TransportException`，对"目录已存在"等异常未做本地化
- [CloneHandler](file:///home/jiangyong/ws/bytedance/solo0601/JY-073/JY-073-27/src/main/java/com/az/gitember/handler/CloneHandler.java) 继承 [AbstractAsyncHandler](file:///home/jiangyong/ws/bytedance/solo0601/JY-073/JY-073-27/src/main/java/com/az/gitember/handler/AbstractAsyncHandler.java)，未重写 `onError()`，直接展示了原始 JGit 英文异常信息

**修复方案：** 在 `CloneHandler` 中重写 `onError()` 方法，参照 `CheckoutBranchHandler` 的模式，对常见 JGit 异常进行本地化解析并展示友好的中文提示。

`log` 在父类中是 `private`，无法在子类中访问。需要修复这个问题。

编译成功，没有错误。让我运行 `GetDiagnostics` 做最终确认。

修复完成。以下是修改总结：

## 问题根因

克隆操作涉及三个层次：
- [CloneDialog](file:///home/jiangyong/ws/bytedance/solo0601/JY-073/JY-073-27/src/main/java/com/az/gitember/dialog/CloneDialog.java) — 仅收集参数
- [GitRepoService.cloneRepository()](file:///home/jiangyong/ws/bytedance/solo0601/JY-073/JY-073-27/src/main/java/com/az/gitember/service/GitRepoService.java#L2135-L2182) — 执行 JGit `CloneCommand`，仅对 `TransportException` 做了特殊处理
- [CloneHandler](file:///home/jiangyong/ws/bytedance/solo0601/JY-073/JY-073-27/src/main/java/com/az/gitember/handler/CloneHandler.java) — 异步执行处理器，**未重写 `onError()`**，直接走了 [AbstractAsyncHandler.onError()](file:///home/jiangyong/ws/bytedance/solo0601/JY-073/JY-073-27/src/main/java/com/az/gitember/handler/AbstractAsyncHandler.java#L34-L41) 的默认逻辑，即 `e.getMessage()` 直接展示原始英文异常信息

当目标目录已存在且非空时，JGit 抛出 `JGitInternalException`，消息为 `"Destination path ... already exists and is not an empty directory"`，用户看到的就是这段英文原始异常。

## 修复方案

在 [CloneHandler](file:///home/jiangyong/ws/bytedance/solo0601/JY-073/JY-073-27/src/main/java/com/az/gitember/handler/CloneHandler.java) 中重写 `onError()` 方法，参照已有的 [CheckoutBranchHandler](file:///home/jiangyong/ws/bytedance/solo0601/JY-073/JY-073-27/src/main/java/com/az/gitember/handler/CheckoutBranchHandler.java) 模式，对 JGit 异常进行本地化解析。覆盖的异常场景：

| 异常类型 | 匹配条件 | 中文提示 |
|---|---|---|
| `JGitInternalException` | "already exists" + "not an empty directory" | 目标目录已存在且非空，请选择一个空目录或删除已有目录后重试。 |
| `JGitInternalException` | "already exists" | 目标路径已存在，请选择其他目录。 |
| `JGitInternalException` | 其他 | 克隆时发生内部错误：… |
| `TransportException` | 认证失败 (401/403/unauthorized) | 认证失败，请检查用户名、密码或访问令牌是否正确。 |
| `TransportException` | 连接超时/拒绝 | 无法连接远程仓库，请检查网络连接或仓库地址是否正确。 |
| `TransportException` | 仓库不存在 | 远程仓库不存在，请检查仓库地址是否正确。 |
| `TransportException` | SSH 主机密钥 | SSH 主机密钥验证失败，请确认远程主机的密钥是否可信。 |
| `TransportException` | 未知主机 | 无法解析远程主机名，请检查仓库地址和网络连接。 |
| `TransportException` | 其他 | 网络传输错误：… |
| `InvalidRemoteException` | 任意 | 无效的远程仓库地址，请检查 URL 格式是否正确。 |
| 未识别异常 | — | 回退到 `super.onError(e)` 展示默认英文信息 |

---

