#Retrofit入门到完全封装
------
> 如果你对RxJava不熟悉请先看给 Android 开发者的 RxJava 详解这篇文章。
如果你对Retrofit不熟悉就先看Retrofit官网。

> 当然也有很多RxJava与Retrofit的文章，但是我觉得很多大家都很纠结的功能都没有被总结出来，而且使用起来总会遇到这样那样的问题。我这里的代码可以直接拷贝应用到项目中去。


###配置文件
app build.gradle文件

```Java
  	implementation 'com.squareup.okhttp3:okhttp:3.6.0'//导入okhttp因为retrofit网络请求还是okhttp,retrofit本身只是okhttp的封装而已
    implementation 'com.squareup.retrofit2:retrofit:2.3.0'//导入retrofit
    implementation 'com.squareup.retrofit2:adapter-rxjava:2.3.0'//导入rxjava
    implementation 'io.reactivex:rxandroid:1.1.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.0.0-beta3'//gson解析
    implementation 'com.squareup.retrofit2:converter-scalars:2.0.0'    //导入scalars包为了让请求放回字符串
    implementation 'com.squareup.okhttp3:logging-interceptor:3.4.1' //拦截器，可以拦截okhttp，比如操作请求头，header里面添加参数
```

AndroidMainfest.xml文件

```Java
  <!-- 网络权限 -->
 <uses-permission android:name="android.permission.INTERNET" />
 
    <!-- 网络状态权限 -->
 <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />   
 
```
```Java
public interface GetAppList {
@GET("/jj")
Call<String> get(@Query("count") int count);


@FormUrlEncoded
@POST("/Index/filter")
Call<String> postList(@Field("sign") String sign,
@Field("interfaceVersion") String interfaceVersion,
@Field("seo") String seo,
@Field("noncestr") String noncestr,
@Field("timestamp") String timestamp,
@Field("sbID") String sbID);  @FormUrlEncoded


@POST("/Index/filter")
Call<TypeInfo> postBeanList(@Field("sign") String sign,
@Field("interfaceVersion") String interfaceVersion,
@Field("seo") String seo,
@Field("noncestr") String noncestr,
@Field("timestamp") String timestamp,
@Field("sbID") String sbID);



@FormUrlEncoded
@POST("/Index/filter")
Observable<TypeInfo> postBeanRxjavaList(@Field("sign") String sign,
@Field("interfaceVersion") String interfaceVersion,
@Field("seo") String seo,
@Field("noncestr") String noncestr,
@Field("timestamp") String timestamp,
@Field("sbID") String sbID);

@FormUrlEncoded
@POST("/Index/filter")
Observable<HttpResult<DataTypeBean>> postBeanRxjavaUtilsList(@Field("sign") String sign,
@Field("interfaceVersion") String interfaceVersion,
@Field("seo") String seo,
@Field("noncestr") String noncestr,
@Field("timestamp") String timestamp,
@Field("sbID") String sbID);

}
```

###普通get请求，以字符串返回
```Java
 //普通get请求，以字符串返回
    private void get() {
        //拼装接口
        Retrofit retrofit = new Retrofit.Builder()
                //这里我需要返回的是String ,所以导入这个类
                .addConverterFactory(ScalarsConverterFactory.create())
                //设置网络请求的 Base Url地址
                .baseUrl("https://www.baidu.com/")
                //设置数据解析器
                .build();
        // 创建 网络请求接口 的实例
        GetAppList request = retrofit.create(GetAppList.class);
        //创建请求，传入参数
        Call<String> call = request.get(88);
        //异步请求
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("retrefit", "onResponse= " + response.body().toString());

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("retrefit", "onFailure= " + t.toString());

            }
        });
    }
  
```
### post普通请求返回字符串
```Java
  //普通post请求，以字符串返回
    private void post() {
        //拼装接口
        Retrofit retrofit = new Retrofit.Builder()
                //这里我需要返回的是String ,所以导入这个类
                .addConverterFactory(ScalarsConverterFactory.create())
                //设置网络请求的 Base Url地址
                .baseUrl("http://skyollie.com")
                //设置数据解析器
                .build();
        // 创建 网络请求接口 的实例
        GetAppList request = retrofit.create(GetAppList.class);
        //创建请求，传入参数
        Call<String> call = request.postList("16d5e1a960a59a704b4b5d27789887bc2be1571d",
                "48",
                "tvdrama",
                "1e9cc0a8-6e90-455b-8550-23180e88b450&devicetype=0",
                "1551167029117",
                "id9be4eb13-668b-4e1a-84de-288a7785d24d");
        //异步请求
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("retrefit", "onResponse= " + response.body().toString());

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("retrefit", "onFailure= " + t.toString());

            }
        });
    }
  
```
###post使用Gson返回对象
```Java
    //普通post请求，使用Gson返回
    private void postBean() {
        //拼装接口
        Retrofit retrofit = new Retrofit.Builder()
                //这里我需要返回的是String ,所以导入这个类
                .addConverterFactory(GsonConverterFactory.create())
                //设置网络请求的 Base Url地址
                .baseUrl("http://skyollie.com")
                //设置数据解析器
                .build();
        // 创建 网络请求接口 的实例
        GetAppList request = retrofit.create(GetAppList.class);
        //创建请求，传入参数
        Call<TypeInfo> call = request.postBeanList("16d5e1a960a59a704b4b5d27789887bc2be1571d",
                "48",
                "tvdrama",
                "1e9cc0a8-6e90-455b-8550-23180e88b450&devicetype=0",
                "1551167029117",
                "id9be4eb13-668b-4e1a-84de-288a7785d24d");
        //异步请求
        call.enqueue(new Callback<TypeInfo>() {
            @Override
            public void onResponse(Call<TypeInfo> call, Response<TypeInfo> response) {
                Log.e("retrefit", "onResponse= " + response.body().getMsg());

            }

            @Override
            public void onFailure(Call<TypeInfo> call, Throwable t) {
                Log.e("retrefit", "onFailure= " + t.toString());

            }
        });
    }
```
###post使用Gson并结合Rxjava返回对象
```Java
    //普通post请求，使用Gson返回
    private void postBeanRxjava() {
        //拼装接口
        Retrofit retrofit = new Retrofit.Builder()
                //这里我需要返回的是String ,所以导入这个类
                .addConverterFactory(GsonConverterFactory.create())
                //设置RXjava
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                //设置网络请求的 Base Url地址
                .baseUrl("http://skyollie.com")
                //设置数据解析器
                .build();
        // 创建 网络请求接口 的实例
        GetAppList request = retrofit.create(GetAppList.class);
        //创建请求，传入参数
        request.postBeanRxjavaList("16d5e1a960a59a704b4b5d27789887bc2be1571d",
                "48",
                "tvdrama",
                "1e9cc0a8-6e90-455b-8550-23180e88b450&devicetype=0",
                "1551167029117",
                "id9be4eb13-668b-4e1a-84de-288a7785d24d")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TypeInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(TypeInfo typeInfo) {
                        Log.e("retrefit", "onNext= " + typeInfo.toString());
                    }
                });
    }
```
###post使用Gson并结合Rxjava并封装返回对象
```Java
  //封装了Retrofit
    private void postUtils() {
        Subscriber<TypeInfo> subscriber = new Subscriber<TypeInfo>() {
            @Override
            public void onCompleted() {
                Toast.makeText(MainActivity.this, "Get Top Movie Completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                // resultTV.setText(e.getMessage());
            }

            @Override
            public void onNext(TypeInfo typeInfo) {
                Log.e("postUtils", "onNext= " + typeInfo.toString());
            }
        };
        HttpMethods.getInstance().postUtils(subscriber);
    }
    
    
    
    
    //retrofit简单封装
    class HttpMethods {

    public static final String BASE_URL = "http://skyollie.com";

    private static final int DEFAULT_TIMEOUT = 5;

    public Retrofit retrofit;
    private GetAppList getAppList;

    //构造方法私有
    private HttpMethods() {
        //手动创建一个OkHttpClient并设置超时时间

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        getAppList = retrofit.create(GetAppList.class);

    }

    //在访问HttpMethods时创建单例
    public static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void postUtils(Subscriber<TypeInfo> subscriber) {

        getAppList.postBeanRxjavaList("16d5e1a960a59a704b4b5d27789887bc2be1571d",
                "48",
                "tvdrama",
                "1e9cc0a8-6e90-455b-8550-23180e88b450&devicetype=0",
                "1551167029117",
                "id9be4eb13-668b-4e1a-84de-288a7785d24d")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


}
```
###终极封装返回统一对象
```Java
//全局统一封装
    private void postUtilsResult() {
        HttpMethods2.getInstance().getTopMovie(new ProgressSubscriber(new SubscriberOnNextListener<DataTypeBean>() {
            @Override
            public void onNext(DataTypeBean subjects) {
               Log.e("postUtilsResult","onNext"+subjects.getFilter().get(0).getType());
            }
        }, MainActivity.this));
    }
```

