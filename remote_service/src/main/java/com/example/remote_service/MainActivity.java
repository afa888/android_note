package com.example.remote_service;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

//https://juejin.im/entry/58e11c1e8d6d810061529b31
public class MainActivity extends AppCompatActivity implements AIDLService.OnLoginListener {

    private TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTv = findViewById(R.id.tv);
        Intent intent = new Intent(this, AIDLService.class);
        bindService(intent, mAIDLConnection, Service.BIND_AUTO_CREATE);

    }

    private ServiceConnection mAIDLConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AIDLService.MyBinder binder = (AIDLService.MyBinder) service;
            AIDLService aidlService = binder.getService();
            aidlService.setOnLoginListener(MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    Handler handler = new Handler();

    @Override
    public void login(final String username, final String password) {
        Log.e("登录成功", username + ", " + password);
        handler.post(new Runnable() {
            @Override
            public void run() {
                mTv.setText(username + ", " + password);
                Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
