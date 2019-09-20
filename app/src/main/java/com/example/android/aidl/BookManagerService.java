package com.example.android.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BookManagerService  extends Service {
    private static final String TAG = "BookManagerService";
    private Handler mHandler = new Handler();
    public BookManagerService() {
    }
    private List<Book> mBookList = new ArrayList<>();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    private IBookManager.Stub mBinder =new IBookManager.Stub(){

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
            final int bookCount = mBookList.size();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(BookManagerService.this,
                            String.format("添加了一本新书, 现在有%d本", bookCount), Toast.LENGTH_SHORT).show();
                }
            });
            Log.d(TAG, String.format("添加了一本新书, 现在有%d本", bookCount));
            Log.d(TAG, "currentThread = " + Thread.currentThread().getName());
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }
    };
}
