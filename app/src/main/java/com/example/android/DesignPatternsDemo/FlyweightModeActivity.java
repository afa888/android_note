package com.example.android.DesignPatternsDemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.android.R;

import java.util.HashMap;
import java.util.Hashtable;

//享元模式：共享模式
public class FlyweightModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flyweight_mode);

        WebSiteFactory f = new WebSiteFactory();

        WebSite fx = f.GetWebSiteCategory("产品展示");
        fx.Use(new User("小才"));

        WebSite fx2 = f.GetWebSiteCategory("产品展示");
        fx2.Use(new User("小才1"));


        WebSite fx3 = f.GetWebSiteCategory("博客");
        fx3.Use(new User("小la"));

        WebSite fx4 = f.GetWebSiteCategory("博客");
        fx4.Use(new User("垃圾"));



    }
}

 class User{

    private String name;
    public User(String name){
        this.name = name;
    }

     public String getName() {
         return name;
     }
 }

 abstract class WebSite{
    public abstract void Use(User user);
 }

 class ConcreteWebSite extends WebSite{
    private String type;
    public ConcreteWebSite(String type){

    }
     @Override
     public void Use(User user) {
        Log.e("FlyweightModeActivity","网站类型"+type+"    用户  "+user.getName());
     }
 }

 class WebSiteFactory{
     Hashtable<Object,WebSite> flyweights = new Hashtable<Object, WebSite>();

    public WebSite GetWebSiteCategory(String key){
        if(!flyweights.contains(key)){
            flyweights.put(key,new ConcreteWebSite(key));
        }
        return  flyweights.get(key);
    }

    public int GetWebSiteCount(){
        return flyweights.size();
    }
 }