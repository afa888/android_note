package com.example.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
//声明这个注解的生命周期  source源码期-->class编译期-->runtime运行期
@Retention(RetentionPolicy.CLASS)
public @interface BindPath {
    String value();//这个就是我们需要跳转路由地址
}
