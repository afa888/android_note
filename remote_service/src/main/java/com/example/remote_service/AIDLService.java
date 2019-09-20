package com.example.remote_service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class AIDLService extends Service {
    private static final String TAG = "AIDLService";

    public AIDLService() {
    }

    public interface OnLoginListener {
        void login(String username, String password);
    }

    private OnLoginListener mOnLoginListener;

    public void setOnLoginListener(OnLoginListener listener) {
        mOnLoginListener = listener;
        Log.e(TAG, "mOnLoginListener = " + mOnLoginListener);
    }

    @Override
    public IBinder onBind(Intent intent) {
          return new MyBinder();
    }


    class MyBinder extends com.example.remote_service.IMyAidlInterface.Stub {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void login(String userName, String password) throws RemoteException {
            Log.d(TAG, "mOnLoginListener = " + mOnLoginListener);
            if (mOnLoginListener != null) {
                mOnLoginListener.login(userName, password);
            }
        }

        public AIDLService getService() {
            return AIDLService.this;
        }

    }
}
