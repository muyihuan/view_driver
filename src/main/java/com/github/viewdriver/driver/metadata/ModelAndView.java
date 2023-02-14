package com.github.viewdriver.driver.metadata;

/**
 * view和model相关元数据.
 * tip: 保留扩展 => view和model元数据的维护方式有很多种：注册中心、基于服务编排等.
 *
 * @author yanghuan
 */
public interface ModelAndView {

    /**
     * 对元数据进行校验，校验失败会抛异常，及时终止进行的流程.
     */
    void check();
}
