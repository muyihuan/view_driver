package com.github.viewdriver;

import java.util.List;

/**
 * View驱动器: 通过View驱动数据加载和View渲染.
 *
 * @author yanghuan
 */
public interface ViewDriver {

    /**
     * 渲染View.
     *
     * @param models 输入model或者model的id集合.
     * @param viewClass 要构建的View.
     * @param context 上下文信息.
     * @return 目标View的集合.
     * @throws Exception 执行异常.
     */
    <V> List<V> mapView(List models, Class<V> viewClass, Context context) throws Exception;
}
