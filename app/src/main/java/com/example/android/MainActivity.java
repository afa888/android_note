package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.android.DraggerDemo.DragActivity;
import com.example.android.EventBusDemo.EventActivity;
import com.example.android.Rxjava.RxjavaActivity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ActivityAnnotation.class));
            }
        });
        findViewById(R.id.tv2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DragActivity.class));
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, EventActivity.class));
            }
        });

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RxjavaActivity.class));
            }
        });
        /* "************通过反射创建对象的方式：********************"*/
        //第一种方式获取Class对象
        Student stu1 = new Student();//这一new 产生一个Student对象，一个Class对象。
        Class stuClass = stu1.getClass();//获取Class对象
        Log.e("MainActivity", stuClass.getName().toString());

        //第二种方式获取Class对象
        Class stuClass2 = Student.class;
        Log.e("MainActivity", (stuClass == stuClass2) + "");//判断第一种方式获取的Class对象和第二种方式获取的是否是同一个

        //第三种方式获取Class对象
        try {
            Class clazz = Class.forName("com.example.android.Student");//注意此字符串必须是真实路径，就是带包名的类路径，包名.类名
            Log.e("MainActivity", (clazz == stuClass2) + "");//判断三种方式是否获取的是同一个Class对象








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
            //	System.out.println("obj = " + obj);
            //	Student stu = (Student)obj;

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
            Object obj6 = stuClass.getConstructor().newInstance();
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

            //	strList.add(100);
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
    }
}
