package com.github.case2.view;

import lombok.Data;

/**
 * @author yanghuan
 */
@Data
public class ViewC {

    /**
     * ViewC的Id
     */
    private String viewCId;

    /**
     * View类型
     */
    private ViewB viewB;

    /**
     * 自己依赖自己类型
     */
    private ViewC dependentViewC;
}
