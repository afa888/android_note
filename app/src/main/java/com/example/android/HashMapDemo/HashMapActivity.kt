package com.example.android.HashMapDemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.android.R


class HashMapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hash_map)

        findViewById<Button>(R.id.button13).setOnClickListener(View.OnClickListener {
            var map = EeasyHashMap();
            map.put("6", "b")
            map.put("6", "a")
            map.put("5", "c")
            map.put("4", "d")
            Log.e("HashMapActivity", "map size=" + map.size);
            Log.e("HashMapActivity", "map 6=" + map.get("6"));
        })


    }
}
