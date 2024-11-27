package com.io.project.ThreadManager;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyThreadPoolExecutor extends ThreadPoolExecutor {

    public MyThreadPoolExecutor(int corePoolSize, String threadNamePrefix) {
        super(corePoolSize, corePoolSize, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
        // Tùy chỉnh tên thread
        setThreadFactory(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName(threadNamePrefix + "-" + thread.getId());
            return thread;
        });
    }

    @Override
    public void shutdown() {
        super.shutdown();
    }
}