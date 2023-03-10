package com.github.views.view;

import com.github.views.domain.modela.model.ObjectA;
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
    private ObjectA innerAttributeAa;

    /**
     * 外部-对象类型属性
     */
    private ObjectA outerAttributeAf;

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
     * View类型 通过 View id 查的
     */
    private ViewG viewG;

    /**
     * 自己依赖自己类型
     */
    private ViewA dependentViewA;

    /**
     * 自定义数据
     */
    public String getCustom() {
        return "自定义信息";
    }

    /**
     * 自定义数据2
     */
    public String getCustom2() {
        return "ViewB的数据: " + (getViewB() == null ? "null" : getViewB().getInnerAttributeBa());
    }
}
