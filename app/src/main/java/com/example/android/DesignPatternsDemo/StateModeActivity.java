package com.example.android.DesignPatternsDemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.android.R;
//状态模式：解决很多选项if else判断的时候，可以把每个判断放到类里面，跟策略模式不同的是每次后面还要判断或者另外设置状态
public class StateModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_mode);

        QQ qq = new QQ();
        qq.setState(new LeaveState());
        qq.handle();
        qq.handle();
        qq.handle();
        qq.handle();
    }
}

class QQ {
    public QQ() {
        state = new LeaveState();
    }

    private QQState state;

    public QQState getState() {
        return state;
    }

    public void setState(QQState state) {
        this.state = state;
    }

    public void handle() {
        state.handle(this);
    }
}

interface QQState {
    void handle(QQ qq);
}

class BusyState implements QQState {
    public void handle(QQ document) {
        //QQ处在忙碌中应该处理代码
        document.setState(new LeaveState());
        Log.e("StateModeActivity","QQ在忙碌中");
    }
}

class LeaveState implements QQState {
    public void handle(QQ document) {
        //离线状态应该处理代码
        document.setState(new LoginingState());
        Log.e("StateModeActivity","QQ已离线");
    }
}

class LiningState implements QQState {
    public void handle(QQ document) {
        //QQ在线应该处理代码
        document.setState(new BusyState());
        Log.e("StateModeActivity","QQ在线");
    }
}

class LoginingState implements QQState {
    public void handle(QQ document) {
        //登录中应该处理代码
        document.setState(new LiningState());
        Log.e("StateModeActivity","QQ登录中");
    }
}
