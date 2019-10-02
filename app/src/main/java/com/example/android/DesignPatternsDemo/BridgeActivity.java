package com.example.android.DesignPatternsDemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.android.R;
//桥接模式：几种维度考虑问题，
/*定义：将抽象部分与它的实现部分分离，使它们都可以独立地变化。
        意图：将抽象与实现解耦。
         桥接模式主要应对的是由于实际的需要，某个类具有两个或者两个以上的维度变化（违反了SRP原则），
        如果只是用继承将无法实现这种需要，或者使得设计变得相当臃肿。*/
public class BridgeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge);
        Road road = new CementRoad(new Car());
        road.driveOnRoad();
    }
}

interface Vehicle {
    public void drive();
}

class Car implements Vehicle {
    @Override
    public void drive() {
        System.out.print("小轿车");
    }
}

class Bus implements Vehicle {
    @Override
    public void drive() {
        System.out.print("大巴");
    }
}

abstract class Road {
    protected Vehicle vehicle;

    public Road(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public abstract void driveOnRoad();
}

class UnpavedRoad extends Road {
    public UnpavedRoad(Vehicle vehicle) {
        super(vehicle);
    }

    @Override
    public void driveOnRoad() {
        super.vehicle.drive();
        System.out.println("行驶在石子路");
    }
}

class CementRoad extends Road {
    public CementRoad(Vehicle vehicle) {
        super(vehicle);
    }

    @Override
    public void driveOnRoad() {
        super.vehicle.drive();
        System.out.println("行驶在水泥路");
    }
}
