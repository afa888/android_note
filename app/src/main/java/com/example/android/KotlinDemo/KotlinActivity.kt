package com.example.android.KotlinDemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.android.R

class KotlinActivity : AppCompatActivity() {
    val a: Int = 1;
    var b = 9;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        b =10;
    }

    fun add(a:Int,b:Int):Int{
        return a+b
    }

    fun printAdd(a:Int,b:Int):Unit{
        Toast.makeText(this,"jkjk",Toast.LENGTH_SHORT).show();
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}
