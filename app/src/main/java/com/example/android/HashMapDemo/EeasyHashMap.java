package com.example.android.HashMapDemo;

import java.util.LinkedList;
/*哈希表（HashSet）：哈希表也称散列表。原理就是通过哈希函数（下面使用hash()代替）计算元素的哈希值。
计算出来的哈希值是32位的。当然，我们可以把这32位的哈希值看成一个无符号的32位整形（下面使用uint32）。
最后，我们再创建一个key类型的数组（假设大小为8）。这时候，假设我们要添加一个元素，
我们就使用 hash(key)%8 计算出元素应该存储在数组中的位置（hash(key)%8的范围是0-7，不会使数组越界）。
这时，key就与数组之间建立了一个映射关系。当然，不同的key计算（hash(key)%8）出来的结果（数组的下标）有可能是一样的。
例如先前这个位置已经有元素存储了，现在又有个元素映射到这个位置，我们怎么办？这种情况我们称为哈希冲突，
解决的方式有2种。
 第1种为链式存储法，也就是数组的每个元素都是一个链表，来记录映射到该下标的所有元素。
  第2种为二度哈希，也就是使用哈希后加上一定的计算，在当前的位置下再计算另一个位置出来。
*/

public class EeasyHashMap {
    LinkedList[] arr = new LinkedList[999];
    int size = 0;
    public EeasyHashMap() {
    }
    public void put(Object key, Object value) {
        MapEntry node = new MapEntry(key, value);
        //哈希表hashtable(key，value) 就是把Key通过一个固定的算法函数既所谓的哈希函数转换成一个整型数字，
        // 然后就将该数字对数组长度进行取余，取余结果就当作数组的下标
        int hash = node.key.hashCode() % arr.length;//这里使用的事取模运算，效率低，事实上采用了位运算的方式
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
