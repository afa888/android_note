package com.example.remote_client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;

import com.example.remote_service.IMyAidlInterface;

import java.lang.reflect.Proxy;

public class MainActivity extends AppCompatActivity {
    private IMyAidlInterface mIMyAidlInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent();
        // 服务端AndroidManifest.xml文件该Service所配置的action
        intent.setAction("com.example.remote_service");
        // Service所在的包名
        intent.setPackage("com.example.remote_service");
        bindService(intent, new ConnectCallBack(), Context.BIND_AUTO_CREATE);


        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login() {
        if (mIMyAidlInterface != null) {
            try {
                mIMyAidlInterface.login("mundane", "123456");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    class ConnectCallBack implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIMyAidlInterface = null;
        }
    }

}
