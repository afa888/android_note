package com.example.android.DesignPatternsDemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.android.R;
/*
* 在不改变原先代码的基础上动态的添加功能
* */
public class DecoratormodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoratormode);
        //装饰者模式
        ConcreteComponent concreteComponent = new ConcreteComponent();

        ConcreteDecorator decorator = new ConcreteDecorator(concreteComponent);
        //decorator.sing();

        ConcreteDecorator2 decorator2 = new ConcreteDecorator2(decorator);
        decorator2.sing();


    }
}

interface Component {
    void sing();
}


class ConcreteComponent implements Component {
    @Override
    public void sing() {
        Log.e("ConcreteComponent", "sing");
    }
}

abstract class Decorator implements Component {
    protected Component component;

    public Decorator(Component component) {
        this.component = component;
    }

    @Override
    public void sing() {
        component.sing();
    }
}

class ConcreteDecorator extends Decorator {

    public ConcreteDecorator(Component concreteComponent) {
        super(concreteComponent);
    }

    @Override
    public void sing() {
        Log.e("ConcreteDecorator", "先交钱");
        super.sing();
    }
}

class ConcreteDecorator2 extends Decorator {

    public ConcreteDecorator2(Component concreteComponent) {
        super(concreteComponent);
    }

    @Override
    public void sing() {
        Log.e("ConcreteDecorator", "先谈判");
        super.sing();
    }
}