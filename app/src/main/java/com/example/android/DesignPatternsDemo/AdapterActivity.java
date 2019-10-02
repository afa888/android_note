package com.example.android.DesignPatternsDemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.android.R;
//适配器模式，能在之前预测不使用就不使用。作用是让不能一起工作的类能一起工作，转接成用户期待的功能；
public class AdapterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Captain captain = new Captain(new FishingBoatAdapter());
        captain.row();
    }
}
 interface RowingBoat {
    void row();
}
//快艇
class Captain implements RowingBoat {

    private RowingBoat rowingBoat;

    public Captain(RowingBoat rowingBoat) {
        this.rowingBoat = rowingBoat;
    }

    @Override
    public void row() {
        rowingBoat.row();
    }
}
//现在海盗来了，只能使用渔船逃跑
//渔船
class FishingBoat {
    public void sail() {
        Log.e("AdapterActivity","使用渔船");
    }
}

class FishingBoatAdapter implements RowingBoat {

    private FishingBoat boat;

    public FishingBoatAdapter() {
        boat = new FishingBoat();
    }

    @Override
    public void row() {
        boat.sail();
    }
}



