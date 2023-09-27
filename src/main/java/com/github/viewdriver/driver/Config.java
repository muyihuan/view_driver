package com.github.viewdriver.driver;

import lombok.Getter;
import lombok.Setter;

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
    DriverConfig driverConfig;

    /**
     * Create a new instance.
     */
    public Config() {
        executorConfig = new ExecutorConfig();
        driverConfig = new DriverConfig();
    }

    public void setGroupCount(int groupCount) {
        executorConfig.groupCount = groupCount;
    }

    public void setCoreThreadCount(int coreThreadCount) {
        executorConfig.coreThreadCount = coreThreadCount;
    }

    public void setMaxThreadCount(int maxThreadCount) {
        executorConfig.maxThreadCount = maxThreadCount;
    }

    public void setKeepAliveTimeSeconds(int keepAliveTimeSeconds) {
        executorConfig.keepAliveTimeSeconds = keepAliveTimeSeconds;
    }

    public void setQueueSize(int queueSize) {
        executorConfig.queueSize = queueSize;
    }

    public void setTimeout(int timeout) {
        driverConfig.timeout = timeout;
    }


    /**
     * 执行器相关配置
     *
     * @author yanghuan
     */
    @Getter
    @Setter
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
         * 任务队列长度，如果任务时效性很短的就没必要设置的很长，设置的越长堆积的时候影响越大.
         */
        private int queueSize;

        // 拒绝策略默认丢弃.
    }

    /**
     * 驱动器相关配置
     *
     * @author yanghuan
     */
    @Getter
    @Setter
    public static class DriverConfig {

        /**
         * view数据加载和渲染的超时时间(ms).
         */
        private long timeout = 1000;
    }
}
