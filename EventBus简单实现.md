###EventBus简单实现

> EventBus 是一种事件发布订阅模式，通过 EventBus 我们可以很方便的实现解耦，将事件的发起和事件的处理的很好的分隔开来，很好的实现解耦

![img](https://img2018.cnblogs.com/blog/489462/201907/489462-20190722075616352-730970754.png)

- 首先来看看EventBus的入口

```
public static EventBus getDefault() {
        if (defaultInstance == null) {
            synchronized (EventBus.class) {
                if (defaultInstance == null) {
                    defaultInstance = new EventBus();
                }
            }
        }
        return defaultInstance;
    }
```

这里使用单例模式来创建EventBus实例，同时加锁，保证在不同线程中，EventBus的唯一

- 在看看EventBus注册的方法

```
public void register(Object subscriber) {
        Class<?> subscriberClass = subscriber.getClass();
        List<SubscriberMethod> subscriberMethods = subscriberMethodFinder.findSubscriberMethods(subscriberClass);
        synchronized (this) {
            for (SubscriberMethod subscriberMethod : subscriberMethods) {
                subscribe(subscriber, subscriberMethod);
            }
        }
    }
```

前两行代码的主要是先获取到订阅类的class，然后将class里面的methods存放到List<SubscriberMethod>，我们在这里主要看一下第二行代码的findSubscriberMethods()方法

- findSubscriberMethods()

```
List<SubscriberMethod> findSubscriberMethods(Class<?> subscriberClass) {
        List<SubscriberMethod> subscriberMethods = METHOD_CACHE.get(subscriberClass);
        if (subscriberMethods != null) {
            return subscriberMethods;
        }

        if (ignoreGeneratedIndex) {
            subscriberMethods = findUsingReflection(subscriberClass);
        } else {
            subscriberMethods = findUsingInfo(subscriberClass);
        }
        if (subscriberMethods.isEmpty()) {
            throw new EventBusException("Subscriber " + subscriberClass
                    + " and its super classes have no public methods with the @Subscribe annotation");
        } else {
            METHOD_CACHE.put(subscriberClass, subscriberMethods);
            return subscriberMethods;
        }
    }
```

首先会在“METHOD_CACHE”这个数组里面查找是否有缓存过的method，ignoreGeneratedIndex默认为false，现在再往下面看看findUsingInfo()方法

- 比较关键的findUsingInfo()

```
private List<SubscriberMethod> findUsingInfo(Class<?> subscriberClass) {
        FindState findState = prepareFindState();
        findState.initForSubscriber(subscriberClass);
        while (findState.clazz != null) {
            findState.subscriberInfo = getSubscriberInfo(findState);
            if (findState.subscriberInfo != null) {
                SubscriberMethod[] array = findState.subscriberInfo.getSubscriberMethods();
                for (SubscriberMethod subscriberMethod : array) {
                    if (findState.checkAdd(subscriberMethod.method, subscriberMethod.eventType)) {
                        findState.subscriberMethods.add(subscriberMethod);
                    }
                }
            } else {
                findUsingReflectionInSingleClass(findState);
            }
            findState.moveToSuperclass();
        }
        return getMethodsAndRelease(findState);
    }
```

这里用了FindState这个内部类来用于保存订阅者类的信息，以map的形式保存；接下来用initForSubscriber()这个方法进行初始化；然后再findUsingReflectionInSingleClass()在这个方法内，会使用反射，将你所有含有注解的方法从class里面遍历出来，并且保存在FindState的list当中，对于有注解但是不符合要求的，会通过异常抛出

- 看看最后一排的getMethodsAndRelease()方法

```
private List<SubscriberMethod> getMethodsAndRelease(FindState findState) {
        List<SubscriberMethod> subscriberMethods = new ArrayList<>(findState.subscriberMethods);
        findState.recycle();
        synchronized (FIND_STATE_POOL) {
            for (int i = 0; i < POOL_SIZE; i++) {
                if (FIND_STATE_POOL[i] == null) {
                    FIND_STATE_POOL[i] = findState;
                    break;
                }
            }
        }
        return subscriberMethods;
    }
```

getMethodsAndRelease()主要做的事情就是将findstate里面的method取出来;然后将findstate里面的map释放掉；最后将自己的引用赋给一个空FIND_STATE_POOL[i],起到复用池的效果

- 在regisert的最后,会调用subscribe()方法,这个方法主要是订阅的方法与事件进行注册
- 发送事件 post

```
public void post(Object event) {
        PostingThreadState postingState = currentPostingThreadState.get();
        List<Object> eventQueue = postingState.eventQueue;
        eventQueue.add(event);

        if (!postingState.isPosting) {
            postingState.isMainThread = Looper.getMainLooper() == Looper.myLooper();
            postingState.isPosting = true;
            if (postingState.canceled) {
                throw new EventBusException("Internal error. Abort state was not reset");
            }
            try {
                while (!eventQueue.isEmpty()) {
                    postSingleEvent(eventQueue.remove(0), postingState);
                }
            } finally {
                postingState.isPosting = false;
                postingState.isMainThread = false;
            }
        }
    }
```

PostingThreadState这个类主要是保存当前线程的信息，订阅者和订阅事件。这里post的流程是先从currentPostingThreadState获取当前线程信息，然后将当前线程的事件取出来，然后添加到队列里面等待分发；这里在循环队列中的事件的时候，也会循环当前事件的父类和接口，查找出所有的订阅者;最后会在postToSubscription()方法里面判断在那个线程里面执行方法，通过反射执行符合需求的包含注解的方法

#### 好的，我们接下来用100多行代码 ，来实现eventbus的事件发送

-先写一个person对象

```
public class Person {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    private String name;
    private String sex;

    public Person(String name, String sex) {
        this.name = name;
        this.sex = sex;
    }
}
```

- 然后在写一个注解接口类

```
@Retention(RetentionPolicy.RUNTIME)
public @interface Subscribe {
    ThreadMode threadMode() default ThreadMode.PostThread;
}
```

- 在写一个SubscriberMethod对象，里面用来存放订阅者的一些信息

```
public class SubscribleMethod {
    //订阅事件的method对象
    private Method method;
    //订阅事件的线程位置
    private ThreadMode threadMode;
    //订阅事件类型
    private Class<?> eventType;

    public SubscribleMethod(Method method, ThreadMode threadMode, Class<?> eventType) {
        this.method = method;
        this.threadMode = threadMode;
        this.eventType = eventType;
    }

    public Method getMethod() {
        return method;
    }

    public ThreadMode getThreadMode() {
        return threadMode;
    }

    public Class<?> getEventType() {
        return eventType;
    }
}
```

-在写一个枚举类，用于存放线程位置的标志，ThreadMode

```
public enum ThreadMode {

    PostThread,

    MainThread,

    BackgroundThread,

    Async
}
```

- 好了 接下来开始写eventbus的注册和发送事件

```
public class Eventbus {
    private static Eventbus instance = new Eventbus();

    public static Eventbus getDefault() {
        return instance;
    }

    //这个map用于保存订阅者的信息
    private Map<Object, List<SubscribleMethod>> cacheMap;

    private Eventbus() {
        this.cacheMap = new HashMap<>();
    }


    public void register(Object activity) {
        Class<?> clazz = activity.getClass();
        List<SubscribleMethod> list = cacheMap.get(activity);
        //判断是否注册过
        if (list == null) {
            list = getSubscribleMethods(activity);
            cacheMap.put(activity, list);
        }
    }
    
 //获取activity下所有的订阅方法
    private List<SubscribleMethod> getSubscribleMethods(Object activity) {
        List<SubscribleMethod> list = new ArrayList<>();
        Class clazz = activity.getClass();
        while (clazz != null) {
            String name = clazz.getName();
            //如果包名是java javax android开头的 说明是系统的方法 直接跳过 节省事件
            if (name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.")) {
                break;
            }
            //这里使用反射获取的是类自身声明的所有方法
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                //返回这个元素的注解指定类型
                Subscribe subscribe = method.getAnnotation(Subscribe.class);
                if (subscribe == null) {
                    continue;
                }
                //对象表示的方法的形式参数类型,event只能接受一个参数,
                Class[] paratems = method.getParameterTypes();
                if (paratems.length != 1) {
                    String methodName = method.getDeclaringClass().getName() + "." + method.getName();
                    try {
                        throw new Exception("@Subscribe method " + methodName +
                                "must have exactly 1 parameter but has " + paratems.length);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //满足所有要求 找到所有的订阅方法 全部return出去
                ThreadMode threadMode = subscribe.threadMode();
                SubscribleMethod subscribleMethod = new SubscribleMethod(method
                        , threadMode, paratems[0]);
                list.add(subscribleMethod);

            }
            clazz = clazz.getSuperclass();
        }
        return list;
    }


    public void post(final Object object) {
        Set<Object> set = cacheMap.keySet();
        Iterator iterator = set.iterator();
        //遍历caheMap表中所有的订阅者
        while (iterator.hasNext()) {
            //获取到actiivty对象
            final Object activity = iterator.next();
            //获取activity下存放订阅者信息的list
            List<SubscribleMethod> list = cacheMap.get(activity);
            //遍历所有订阅者
            for (final SubscribleMethod subscribleMethod : list) {
                //判断订阅类型是不是一样的 如果是一样的就需要接受事件
                if (subscribleMethod.getEventType().isAssignableFrom(object.getClass())) {
                    invoke(subscribleMethod, activity, object);
                }
            }
        }
    }

     //反射发送事件
    private void invoke(SubscribleMethod subscribleMethod, Object activity, Object object) {
        Method method = subscribleMethod.getMethod();
        try {
            //执行activity中注解的方法
            method.invoke(activity, object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
```

- 好了，所有代码写完了，现在在activity里面使用就行

```
//注册
Eventbus.getDefault().register(this);

//发送消息
Eventbus.getDefault().post(new Person("abc", "男"));

//接受消息
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void receive(Person person ) {
        Toast.makeText(this, person.getName()+"----"+person.getSex(), Toast.LENGTH_SHORT).show();
    }
```

如果想要加上线程判断的话，在post方法里面通过subscribleMethod.getThreadMode()进行判断就行


