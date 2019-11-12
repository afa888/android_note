package com.example.componentbase.service;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public interface IAccountService {
    /**
     * 是否已经登录
     * @return
     */
    boolean isLogin();

    /**
     * 获取登录用户的 AccountId
     * @return
     */
    String getAccountId();

    Fragment newUserFragment(Activity activity, int containerId, FragmentManager manager, Bundle bundle, String tag);

    Class<?> get();
}
