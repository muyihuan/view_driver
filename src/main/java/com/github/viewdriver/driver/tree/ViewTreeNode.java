package com.github.viewdriver.driver.tree;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 视图树节点.
 *
 * @author yanghuan
 */
public class ViewTreeNode {

    /**
     * 节点类型 0：视图、1：非视图
     */
    int type;

    /**
     * 是否存在自己依赖自己
     */
    boolean isDependSelf;

    /**
     * 视图 或 非视图.
     */
    Class nodeClass;

    /**
     * 对应的getter方法
     */
    Method getter;

    /**
     * 父节点连向自己的连线.
     */
    ViewTreeLine fromParentLine;

    /**
     * 连向的子节点的连线.
     */
    List<ViewTreeLine> toChildLines;
}
