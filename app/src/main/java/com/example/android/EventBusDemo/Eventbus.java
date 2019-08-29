package com.example.android.EventBusDemo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Eventbus {
    //这个map用于保存订阅者的信息
    private Map<Object, List<SubscribleMethod>> cacheMap;
    private Eventbus(){
        this.cacheMap = new HashMap<>();
    }

    private static class SingleTonHoler{
        private static Eventbus INSTANCE = new Eventbus();
    }

    public static Eventbus getDefault() {
        return SingleTonHoler.INSTANCE;

    }

    public void register(Object activity) {
        Class<?> clazz = activity.getClass();
        List<SubscribleMethod> list = cacheMap.get(activity);
        //判断是否注册过
        if (list == null) {
            list = getSubscribleMethods(activity);
            cacheMap.put(activity, list);
        }
    }

    //获取activity下所有的订阅方法
    private List<SubscribleMethod> getSubscribleMethods(Object activity) {
        List<SubscribleMethod> list = new ArrayList<>();
        Class clazz = activity.getClass();
        while (clazz != null) {
            String name = clazz.getName();
            //如果包名是java javax android开头的 说明是系统的方法 直接跳过 节省事件
            if (name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.")) {
                break;
            }
            //这里使用反射获取的是类自身声明的所有方法
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                //返回这个元素的注解指定类型
                Subscribe subscribe = method.getAnnotation(Subscribe.class);
                if (subscribe == null) {
                    continue;
                }
                //对象表示的方法的形式参数类型,event只能接受一个参数,
                Class[] paratems = method.getParameterTypes();
                if (paratems.length != 1) {
                    String methodName = method.getDeclaringClass().getName() + "." + method.getName();
                    try {
                        throw new Exception("@Subscribe method " + methodName +
                                "must have exactly 1 parameter but has " + paratems.length);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //满足所有要求 找到所有的订阅方法 全部return出去
                ThreadMode threadMode = subscribe.threadMode();
                SubscribleMethod subscribleMethod = new SubscribleMethod(method
                        , threadMode, paratems[0]);
                list.add(subscribleMethod);

            }
            clazz = clazz.getSuperclass();
        }
        return list;
    }

    public void post(final Object object) {
        Set<Object> set = cacheMap.keySet();
        Iterator iterator = set.iterator();
        //遍历caheMap表中所有的订阅者
        while (iterator.hasNext()) {
            //获取到actiivty对象
            final Object activity = iterator.next();
            //获取activity下存放订阅者信息的list
            List<SubscribleMethod> list = cacheMap.get(activity);
            //遍历所有订阅者
            for (final SubscribleMethod subscribleMethod : list) {
                //判断订阅类型是不是一样的 如果是一样的就需要接受事件
                if (subscribleMethod.getEventType().isAssignableFrom(object.getClass())) {
                    invoke(subscribleMethod, activity, object);
                }
            }
        }
    }

    //反射发送事件
    private void invoke(SubscribleMethod subscribleMethod, Object activity, Object object) {
        Method method = subscribleMethod.getMethod();
        try {
            //执行activity中注解的方法
            method.invoke(activity, object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


}
