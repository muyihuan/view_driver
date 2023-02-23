package com.github.case2.view;

import com.github.case2.domain.modela.model.ObjectInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author yanghuan
 */
@Getter
@Setter
public class ViewA {

    /**
     * ViewA的Id
     */
    private String viewAId;

    /**
     * 内部-基础属性
     */
    private ObjectInfo innerAttributeAa;

    /**
     * 外部-对象类型属性
     */
    private ObjectInfo outerAttributeAf;

    /**
     * 外部-对象类型集合
     */
    private List<Object> outerAttributeAgs;

    /**
     * View类型 通过 View id 查的
     */
    private ViewB viewB;

    /**
     * View类型 不是通过 View id 查的
     */
    private ViewE viewE;

    /**
     * View集合 通过 View id 查的
     */
    private List<ViewD> viewDList;

    /**
     * View集合 不是通过 View id 查的
     */
    private List<ViewC> viewCList;

    /**
     * View集合 不是通过 View id 查的
     */
    private List<ViewC> viewC2List;

    /**
     * 自己依赖自己类型
     */
    private ViewA dependentViewA;
}
