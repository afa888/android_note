package com.example.android.DesignPatternsDemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.android.R;
/*
* 责任链模式是一种对象的行为模式。在责任链模式里，很多对象由每一个对象对其下家的引用而连接起来形成一条链。
* 请求在这个链上传递，直到链上的某一个对象决定处理此请求。
* 发出这个请求的客户端并不知道链上的哪一个对象最终处理这个请求，这使得系统可以在不影响客户端的情况下动态地重新组织和分配责任。
*
* */
public class ChainOfResponsibilityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chain_of_responsibility);
        StaffMember staffMember = new StaffMember();
        SectionChief sectionChief = new SectionChief();
        Director director = new Director();

        staffMember.setNextHandler(sectionChief);
        sectionChief.setNextHandler(director);

        staffMember.dispose("小王",400);
        staffMember.dispose("小混混",800);
        staffMember.dispose("老李",1200);

        staffMember.dispose("小明",10000);
    }
}
abstract class Handler {
    protected Handler nextHandler = null;

    public Handler getNextHandler() {
        return nextHandler;
    }

    public void setNextHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public abstract String dispose(String user , double fee);
}
//职员类：
 class StaffMember extends Handler {
    @Override
    public String dispose(String user, double fee) {
        if(fee < 500){
            System.out.println("职员 给了 "+user+" "+fee+"元");
        }else if (super.getNextHandler() == null){
            System.out.println("谁都处理不了 "+user+" 要 "+fee+"元的事情");
        }else {
            super.getNextHandler().dispose(user,fee);
        }
        return null;
    }
}
//处长类：
 class SectionChief extends Handler {
    @Override
    public String dispose(String user, double fee) {
        if(fee < 1000){
            System.out.println("小主管 给了 "+user+" "+fee+"元");
        }else if (super.getNextHandler() == null){
            System.out.println("谁都处理不了 "+user+" 要 "+fee+"元的事情");
        }else {
            super.getNextHandler().dispose(user,fee);
        }
        return null;
    }
}
//老大类：
class Director extends Handler {
    @Override
    public String dispose(String user, double fee) {
        if(fee < 5000){
            System.out.println("老大 给了 "+user+" "+fee+"元");
        }else if (super.getNextHandler() == null){
            System.out.println("谁都处理不了 "+user+" 要 "+fee+"元的事情");
        }else {
            super.getNextHandler().dispose(user,fee);
        }
        return null;
    }
}