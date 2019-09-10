#Handler机制

###1.为什么要用 `Handler`消息传递机制

答：**多个线程并发更新UI的同时 保证线程安全**

具体描述如下

![img](https://imgconvert.csdnimg.cn/aHR0cDovL3VwbG9hZC1pbWFnZXMuamlhbnNodS5pby91cGxvYWRfaW1hZ2VzLzk0NDM2NS03NDc5YjRhOGI4ZmU0OGJmLnBuZz9pbWFnZU1vZ3IyL2F1dG8tb3JpZW50L3N0cmlwJTdDaW1hZ2VWaWV3Mi8yL3cvMTI0MA)

###2.作用
在多线程的应用场景中，将工作线程中需更新UI的操作信息 传递到 UI主线程，从而实现 工作线程对UI的更新处理，最终实现异步消息的处理

![](https://imgconvert.csdnimg.cn/aHR0cDovL3VwbG9hZC1pbWFnZXMuamlhbnNodS5pby91cGxvYWRfaW1hZ2VzLzk0NDM2NS00YTY0MDM4NjMyYzRjODhmLnBuZz9pbWFnZU1vZ3IyL2F1dG8tb3JpZW50L3N0cmlwJTdDaW1hZ2VWaWV3Mi8yL3cvMTI0MA)

###3.相关概念
>即 Handler、Message、Message Queue、Looper，

* handler 处理者，主线程和子线程的通信媒介，添加 message到message Queue,处理looper分派过来的Message
* message 消息，线程间通信的数据单元，存储需要操作的通信信息
* Message Queue 消息队列，一种数据结构，先进先出，存储Handler发过来的Message
* Looper 循环器，Handler和Message Queue的通信媒介，循环取出消息队列的消息，将取出的消息分送给对应的handler（一个Looper可以对应多个线程Handler）

###4.工作流程

* 1.异步通信准备：在主线程中创建Handler,Looper,Message Queue,此时Looper进入消息循环，Handler自动绑定了主线程的Looper,Message Queue.

* 2.消息发送：handler发送消息到消息队列中
* 3.消息循环：Looper循环取出Message Queue中的message,分发给创建该消息的处理者handler
* 4.消息处理：handler接收Looper发送过来的消息Message,然后处理message进行UI操作

###5特别注意
线程（Thread）、循环器（Looper）、处理者（Handler）之间的对应关系如下：

1个线程（Thread）只能绑定 1个循环器（Looper），但可以有多个处理者（Handler）
1个循环器（Looper） 可绑定多个处理者（Handler）
1个处理者（Handler） 只能绑定1个1个循环器（Looper）

![](https://imgconvert.csdnimg.cn/aHR0cDovL3VwbG9hZC1pbWFnZXMuamlhbnNodS5pby91cGxvYWRfaW1hZ2VzLzk0NDM2NS02MWIzODdjMGU2NmVkOGVlLnBuZz9pbWFnZU1vZ3IyL2F1dG8tb3JpZW50L3N0cmlwJTdDaW1hZ2VWaWV3Mi8yL3cvMTI0MA)
