package com.example.android;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//@Target：注解对象的作用范围。

/*
@Target
Target标明了注解的适用范围，对应ElementType枚举，明确了注解的有效范围。

        TYPE：类、接口、枚举、注解类型。
        FIELD：类成员（构造方法、方法、成员变量）。
        METHOD：方法。
        PARAMETER：参数。
        CONSTRUCTOR：构造器。
        LOCAL_VARIABLE：局部变量。
        ANNOTATION_TYPE：注解。
        PACKAGE：包声明。
        TYPE_PARAMETER：类型参数。
        TYPE_USE：类型使用声明。
*/

//@Retention：注解保留的生命周期

/*@Retention
Retention说标明了注解被生命周期，对应RetentionPolicy的枚举，表示注解在何时生效：

SOURCE：只在源码中有效，编译时抛弃，如上面的@Override。

CLASS：编译class文件时生效。

RUNTIME：运行时才生效。*/

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GetViewTo {
        int value() default -1;
}
