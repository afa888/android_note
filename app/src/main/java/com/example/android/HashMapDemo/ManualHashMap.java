package com.example.android.HashMapDemo;

import java.util.LinkedList;

public class ManualHashMap {
    LinkedList[] arr = new LinkedList[999];
    int size = 0;

    public ManualHashMap() {

    }

    public void put(Object key, Object value) {
        MapEntry node = new MapEntry(key, value);
        int hash = node.key.hashCode() % arr.length;
        hash = hash < 0 ? -hash : hash;
        if (arr[hash] == null) {
            LinkedList<MapEntry> list = new LinkedList<>();
            arr[hash] = list;
            list.add(node);
            size++;

        } else {
            LinkedList<MapEntry> list = arr[hash];
            boolean flag = false;
            for (int i = 0; i < list.size(); i++) {
                MapEntry temp = list.get(i);
                if (temp.key.equals(key)) {
                    temp.value = value;
                    flag = true;
                }
            }
            if (!flag) {
                list.add(node);
                size++;
            }
        }
    }


    public Object get(Object key) {
        int hash = key.hashCode() % arr.length;
        hash = hash < 0 ? -hash : hash;
        if (arr[hash] != null) {
            LinkedList<MapEntry> list = arr[hash];
            for (int i = 0; i < list.size(); i++) {
                MapEntry temp = (MapEntry) list.get(i);
                if (temp.key.equals(key)) {
                    return temp.value;
                }
            }
        }
        return null;
    }

}
