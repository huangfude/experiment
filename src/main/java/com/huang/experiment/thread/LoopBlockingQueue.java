package com.huang.experiment.thread;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 循环阻塞队列
 */
public class LoopBlockingQueue <E> extends AbstractQueue<E>
        implements BlockingQueue<E>, java.io.Serializable{

    private static final long serialVersionUID = -5701196880678867011L;

    private E[] data;
    private int front, tail;
    //队列中元素个数
    private int size;

    private final AtomicInteger count = new AtomicInteger();

    private final ReentrantLock takeLock = new ReentrantLock();

    private final Condition notEmpty = takeLock.newCondition();

    private final ReentrantLock putLock = new ReentrantLock();

    private final Condition notFull = putLock.newCondition();

    /**
     * 构造函数，传入队列的容量capacity构造函数
     * @param capacity
     */
    public LoopBlockingQueue(int capacity) {
        data = (E[]) new Object[capacity + 1];//浪费与一个空间
        front = 0;
        tail = 0;
        size = 0;
    }

    /**
     * 无参构造函数，默认队列的容量capacity=10
     * @return
     */
    public LoopBlockingQueue() {
        this(10);
    }

    /**
     * 真正容量
     * @return
     */
    public int getCapacity() {
        return data.length - 1;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    /**
     * 队列中元素个数
     * @return
     */
    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(E e) throws InterruptedException {

    }

    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public E take() throws InterruptedException {
        return null;
    }

    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    @Override
    public int remainingCapacity() {
        return 0;
    }

    @Override
    public int drainTo(Collection<? super E> c) {
        return 0;
    }

    @Override
    public int drainTo(Collection<? super E> c, int maxElements) {
        return 0;
    }

    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E poll() {
        return null;
    }

    @Override
    public E peek() {
        return null;
    }
}
