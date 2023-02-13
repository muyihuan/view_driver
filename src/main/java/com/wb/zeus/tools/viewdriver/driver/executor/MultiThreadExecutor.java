package com.wb.zeus.tools.viewdriver.driver.executor;

import com.wb.zeus.tools.viewdriver.driver.Config;

import java.util.concurrent.Executor;

/**
 * 多线程任务执行器.
 *
 * @author yanghuan
 */
public class MultiThreadExecutor implements Executor {

    private final Config config;

    /**
     * Create a new instance
     *
     * @param config 执行器相关配置
     */
    public MultiThreadExecutor(String threadName, Config config) {
        this.config = config;
    }

    @Override
    public void execute(Runnable task) {

    }
}
