package com.github.viewdriver.driver.tree;

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
     * 视图的类型 或 非视图的类型.
     */
    Class nodeClass;

    /**
     * 父节点连向自己的连线.
     */
    ViewTreeLine fromParentLine;

    /**
     * 连接的子节点的连线.
     */
    List<ViewTreeLine> toChildLines;
}
