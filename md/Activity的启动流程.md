##Activity的启动流程

```
  @Override
    public void startActivity(Intent intent) {
        this.startActivity(intent, null);
    }
```

```
  @Override
    public void startActivity(Intent intent, @Nullable Bundle options) {
        if (options != null) {
            startActivityForResult(intent, -1, options);
        } else {
            // Note we want to go through this call for compatibility with
            // applications that may have overridden the method.
            startActivityForResult(intent, -1);//这里最终走的还是三个参数的方法
        }
    }
```

```
  public void startActivityForResult(@RequiresPermission Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode, null);
    }
```

```
 public void startActivityForResult(@RequiresPermission Intent intent, int requestCode,
            @Nullable Bundle options) {
        if (mParent == null) {
            options = transferSpringboardActivityOptions(options);
            Instrumentation.ActivityResult ar =
                mInstrumentation.execStartActivity(
                    this, mMainThread.getApplicationThread(), mToken, this,
                    intent, requestCode, options);
            if (ar != null) {
                mMainThread.sendActivityResult(
                    mToken, mEmbeddedID, requestCode, ar.getResultCode(),
                    ar.getResultData());
            }
            if (requestCode >= 0) {
                // If this start is requesting a result, we can avoid making
                // the activity visible until the result is received.  Setting
                // this code during onCreate(Bundle savedInstanceState) or onResume() will keep the
                // activity hidden during this time, to avoid flickering.
                // This can only be done when a result is requested because
                // that guarantees we will get information back when the
                // activity is finished, no matter what happens to it.
                mStartedActivity = true;
            }

            cancelInputsAndStartExitTransition(options);
            // TODO Consider clearing/flushing other event sources and events for child windows.
        } else {
            if (options != null) {
                mParent.startActivityFromChild(this, intent, requestCode, options);
            } else {
                // Note we want to go through this method for compatibility with
                // existing applications that may have overridden it.
                mParent.startActivityFromChild(this, intent, requestCode);
            }
        }
    }
```

直接看mParent == null，然后看关键方法mInstrumentation.execStartActivity（）

```
   @UnsupportedAppUsage
    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) {
     ...
            int result = ActivityTaskManager.getService()
                .startActivity(whoThread, who.getBasePackageName(), intent,
                        intent.resolveTypeIfNeeded(who.getContentResolver()),
                        token, target != null ? target.mEmbeddedID : null,
                        requestCode, 0, null, options);
            checkStartActivityResult(result, intent);
     ...
    }
```

- who：正在启动该Activity的上下文
- contextThread：正在启动该Activity的上下文线程，这里为ApplicationThread
- token：正在启动该Activity的标识
- target：正在启动该Activity的Activity，也就是回调结果的Activity

先看在这个方法中，我们还发现了熟悉的字眼startActivity，但调用者却很陌生ActivityManagerNative.getDefault()，这到底是什么玩意？

```
/** @hide */
public static IActivityTaskManager getService() {
    return IActivityTaskManagerSingleton.get();
}

@UnsupportedAppUsage(trackingBug = 129726065)
private static final Singleton<IActivityTaskManager> IActivityTaskManagerSingleton =
        new Singleton<IActivityTaskManager>() {
            @Override
            protected IActivityTaskManager create() {
                final IBinder b = ServiceManager.getService(Context.ACTIVITY_TASK_SERVICE);
                return IActivityTaskManager.Stub.asInterface(b);
            }
        };
        
static public IActivityManager asInterface(IBinder obj) {
    ...
    return new ActivityManagerProxy(obj);
}
```

这样看下来好像是ServiceManager构建了一个key为activity的对象，该对象作为ActivityManagerProxy的参数实例化创建单例并get返回。
这里先不作解析，继续连接上面的流程ActivityManagerNative.getDefault()
.startActivity(…)。我们已经知道startActivity方法其实是ActivityManagerProxy调的，我们再来看看。

```
public int startActivity(IApplicationThread caller, String callingPackage, Intent intent,
            String resolvedType, IBinder resultTo, String resultWho, int requestCode,
            int startFlags, ProfilerInfo profilerInfo, Bundle options) throws RemoteException {
    Parcel data = Parcel.obtain();
    Parcel reply = Parcel.obtain();
    ...
    mRemote.transact(START_ACTIVITY_TRANSACTION, data, reply, 0);
    ...
    reply.recycle();
    data.recycle();
    return result;
}
```

如果对transact比较熟悉的话，那就很棒了，可以直接跳到下一节。不懂的同学继续，这里引入了Binder的概念，那么Android为什么要引入呢？这样说来ActivityManagerProxy是不是个代理类啊？为什么要引入，我不正面回答，假如我们要跨进程服务通信，你是不是先创建一个xx.aidl文件，然后会自动生成一个xx.java文件。你有没有仔细看过里面的内容呢？没看过的可以去看看。你会发现惊人的相似，如果是同一进程，我相信没人会这么做吧？从这些我们平时的开发经验来看，这玩意貌似用在跨进程通信上，就上面栗子来说，我们可以通过Service返回的Binder（其实就是个代理类）调用远程的方法，我们的Activity相当于客户端，而Service相当于服务端，这里也是一样，代理类就是ActivityManagerProxy。而真正传输数据的是mRemote.transact，简单介绍下，第一个参数code是告诉服务端，我们请求的方法是什么， 第二个参数data就是我们所传递的数据。这里比较纠人的是为什么要跨进程？这个你得了解下应用的启动过程了，这里我针对本篇文章简单的科普一下，应用启动会初始化一个init进程，而init进程会产生Zygote进程，从名字上看就是一个受精卵，它会fork很重要的SystemServer进程，比较著名的AMS，WMS，PMS都是这玩意启动的，而我们的应用进程也是靠AMS通信告诉Zygote进程fork出来的，因此需要跨进程通信，顺便解答下上面没回答的问题，ServiceManager构建的就是我们传说中的AMS。

花了比较长的篇幅介绍Binder，也是无奈之举，帮助你们理解，但只是简单的介绍，有些细节并没有介绍，大家大可文外系统化地了解，很复杂，嘿嘿。



我们再来看看下面的checkStartActivityResult方法。

```
   /** @hide */
    @UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    public static void checkStartActivityResult(int res, Object intent) {
        if (!ActivityManager.isStartResultFatalError(res)) {
            return;
        }

        switch (res) {
            case ActivityManager.START_INTENT_NOT_RESOLVED:
            case ActivityManager.START_CLASS_NOT_FOUND:
                if (intent instanceof Intent && ((Intent)intent).getComponent() != null)
                    throw new ActivityNotFoundException(
                            "Unable to find explicit activity class "
                            + ((Intent)intent).getComponent().toShortString()
                            + "; have you declared this activity in your AndroidManifest.xml?");
                throw new ActivityNotFoundException(
                        "No Activity found to handle " + intent);
            case ActivityManager.START_PERMISSION_DENIED:
                throw new SecurityException("Not allowed to start activity "
                        + intent);
            case ActivityManager.START_FORWARD_AND_REQUEST_CONFLICT:
                throw new AndroidRuntimeException(
                        "FORWARD_RESULT_FLAG used while also requesting a result");
            case ActivityManager.START_NOT_ACTIVITY:
                throw new IllegalArgumentException(
                        "PendingIntent is not an activity");
            case ActivityManager.START_NOT_VOICE_COMPATIBLE:
                throw new SecurityException(
                        "Starting under voice control not allowed for: " + intent);
            case ActivityManager.START_VOICE_NOT_ACTIVE_SESSION:
                throw new IllegalStateException(
                        "Session calling startVoiceActivity does not match active session");
            case ActivityManager.START_VOICE_HIDDEN_SESSION:
                throw new IllegalStateException(
                        "Cannot start voice activity on a hidden session");
            case ActivityManager.START_ASSISTANT_NOT_ACTIVE_SESSION:
                throw new IllegalStateException(
                        "Session calling startAssistantActivity does not match active session");
            case ActivityManager.START_ASSISTANT_HIDDEN_SESSION:
                throw new IllegalStateException(
                        "Cannot start assistant activity on a hidden session");
            case ActivityManager.START_CANCELED:
                throw new AndroidRuntimeException("Activity could not be started for "
                        + intent);
            default:
                throw new AndroidRuntimeException("Unknown error code "
                        + res + " when starting " + intent);
        }
    }
```

这些异常你都见过吗？它就是根据返回码和intent抛出相应异常，最熟悉的就是activity没有在AndroidManifest里面注册了。

小总结：

![](http://crazysunj.com/img/activity3.png)

### 应用的主要启动流程

![](http://crazysunj.com/img/activity7.png)

关于 App 启动流程的文章很多，文章底部有一些启动流程相关的参考文章，这里只列出大致流程如下：

1. 通过 Launcher 启动应用时，点击应用图标后，Launcher 调用 `startActivity()` 启动应用。
2. Launcher Activity 最终调用`Instrumentation` 的 `execStartActivity` 来启动应用。
3. `Instrumentation` 调用 `ActivityManagerProxy` (`ActivityManagerService` 在应用进程的一个代理对象) 对象的 `startActivity` 方法启动 Activity。
4. 到目前为止所有过程都在 Launcher 进程里面执行，接下来`ActivityManagerProxy` 对象跨进程调用`ActivityManagerService` (运行在**system_server** 进程)的 `startActivity` 方法启动应用。
5. `ActivityManagerService` 的 `startActivity` 方法经过一系列调用，最后调用 `zygoteSendArgsAndGetResult` 通过`socket` 发送给 `zygote` 进程，`zygote` 进程会孵化出新的应用进程。
6. `zygote` 进程孵化出新的应用进程后，会执行`ActivityThread` 类的 `main()` 方法。在该方法里会先准备好 `Looper` 和消息队列，然后调用 `attach()` 方法将应用进程绑定到 `ActivityManagerService`，然后进入`loop` 循环，不断地读取消息队列里的消息，并分发消息。
7. `ActivityManagerService` 保存应用进程的一个代理对象，然后 `ActivityManagerService` 通过代理对象通知应用进程创建入口 Activity 的实例，并执行它的生命周期函数。

**总结过程就是：**用户在 `Launcher` 程序里点击应用图标时，会通知 `ActivityManagerService` 启动应用的入口 Activity， `ActivityManagerService` 发现这个应用还未启动，则会通知 `Zygote`进程孵化出应用进程，然后在这个应用进程里执行 `ActivityThread` 的 main() 方法。应用进程接下来通知`ActivityManagerService` 应用进程已启动，`ActivityManagerService` 保存应用进程的一个代理对象，这样`ActivityManagerService` 可以通过这个代理对象控制应用进程，然后 ActivityManagerService 通知应用进程创建入口 Activity 的实例，并执行它的生命周期函数。

**到这里，我们大概理解一下这几个相关类的定位**

（一）**ActivityManagerService**：（ActivityManagerNative）是核心管理类，负责组件的管理，在这里主要与ActivityStackSupervisor通信。

（二）**ActivityStackSupervisor**：管理整个手机任务栈，即管理着ActivityStack。

（三）**ActivityStack**：是Activity的栈，即任务栈，从中可以获取需要进行操作的ActivityRecord，并且可以对任务的进程进行操作。

（四）**ActivityThread**：是安卓java应用层的入口函数类，它会执行具体对Activity的操作，并将结果通知给ActivityManagerService。



[http://crazysunj.com/2017/07/16/%E5%9B%BE%E8%A7%A3Activity%E5%90%AF%E5%8A%A8%E6%B5%81%E7%A8%8B%EF%BC%8C%E8%BF%9B%E9%98%B6%E9%AB%98%E7%BA%A7/](http://crazysunj.com/2017/07/16/图解Activity启动流程，进阶高级/)

[https://itimetraveler.github.io/2017/12/18/【Android】源码分析%20-%20Activity启动流程/#启动Activity的方式](https://itimetraveler.github.io/2017/12/18/[Android]源码分析 - Activity启动流程/#启动Activity的方式)