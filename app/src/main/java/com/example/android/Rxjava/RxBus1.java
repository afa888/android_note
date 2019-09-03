package com.example.android.Rxjava;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class RxBus1 {
    //确保本条指令不会因编译器的优化而省略，且要求每次直接读值,编译器在用到这个变量时必须
    // 每次都小心地重新读取这个变量的值，而不是使用保存在寄存器里的备份
    private static volatile RxBus1 defaultInstance;
    private final Subject<Object, Object> bus;

    public RxBus1(){
        bus = new SerializedSubject<>(PublishSubject.create());
    }

    public static RxBus1 getDefault(){
        if(defaultInstance==null){
            synchronized (RxBus1.class){
                if(defaultInstance==null){
                    defaultInstance = new RxBus1();
                }
            }
        }
        return defaultInstance;
    }


    public void post(Object o){
        bus.onNext(o);
    }


    public <T> Observable<T> toObservable(Class<T> eventType){
        return bus.ofType(eventType);

    }
}
