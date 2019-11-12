package com.example.app2;

import android.app.Application;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.arouterlibrary.Arout.Arouter;
import com.example.base.AppConfig;
import com.example.base.BaseApp;
import com.example.login.LoginApp;

public class MainApplication extends BaseApp {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化组件 Application
        initModuleApp(this);

        // 所有 Application 初始化后的操作
        initModuleData(this);
        // 初始化 ARouter
        if (isDebug()) {
            // 这两行必须写在init之前，否则这些配置在init过程中将无效
            // 打印日志
            ARouter.openLog();
            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug();
        }
        // 初始化 ARouter
        ARouter.init(this);

        Arouter.getInstance().init(this);

        // 其他操作 ...
    }

    private boolean isDebug() {
        return BuildConfig.DEBUG;
    }


    @Override
    public void initModuleApp(Application application) {
        Log.e("MainApplication","initModuleApp");
        for (String moduleApp : AppConfig.moduleApps) {
            try {
                Class clazz = Class.forName(moduleApp);
                BaseApp baseApp = (BaseApp) clazz.newInstance();
                baseApp.initModuleApp(this);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }
    //Class.forName("driver"),driver是通过配置文件配置的，当你要连接不同的数据库时，
    // 也许你就只要改一下配置文件就可以了，不用改动程序。对扩展开发，对修改关闭，著名的OPC原则。
    @Override
    public void initModuleData(Application application) {
        Log.e("MainApplication","initModuleData");
        for (String moduleApp : AppConfig.moduleApps) {
            try {
                Class clazz = Class.forName(moduleApp);
                BaseApp baseApp = (BaseApp) clazz.newInstance();
                baseApp.initModuleData(this);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }
}
