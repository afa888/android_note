package com.example.android.MvpDemo.model;

import com.example.android.MvpDemo.OnLoginFinishedListener;

public interface LoginModel {
    void login(String username, String password, OnLoginFinishedListener listener);
}
