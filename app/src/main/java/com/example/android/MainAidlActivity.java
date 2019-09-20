package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.aidl.Book;
import com.example.android.aidl.IBookManager;

public class MainAidlActivity extends AppCompatActivity {
    Button btBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_aidl);
        btBind =  findViewById(R.id.btBind);
        btBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindService(btBind);
            }
        });
        findViewById(R.id.btAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBook();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIBookManager != null) {
            unbindService(mServiceConnection);
        }
    }

    private IBookManager mIBookManager;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIBookManager = IBookManager.Stub.asInterface(service);
            Toast.makeText(MainAidlActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIBookManager = null;
        }
    };

    public void bindService(View view) {
        Intent intent = new Intent();
        intent.setAction("com.example.android.aidl.BookManagerService");
        intent.setPackage(getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    public void addBook() {
        if (mIBookManager != null) {
            try {
                mIBookManager.addBook(new Book(18, "漫画书"));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
