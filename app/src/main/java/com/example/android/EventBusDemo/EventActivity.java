package com.example.android.EventBusDemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.android.R;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        //注册
        Eventbus.getDefault().register(this);
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //发送消息
                Eventbus.getDefault().post(new Person("abc", "男"));
            }
        });

    }

    //接受消息
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void receive(Person person) {
        Toast.makeText(this, person.getName() + "----" + person.getSex(), Toast.LENGTH_SHORT).show();
    }
}
