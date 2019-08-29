package com.example.android.EventBusDemo;

public class Person {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    private String name;
    private String sex;

    public Person(String name, String sex) {
        this.name = name;
        this.sex = sex;
    }
}
