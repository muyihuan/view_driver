package com.github.viewdriver.driver;

import lombok.Data;

/**
 * 视图驱动器配置.
 * 1.执行器相关配置.
 * 2.驱动器相关配置.
 *
 * @author yanghuan
 */
@Data
public class Config {

    /**
     * 执行器相关配置.
     */
    private Object executorConfig;

    /**
     * 驱动器相关配置.
     */
    private Object driverConfig;
}
