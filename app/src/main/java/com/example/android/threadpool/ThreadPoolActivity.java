package com.example.android.threadpool;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import com.example.android.MainActivity;
import com.example.android.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolActivity extends AppCompatActivity {
    public ThreadPoolExecutor threadPoolExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_pool);


        new MyThread2().start();

        MyThread myThread = new MyThread();
        new Thread(myThread).start();


        MyThread3 myThread3 = new MyThread3();
        Thread thread = new Thread(myThread3);
        thread.run();  //这种调用run方法运行在主线程上
        thread.start();

        // AsyncTask，它封装了线程池和Handler，主要为我们在子线程中更新UI提供便利。
        // HandlerThread，它是个具有消息队列的线程，可以方便我们在子线程中处理不同的事务。一个允许Handler的特殊线程


        //execute串行
        (new MyAsyncTask("MyAsyncTask01")).execute();
        (new MyAsyncTask("MyAsyncTask02")).execute();
        (new MyAsyncTask("MyAsyncTask03")).execute();

        //executeOnExecutor 并行
        (new MyAsyncTask("MyAsyncTask01")).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
        (new MyAsyncTask("MyAsyncTask02")).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
        (new MyAsyncTask("MyAsyncTask03")).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");

        // IntentService，我们可以将它看做为HandlerThread的升级版，它是服务，优先级更高。
        // 使用 Service 就是想在后台执行一些其他的操作，例如：下载等，而这些，又需要额外开启线程来完成任务，这样无形之中加大了我们的代码量。自动销毁

        startService(new Intent(this, MuyIntentService.class));

        //https://blog.csdn.net/u012702547/article/details/52259529
        //corePoolSize  线程池中核心线程的数量
        //maximumPoolSize  线程池中最大线程数量
        //keepAliveTime 非核心线程的超时时长
        //workQueue 线程池中的任务队列

           threadPoolExecutor = new ThreadPoolExecutor(3,5,1, TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>(128));
        // 由于核心线程数为3，workQueue的大小为128，所以我们的线程的执行应该是先启动三个核心线程来执行任务，
        // 剩余的27个任务全部存在workQueue中，等待核心线程空余出来之后执行

     /*   threadPoolExecutor = new ThreadPoolExecutor(3, 30, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(6));
        //首先打印出来0，1，2说明往核心线程添加了三个任务，然后将3，4，5，6，7，8六个任务添加到了任务队列中，
        // 接下来要添加的任务因为核心线程已满，队列已满所以就直接开一个非核心线程来执行，
        // 因此后添加的任务反而会先执行（3，4，5，6，7，8都在队列中等着*/
        btnClick();


    /*    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);

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
        }*/

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
        }


        //第一次延迟initialDelay秒，以后每次延迟delay秒执行一个任务
/*        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d("google_lenve_fb", "run: ----");
            }
        };
        scheduledExecutorService.scheduleWithFixedDelay(runnable, 1, 1, TimeUnit.SECONDS);*/

/*        8.线程池其他常用功能
        1.shutDown()  关闭线程池，不影响已经提交的任务

        2.shutDownNow() 关闭线程池，并尝试去终止正在执行的线程

        3.allowCoreThreadTimeOut(boolean value) 允许核心线程闲置超时时被回收

        4.submit 一般情况下我们使用execute来提交任务，但是有时候可能也会用到submit，使用submit的好处是submit有返回值，举个栗子：*/


    }


    public void btnClick() {
        for (int i = 0; i < 30; i++) {
            final int finalI = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.e("ThreadPoolExecutor", "rune: " + finalI);
                    Log.e("ThreadPoolExecutor", Thread.currentThread().getName());

                }
            };
            threadPoolExecutor.execute(runnable);

        }
    }

    public void customThreadPool(View view) {
        final MyThreadPool myThreadPool = new MyThreadPool(3, 5, 1, TimeUnit.MINUTES, new LinkedBlockingDeque<Runnable>());
        for (int i = 0; i < 10; i++) {
            final int finalI = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(100);
                    Log.d("google_lenve_fb", "run: " + finalI);
                }
            };
            myThreadPool.execute(runnable);
        }
    }


    class MyThread2 extends Thread {
        @Override
        public void run() {
            super.run();
            Log.e("MyThread2", Thread.currentThread().getId() + Thread.currentThread().getName());
        }
    }


    class MyThread implements Runnable {
        @Override
        public void run() {
            Log.e("MyThread", Thread.currentThread().getId() + Thread.currentThread().getName());
        }
    }

    class MyThread3 implements Runnable {
        @Override
        public void run() {
            Log.e("MyThread3", Thread.currentThread().getId() + Thread.currentThread().getName());
        }
    }


     class MyAsyncTask extends AsyncTask {

        private String mName = "";

        public MyAsyncTask(String name) {
            mName = name;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        protected Object doInBackground(Object[] params) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Log.i(mName, dateFormat.format(new Date(System.currentTimeMillis())));
            return null;
        }
    }

    class MyThreadPool extends ThreadPoolExecutor{

        public MyThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            super.beforeExecute(t, r);
            Log.d("google_lenve_fb", "beforeExecute: 开始执行任务！");

        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            super.afterExecute(r, t);
            Log.d("google_lenve_fb", "beforeExecute: 任务执行结束！");

        }

        @Override
        protected void terminated() {
            super.terminated();
            Log.d("google_lenve_fb", "terminated: 线程池关闭！");

        }
    }


}
