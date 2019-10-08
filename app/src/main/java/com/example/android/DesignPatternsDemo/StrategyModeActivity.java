package com.example.android.DesignPatternsDemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.android.R;
/*
* 封装，继承，多态
*
*
*
*
*https://juejin.im/post/5a52144d6fb9a01c9b65c651
* 单一职责
* 开放封闭原则： 不能修改，只能扩展
* 依赖倒转原则：抽象不应该依赖细节，细节应该依赖于抽象   https://blog.csdn.net/yabay2208/article/details/73826719
* 里氏替换原则：子类可以扩展父类的功能，但不能改变父类原有的功能   https://blog.csdn.net/zhengzhb/article/details/7281833
* 迪米特原则：最少知识原则，强调类之间的松耦合，利于复用，改掉一个弱耦合的类，不会对关系的类造成波及（）
* 接口隔离原则
*
* */

abstract class CashSuper {
    public abstract double acceptCash(double money);
}

//正常收费
class CashNormal extends CashSuper {

    @Override
    public double acceptCash(double money) {
        return money;
    }
}

//打折收费
class CashRebate extends CashSuper {
    private double moneyRebate = 1d;

    public CashRebate(String moneyRebate) {
        this.moneyRebate = Double.parseDouble(moneyRebate);
    }

    @Override
    public double acceptCash(double money) {
        return money * moneyRebate;
    }
}


class CashReturn extends CashSuper {

    private double moneyCondition = 0.0d;
    private double moneyReturn = 0.0d;

    public CashReturn(String moneyCondition, String moneyReturn) {
        this.moneyCondition = Double.parseDouble(moneyCondition);
        this.moneyReturn = Double.parseDouble(moneyReturn);
    }

    @Override
    public double acceptCash(double money) {
        double result = money;
        if (money >= moneyCondition) {
            result = money - (int) (money / moneyCondition) * moneyReturn;
        }
        return result;
    }
}

//简单工厂模式
class CashFactory {
    public static CashSuper createCashAccept(String type) {
        CashSuper cs = null;
        switch (type) {
            case "正常收费": {
                cs = new CashNormal();
                break;
            }
            case "满300返100": {
                cs = new CashReturn("300", "100");
                break;
            }
            case "打8折": {
                cs = new CashRebate("0.8");
                break;
            }
        }
        return cs;

    }
}


//策略模式
class CashContext {
    private CashSuper cs;

    public CashContext(CashSuper csuper) {
        this.cs = csuper;
    }

    public double GetResult(double money) {
        return cs.acceptCash(money);
    }
}


//策略模式和工厂模式结合
class CashContext2 {
    private CashSuper cs;

    public CashContext2(String type) {
        switch (type){
            case "正常收费": {
                cs = new CashNormal();
                break;
            }
            case "满300返100": {
                cs = new CashReturn("300", "100");
                break;
            }
            case "打8折": {
                cs = new CashRebate("0.8");
                break;
            }
        }
    }

    public double GetResult(double money) {
        return cs.acceptCash(money);
    }
}

public class StrategyModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strategy_mode);
        //工厂模式调用
        findViewById(R.id.button14).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CashSuper cs = CashFactory.createCashAccept("打八折");
                double price = cs.acceptCash(90);
                Log.e("StrategyModeActivity", "price  八折= " + price);
            }
        });
        //策略模式调用
        findViewById(R.id.button15).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = "打八折";
                CashContext cs = null;
                switch (type) {
                    case "正常收费": {
                        cs = new CashContext(new CashNormal());
                        break;
                    }
                    case "满300返100": {
                        cs = new CashContext(new CashReturn("300", "100"));
                        break;
                    }
                    case "打8折": {
                        cs = new CashContext(new CashRebate("0.8"));
                        break;
                    }
                }

                double price = cs.GetResult(90);
                Log.e("StrategyModeActivity", "price  八折= " + price);
            }
        });
        //策略模式和工厂模式结合调用
        findViewById(R.id.button16).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = "打八折";
                CashContext2 cs = new CashContext2(type);
                double price = cs.GetResult(90);
                Log.e("StrategyModeActivity", "price  八折= " + price);
            }
        });

    }
}


