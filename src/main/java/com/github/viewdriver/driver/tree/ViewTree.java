package com.github.viewdriver.driver.tree;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

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

        if(max_dept > 0 && dept > max_dept) {
            return Collections.emptyList();
        }

        Queue<ViewTreeNode> curr_dept_nodes = new LinkedList<>();
        curr_dept_nodes.offer(rootNode);
        int curr_dept = 1;
        while (curr_dept != dept) {
            int count = curr_dept_nodes.size();
            if(count == 0) {
                max_dept = curr_dept;
            }

            while (count > 0) {
                ViewTreeNode node = curr_dept_nodes.poll();
                if (node != null && node.toChildLines != null && node.toChildLines.size() > 0) {
                    node.toChildLines.forEach(line -> {
                        curr_dept_nodes.offer(line.right);
                    });
                }

                count--;
            }

            curr_dept++;
            if (curr_dept % 100 == 0) {
                logger.info("请注意当前已进入视图树的第 " + curr_dept + "层");
            }
        }

        return new ArrayList<>(curr_dept_nodes);
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
