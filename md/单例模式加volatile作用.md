# 单例模式加volatile作用
###1 保证内存可见性
####1.1 基本概念
  * ”可见性“ 是指线程之间的可见性，一个线程修改的状态对另一个线程是可见的。也就是一个线程修改的结果，另一个线程马上就能看到。
* “原子性”由Java内存模型来直接保证的原子性变量操作包括read、load、use、assign、store和write六个，大致可以认为基础数据类型的访问和读写是具备原子性的。如果应用场景需要一个更大范围的原子性保证，Java内存模型还提供了lock和unlock操作来满足这种需求，尽管虚拟机未把lock与unlock操作直接开放给用户使用，但是却提供了更高层次的字节码指令monitorenter和monitorexit来隐匿地使用这两个操作，这两个字节码指令反映到Java代码中就是同步块---synchronized关键字，因此在synchronized块之间的操作也具备原子性。

* “有序性”对于Java 指 “指令重排序”现象和 “工作内存和主内存同步延迟”现象

####1.2 实现原理
  当对非volatile变量进行读写的时候，每个线程先从主内存拷贝变量到CPU缓存中，如果计算机有多个CPU，每个线程可能在不同的CPU上被处理，这意味着每个线程可以拷贝到不同的CPU cache中。
  volatile变量不会被缓存在寄存器或者对其他处理器不可见的地方，保证了每次读写变量都从主内存中读，跳过CPU cache这一步。当一个线程修改了这个变量的值，新值对于其他线程是立即得知的。

![](https://img-blog.csdn.net/20170324153250020?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMDI1NTgxOA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

###2 禁止指令重排
####2.1 基本概念
  指令重排序是JVM为了优化指令、提高程序运行效率，在不影响单线程程序执行结果的前提下，尽可能地提高并行度。指令重排序包括编译器重排序和运行时重排序。
  在JDK1.5之后，可以使用volatile变量禁止指令重排序。针对volatile修饰的变量，在读写操作指令前后会插入内存屏障，指令重排序时不能把后面的指令重排序到内存屏

示例说明：
```
double r = 2.1; //(1) 
double pi = 3.14;//(2) 
double area = pi*r*r;//(3)
```
  虽然代码语句的定义顺序为1->2->3，但是计算顺序1->2->3与2->1->3对结果并无影响，所以编译时和运行时可以根据需要对1、2语句进行重排序。

####2.2 指令重排带来的问题
如果一个操作不是原子的，就会给JVM留下重排的机会。

线程A中

```
{
    context = loadContext();
    inited = true;
}

线程B中
{
    if (inited) 
        fun(context);
}

```
  如果线程A中的指令发生了重排序，那么B中很可能就会拿到一个尚未初始化或尚未初始化完成的context,从而引发程序错误。

###3.指令重排在双重锁定单例模式中的影响
```
public class Singleton {
    private volatile static Singleton uniqueInstance;
    private Singleton(){}
    public static Singleton getInstance(){
        if(uniqueInstance == null){
         // B线程检测到uniqueInstance不为空
            synchronized(Singleton.class){
                if(uniqueInstance == null){
                    uniqueInstance = new Singleton();
                     // A线程被指令重排了，刚好先赋值了；但还没执行完构造函数。
                }
            }
        }
        return uniqueInstance;// 后面B线程执行时将引发：对象尚未初始化错误。
    }
}
```
1.线程A先执行：第一步：分配uniqueInstance的内存空间，第二步：初始化对象，第三步：设置uniqueInstance指向分配的内存空间。这事因为指令重排，先走第三步，然后第二步。

2.线程B执行：线程A因为指令重排，初始化分配对象之前就已经将其赋值给instance引用，刚好线程B进入方法判断uniqueInstance引用不为null，然后就将其返回使用，导致对象尚未初始化出错。

###4. 适用场景
（1）volatile是轻量级同步机制。在访问volatile变量时不会执行加锁操作，因此也就不会使执行线程阻塞，是一种比synchronized关键字更轻量级的同步机制。
（2）volatile**无法同时保证内存可见性和原子性。加锁机制既可以确保可见性又可以确保原子性，而volatile变量只能确保可见性**。
（3）volatile不能修饰写入操作依赖当前值的变量。声明为volatile的简单变量如果当前值与该变量以前的值相关，那么volatile关键字不起作用，也就是说如下的表达式都不是原子操作：“count++”、“count = count+1”。
（4）当要访问的变量已在synchronized代码块中，或者为常量时，没必要使用volatile；
（5）volatile屏蔽掉了JVM中必要的代码优化，所以在效率上比较低，因此一定在必要时才使用此关键字。

原文链接：https://blog.csdn.net/u010255818/article/details/65633033