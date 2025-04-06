package com.GinElmaC;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

public class MyLock {

    //原子变量，用来判断当前线程是否持有锁
    AtomicBoolean flag = new AtomicBoolean(false);

    //持有锁的线程对象
    Thread owner = null;

    //因为对象本身的引用操作例如：tail = thread 并不是线程安全的，所以我们需要有一个线程安全的对象，JUC提供的AtomicReference就可以解决这个问题
    AtomicReference<Node> head = new AtomicReference<>(new Node());
    AtomicReference<Node> tail = new AtomicReference<>(head.get());

    void lock(){
        /**
         * 如果修改成功则直接返回，如果加入了这段代码，那这个锁就是非公平锁，因为新加入的线程可以直接获取锁。
         * 如果将这段去掉，就是公平锁，所有来到的线程都需要将自己加入到链表尾，然后等待唤醒
         */
        if(flag.compareAndSet(false,true)){
            System.out.println(Thread.currentThread().getName()+"直接拿到了锁");
            owner = Thread.currentThread();
            return;
        }
        /**
         * 修改失败表示当前线程没有持有锁，放入尾节点
         */
        //获取当前节点
        Node current = new Node();
        current.thread = Thread.currentThread();
        while(true){
            //获取旧尾节点,每次都要获取到新的尾节点
            Node currentTail = tail.get();
            if (tail.compareAndSet(currentTail,current)) {
                System.out.println(Thread.currentThread().getName()+"成功见自己加入到链表尾部");
                //成功连接到尾节点
                current.pre = currentTail;
                currentTail.next = current;
                break;
            }
        }
        /**
         * 放入尾节点后进行沉睡，由于存在虚假唤醒的情况，所以这里需要进行死循环直到满足一定条件
         */
        while(true){
            /**
             * 先尝试获取一次锁，如果获取不到再等待别人唤醒
             */
            //线程被unpark唤醒，执行下面逻辑
            if(current.pre == head.get() && flag.compareAndSet(false,true)){
                owner = Thread.currentThread();
                /**
                 * 这里直接set是因为如果线程走到了这里，一定是持有了锁，所以这里不需要CAS操作
                 */
                head.set(current);
                //当前节点成为头结点
                current.pre.next = null;
                current.pre = null;
                System.out.println(Thread.currentThread().getName()+"被唤醒后拿到锁");
                return;
            }
            //线程被阻塞，等待唤醒
            LockSupport.park();
        }
    }

    void unlock(){
        if(!(Thread.currentThread() == owner)){
            throw new RuntimeException("当前线程并不是owner");
        }
        Node headNode = head.get();
        Node next = headNode.next;
        //这里直接set同理，因为上面有if判断进行过滤
        flag.set(false);
        if(next != null){
            System.out.println(Thread.currentThread().getName()+"线程唤醒了"+next.thread.getName());
            LockSupport.unpark(next.thread);
        }
    }

    class Node{
        Node pre;
        Node next;
        Thread thread;
    }

}
