package com.example.android;

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
