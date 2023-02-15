package com.github.viewdriver.driver.executor;

import com.github.viewdriver.driver.Config;
import com.github.viewdriver.driver.exception.ParamIsNullException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * 支持分组隔离的并行任务执行器.
 *
 * @author yanghuan
 */
public class IsolatedExecutor implements Executor {

    private final Logger logger = LoggerFactory.getLogger(IsolatedExecutor.class);

    private final List<ConfusedExecutor> threadPoolExecutorList = new ArrayList<>();

    private final int groupCount;

    /**
     * Create a new instance.
     *
     * @param threadName 线程名称.
     * @param config 执行器相关配置.
     */
    public IsolatedExecutor(String threadName, Config.ExecutorConfig config) {
        if(config == null || config.getGroupCount() < 1) {
            throw new ParamIsNullException("分组数量 < 1，(expected: > 1)");
        }

        groupCount = config.getGroupCount();
        for(int i = 0; i < groupCount; i ++) {
            threadPoolExecutorList.add(new ConfusedExecutor(threadName + "-" + i, config));
        }

        if(logger.isDebugEnabled()) {
            logger.debug("分组隔离执行器创建完成 groupCount=" + groupCount);
        }
    }

    @Override
    public void execute(Runnable task) {
        int id = 0;
        if(task instanceof IsolatedTask) {
            // 分配的算法目前还不精致.
            id = ((IsolatedTask) task).getTransactionId() % groupCount;
        }

        threadPoolExecutorList.get(id).execute(task);

        if(logger.isDebugEnabled()) {
            logger.debug("分组隔离执行器-任务执行完成 id=" + id);
        }
    }
}
