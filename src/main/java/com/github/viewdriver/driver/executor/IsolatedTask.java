package com.github.viewdriver.driver.executor;

/**
 * 隔离型任务.
 *
 * @author yanghuan
 */
public interface IsolatedTask extends Runnable {

    /**
     * 获取隔离ID，同一个隔离ID的任务会公用同一组线程，不同隔离ID的任务会尽量放到不同组执行，防止互相影响.
     *
     * @return 隔离ID.
     */
    int getTransactionId();
}
