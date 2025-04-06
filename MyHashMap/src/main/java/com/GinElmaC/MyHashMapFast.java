package com.GinElmaC;

/**
 * 第二版hashmap，也是符合官方的一版
 * @param <K>
 * @param <V>
 */
public class MyHashMapFast<K,V> {

    private Node<K,V>[] table = new Node[16];
    private int size = 0;

    public V put(K key,V value){
        int keyIndex = indexOf(key);
        Node<K,V> node = table[keyIndex];
        if(node == null){
            table[keyIndex] = new Node<>(key,value);
            size++;
            resize();
            return null;
        }
        while(true){
            if(node.key.equals(key)){
                V oldValue = node.value;
                node.value = value;
                return oldValue;
            }
            if(node.next == null){
                node.next = new Node<>(key,value);
                size++;
                resize();
                return null;
            }
            node = node.next;
        }
    }
    public V get(K key){
        int keyIndex = indexOf(key);
        Node<K,V> node = table[keyIndex];
        while(node != null){
            if(node.key.equals(key)){
                return node.value;
            }
            node = node.next;
        }
        return null;
    }
    public V remove(K key){
        int keyIndex = indexOf(key);
        Node<K,V> node = table[keyIndex];
        if(node == null){
            return null;
        }
        if(node.key.equals(key)){
            table[keyIndex] = node.next;
            size--;
            return node.value;
        }
        Node<K,V> pre = node;
        Node<K,V> current = node.next;
        while(current!=null){
            if(current.key.equals(key)){
                pre.next = current.next;
                size--;
                return current.value;
            }
            pre = pre.next;
            current = current.next;
        }
        return null;
    }
    public int size(){
        return size;
    }

    /**
     * 使用hash获取
     * @param key
     * @return
     */
    private int indexOf(Object key){
//        return key.hashCode()%table.length;
        /**
         * 当一个数字时2的多少次幂例如16、32、64时，这个数减一所构成的二进制数就全部为1，这时候我们进行与运算，得到的结果一定是小于等于这个数字的，也就是一个完美的下标
         * 所以这里直接与运算就行
         */
        return key.hashCode() & (table.length-1);
    }

    /**
     * 扩容
     */
    private void resize(){
        if(this.size<table.length*0.75){
            return;
        }
        Node<K,V>[] newTable = new Node[this.table.length*2];
        for(Node<K,V> head:this.table){
            if(head == null){
                continue;
            }
            Node<K,V> current = head;
            while(current != null){
                int newIndex = current.key.hashCode() & (table.length)-1;
                if(newTable[newIndex] == null){
                    newTable[newIndex] = current;
                    Node<K,V> next = current.next;
                    current.next = null;
                    current = next;
                }else{
                    Node<K,V> next = current.next;
                    /**
                     * 假如有另一个线程同时进行了扩容并已经将current.next放入了这个位置，那么这里就会变成一个自己指向自己的死循环
                     * 这也就是HashMap的头插法为什么有线程安全问题
                     * 在JDK8之后变成了尾插法
                     */
                    current.next = newTable[newIndex];
                    newTable[newIndex] = current;
                    current = next;
                }
            }
        }
        this.table = newTable;
    }

    class Node<K,V>{
        K key;
        V value;
        Node<K,V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
