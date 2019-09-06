package com.example.android.mvvmDemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.android.R;
import com.example.android.databinding.ActivityMvvmBinding;
//参考：https://juejin.im/post/5c76aa1c6fb9a04a0a5fdc61
public class MvvmActivity extends AppCompatActivity {
    User user;
    UserObj userObj;
    int  num = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvvm);

        final ActivityMvvmBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_mvvm);
        user = new User("apple", "age");
        userObj= new UserObj();
        userObj.setFirstName("pp");
        userObj.setLastName("kk");
        binding.setUser(user);
        binding.setUserObj(userObj);

        binding.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num++;
                user = new User("apple222", "age22222");
                //会跟随字段变化的
                userObj.setLastName("99"+num);
                userObj.setFirstName("ll");
                binding.firstName.setText(num+"我是findview");
               // binding.setUser(user);
                Toast.makeText(MvvmActivity.this, "我被点击了", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
