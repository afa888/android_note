package com.example.android.DraggerDemo;

import android.util.Log;

import javax.inject.Inject;

public class Boy {
    String name;

    @Inject()
    public Boy() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public void run() {
        Log.e("boy", name + "跑起来啦");

    }
}
