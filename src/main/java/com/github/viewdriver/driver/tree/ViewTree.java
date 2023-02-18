package com.github.viewdriver.driver.tree;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 视图树.
 * 示例：
 *   1.View1 包含 View2、View3.
 *   2.View2 包含 View4.
 *   View1的视图树如下：
 *
 *               View1
 *               \   \
 *            View2 View3
 *              \
 *           View4
 *
 * @author yanghuan
 */
public class ViewTree {

    private static final Logger logger = LoggerFactory.getLogger(ViewTree.class);
    private int max_dept;

    /**
     * 根节点.
     */
    ViewTreeNode rootNode;


    /**
     * 获取视图树第几层的所有节点，跟节点为第一层.
     * @param dept 第几层.
     * @return 所有节点.
     */
    public List<ViewTreeNode> getDeptNodes(int dept) {
        if(dept <= 0) {
            return Collections.emptyList();
        }

        ViewTreeNode curr_dept_first = rootNode;
        int curr_dept = 1;
        while(true) {
            if(curr_dept == dept) {
                List<ViewTreeNode> nodes = new ArrayList<>();
                ViewTreeNode node = curr_dept_first;
                while(node != null) {
                    nodes.add(node);

                    ViewTreeLine to_brother_line = node.toBrotherLine;
                    if(to_brother_line == null) {
                        node = null;
                    }
                    else {
                        node = to_brother_line.right;
                    }
                }
                return nodes;
            }

            List<ViewTreeLine> to_child_lines = curr_dept_first.toChildLines;
            if(to_child_lines == null || to_child_lines.size() == 0) {
                ViewTreeLine to_brother_line = rootNode.toBrotherLine;
                if(to_brother_line == null) {
                    max_dept = curr_dept;
                    return Collections.emptyList();
                }
                else {
                    curr_dept_first = to_brother_line.right;
                }
            }
            else {
                curr_dept_first = to_child_lines.get(0).right;
                curr_dept ++;
            }

            if(curr_dept % 100 == 0) {
                logger.info("请注意当前已进入视图树的第 " + curr_dept + "层");
            }
        }
    }

    /**
     * 可视化视图树.
     */
    void draw_it() {
        if(!logger.isDebugEnabled()) {
            return;
        }

        if(rootNode == null) {
            logger.debug("********************空*********************");
            return;
        }

        logger.debug("请查看视图树=> *********************************");
        logger.debug("*********************************************");
    }
}
