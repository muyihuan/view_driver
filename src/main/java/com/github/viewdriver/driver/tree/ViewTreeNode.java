package com.github.viewdriver.driver.tree;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 视图树节点.
 *
 * @author yanghuan
 */
public class ViewTreeNode {

    // 需要
    private Need need;

    // 要加载
    private Load load;

    // 已加载的model
    private Map<Object, Object> okModels;

    // 构建好的view
    private Object okView;

    // 父节点 不可为空
    private ViewTreeNode parentNode;

    // 子节点 不可为空
    private List<ViewTreeNode> childNodeList;

    @Data
    private static class Need {

        private Class<?> fromModel;

        // 需要什么是model吗
        private boolean isNeedModel;

        // 是否是1对n查
        private boolean isN;

        // 目标model
        private Class<?> toModel;

        // 外部非model
        private String viewAttribute;

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean equals(Object o) {
            if(!(o instanceof Need)) {
                return false;
            }

            Need copy = (Need) o;

            if(isNeedModel && copy.isNeedModel) {
                if(isN ^ copy.isN) {
                    return false;
                }
                else {
                    return (copy.fromModel == fromModel) && (copy.toModel == toModel);
                }
            }
            else if(isNeedModel && !copy.isNeedModel) {
                return false;
            }
            else if(!isNeedModel && copy.isNeedModel) {
                return false;
            }
            else {
                return (viewAttribute.equals(copy.viewAttribute)) && (copy.fromModel == fromModel);
            }
        }
    }

    @Data
    private static class Load {

        // 需要什么是model吗
        private boolean isModel;

        // 是否是1对n查
        private boolean isN;

        // 目标model
        private Class<?> toModel;

        // 绑定的view的属性
        private String viewAttribute;

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean equals(Object o) {
            if(!(o instanceof Need)) {
                return false;
            }

            Load copy = (Load) o;

            if(isModel && copy.isModel) {
                if(isN ^ copy.isN) {
                    return false;
                }
                else {
                    return copy.toModel == toModel;
                }
            }
            else if(isModel && !copy.isModel) {
                return false;
            }
            else if(!isModel && copy.isModel) {
                return false;
            }
            else {
                return copy.viewAttribute == viewAttribute;
            }
        }
    }
}
