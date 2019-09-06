package com.example.android.mvvmDemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;
import com.example.android.databinding.RecyclerviewitemBinding;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserAdapterHolder> {
    private List<UserObj> userObjList;
    
    public UserAdapter(List<UserObj> userObjList){
        this.userObjList = userObjList;
        
    }

    @NonNull
    @Override
    public UserAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       RecyclerviewitemBinding binding =  DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerviewitem, parent, false);
        return new UserAdapterHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapterHolder holder, int position) {
            holder.getBinding().setUserObj(userObjList.get(position));
    }

    @Override
    public int getItemCount() {
        if(userObjList==null){
            return 0;
        }
        return userObjList.size();
    }

    class UserAdapterHolder extends RecyclerView.ViewHolder{
        RecyclerviewitemBinding binding;

        public UserAdapterHolder( RecyclerviewitemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public RecyclerviewitemBinding getBinding(){
            return binding;
        }
    }
}
