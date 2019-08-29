package com.example.android.EventBusDemo;

import java.lang.reflect.Method;

public class SubscribleMethod {
    //订阅事件的method对象
    private Method method;
    //订阅事件的线程位置
    private ThreadMode threadMode;
    //订阅事件类型
    private Class<?> eventType;

    public SubscribleMethod(Method method, ThreadMode threadMode, Class<?> eventType) {
        this.method = method;
        this.threadMode = threadMode;
        this.eventType = eventType;
    }

    public Method getMethod() {
        return method;
    }

    public ThreadMode getThreadMode() {
        return threadMode;
    }

    public Class<?> getEventType() {
        return eventType;
    }
}
