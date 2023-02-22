package com.github.viewdriver.driver.tree;

import lombok.Data;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 视图树节点.
 *
 * @author yanghuan
 */
@Data
public class ViewTreeNode {

    /**
     * 节点类型 0：视图、1：非视图&非基础属性、2：基础属性
     */
    int type;

    /**
     * 是否存在自己依赖自己
     */
    boolean isDependSelf;

    /**
     * 自己依赖自己的getter方法
     */
    List<Method> dependSelfGetters;

    /**
     * 视图 或 非视图.
     */
    Class nodeClass;

    /**
     * 对应的getter方法
     */
    Method parentGetter;

    /**
     * 父节点连向自己的连线.
     */
    ViewTreeLine fromParentLine;

    /**
     * 连向的子节点的连线.
     */
    List<ViewTreeLine> toChildLines;

    /**
     * 获取所有子节点.
     */
    public List<ViewTreeNode> getChildNodes() {
        if(toChildLines == null || toChildLines.size() <= 0) {
            return Collections.emptyList();
        }

        return toChildLines.stream().map(line -> line.right).collect(Collectors.toList());
    }

    /**
     * 获取所有子节点.
     */
    public ViewTreeNode getChildNodeByGetter(String getterName) {
        if(toChildLines == null || toChildLines.size() <= 0 || getterName == null) {
            return null;
        }

        for(ViewTreeLine line : toChildLines) {
            if(line.right.parentGetter.getName().equals(getterName)) {
                return line.right;
            }
        }

        return null;
    }
}
