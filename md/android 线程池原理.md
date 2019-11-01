##android 线程池原理

###线程池的好处
1.频繁的创建和销毁线程，会带来性能的问题。线程的创建和销毁都需要时间，当有大量的线程创建和销毁时，那么这些时间的消耗则比较明显，将导致性能上的缺失。
2.线程池方便管理线程，定时执行线程，间隔执行线程。线程池能控制线程的最大并发数量，避免大量抢占资源导致的阻塞现象。

###ThreadPoorExecutor构造方法

```
threadPoolExecutor = new ThreadPoolExecutor(3,5,1, TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>(128));

 for (int i = 0; i < 30; i++) {
            final int finalI = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(2000);
                    Log.e("ThreadPoolExecutor", "rune: " + finalI);
                    Log.e("ThreadPoolExecutor", Thread.currentThread().getName());

                }
            };
            threadPoolExecutor.execute(runnable);
}

```

第一个参数corePoolSize  线程池中核心线程的数量，第二个参数maximumPoolSize  线程池中最大线程数量，
第三个参数keepAliveTime 非核心线程的超时时长，第四个参数workQueue 线程池中的任务队列。

### ThreadPoorExecutor执行任务的顺序

（corePoolSize -> workQueue -> maximumPoolSize）

1. 当未超过核心线程数时，就直接创建一个核心线程去执行任务。
2. 当超过核心线程数，就将任务加入到workQueue的任务队列中等待
3. 当任务队列中任务添满时候，在不超过最大线程数的情况下启动线程去处理任务
4. 当线程数量超过最大线程数时，RejectedExecutionHandler对象通知调用者

###Android中常用几种线程线程池

####1 FixedThreadPool 

核心线程数和最大线程数相同，一个无限的任务队列，就是说线程池一直有固定的线程数处理务。

```
  ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
  for (int i = 0; i < 30; i++) {
        final int finalI = i;
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                SystemClock.sleep(3000);
                Log.e("fixedThreadPool", "run: "+ finalI);
                Log.e("fixedThreadPool", Thread.currentThread().getName());

            }
        };
   fixedThreadPool.execute(runnable);
```

####2 CachedThreadPool

没有核心线程，最大线程数为2^31-1，线程空闲60秒后被回收，任务队列SynchronousQueue是一个不存储的，所以这个线程的特点是只要任务一来，马上就有线程去执行。

```
  ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
   for (int i = 0; i < 30; i++) {
       final int finalI = i;
       Runnable runnable = new Runnable(){
           @Override
           public void run() {
               SystemClock.sleep(2000);
               Log.e("cachedThreadPool", "run: "+ finalI);
               Log.e("cachedThreadPool", Thread.currentThread().getName());                }
       };
   cachedThreadPool.execute(runnable);
  SystemClock.sleep(1000);
```

#### 3 SingleThreadPool

线程池中只有一个核心线程，按顺序执行队列中的任务

```cpp
   public static ExecutorService newSingleThreadExecutor() {
        return new FinalizableDelegatedExecutorService
            (new ThreadPoolExecutor(1, 1,
                                    0L, TimeUnit.MILLISECONDS,
                                    new LinkedBlockingQueue<Runnable>()));
    }
```

#### 4 ScheduledThreadPool

该线程最大的特点就可以延迟执行

```
//第一次延迟initialDelay秒，以后每次延迟delay秒执行一个任务
ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d("google_lenve_fb", "run: ----");
            }
        };
scheduledExecutorService.scheduleWithFixedDelay(runnable, 1, 1, TimeUnit.SECONDS);
```

###线程池其他常用功能
1.shutDown()  关闭线程池，不影响已经提交的任务
    
2.shutDownNow() 关闭线程池，并尝试去终止正在执行的线程
    
3.allowCoreThreadTimeOut(boolean value) 允许核心线程闲置超时时被回收
    
4.submit 一般情况下我们使用execute来提交任务，但是有时候可能也会用到submit，使用submit的好处是submit有返回值



