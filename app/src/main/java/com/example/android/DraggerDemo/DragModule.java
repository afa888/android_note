package com.example.android.DraggerDemo;

import dagger.Module;

@Module
public class DragModule {
    DragActivity activity;
    public DragModule(DragActivity activity){

        this.activity = activity;
    }
}
