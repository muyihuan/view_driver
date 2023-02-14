package com.github.viewdriver.driver.tree;

/**
 * 视图解析器.
 * .解析视图生成视图树
 * .根据视图树反向生成视图
 *
 * @author yanghuan
 */
public class ViewTreeParser {

    /**
     * 解析视图并生成视图树.
     * @param viewClass 视图.
     * @return 视图树.
     */
    public ViewTree generateViewTree(Class<?> viewClass) {
        return new ViewTree();
    }
}
