package com.example.android.mvvmDemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.android.R;

public class MvvmRecyclerViewActivity extends AppCompatActivity {
    private ObservableArrayList<UserObj> userObservableArrayList;
    private UserAdapter userAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvvm_recycler_view);
        RecyclerView rvList = findViewById(R.id.kklk);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        initData();

         userAdapter=new UserAdapter(userObservableArrayList);
        userAdapter.notifyDataSetChanged();
       // userObservableArrayList.addOnListChangedCallback(new Dy);
        rvList.setAdapter(userAdapter);

        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(userObservableArrayList);
                userAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initData() {
        userObservableArrayList = new ObservableArrayList<>();
        for(int i=0;i<5;i++){
            UserObj userObj = new UserObj();
            userObj.setLastName("魔道蛛丝 第"+i+"集");
            userObservableArrayList.add(userObj);
        }
    }
    private void addItem(ObservableArrayList<UserObj> userObservableArrayList){
        UserObj userObj = new UserObj();
        userObj.setLastName("魔道蛛丝 第"+userObservableArrayList.size()+"集");
        userObservableArrayList.add(userObj);

    }
}
