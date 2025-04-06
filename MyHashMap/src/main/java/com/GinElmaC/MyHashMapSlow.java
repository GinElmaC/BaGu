package com.GinElmaC;

import java.util.ArrayList;
import java.util.List;

/**
 * 第一版hashmap，能实现功能，但是太慢了
 * @param <K>
 * @param <V>
 */
public class MyHashMapSlow<K,V> {

    List<Node<K,V>> table = new ArrayList<>();

    public V put(K key,V value){
        for(Node<K,V> node:table){
            if(node.key.equals(key)){
                V oldValue = node.value;
                node.value = value;
                return oldValue;
            }
        }
        Node<K,V> node = new Node<>();
        node.key = key;
        node.value = value;
        table.add(node);
        return null;
    }
    public V get(K key){
        for(Node<K,V> node:table){
            if(node.key.equals(key)){
                return node.value;
            }
        }
        return null;
    }
    public V remove(K key){
        for(int i = 0;i<table.size();i++){
            if(table.get(i).key.equals(key)){
                Node<K,V> removeNode = this.table.remove(i);
                return removeNode.value;
            }
        }
        return null;
    }
    public int size(){
        return table.size();
    }


    /**
     * 将key和value封装为一个整体
     * Node就是装着key和value的容器，map就是装着一堆Node的容器
     */
    class Node<K,V>{
        K key;
        V value;
    }
}
