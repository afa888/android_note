package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.Annotation.ConsAnnotation;
import com.example.android.Annotation.Fields;
import com.example.android.Annotation.Programer;

import java.lang.reflect.Field;
import java.util.Arrays;

public class ActivityAnnotation extends AppCompatActivity {
    @GetViewTo(R.id.tv)
    private TextView mTv;

    @GetViewTo(R.id.bt)
    private Button mBt;

    @GetViewTo(R.id.btget)
    private Button mBget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotation);
        //通过注解生成View；
        getAllAnnotationView();
        mBget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Programer user = new Programer();
                    // 1、 获取 User类上的注解 @ConsAnnotation
                    ConsAnnotation anno = user.getClass().getAnnotation(ConsAnnotation.class);
                    String[] arr = anno.request();
                    System.out.println(Arrays.toString(arr)); // [hello, world, annotation!]

                    // 2、 获取User类中 private String userName; 变量上的注解 @Field
                    Field f = user.getClass().getDeclaredField("userName");
                    Fields anno2 = f.getAnnotation(Fields.class);
                    user.setUserName(anno2.value());
                    Toast.makeText(ActivityAnnotation.this, "1 == " + Arrays.toString(arr) + "2 == " + anno2.value(), Toast.LENGTH_SHORT).show();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        });
        mBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean hasAnnotation = Test.class.isAnnotationPresent(TestAnnotation.class);
                if (hasAnnotation) {
                    TestAnnotation testAnnotation = Test.class.getAnnotation(TestAnnotation.class);

                    Log.e("ActivityAnnotation", "id:" + testAnnotation.id());
                    Log.e("ActivityAnnotation", "msg:" + testAnnotation.msg());
                }

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
                Log.e("ActivityAnnotation", field.getName().toString());
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
