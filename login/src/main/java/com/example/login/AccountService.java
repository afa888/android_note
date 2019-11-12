package com.example.login;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.componentbase.service.IAccountService;

public class AccountService implements IAccountService {
    @Override
    public boolean isLogin() {
        return AccountUtils.userInfo != null;
    }

    @Override
    public String getAccountId() {
        return AccountUtils.userInfo == null ? null : AccountUtils.userInfo.getAccountId();
    }

    @Override
    public Fragment newUserFragment(Activity activity, int containerId, FragmentManager manager, Bundle bundle, String tag) {
        FragmentTransaction transaction = manager.beginTransaction();
        // 创建 UserFragment 实例，并添加到 Activity 中
        Fragment userFragment = new BlankFragment();
        transaction.add(containerId, userFragment, tag);
        transaction.commit();
        return userFragment;
    }

    @Override
    public Class<?> get() {
        return LoginActivity.class;
    }
}
