package com.example.android.threadpool;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MuyIntentService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    private String mName = "MuyIntentService";

    public MuyIntentService() {
        super("MuyIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(mName, "onCreate");

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.e(mName, Thread.currentThread().getId() + Thread.currentThread().getName());
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Log.e(mName, dateFormat.format(new Date(System.currentTimeMillis())));

    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(mName, "onDestroy");

    }
}
