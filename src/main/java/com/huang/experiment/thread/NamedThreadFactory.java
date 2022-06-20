package com.huang.experiment.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description: 添加Thread命名
 * @author: huangmengwei
 * @create: 2022/5/30 15:43
 */
public class NamedThreadFactory implements ThreadFactory {
    private final AtomicInteger sequence = new AtomicInteger(1);
    private final String prefix;

    public NamedThreadFactory(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        int seq = sequence.getAndIncrement();
        thread.setName(prefix + (seq > 0 ? seq : ""));
        if (!thread.isDaemon())
            thread.setDaemon(true);
        return thread;
    }
}