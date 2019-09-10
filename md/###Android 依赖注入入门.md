###Android 依赖注入入门

####一.原理

依赖注入是实现程序解耦的一种方式。如果通过百度搜索可以找到如下答案：

控制反转（Inversion of Control，英文缩写为IoC）是一个重要的面向对象编程的法则来削减计算机程序的耦合问题.控制反转一般分为两种类型，依赖注入（Dependency Injection，简称DI）和依赖查找（Dependency Lookup）。依赖注入应用比较广泛。


#####比较形象的比喻和解释：

(1)原始社会里，几乎没有社会分工。需要斧子的人(调用者)只能自己去磨一把斧子(被调用者)。对应的情形为:Java程序里的调用者自己创建被调用者。

(2)进入工业社会，工厂出现。斧子不再由普通人完成，而在工厂里被生产出来，此时需要斧子的人(调用者)找到工厂，购买斧子，无须关心斧子的制造过程。对应Java程序的简单工厂的设计模式。

(3)进入“按需分配”社会，需要斧子的人不需要找到工厂，坐在家里发出一个简单指令:需要斧子。斧子就自然出现在他面前。对应Spring的依赖注入。

第一种情况下，Java实例的调用者创建被调用的Java实例，必然要求被调用的Java类出现在调用者的代码里。无法实现二者之间的松耦合。

第二种情况下，调用者无须关心被调用者具体实现过程，只需要找到符合某种标准(接口)的实例，即可使用。此时调用的代码面向接口编程，可以让调用者和被调用者解耦，这也是工厂模式大量使用的原因。但调用者需要自己定位工厂，调用者与特定工厂耦合在一起。

第三种情况下，调用者无须自己定位工厂，程序运行到需要被调用者时，系统自动提供被调用者实例。

#####个人理解：

在程序中，一个对象中的方法需要依赖另一个对象，该对象中保存其所依赖对象的实例，生成依赖对象的方式不再该对象中通过new创建，而是调用者外部创建依赖对象，通过一定的方式进行传入。

#####Demo
```java
public class Classes {
    // 依赖类
    private Boy boy;
    
    public Classes(){
        // 在当前对象中直接 new 出依赖类
        boy = new Boy();
    }
    
    public void run(){
        boy.run();
    }
}

public class Boy {
    String name;
    
    public Boy(){
    
    }
    public void run(){
    
    }
}
```
有一个班级，班级中有一个boy。直接在班级中new出boy。班级中有一个run()方法，其内部实际调用的是boy的run()。

此时看着无大碍，那么如果boy发生了变化，其构造方法发生了变化，需要传入一个姓名。那么需要修改代码：

```java
public class Boy {

    String name;
    
    public Boy(String name ){
        // 修改了构造方法
        this.name = name;
    }
    
    public void run(){
    
    }
}

public class Classes {
    // 依赖类
    private Boy boy;
    public Classes(){
        //  因为Boy的构造方法发生变化，所以需要修改该处代码
        boy = new Boy("lilei");
    }
    public void run(){
        boy.run();
    }
}
```
修改了Boy的构造方法之后，因为Classes依赖Boy，所以其内部也需要修改。

如果又发生了变化，Boy的姓名更改了，又要修改Classes中的代码。。。这样的话，一个还是不明显，当工程量很浩大时，呵呵了。

此时，我们可以将Boy该对象的实例化交给其调用者，通过某种方式传入进来。这种模式就是依赖注入。

###二.依赖注入的三种实现方式

#####1.构造方法注入
该方式是通过构造方法将其所依赖的外部类对象传入进来，是我认为的最简单的方式。其实现方式如下，我们修改之前的代码：
```java
public class Classes {
    // 依赖类
    private Boy boy;
    /**
     * 构造方法注入，通过构造方法传入该对象
     * @param boy
     */
    public Classes(Boy boy) {
        this.boy = boy;
    }
    public void run() {
        boy.run();
    }
```
#####2.Setter 注入
```java
public class Classes {
    //....

    private Boy boy;
    
    public void setBoy(Boy boy){
        this.boy = boy;
    }
    
    //....

}
```
通过手动方式调用set方法将Boy设置进来。

#####3.接口方式
接口方式是定义一个接口，该接口中声明一个注入的方法，而需要注入的类实现该接口，实现接口中定义的方法。
​```java
/**
 *

 * 定义接口，声明注入方法
 * Created by MH on 2016/7/12.
 */
public interface BoyInjection {

    void inject(Boy boy);
}

public class Classes implements BoyInjection {
    //....
    private Boy boy;
    @Override
    public void inject(Boy boy) {
        //实现接口中的方法
        this.boy = boy;
    }
    //....
}

###三.Android Dagger2依赖注入

####1.添加依赖build.gradle

```java 
 compile 'com.google.dagger:dagger:2.7'
 annotationProcessor 'com.google.dagger:dagger-compiler:2.7'
```

*如果，build项目抛出“ Execution failed for task ':app:javaPreCompileDebug” 就在gradle中填写下面代码。

```java
defaultConfig {
    javaCompileOptions { annotationProcessorOptions { 		      includeCompileClasspath = true } }
}
```

####2.如何使用
#####1、 创建Boyt类：
```java
public class Boy {
    String name;
    @Inject()
    public Boy() {

    }
    public void setName(String name) {
        this.name = name;
    }
    public void run() {
        Log.e("boy", name + "跑起来啦");
    }
}
```
Boy类中有一个空的构造方法，并且在构造方法上面添加了一个@Inject注解。
然后，我们使用ctrl+F9(mac使用Cmd+F9)进行一次编译，查看项目路径：
app\build\generated\source\apt\debug\你的包名下    
编译器自动生成了一个生成了Boy_Factory
```java
public enum Boy_Factory implements Factory<Boy> {
  INSTANCE;
  @Override
  public Boy get() {
    return new Boy();
  }
  public static Factory<Boy> create() {
    return INSTANCE;
  }
}
``
#####2、调用Student类：
​```java
public class DragActivity extends AppCompatActivity {
    @Inject
    Boy boy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);
        //新添代码
        DaggerDragComponent.builder()
                .dragModule(new DragModule(this))
                .build()
                .inject(this);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boy.setName("小码");
                boy.run();
                Toast.makeText(DragActivity.this, boy.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
```
Activity类中创建一个成员变量Boy，按照Dagger2给我们的指示，当我们需要一个Boy，我们只需要在这个成员变量上方加一个@Inject注解，编译器会自动帮我们产生对应的代码，我们就可以直接使用这个Boy对象了！运行代码，并点击Button直接报空指针异常：
#####3、Module和Component使用：
创建Module类、Component接口
```java
@Module
public class DragModule {
    DragActivity activity;
    public DragModule(DragActivity activity){

        this.activity = activity;
    }
}
```

```java
@Component(modules = DragModule.class)
public interface DragComponent {

    void inject(DragActivity activity);
}
```
* Module类上方的@Module注解意味着这是一个提供数据的"模块";
* @Component(modules = A01SimpleModule.class)注解意味着这是一个"组件" 或称作：注射器。

