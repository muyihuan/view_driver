package com.github.case2.view;

import lombok.Data;

/**
 * @author yanghuan
 */
@Data
public class ViewB {

    /**
     * ViewB的Id
     */
    private String viewBId;

    /**
     * 基础属性 ba
     */
    private Object innerAttributeBa;

    /**
     * View类型 不是通过 View id 查的
     */
    private ViewF viewF;
}
