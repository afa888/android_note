package com.example.android.DesignPatternsDemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.android.R;

import java.util.ArrayList;
import java.util.List;
//封装一些作用于某种数据结构中的各元素的操作，它可以在不改变这个数据结构的前提下定义作用于这些元素的新的操作。
public class VisitorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor);
        Car2 car = new Car2();
        car.addVisit(new Body());
        car.addVisit(new Engine());

        Visitor print = new PrintCar();
        car.show(print);
    }
}
//访问者结构
 interface Visitor {
    void visit(Engine engine);
    void visit(Body body);
    void visit(Car car);
}
//汽车打印访问者：
 class PrintCar implements Visitor {
    public void visit(Engine engine) {
        System.out.println("Visiting engine");
    }
    public void visit(Body body) {
        System.out.println("Visiting body");
    }
    public void visit(Car car) {
        System.out.println("Visiting car");
    }
}
//汽车检修的访问者：
 class CheckCar implements Visitor {
    public void visit(Engine engine) {
        System.out.println("Check engine");
    }
    public void visit(Body body) {
        System.out.println("Check body");
    }
    public void visit(Car car) {
        System.out.println("Check car");
    }
}
//抽象的对象
 interface Visitable {
    void accept(Visitor visitor);
}
//具体的对象
class Body implements Visitable {
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

class Engine implements Visitable {
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}


//对象结构
class Car2{
    private List<Visitable> visit = new ArrayList<>();
    public void addVisit(Visitable visitable) {
        visit.add(visitable);
    }
    public void show(Visitor visitor) {
        for (Visitable visitable: visit) {
            visitable.accept(visitor);
        }
    }
}
