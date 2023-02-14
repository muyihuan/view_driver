package com.github.viewdriver.driver.executor;

/**
 * 隔离型任务.
 *
 * @author yanghuan
 */
public interface IsolatedTask extends Runnable {

    /**
     * 获取隔离ID
     *
     * @return 视图
     */
    int getTransactionId();
}
