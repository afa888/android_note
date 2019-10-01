package com.example.android.DesignPatternsDemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.android.R;

import java.io.Serializable;
import java.util.ArrayList;

//原型模式：隐藏创建对象的细节，多用在创建实例比较复杂或者耗时的情况下,因为复制一个已经存在的实例可以使程序运行更高效.
public class PrototypeModeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prototype_mode);


        // 原始对象
        Student stud = new Student("杨充", "潇湘剑雨");
        Log.e("PrototypeModeActivity", "原始对象: " + stud.getName() + " - " + stud.getSubj().getName());

        // 拷贝对象
        Student clonedStud = (Student) stud.clone();
        Log.e("PrototypeModeActivity", "拷贝对象: " + clonedStud.getName() + " - " + clonedStud.getSubj().getName());

        // 原始对象和拷贝对象是否一样：
        Log.e("PrototypeModeActivity", "原始对象和拷贝对象是否一样: " + (stud == clonedStud));
        // 原始对象和拷贝对象的name属性是否一样
        Log.e("PrototypeModeActivity", "原始对象和拷贝对象的name属性是否一样: " + (stud.getName() == clonedStud.getName()));
        // 原始对象和拷贝对象的subj属性是否一样
        Log.e("PrototypeModeActivity", "原始对象和拷贝对象的subj属性是否一样: " + (stud.getSubj() == clonedStud.getSubj()));

        stud.setName("小杨逗比");
        stud.getSubj().setName("潇湘剑雨大侠");
        Log.e("PrototypeModeActivity", "更新后的原始对象: " + stud.getName() + " - " + stud.getSubj().getName());
        Log.e("PrototypeModeActivity", "更新原始对象后的克隆对象: " + clonedStud.getName() + " - " + clonedStud.getSubj().getName());

        //浅拷贝结果
        /*2019-10-01 15:29:19.718 11547-11547/com.example.android E/PrototypeModeActivity: 原始对象: 杨充 - 潇湘剑雨
2019-10-01 15:29:19.718 11547-11547/com.example.android E/PrototypeModeActivity: 拷贝对象: 杨充 - 潇湘剑雨
2019-10-01 15:29:19.718 11547-11547/com.example.android E/PrototypeModeActivity: 原始对象和拷贝对象是否一样: false
2019-10-01 15:29:19.718 11547-11547/com.example.android E/PrototypeModeActivity: 原始对象和拷贝对象的name属性是否一样: true
2019-10-01 15:29:19.718 11547-11547/com.example.android E/PrototypeModeActivity: 原始对象和拷贝对象的subj属性是否一样: true
2019-10-01 15:29:19.718 11547-11547/com.example.android E/PrototypeModeActivity: 更新后的原始对象: 小杨逗比 - 潇湘剑雨大侠
2019-10-01 15:29:19.718 11547-11547/com.example.android E/PrototypeModeActivity: 更新原始对象后的克隆对象: 杨充 - 潇湘剑雨大侠*/

        //深拷贝结果
     /*   2019-10-01 15:50:20.969 11711-11711/com.example.android E/PrototypeModeActivity: 原始对象: 杨充 - 潇湘剑雨
        2019-10-01 15:50:20.969 11711-11711/com.example.android E/PrototypeModeActivity: 拷贝对象: 杨充 - 潇湘剑雨
        2019-10-01 15:50:20.969 11711-11711/com.example.android E/PrototypeModeActivity: 原始对象和拷贝对象是否一样: false
        2019-10-01 15:50:20.969 11711-11711/com.example.android E/PrototypeModeActivity: 原始对象和拷贝对象的name属性是否一样: true
        2019-10-01 15:50:20.969 11711-11711/com.example.android E/PrototypeModeActivity: 原始对象和拷贝对象的subj属性是否一样: false
        2019-10-01 15:50:20.969 11711-11711/com.example.android E/PrototypeModeActivity: 更新后的原始对象: 小杨逗比 - 潇湘剑雨大侠
        2019-10-01 15:50:20.969 11711-11711/com.example.android E/PrototypeModeActivity: 更新原始对象后的克隆对象: 杨充 - 潇湘剑雨*/
    }

    public class Subject {

        private String name;

        public Subject(String s) {
            name = s;
        }

        public String getName() {
            return name;
        }

        public void setName(String s) {
            name = s;
        }
    }


    public class Student implements Cloneable {

        // 对象引用
        private Subject subj;
        private String name;

        public Student(String s, String sub) {
            name = s;
            subj = new Subject(sub);
        }

        public Subject getSubj() {
            return subj;
        }

        public String getName() {
            return name;
        }

        public void setName(String s) {
            name = s;
        }

        /**
         * 重写clone()方法
         *
         * @return
         */
        public Object clone() {
            //浅拷贝
/*            try {
                // 直接调用父类的clone()方法
                return super.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }*/


            //深拷贝
            Student s = new Student(name, subj.getName());
            return s;
        }
    }


}


