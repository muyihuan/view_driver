package com.github.viewdriver.driver.tree;

/**
 * 视图树节点之间的连线.
 *
 * @author yanghuan
 */
public class ViewTreeLine {

    ViewTreeNode left;
    ViewTreeNode right;
    boolean is_one_to_n;

    /**
     * Create a new instance.
     *
     * @param left 左 边的节点.
     * @param right 左 边的节点.
     * @param is_one_to_n 是否是1:n，只存在1:1和1:n不存在其他情况.
     */
    public ViewTreeLine(ViewTreeNode left, ViewTreeNode right, boolean is_one_to_n) {
        this.left = left;
        this.right = right;
        this.is_one_to_n = is_one_to_n;
    }
}
