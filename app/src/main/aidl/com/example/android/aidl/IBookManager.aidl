// IBookManager.aidl
package com.example.android.aidl;

// Declare any non-default types here with import statements
import com.example.android.aidl.Book;
//https://juejin.im/post/5aab3f516fb9a028e33b31a5
interface IBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    void addBook(in Book book);

    List<Book> getBookList();
}
