package com.github.viewdriver.driver.executor;

/**
 * 事务执行器.
 *
 * @author yanghuan
 */
public interface TransactionExecutor {

    /**
     * 执行.
     *
     * @param command
     */
    void execute(Runnable command);
}