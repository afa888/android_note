package com.example.android.AlgorithmDemo;

import android.util.Log;

//https://wiki.jikexueyuan.com/project/easy-learn-algorithm/fast-sort.html
//
public class Kp {
    int[] a;int n;//定义全局变量，这两个变量需要在子函数中使用
    public void setA(int[] a){
        this.a = a;
    }
    //快排
    public void quicksort(int left, int right) {
        int i,j,t,temp;//i左边比较下标，j右边开始下标，t用来交换的变量,用来交换i，，temp存放基数
        if(left>right){
            return;
        }
        temp = a[left];//这里
        i= left;
        j = right;

        while (i!=j){
            while (a[j]>=temp&&i<j){
                j--;
            }
            while (a[i]<=temp&&i<j){
                i++;
            }
            if(i<j){//上面找完停下来没有碰到。交换位置两个
                t = a[i];
                a[i] = a[j];
                a[j] = t;
            }
            a[left]=a[i];
            a[i] = temp;
            quicksort(left,i-1);
            quicksort(i+1,right);
        }
    }
    //冒泡
    public void mpSort(){
        int t;
        for(int i=0;i<a.length-1;i++){
            for(int j=0;j<a.length-1-i;j++){
                if(a[j]>a[j+1]){
                    t = a[j+1];
                    a[j+1] = a[j];
                    a[j]=t;
                }
            }
        }
    }

    public  void log(){
        for(int i=0;i<a.length;i++){
            Log.e("快速排序","a= "+a[i]);
        }
    }

}
