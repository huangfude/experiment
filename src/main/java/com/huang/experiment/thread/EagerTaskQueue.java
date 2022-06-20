package com.huang.experiment.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @description: TaskQueue
 * @author: huangmengwei
 * @create: 2022/5/30 11:02
 */
public class EagerTaskQueue<R extends Runnable> extends LinkedBlockingQueue<Runnable> {

    private static final long serialVersionUID = -2635853580887179627L;

    private EagerThreadPoolExecutor executor;

    public EagerTaskQueue(int capacity) {
        super(capacity);
    }

    public void setExecutor(EagerThreadPoolExecutor exec) {
        executor = exec;
    }

    /**
     * 覆盖JDK默认的offer方法，融入了 EagerThreadPoolExecutor 的属性读取
     * @param runnable
     * @return
     */
    @Override
    public boolean offer(Runnable runnable) {
        if (executor == null) {
            throw new RejectedExecutionException("The task queue does not have executor!");
        }

        int currentPoolThreadSize = executor.getPoolSize();
        // have free worker. put task into queue to let the worker deal with task.
        // 如果当前运行中的任务数比线程池中当前的线程总数还小，就不管了，每个任务一个线程，管够，直接走JDK的原来逻辑
        if (executor.getActiveCount() < currentPoolThreadSize) {
            return super.offer(runnable);
        }

        // return false to let executor create new worker.
        // 当前运行的任务数是大于线程池当前的线程数，说明会有任务没有线程可用，需要处理这种情况
        if (currentPoolThreadSize < executor.getMaximumPoolSize()) {
            /*
            如果发现当前线程池的数量还没有到最大的maxPoolSize,返回false； 告知 ThreadPoolExecutor ，插入到任务队列失败。
            这一步，需要先配合EagerThreadPoolExecutor.execute 一块看，EagerThreadPoolExecutor和TaskQueue是深度配合的。
            当EagerThreadPoolExecutor.execute执行的时候，发现corePoolSize已经满了，会先把任务offer添加到任务队列里；
            如果任务队列满了，拒绝添加，那么线程池，会马上开始尝试创建新的线程。
            这里直接返回false，就是强制线程池立刻马上创建线程。
             */
            return false;
        }

        // currentPoolThreadSize >= max
        // 当前的线程数已经到到了maxPoolSize，调用原始的offer逻辑，真的向任务队列插入。
        return super.offer(runnable);
    }

    /**
     * retry offer task
     *
     * @param o task
     * @return offer success or not
     * @throws RejectedExecutionException if executor is terminated.
     */
    public boolean retryOffer(Runnable o, long timeout, TimeUnit unit) throws InterruptedException {
        if (executor.isShutdown()) {
            throw new RejectedExecutionException("Executor is shutdown!");
        }
        return super.offer(o, timeout, unit);
    }
}
