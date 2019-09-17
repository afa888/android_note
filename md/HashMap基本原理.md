##HashMap原理

###1.基本结构。

####1.1.HashMap 基于散列表实现
哈希表也称散列表，原理就是把Key通过一个固定的算法函数既所谓的哈希函数转换成一个整型数字，然后就将该数字对数组长度进行取余，取余结果就当作数组的下标，将value存储在以该数字为下标的数组空间里，
当使用哈希表进行查询的时候，就是再次使用哈希函数将key转换为对应的数组下标，并定位到该空间获取value，如此一来，就可以充分利用到数组的定位性能进行数据定位（但是在JDK 1.8中引入了红黑树，在链表的长度大于等于8并且hash桶的长度大于等于64的时候，会将链表进行树化。这里的树使用的数据结构是红黑树，红黑树是一个自平衡的二叉查找树，查找效率会从链表的o(n)降低为o(logn)，效率是非常大的提高）

![](https://pic1.zhimg.com/80/v2-7f2ab8fcb50e6dde4acc83e744001880_hd.jpg)



####1.2.为什么要用哈希表
数组的特点是：寻址容易，插入和删除困难；
而链表的特点是：寻址困难，插入和删除容易。
那么我们能不能综合两者的特性，做出一种寻址容易，插入删除也容易的数据结构？答案是肯定的，这就是我们要提起的哈希表，哈希表有多种不同的实现方法，我接下来解释的是最常用的一种方法——拉链法，我们可以理解为“链表的数组”

![](http://static.zybuluo.com/Rico123/cb85d6xr1t8976xtqmrzyghv/%E6%8B%89%E9%93%BE%E5%93%88%E5%B8%8C%E8%A1%A8.jpg)

###2.基本原理
####2.1.存储方式
HashMap是一个用于存储Key-Value键值对的集合，每一个键值对也叫做Entry。这些个键值对（Entry）分散存储在一个数组当中，这个数组就是HashMap的主干，HashMap数组每一个元素的初始值都是Null。
![](https://pic1.zhimg.com/80/v2-9ca7e8eb5ecf7a7c2e45f90177d785f0_hd.jpg)

####2.2.Put方法的原理
比如调用 hashMap.put("apple", 0) ，插入一个Key为“apple"的元素。这时候我们需要利用一个哈希函数来确定Entry的插入位置（index）：index = Hash（“apple”）
假定最后计算出的index是2，那么结果如下：
![](https://pic2.zhimg.com/80/v2-f50984206aae3ccf1c92c12c0c0f0329_hd.jpg)

因为HashMap的长度是有限的，当插入的Entry越来越多时，再完美的Hash函数也难免会出现index冲突的情况。比如：Entry6的  index也等于2，这时就利用链表解决。

HashMap数组的每一个元素不止是一个Entry对象，也是一个链表的头节点。每一个Entry对象通过Next指针指向它的下一个Entry节点。当新来的Entry映射到冲突的数组位置时，只需要插入到对应的链表即可：

![](https://pic1.zhimg.com/80/v2-89b55c75205cfb87f8bbd21f27d846dc_hd.jpg)

需要注意的是，新来的Entry节点插入链表时，使用的是“头插法”。至于为什么不插入链表尾部，后面会有解释。
####2.3 Get方法原理
首先会把输入的Key做一次Hash映射，得到对应的index：比如：index = Hash（“apple”）
由于刚才所说的Hash冲突，同一个位置有可能匹配到多个Entry，这时候就需要顺着对应链表的头节点，一个一个向下来查找。假设我们要查找的Key是“apple”：
![](https://pic3.zhimg.com/80/v2-6ff928d2047b013e869056ad3bf6ad66_hd.jpg)

第一步，我们查看的是头节点Entry6，Entry6的Key是banana，显然不是我们要找的结果。

第二步，我们查看的是Next节点Entry1，Entry1的Key是apple，正是我们要找的结果。

之所以把Entry6放在头节点，是因为HashMap的发明者认为，**后插入的Entry被查找的可能性更大**。

###3. HashMap寻址原理？
HashMap为了实现高效的Hash算法，HashMap的发明者采用了位运算的方式。
如何进行位运算呢？有如下的公式（Length是HashMap的长度）：
index = HashCode（Key） & （Length - 1）

下面我们以值为“book”的Key来演示整个过程：
1.计算book的hashcode，结果为十进制的3029737，二进制的101110001110101110 1001。

2.假定HashMap长度是默认的16，计算Length-1的结果为十进制的15，二进制的1111。

3.把以上两个结果做与运算，101110001110101110 1001 & 1111 = 1001，十进制是9，所以 index=9。

4.可以说，Hash算法最终得到的index结果，完全取决于Key的Hashcode值的最后几位。

###4.HashMap初始化长度是16或者是2的幂，为什么？
* 那我们假设HashMap的长度是10，重复上面的运算步骤：
  ![](https://pic3.zhimg.com/80/v2-7425ef68c55b259aaf0cf479a52a5e2a_hd.jpg)
*  单独看这个结果，上面的Index依然是9并没有问题。我们再来尝试一个新的HashCode 101110001110101110 1011 ：
  ![](https://pic4.zhimg.com/80/v2-fc786535ce20a9205b6fdc52bbb48943_hd.jpg)
*   让我们再换一个HashCode 101110001110101110 1111 试试 ：
  ![](https://pic3.zhimg.com/80/v2-285fe73e599aa0d78d77b776bb674b12_hd.jpg)
  是的，虽然HashCode的倒数第二第三位从0变成了1，但是运算的结果都是1001。也就是说，当HashMap长度为10的时候，有些index结果的出现几率会更大，而有些index结果永远不会出现（比如0111）！
  这样，显然不符合Hash算法均匀分布的原则。

####4.1结论:

​	**长度16或者其他2的幂，Length-1的值是所有二进制位全为1，这种情况下，index的结果等同于HashCode后几位的值。只要输入的HashCode本身分布均匀，Hash算法的结果就是均匀的。**

###5. HashMap简单实现

####3.1HashMap存储的对象代码

```
public class MapEntry {
    Object key;
    Object value;

    public MapEntry(Object key,Object value){
        super();
        this.key = key;
        this.value = value;
    }
}
```
####3.2的HashMap实现代码
```
import java.util.LinkedList;
public class EeasyHashMap {
    LinkedList[] arr = new LinkedList[999];
    int size = 0;
    
    public EeasyHashMap() {
    }
    
    public void put(Object key, Object value) {
        MapEntry node = new MapEntry(key, value);
        //这里hash使用的是取模运算，效率低，事实上采用了位运算的方式
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
```

源码分析：参考https://zhuanlan.zhihu.com/p/34280652





