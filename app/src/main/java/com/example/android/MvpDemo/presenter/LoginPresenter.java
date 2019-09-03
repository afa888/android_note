package com.example.android.MvpDemo.presenter;

public interface LoginPresenter {
    void validateCredentials(String username, String password);

    void onDestroy();
}
