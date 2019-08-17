package com.example.android.DraggerDemo;

import dagger.Component;

@Component(modules = DragModule.class)
public interface DragComponent {

    void inject(DragActivity activity);
}
