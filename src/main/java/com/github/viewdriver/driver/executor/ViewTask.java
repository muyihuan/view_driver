package com.github.viewdriver.driver.executor;

/**
 * 视图处理任务.
 *
 * @author yanghuan
 */
public interface ViewTask extends Runnable {

    /**
     * 获取任务负责的视图
     *
     * @return 视图
     */
    Class<?> getView();
}
