##IPC机制和binder

> ipc(Inter-Process Communication)含义是进程间通信或者跨进程通信，ipc机制不是Android独有，任何一个ipc机制都需要IPC机制，对于Android来说，他是一种基于Linux内核的移动操作系统，但它有自己的进程间通信方式Binder

### 1. Binder到底是什么？
![](https://imgconvert.csdnimg.cn/aHR0cDovL3VwbG9hZC1pbWFnZXMuamlhbnNodS5pby91cGxvYWRfaW1hZ2VzLzk0NDM2NS00NWRiNGRmMzM5MzQ4YjliLnBuZz9pbWFnZU1vZ3IyL2F1dG8tb3JpZW50L3N0cmlwJTdDaW1hZ2VWaWV3Mi8yL3cvMTI0MA)
###2.为啥要用Binder机制
 Linux （Android基于Linux）上的其他进程通信方式（管道、消息队列、共享内存、
信号量、Socket不好吗？因为binder机制跟他们相比有以下优点：
![](https://imgconvert.csdnimg.cn/aHR0cDovL3VwbG9hZC1pbWFnZXMuamlhbnNodS5pby91cGxvYWRfaW1hZ2VzLzk0NDM2NS1jMzIxMTYxYmZlYTdlNzhkLnBuZz9pbWFnZU1vZ3IyL2F1dG8tb3JpZW50L3N0cmlwJTdDaW1hZ2VWaWV3Mi8yL3cvMTI0MA)

###3.跨进程通信核心原理

`Binder`机制在 `Android`中的实现主要依靠 `Binder`类，其实现了`IBinder` 接口

实例说明：`Client`进程 需要调用 `Server`进程的加法函数（将整数a和b相加）

* `Client`进程 需要传两个整数给 `Server`进程
* `Server`进程 需要把相加后的结果 返回给`Client`进程



![](https://imgconvert.csdnimg.cn/aHR0cDovL3VwbG9hZC1pbWFnZXMuamlhbnNodS5pby91cGxvYWRfaW1hZ2VzLzk0NDM2NS0xNjMwYzY5ZTQ4Y2IxZGViLnBuZz9pbWFnZU1vZ3IyL2F1dG8tb3JpZW50L3N0cmlwJTdDaW1hZ2VWaWV3Mi8yL3cvMTI0MA)
Binder驱动 & Service Manager进程 属于 Android基础架构（即系统已经实现好了）；而Client 进程 和 Server 进程 属于Android应用层（需要开发者自己实现）

#### 	3.1 Binder通信模型

Binder基于C/S的结构下，定义了4个角色：Server、Client、ServerManager、Binder驱动，其中前三者是在用户空间的，也就是彼此之间无法直接进行交互，Binder驱动是属于内核空间的，属于整个通信的核心，虽然叫驱动，但是实际上和硬件没有太大关系，只是实现的方式和驱动差不多，驱动负责进程之间Binder通信的建立，Binder在进程之间的传递，Binder引用计数管理，数据包在进程之间的传递和交互等一系列底层支持。

####	3.2Binder请求的线程管理
* Server进程会创建很多线程来处理Binder请求
* Binder模型的线程管理 采用Binder驱动的线程池，并由Binder驱动自身进行管理,而不是由Server进程来管理的
* 一个进程的Binder线程数默认最大是16，超过的请求会被阻塞等待空闲的Binder线程。
* 所以，在进程间通信时处理并发问题时，如使用ContentProvider时，它的CRUD（创建、检索、更新和删除）方法只能同时有16个线程同时工作

###4.Android IPC实现方式

| 名称 | 优点 | 缺点 | 适用场景|
|--------|----------|---------|---------------------------|
| Bundle |简单易用|只能传输Bundle支持的数据类型|四大组件的进程间通信|
|文件共享|简单易用|不适合高并发场景, 并且无法做到进程间的即时通信|无并发访问情形, 交换简单的数据, 实时性不高的场景|
|AIDL|功能强大, 支持一对多并发通信, 支持实时通信|使用稍复杂, 需要处理好线程同步|一对多通信且有RPC需求|
|Messenger|功能一般, 支持一对多串行通信, 支持实时通信|不能很好处理高并发情形, 不支持RPC, 数据通过Message进行传输, 因此只能传输Bundle支持的数据类型|低并发的一对多即时通信, 无RPC需求, 或者无需要返回结果的RPC需求|
|ContentProvider|在数据源访问方便功能强大, 支持一对多并发数据共享, 可通过Call方法扩展其他操作|可以理解为受约束的AIDL, 主要提供数据源的CURD操作|一对多的进程间的数据共享|
|Socket|功能强大, 可以通过网络传输字节流, 支持一对多并发实时通信|实现细节有点繁琐, 不支持直接的RPC|网络数据交换|


###5.Binder机制实例

这里我们选择AIDL来分析binder机制

5.1 文件结构

![](https://github.com/afa888/android_reflection/blob/master/app/src/main/res/drawable/aidl.png?raw=true)

####5.2Demo

1.先写一个aidl文件

```
interface IBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    void addBook(in Book book);

    List<Book> getBookList();
}
```

然后make一下

2.写下我们要传输的对象Book
```
public class Book implements android.os.Parcelable {

    public int bookId;
    public String bookName;

    public Book(int bookId, String bookName) {
        this.bookId = bookId;
        this.bookName = bookName;
    }

    public Book() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.bookId);
        dest.writeString(this.bookName);
    }

    protected Book(Parcel in) {
        this.bookId = in.readInt();
        this.bookName = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
```

3.aidl中用到了自定义的对象必须要新建一个和它同名的aidl文件 Book.aidl

```
package com.example.android.aidl;

// Declare any non-default types here with import statements

parcelable Book;
```

4.服务端代码 BookManagerService

```
public class BookManagerService  extends Service {
    private static final String TAG = "BookManagerService";
    private Handler mHandler = new Handler();
    public BookManagerService() {
    }
    private List<Book> mBookList = new ArrayList<>();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    private IBookManager.Stub mBinder =new IBookManager.Stub(){

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
            final int bookCount = mBookList.size();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(BookManagerService.this,
                            String.format("添加了一本新书, 现在有%d本", bookCount), Toast.LENGTH_SHORT).show();
                }
            });
            Log.d(TAG, String.format("添加了一本新书, 现在有%d本", bookCount));
            Log.d(TAG, "currentThread = " + Thread.currentThread().getName());
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }
    };
}
```

5.在清单文件开启单独进程

```
 <com.example.componentbase.service
            android:name=".aidl.BookManagerService"
            android:process=":remote">
            <intent-filter>
                <category android:name="android.intent.category.DEFAULT" />
                <action android:name="com.example.android.aidl.BookManagerService" />
            </intent-filter>
        </com.example.componentbase.service>
```

6.客户端调用MainActivity

```
public class MainAidlActivity extends AppCompatActivity {
    Button btBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_aidl);
        btBind =  findViewById(R.id.btBind);
        btBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindService(btBind);
            }
        });
        findViewById(R.id.btAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBook();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIBookManager != null) {
            unbindService(mServiceConnection);
        }
    }

    private IBookManager mIBookManager;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder com.example.componentbase.service) {
            mIBookManager = IBookManager.Stub.asInterface(com.example.componentbase.service);
            Toast.makeText(MainAidlActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIBookManager = null;
        }
    };

    public void bindService(View view) {
        Intent intent = new Intent();
        intent.setAction("com.example.android.aidl.BookManagerService");
        intent.setPackage(getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    public void addBook() {
        if (mIBookManager != null) {
            try {
                mIBookManager.addBook(new Book(18, "漫画书"));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
```

这样就利用AIDL使用Binder完成了跨进程通信；

####5.3代码分析

代码分析主要从aidl文件自动生成的Java文件IBookManager入手

```
   public static abstract class Stub extends android.os.Binder implements com.example.android.aidl.IBookManager {
        private static final java.lang.String DESCRIPTOR = "com.example.android.aidl.IBookManager";
        /**
         * Construct the stub at attach it to the interface.
         */
        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }
        ...
```

可以看到这个内部类Stub是一个抽象类, 跟踪`this.attachInterface(this, DESCRIPTOR);`

```
    public void attachInterface(@Nullable IInterface owner, @Nullable String descriptor) {
        mOwner = owner;
        mDescriptor = descriptor;
    }
```

可以看到只是将它自己本身和DESCRIPTOR这个字符串标识作为Binder中的全局变量保存了起来.DESRIPTOR作为Binder 的唯一标识，一般用当前 Binder 的全类名表示。

然后我们看客户端的这段代码

```
   private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder com.example.componentbase.service) {
            mIBookManager = IBookManager.Stub.asInterface(com.example.componentbase.service);
            Toast.makeText(MainAidlActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIBookManager = null;
        }
    };
```

跟踪`IBookManager.Stub.asInterface(com.example.componentbase.service);`这个方法

```
  public static com.example.android.aidl.IBookManager asInterface(android.os.IBinder obj) {
            if ((obj == null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin != null) && (iin instanceof com.example.android.aidl.IBookManager))) {
                return ((com.example.android.aidl.IBookManager) iin);
            }
            return new com.example.android.aidl.IBookManager.Stub.Proxy(obj);
        }
```

注释是说将IBinder接口对象强转为IBookManager对象, 如果需要的话, 生成一个代理. 首先会根据标识符去 IBinder 的本地去查找是否有该对象，也就是调用 obj.queryLocalInterface(DESCRIPTOR) 方法，继续源码中 Binder.java

```
  public @Nullable IInterface queryLocalInterface(@NonNull String descriptor) {
        if (mDescriptor.equals(descriptor)) {
            return mOwner;
        }
        return null;
    }
```

意思就是如果本地存在这个标识符的 IInterface 对象，那就直接返回之前构造方法中初始化的 mOwner 对象，否则返回 null.因为我们这里涉及到了跨进程通信，虽然服务端在初始化mBinder进行了`attachInterface(this, DESCRIPTOR)`(可以再看看上面的源码), 但那时另一个进程的, 所以这里会直接返回 null。接着就return一个 `new Proxy(obj);`, 我们继续跟踪这个Proxy类

```
 private static class Proxy implements IBookManager {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                mRemote = remote;
            }

            @Override
            public android.os.IBinder asBinder() {
                return mRemote;
            }

            public String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            /**
             * Demonstrates some basic types that you can use as parameters
             * and return values in AIDL.
             */
            @Override
            public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
                    double aDouble, String aString) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(anInt);
                    _data.writeLong(aLong);
                    _data.writeInt(((aBoolean) ? (1) : (0)));
                    _data.writeFloat(aFloat);
                    _data.writeDouble(aDouble);
                    _data.writeString(aString);
                    mRemote.transact(Stub.TRANSACTION_basicTypes, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void addBook(Book book) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    if ((book != null)) {
                        _data.writeInt(1);
                        book.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    mRemote.transact(Stub.TRANSACTION_addBook, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public java.util.List<Book> getBookList() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                java.util.List<Book> _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    mRemote.transact(Stub.TRANSACTION_getBookList, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.createTypedArrayList(Book.CREATOR);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
        }

        static final int TRANSACTION_basicTypes = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
        static final int TRANSACTION_addBook = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
        static final int TRANSACTION_getBookList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    }
```

在客户端中, 我们调用的是IBookManager的addBook()方法, 也就是这个Proxy对象的addBook()方法, 观察这个addBook()方法. 这里就涉及到了一个重要的类 Parcel，Parcel 天生具备跨进程传输数据能力. 把需要传递的数据写入 Parcel 中，然后到达目标进程后，将 Parcel 中的数据读出即可，所以可以将 Parcel 称为数据传输载体。 这里的_data就是一个Parcel对象, 我们将Book写入Parcel, 然后调用`mRemote.transact(Stub.TRANSACTION_addBook, _data, _reply, 0);`

这个mRemote其实就是之前传进来的也就是Service返回的一个IBookManager.Stub对象, 我们还是再跟踪一下这个transact()方法吧, 在Binder类中

```
    public final boolean transact(int code, @NonNull Parcel data, @Nullable Parcel reply,
            int flags) throws RemoteException {
        if (false) Log.v("Binder", "Transact: " + code + " to " + this);

        if (data != null) {
            data.setDataPosition(0);
        }
        boolean r = onTransact(code, data, reply, flags);
        if (reply != null) {
            reply.setDataPosition(0);
        }
        return r;
    }

```

可以看到onTransact()方法会被调用, 这里其实是这样 client 端：BpBinder.transact() 来发送事务请求； server 端：BBinder.onTransact() 会接收到相应事务。 所以服务端的onTransact()方法会被调用, 其实就是IBookManager.Stub.onTransact()会被调用

```
@Override
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply,
                int flags) throws android.os.RemoteException {
            switch (code) {
                case INTERFACE_TRANSACTION: {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                case TRANSACTION_basicTypes: {
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0;
                    _arg0 = data.readInt();
                    long _arg1;
                    _arg1 = data.readLong();
                    boolean _arg2;
                    _arg2 = (0 != data.readInt());
                    float _arg3;
                    _arg3 = data.readFloat();
                    double _arg4;
                    _arg4 = data.readDouble();
                    String _arg5;
                    _arg5 = data.readString();
                    this.basicTypes(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_addBook: {
                    data.enforceInterface(DESCRIPTOR);
                    Book _arg0;
                    if ((0 != data.readInt())) {
                        _arg0 = Book.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    this.addBook(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_getBookList: {
                    data.enforceInterface(DESCRIPTOR);
                    java.util.List<Book> _result = this.getBookList();
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }
```

case TRANSACTION_addBook的时候, 调用addBook()方法, 这样自然就完成了服务端中收到响应之后的操作.返回true表示这个事务是成功的. 当 onTransact 返回 true，调用成功，从 reply 对象中取出返回值，返回给客户端调用方。

####总结

高度概括 AIDL 的用法，就是服务端里有一个 Service，给与之绑定 (bindService) 的特定客户端进程提供 Binder 对象。客户端通过 AIDL 接口的静态方法asInterface 将 Binder 对象转化成 AIDL 接口的代理对象，通过这个代理对象就可以发起远程调用请求了。 用这张图来表述再清楚不过了.

![](http://www.10tiao.com/img.do?url=https%3A//mmbiz.qpic.cn/mmbiz_png/v1LbPPWiaSt5jyYEPLF4P2LJac4UZ13xibXYfzYtEwUjOKIhG26dMjoRbTbgqo2zEcpGSAmrksgGe4PLOpKt4g9w/0%3Fwx_fmt%3Dpng)




