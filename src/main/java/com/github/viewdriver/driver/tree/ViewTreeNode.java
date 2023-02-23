package com.github.viewdriver.driver.tree;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 视图树节点.
 *
 * @author yanghuan
 */
@Setter
@Getter
public class ViewTreeNode {

    /**
     * 节点类型 0：视图、1：非视图&非基础属性、2：基础属性
     */
    int type;

    /**
     * 视图 或 非视图.
     */
    Class nodeClass;

    /**
     * 对应的getter方法
     */
    Map<ViewTreeNode, List<Method>> parentGetter = new HashMap<>();

    /**
     * 父节点连向自己的连线.
     */
    Map<ViewTreeNode, Map<Method, ViewTreeLine>> fromParentLine = new HashMap<>();

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
     *
     * @param getterName getter方法.
     */
    public ViewTreeNode getChildNodeByGetter(String getterName) {
        if(toChildLines == null || toChildLines.size() <= 0 || getterName == null) {
            return null;
        }

        for(ViewTreeLine line : toChildLines) {
            List<Method> methods = line.right.parentGetter.get(this);
            if(methods != null && methods.size() > 0) {
                for(Method method : methods) {
                    if(method.getName().equals(getterName)) {
                        return line.right;
                    }
                }
            }
        }

        return null;
    }

    /**
     * 保存来自父节点对应的getter.
     *
     * @param parent_node 父节点.
     * @param method getter方法.
     */
    public void saveParentGetter(ViewTreeNode parent_node, Method method) {
        List<Method> methods = parentGetter.get(parent_node);
        if(methods == null) {
            parentGetter.put(parent_node, new ArrayList<>());

            saveParentGetter(parent_node, method);
        }
        else {
            methods.add(method);
        }
    }

    /**
     * 保存来自父节点对应的getter.
     *
     * @param parent_node 父节点.
     * @param getter getter方法.
     */
    public void saveFromParentLine(ViewTreeNode parent_node, Method getter, ViewTreeLine line) {
        Map<Method, ViewTreeLine> method_line = fromParentLine.get(parent_node);
        if(method_line == null) {
            fromParentLine.put(parent_node, new HashMap<>());

            saveFromParentLine(parent_node, getter, line);
        }
        else {
            method_line.put(getter, line);
        }
    }
}
