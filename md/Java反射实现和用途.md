
# Java反射实现和用途

##一、反射的概述
JAVA反射机制是在运行状态中，对于任意一个类，都能够知道这个类的所有属性和方法；对于任意一个对象，都能够调用它的任意一个方法和属性；这种动态获取的信息以及动态调用对象的方法的功能称为java语言的反射机制。要想解剖一个类,必须先要获取到该类的字节码文件对象。而解剖使用的就是Class类中的方法.所以先要获取到每一个字节码文件对应的Class类型的对象.

##二、要想更好地理解反射，就要先知道java类的加载过程
每个类都会产生一个对应的Class对象，也就是保存在.class文件。所有类都是在对其第一次使用时，动态加载到JVM的，当程序创建一个对类的静态成员的引用时，就会加载这个类。Class对象仅在需要的时候才会加载，static修饰的属性和代码块初始化是在类加载时进行的。

##三.类加载的三个步骤
加载：由类加载器完成，找到对应的字节码，创建一个Class对象
链接：验证类中的字节码，为静态域分配空间
初始化：如果该类有超类，则对其初始化，执行静态初始化器和静态初始化块

![img](https://upload-images.jianshu.io/upload_images/3264661-817934a8b77d0ddc.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/839/format/webp)

![img](https://upload-images.jianshu.io/upload_images/3264661-80ccc66ff83050f1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/818/format/webp)

![img](https://upload-images.jianshu.io/upload_images/3264661-e37ad7ff514f3d99.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/825/format/webp)

##四.代码实现

###student类代码
```java
package com.example.android;
public class Student {

    //---------------构造方法-------------------
    //（默认的构造方法）
    Student(String str) {
        System.out.println("(默认)的构造方法 s = " + str);
    }
    
    //无参构造方法
    public Student() {
        System.out.println("调用了公有、无参构造方法执行了。。。");
    }
    
    //有一个参数的构造方法
    public Student(char name) {
        System.out.println("姓名：" + name);
    }
    
    //有多个参数的构造方法
    public Student(String name, int age) {
        System.out.println("姓名：" + name + "年龄：" + age);//这的执行效率有问题，以后解决。
    }
    
    //受保护的构造方法
    protected Student(boolean n) {
        System.out.println("受保护的构造方法 n = " + n);
    }
    
    //私有构造方法
    private Student(int age) {
        System.out.println("私有的构造方法   年龄：" + age);
    }
    
    //**********字段   获取成员变量并调用*************//
    public String name;
    protected int age;
    char sex;
    private String phoneNum;
    @Override
    public String toString() {
        return "Student [name=" + name + ", age=" + age + ", sex=" + sex
                + ", phoneNum=" + phoneNum + "]";
    }
    
    //**************成员方法***************//
    public void show1(String s){
        System.out.println("调用了：公有的，String参数的show1(): s = " + s);
    }
    protected void show2(){
        System.out.println("调用了：受保护的，无参的show2()");
    }
    void show3(){
        System.out.println("调用了：默认的，无参的show3()");
    }
    private String show4(int age) {
        System.out.println("调用了，私有的，并且有返回值的，int参数的show4(): age = " + age);
        return "abcd";
    }
}

```

###1.通过反射创建对象的方式
###2.通过反射获取成员变量并调用
###3.通过反射越过泛型检查

```java
  /* "************通过反射创建对象的方式：********************"*/
        //第一种方式获取Class对象
        Student stu1 = new Student();//这一new 产生一个Student对象，一个Class对象。
        Class stuClass = stu1.getClass();//获取Class对象
        Log.e("MainActivity", stuClass.getName().toString());

        //第二种方式获取Class对象
        Class stuClass2 = Student.class;
            //判断第一种方式获取的Class对象和第二种方式获取的是否是同一个
        Log.e("MainActivity", (stuClass == stuClass2) + "");
    
        //第三种方式获取Class对象
        try {
          //注意此字符串必须是真实路径，就是带包名的类路径，包名.类名
            Class clazz = Class.forName("com.example.android.Student");
          //判断三种方式是否获取的是同一个Class对象
            Log.e("MainActivity", (clazz == stuClass2) + "");


            /* "************通过反射获取构造方法并使用：********************"*/
    
            Constructor[] conArray = clazz.getConstructors();
            for (Constructor c : conArray) {
                Log.e("MainActivity", "**********************所有公有构造方法*********************************" + c);
            }


            System.out.println();
            conArray = clazz.getDeclaredConstructors();
            for (Constructor c : conArray) {
                Log.e("MainActivity", "************所有的构造方法(包括：私有、受保护、默认、公有)***************" + c);
            }


            Constructor con = clazz.getConstructor(null);
            //1>、因为是无参的构造方法所以类型是一个null,不写也可以：这里需要的是一个参数的类型，切记是类型
            //2>、返回的是描述这个无参构造函数的类对象。
    
            Log.e("MainActivity", "*****************获取公有、无参的构造方法*******************************con = " + con);
            //调用构造方法
            Object obj = con.newInstance();
            // System.out.println("obj = " + obj);
            // Student stu = (Student)obj;
    
            con = clazz.getDeclaredConstructor(int.class);
            Log.e("MainActivity", "******************获取私有构造方法，并调用*******************************" + con);
            //调用构造方法
            con.setAccessible(true);//暴力访问(忽略掉访问修饰符)
            obj = con.newInstance(16);








            /* "************通过反射获取成员变量并调用********************"*/
    
            //2.获取字段
            System.out.println("************获取所有公有的字段********************");
            Field[] fieldArray = stuClass.getFields();
            for (Field f : fieldArray) {
                System.out.println(f);
            }
            System.out.println("************获取所有的字段(包括私有、受保护、默认的)********************");
            fieldArray = stuClass.getDeclaredFields();
            for (Field f : fieldArray) {
                System.out.println(f);
            }
            System.out.println("*************获取公有字段**并调用***********************************");
            Field f = stuClass.getField("name");
            System.out.println(f);
            //获取一个对象
            Object obj2 = stuClass.getConstructor().newInstance();//产生Student对象--》Student stu = new Student();
            //为字段设置值
            f.set(obj2, "刘德华");//为Student对象中的name属性赋值--》stu.name = "刘德华"
            //验证
            Student stu = (Student) obj2;
            System.out.println("验证姓名：" + stu.name);


            System.out.println("**************获取私有字段****并调用********************************");
            f = stuClass.getDeclaredField("phoneNum");
            System.out.println(f);
            f.setAccessible(true);//暴力反射，解除私有限定
            f.set(obj2, "18888889999");











            /* "************通过反射获取成员方法并调用********************"*/
    
            //2.获取所有公有方法
            System.out.println("***************获取所有的”公有“方法*******************");
            stuClass.getMethods();
            Method[] methodArray = stuClass.getMethods();
            for (Method m : methodArray) {
                System.out.println(m);
            }
            System.out.println("***************获取所有的方法，包括私有的*******************");
            methodArray = stuClass.getDeclaredMethods();
            for (Method m : methodArray) {
                System.out.println(m);
            }
            System.out.println("***************获取公有的show1()方法*******************");
            Method m = stuClass.getMethod("show1", String.class);
            System.out.println(m);
            //实例化一个Student对象
            Object obj6= stuClass.getConstructor().newInstance();
            m.invoke(obj6, "刘德华");
    
            System.out.println("***************获取私有的show4()方法******************");
            m = stuClass.getDeclaredMethod("show4", int.class);
            System.out.println(m);
            m.setAccessible(true);//解除私有限定
            Object result = m.invoke(obj6, 20);//需要两个参数，一个是要调用的对象（获取有反射），一个是实参


            /*5、反射main方法*/
    
            /*6、反射方法的其它使用之---通过反射运行配置文件内容*/


            /* "************反射方法的其它使用之---通过反射越过泛型检查********************"*/
    
            ArrayList<String> strList = new ArrayList<>();
            strList.add("aaa");
            strList.add("bbb");
    
            // strList.add(100);
            //获取ArrayList的Class对象，反向的调用add()方法，添加数据
            Class listClass = strList.getClass(); //得到 strList 对象的字节码 对象
            //获取add()方法
            Method m2 = listClass.getMethod("add", Object.class);
            //调用add()方法
            m2.invoke(strList, 100);
    
            //遍历集合
            for (Object obj3 : strList) {
                System.out.println(obj3);
    
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
```





##四.总结
####1.Java反射框架提供以下功能：
* 在运行时判断任意一个对象所属的类
* 在运行时构造任意一个类的对象
* 在运行时判断任意一个类所具有的成员变量和方法（通过反射设置可以调用 private）
* 在运行时调用任意一个对象的方法

####2.反射常见的主要用途：

当我们在使用 IDE（如Eclipse\IDEA）时，当我们输入一个队长或者类并向调用它的属性和方法时，一按 (“.”)点号，编译器就会自动列出她的属性或方法，这里就会用到反射。

####3.反射最重要的用途就是开发各种通用框架。

很多框架（比如 Spring）都是配置化的（比如通过 XML文件配置JavaBean，Action之类的），为了保证框架的通用性，他们可能根据配置文件加载不同的对象或类，调用不同的方法，这个时候就必须用到反射——运行时动态加载需要加载的对象。

