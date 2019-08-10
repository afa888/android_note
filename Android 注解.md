##Android 注解
>研究EventBus和Retrofit 、bufferKnike的过程中，都遇到了**注解**这个概念。不少的人员会认为注解的地位不高。其实同 classs 和 interface 一样，注解也属于一种类型。它是在 Java SE 5.0 版本中开始引入的概念。这里总结下
>
>

###一.什么是注解
Annotation（注解）就是Java提供了一种源程序中的元素关联任何信息或者任何元数据（metadata）的途径和方法。它提供数据用来解释程序代码，但是注解并非是所解释的代码本身的一部分。注解对于代码的运行效果没有直接影响。



###二.注解的作用

####注解有很多用处主要如下：

* 提供信息给编译器： 编译器可以利用注解来探测错误和警告信息
- 编译阶段时的处理： 软件工具可以用来利用注解信息来生成代码、Html文档或者做其它相应处理（比如eventbus就利用编译的时候生成Java代码）。
- 运行时的处理： 某些注解可以在程序运行的时候接受代码的提取

###三.注解的定义
注解通过 @interface关键字进行定义
```java
@Retention(RetentionPolicy.RUNTIME)
public @interface TestAnnotation {
}
```

它的形式跟接口很类似，不过前面多了一个 @ 符号。上面的代码就创建了一个名字为 TestAnnotaion 的注解。

你可以简单理解为创建了一张名字为 TestAnnotation 的标签。

###四.注解的应用
上面创建了一个注解，那么注解的的使用方法是什么呢。
```java
@TestAnnotation
public class Test {
}
```

###五.元注解

元注解是可以注解到注解上的注解，或者说元注解是一种基本注解，但是它能够应用到其它的注解上面。

如果难于理解的话，你可以这样理解。元注解也是一张标签，但是它是一张特殊的标签，它的作用和目的就是给其他普通的标签进行解释说明的。

元标签有 @Retention、@Documented、@Target、@Inherited、@Repeatable 5 种。
在解释元注解前直接看个自定义注解更好理解，然后再一一介绍各个注解的功能(有点多怕看了更晕)。

###六.自定义注解
使用运行时注解 模仿 bufferKnike
1.定义一个注解
```java
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GetViewTo {
        int value() default -1;
}
```
2.模仿knife
```java
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.lang.reflect.Field;
public class ActivityAnnotation extends AppCompatActivity {
    @GetViewTo(R.id.tv)
    private TextView mTv;
    @GetViewTo(R.id.bt)
    private Button mBt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotation);
        //通过注解生成View；
        getAllAnnotationView();
        mBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 解析注解，结合反射获取控件
     */
    private void getAllAnnotationView() {
        try {
            /*通过反射获取对象成员的字段值*/
            Field[] fields = this.getClass().getDeclaredFields();//获取自己声明的各种字段，包括public，protected，private
            for (Field field : fields) {
                Log.e("ActivityAnnotation",field.getName().toString());
                //判断注解
                if (field.getAnnotations() != null) {
                    if (field.isAnnotationPresent(GetViewTo.class)) {
                        //允许修改反射属性
                        field.setAccessible(true);
                        GetViewTo getViewTo = field.getAnnotation(GetViewTo.class);
                        field.set(this, findViewById(getViewTo.value()));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

以上就是butterKnife的注解和反射应用，不过真正的ButterKnife的`@BindView`注解，它是一个编译时注解，在编译时生成对应java代码，实现注入


![img](https://upload-images.jianshu.io/upload_images/3673902-8b47982ec6cb1e35.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1000/format/webp)

###七.主要元注解说明
####@Retention
Retention 的英文意为保留期的意思。当 @Retention 应用到一个注解上的时候，它解释说明了这个注解的的存活时间。
RetentionPolicy.SOURCE 注解只在源码阶段保留，在编译器进行编译时它将被丢弃忽视。
RetentionPolicy.CLASS 注解只被保留到编译进行的时候，它并不会被加载到 JVM 中。
RetentionPolicy.RUNTIME 注解可以保留到程序运行的时候，它会被加载进入到 JVM 中，所以在程序运行时可以获取到它们。
我们可以这样的方式来加深理解，@Retention 去给一张标签解释的时候，它指定了这张标签张贴的时间。@Retention 相当于给一张标签上面盖了一张时间戳，时间戳指明了标签张贴的时间周期。
```java
@Retention(RetentionPolicy.RUNTIME)
public @interface TestAnnotation {
}
```
上面的代码中，我们指定 TestAnnotation 可以在程序运行周期被获取到，因此它的生命周期非常的长。

####@Target
Target 是目标的意思，@Target 指定了注解运用的地方。
你可以这样理解，当一个注解被 @Target 注解时，这个注解就被限定了运用的场景。
类比到标签，原本标签是你想张贴到哪个地方就到哪个地方，但是因为 @Target 的存在，它张贴的地方就非常具体了，比如只能张贴到方法上、类上、方法参数上等等。@Target 有下面的取值
ElementType.ANNOTATION_TYPE 可以给一个注解进行注解
ElementType.CONSTRUCTOR 可以给构造方法进行注解
ElementType.FIELD 可以给属性进行注解
ElementType.LOCAL_VARIABLE 可以给局部变量进行注解
ElementType.METHOD 可以给方法进行注解
ElementType.PACKAGE 可以给一个包进行注解
ElementType.PARAMETER 可以给一个方法内的参数进行注解
ElementType.TYPE 可以给一个类型进行注解，比如类、接口、枚举

####@Inherited
Inherited 是继承的意思，但是它并不是说注解本身可以继承，而是说如果一个超类被 @Inherited 注解过的注解进行注解的话，那么如果它的子类没有被任何注解应用的话，那么这个子类就继承了超类的注解。
说的比较抽象。代码来解释。
```java
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@interface Test {}
@Test
public class A {}
public class B extends A {}
```
注解 Test 被 @Inherited 修饰，之后类 A 被 Test 注解，类 B 继承 A,类 B 也拥有 Test 这个注解。
####@Repeatable
Repeatable 自然是可重复的意思。@Repeatable 是 Java 1.8 才加进来的，所以算是一个新的特性。
举个例子，一个人他既是程序员又是产品经理,同时他还是个画家。
```java
@interface Persons {
	Person[]  value();
}


@Repeatable(Persons.class)
@interface Person{
	String role default "";
}


@Person(role="artist")
@Person(role="coder")
@Person(role="PM")
public class SuperMan{
	
}
```
###八.注解的属性
注解的属性也叫做成员变量。注解只有成员变量，没有方法。注解的成员变量在注解的定义中以“无形参的方法”形式来声明，其方法名定义了该成员变量的名字，其返回值定义了该成员变量的类型。
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestAnnotation {
	
	int id();
	
	String msg();

}

```
注解中属性可以有默认值，默认值需要用 default 关键值指定。比如：
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestAnnotation {
	
	public int id() default -1;
	
	public String msg() default "Hi";

}
```
上面代码定义了 TestAnnotation 这个注解中拥有 id 和 msg 两个属性。在使用的时候，我们应该给它们进行赋值。
赋值的方式是在注解的括号内以 value="" 形式，多个属性之前用 ，隔开。
```java
@TestAnnotation(id=3,msg="hello annotation")
public class Test {

}
```

获取类的注解值

通过 getAnnotation() 方法来获取 Annotation 对象。

```java
	boolean hasAnnotation = Test.class.isAnnotationPresent(TestAnnotation.class);
		
		if ( hasAnnotation ) {
			TestAnnotation testAnnotation = Test.class.getAnnotation(TestAnnotation.class);
			
			System.out.println("id:"+testAnnotation.id());
			System.out.println("msg:"+testAnnotation.msg());
		}
```

###总结
1.如果注解难于理解，你就把它类同于标签，标签为了解释事物，注解为了解释代码。
2.注解的基本语法，创建如同接口，但是多了个 @ 符号。
3.注解的元注解。
4.注解的属性。
5.注解主要给编译器及工具类型的软件用的。
6.注解的提取需要借助于 Java 的反射技术，反射比较慢，所以注解使用时也需要谨慎计较时间成本。