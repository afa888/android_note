package com.example.login;

import android.app.Application;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.arouterlibrary.Arout.Arouter;
import com.example.base.BaseApp;
import com.example.componentbase.ServiceFactory;


public class LoginApp extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
        initModuleApp(this);
        initModuleData(this);
    }

    @Override
    public void initModuleApp(Application application) {
        // 将 AccountService 类的实例注册到 ServiceFactory
        Log.e("LoginApp", "initModuleApp");
        ServiceFactory.getInstance().setAccountService(new AccountService());
        Arouter.getInstance().init(this);
        //new ActivityUtils().putActivity();

    }

    @Override
    public void initModuleData(Application application) {
        Log.e("LoginApp", "initModuleData");
    }
}

