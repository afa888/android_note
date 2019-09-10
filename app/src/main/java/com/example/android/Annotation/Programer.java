package com.example.android.Annotation;


@ConsAnnotation(request = { "hello","world","annotation!" })
public class Programer {

    @Fields("中华人民共和国")
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
