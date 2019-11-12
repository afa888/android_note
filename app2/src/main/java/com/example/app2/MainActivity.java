package com.example.app2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import  com.example.arouterlibrary.Arout.Arouter;
import com.example.componentbase.ServiceFactory;
import com.example.login.LoginActivity;
import com.example.share.ShareActivity;
//组件化主项目
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 跳登录界面
     *
     * @param view
     */
    public void login(View view) {
      //  ARouter.getInstance().build("/account/login").navigation();
    }

    /**
     * 跳分享界面
     *
     * @param view
     */
    public void share(View view) {
       // ARouter.getInstance().build("/share/share").withString("share_content", "分享数据到微博").navigation();
    }
    /**
     * 跳 FragmentActivity
     *
     * @param view
     */
    public void fragment(View view) {
        startActivity(new Intent(this, FragmentActivity.class));
    }
    /**
     * 跳 FragmentActivity
     *
     * @param view
     */
    public void ha(View view) {
       // startActivity(new Intent(this, ShareActivity.class));
        Arouter.getInstance().jumpActivity("login/login",new Bundle());
    }

}
