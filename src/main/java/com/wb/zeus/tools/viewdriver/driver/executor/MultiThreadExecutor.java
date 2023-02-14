package com.wb.zeus.tools.viewdriver.driver.executor;

import com.wb.zeus.tools.viewdriver.driver.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 并行任务执行器.
 *
 * @author yanghuan
 */
public class MultiThreadExecutor implements Executor {

    private List<ThreadPoolExecutor> threadPoolExecutorList = new ArrayList<>();

    private final Config config;

    /**
     * Create a new instance
     *
     * @param config 执行器相关配置
     */
    public MultiThreadExecutor(String threadName, Config config) {
        this.config = config;

        threadPoolExecutorList.add(null);
        threadPoolExecutorList.add(null);
        threadPoolExecutorList.add(null);
        threadPoolExecutorList.add(null);
        threadPoolExecutorList.add(null);
        threadPoolExecutorList.add(null);
    }

    @Override
    public void execute(Runnable task) {

    }
}
