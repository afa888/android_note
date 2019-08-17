package com.example.android.DraggerDemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.android.R;

import javax.inject.Inject;


public class DragActivity extends AppCompatActivity {

    @Inject
    Boy boy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);
        //新添代码
        DaggerDragComponent.builder()
                .dragModule(new DragModule(this))
                .build()
                .inject(this);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boy.setName("小码");
                boy.run();
                Toast.makeText(DragActivity.this, boy.toString(), Toast.LENGTH_SHORT).show();

            }
        });


    }
}
