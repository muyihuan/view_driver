package com.github.viewdriver.driver.executor;

import com.github.viewdriver.driver.Config;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 任务执行器.
 *
 * @author yanghuan
 */
public class ConfusedExecutor implements Executor {

    private final ThreadPoolExecutor executor;

    /**
     * Create a new instance.
     *
     * @param threadName 线程名称.
     * @param config 执行器相关配置.
     */
    public ConfusedExecutor(String threadName, Config.ExecutorConfig config) {
        int coreThreadCount = 2;
        if(config != null && config.getCoreThreadCount() > 0) {
            coreThreadCount = config.getCoreThreadCount();
        }

        int maxThreadCount = 4;
        if(config != null && config.getMaxThreadCount() >= coreThreadCount) {
            maxThreadCount = config.getCoreThreadCount();
        }

        int keepAliveTimeSeconds = 120;
        if(config != null && config.getKeepAliveTimeSeconds() > 0) {
            keepAliveTimeSeconds = config.getKeepAliveTimeSeconds();
        }

        int queueSize = 20;
        if(config != null && config.getQueueSize() > 0) {
            queueSize = config.getQueueSize();
        }

        executor = new ThreadPoolExecutor(
                coreThreadCount,
                maxThreadCount,
                keepAliveTimeSeconds,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(queueSize),
                r -> new Thread(r, (threadName == null || "".equals(threadName)) ? "vd-confused-executor" : threadName),
                new ThreadPoolExecutor.DiscardPolicy());
    }

    @Override
    public void execute(Runnable task) {
        executor.execute(task);
    }
}
