package com.wb.zeus.tools.viewdriver.driver.exception;

/**
 * 视图驱动元数据不存在异常.
 *
 * @author yanghuan
 */
public class MetaDataIsNullException extends Exception {

    public MetaDataIsNullException() {
        super("视图驱动元数据不存在！");
    }
}