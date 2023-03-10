package com.github.views.view;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yanghuan
 */
@Getter
@Setter
public class ViewB {

    /**
     * ViewB的Id
     */
    private String viewBId;

    /**
     * 基础属性 ba
     */
    private String innerAttributeBa;

    /**
     * View类型 不是通过 View id 查的
     */
    private ViewF viewF;
}
