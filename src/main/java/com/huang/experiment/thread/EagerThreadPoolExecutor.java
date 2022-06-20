package com.huang.experiment.thread;


import java.util.concurrent.*;

/**
 * @description: EagerThreadPoolExecutor
 * @author: huangmengwei
 * @create: 2022/5/30 11:03
 */
public class EagerThreadPoolExecutor extends ThreadPoolExecutor {


    /**
     * 构造函数
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     * @param threadFactory
     * @param handler
     */
    public EagerThreadPoolExecutor(int corePoolSize,
                                   int maximumPoolSize,
                                   long keepAliveTime,
                                   TimeUnit unit, EagerTaskQueue<Runnable> workQueue,
                                   ThreadFactory threadFactory,
                                   RejectedExecutionHandler handler) {

        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    public void execute(Runnable command) {
        if (command == null) {
            throw new NullPointerException();
        }

        try {
            //调用线程池的原始execute，配合自定义的TaskQueue，实现如果corePoolSize满了，offer到taskQueue返回false，强制创建线程
            super.execute(command);
        } catch (RejectedExecutionException rx) {
            // retry to offer the task into queue.
            //这里要再次尝试retryOffer，再次尝试把任务插入到任务队列里，是考虑到 TaskQueue带来的副作用。
            final EagerTaskQueue queue = (EagerTaskQueue) super.getQueue();
            try {
                if (!queue.isEmpty() && !queue.retryOffer(command, 0, TimeUnit.MILLISECONDS)) {
                    throw new RejectedExecutionException("Queue capacity is full.", rx);
                }
            } catch (InterruptedException x) {
                throw new RejectedExecutionException(x);
            }
        }
    }

}

