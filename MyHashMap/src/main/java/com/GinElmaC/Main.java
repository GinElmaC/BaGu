package com.GinElmaC;

public class Main {
    public static void main(String[] args) {
//        MyHashMapSlow<String,String> hashMap = new MyHashMapSlow<>();
        MyHashMapFast<String,String> hashMap = new MyHashMapFast<>();
        int count = 100000;
        for(int i = 0;i<count;i++){
            hashMap.put(String.valueOf(i),String.valueOf(i));
        }

        for(int i = 0;i<count;i++){
            System.out.println(hashMap.get(String.valueOf(i)));
        }

        hashMap.remove("8");
    }
}
