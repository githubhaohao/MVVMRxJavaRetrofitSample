![Cover](https://github.com/githubhaohao/ImageRoom/blob/master/Images/country-1295915__340.png?raw=true)

在GitHub上看了几个关于MVVM设计模式的例子，发现他们并没有做到 View 层与 Model 层逻辑的完全分离，以此实践作为对 MVVM 的总结。

## 效果预览
![result](https://github.com/githubhaohao/MVVMRxJavaRetrofitSample/blob/master/image/sample.gif?raw=true)
## 准备知识
### MVC
![mvc](https://github.com/githubhaohao/ImageRoom/blob/master/Images/mvvm/mvc.PNG?raw=true)

- **视图（View）**：用户界面。
- **控制器（Controller）**：业务逻辑
- **模型（Model）**：数据保存

---
1. View 传送指令到 Controller
2. Controller 完成业务逻辑后，要求 Model 改变状态
3. Model 将新的数据发送到 View，使用户得到反馈

**缺陷**:View 和 Model 是相互可知，耦合性大，像 Activity 或者 Fragment 既是 Controller 层，由是 View 层，造成工程的可扩展性可维护性非常差。

### MVP
![mvp](https://github.com/githubhaohao/ImageRoom/blob/master/Images/mvvm/mvp.png?raw=true)

在 MVP 设计模式中，Controller 变成了 Presenter。

1. 各层之间的通信，都是双向的。
2. View 与 Model 不直接发生联系，都通过 Presenter 进行间接通信。
3. Model 层与 Presenter 层，Presenter 层与 View 层之间通过接口建立联系。

采用 MVP 设计模式，Activity 与 Fragment 只位于 View 层。

**MVP 的缺陷在于**:由于我们使用了接口的方式去连接 View 层和  Presenter 层，这样就导致了一个问题，当你的页面逻辑很复杂的时候，你的接口会有很多，如果你的 app 中有很多个这样复杂的页面，维护接口的成本就会变的非常的大。

### MVVM
![MVVM](https://github.com/githubhaohao/ImageRoom/blob/master/Images/mvvm/mvvp.PNG?raw=true)

MVVM 模式将 Presenter 改名为 ViewModel，基本上与 MVP 模式完全一致。
**区别在于**: View 层与 ViewModel 层通过`DataBinding`相互绑定，View的变动，自动反映在 ViewModel，反之亦然。

### [RxJava](https://github.com/ReactiveX/RxJava )

![RxJava](https://github.com/githubhaohao/ImageRoom/blob/master/Images/mvvm/kotlin-android-rxjava.png?raw=true)

RxJava 在 GitHub 主页上的自我介绍是 "a library for composing asynchronous and event-based programs using observable sequences for the Java VM"（一个在 Java VM 上使用可观测的序列来组成异步的、基于事件的程序的库）。

Rx 是微软 .Net 的一个响应式扩展，Rx 借助可观测的序列提供一种简单的方式来创建异步的，基于事件驱动的程序。2012 年 Netflix 为了应对不断增长的业务需求开始将 .NET Rx 迁移到 JVM 上面。并于 13 年二月份正式向外展示了 RxJava 。

从语义的角度来看， RxJava 就是 .NET Rx 。从语法的角度来看， Netflix 考虑到了对应每个 Rx 方法,保留了 Java 代码规范和基本的模式。

RxJava 本质上是一个异步操作库，是一个能让你用极其简洁的逻辑去处理繁琐复杂任务的异步事件库。

简而言之，RxJava 可以用几个关键字概括：**简洁**，**队列化**，**异步**。

### [Retrofit](https://github.com/square/retrofit)

![retrofit](https://github.com/githubhaohao/ImageRoom/blob/master/Images/mvvm/android-libs-retrofit-1-638.jpg?raw=true)

一个 Android 和 Java 上 HTTP 库（利用注解和 okhttp 来实现和服务器的数据交互）。

{% blockquote %}
[**Retrofit 官方文档:http://square.github.io/retrofit/**](http://square.github.io/retrofit/)
{% endblockquote %}

### [DataBinding](https://developer.android.com/topic/libraries/data-binding/index.html)

![data-binding](https://github.com/githubhaohao/ImageRoom/blob/master/Images/mvvm/data_binding.png?raw=true)

在今年的Google IO 2015 中，Google 在 support-v7 中新增了 Data Binding，使用 Data Binding可以直接在布局的 xml 中绑定布局与数据，从而简化代码，Android Data Binding是Android 的 MVVM 框架。因为 Data Binding 是包含在 support-v7 包里面的，所以可以向下兼容到最低 Android 2.1 (API level 7+).

