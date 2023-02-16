package com.github.viewdriver.driver.exception;

/**
 * 这不是一个视图异常.
 *
 * @author yanghuan
 */
public class NotViewException extends Exception {

    public NotViewException() {
        super("这不是一个视图！");
    }
}