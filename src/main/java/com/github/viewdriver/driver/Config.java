package com.github.viewdriver.driver;

import lombok.Data;

/**
 * 视图驱动器配置.
 * 1.执行器相关配置.
 * 2.驱动器相关配置.
 *
 * @author yanghuan
 */
public class Config {

    /**
     * 执行器相关配置.
     */
    ExecutorConfig executorConfig;

    /**
     * 驱动器相关配置.
     */
    Object driverConfig;

    /**
     * 执行器相关配置
     *
     * @author yanghuan
     */
    @Data
    public static class ExecutorConfig {

        /**
         * 分组数量，不同分组的执行任务是隔离互不影响的.
         * &lt 1 => 走默认.
         * &eq 1 => 默认.
         * &gt 1 => 按照处理的视图进行分组，将不同视图的驱动过程隔离.
         */
        private int groupCount;

        /**
         * 核心线程数.
         */
        private int coreThreadCount;

        /**
         * 最大线程数.
         */
        private int maxThreadCount;

        /**
         * 非核心线程最大存活时间(秒).
         */
        private int keepAliveTimeSeconds;

        /**
         * 任务队列长度
         */
        private int queueSize;
    }
}
