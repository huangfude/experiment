package com.huang.experiment.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.TimeUnit;

/**
 * @description: 获得EagerThreadPoolExecutor
 * @author: huangmengwei
 * @create: 2022/5/30 14:09
 */
public class EagerThreadPool {

    /**
     * 获得EagerThreadPoolExecutor
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param capacity
     * @param prefix
     * @param handler
     * @return
     */
    public Executor getExecutor(int corePoolSize,
                                int maximumPoolSize,
                                long keepAliveTime,
                                int capacity,
                                String prefix,
                                RejectedExecutionHandler handler) {

        // init queue and executor
        EagerTaskQueue<Runnable> eagerTaskQueue = new EagerTaskQueue<Runnable>(capacity <= 0 ? 1 : capacity);
        EagerThreadPoolExecutor executor = new EagerThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                eagerTaskQueue,
                new NamedThreadFactory(prefix),
                handler);
        eagerTaskQueue.setExecutor(executor);

        return executor;
    }

}
