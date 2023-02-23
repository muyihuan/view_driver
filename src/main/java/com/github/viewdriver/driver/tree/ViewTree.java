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
    ViewTreeNode root_node;

    /**
     * 获取树的根节点.
     *
     * @return 根节点.
     */
    public ViewTreeNode getRoot() {
        return root_node;
    }

    /**
     * 视图树是否为空.
     *
     * @return true: 空、 false: 不为空.
     */
    public boolean isEmpty() {
        return root_node == null;
    }

    /**
     * 获取视图树第几层的所有节点，跟节点为第一层.
     * @param dept 第几层.
     * @return 所有节点.
     */
    List<ViewTreeNode> getDeptNodes(int dept) {
        if(dept <= 0) {
            return Collections.emptyList();
        }

        if(max_dept > 0 && dept > max_dept) {
            return Collections.emptyList();
        }

        Queue<ViewTreeNode> curr_dept_nodes = new LinkedList<>();
        curr_dept_nodes.offer(root_node);
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

        if(root_node == null) {
            logger.debug("********************空*********************");
            return;
        }

        logger.debug("start 可视化视图树 =>");
        logger.debug("-------------------=>");
        logger.debug("---------------------=>");
        int curr_dept = 1;
        while(curr_dept <= 20) {
            List<ViewTreeNode> nodes = getDeptNodes(curr_dept);
            if(nodes == null || nodes.size() == 0) {
                break;
            }

            String views = "|  ";
            for(int i = 0; i <  nodes.size(); i ++) {
                if(nodes.get(i).getType() == 2) {
                    continue;
                }
                if(i == (nodes.size() - 1)) {
                    views += "<" + nodes.get(i).getNodeClass().getSimpleName() + ">" + "  |";
                }
                else {
                    views += "<" + nodes.get(i).getNodeClass().getSimpleName() + ">" + "  |  ";
                }
            }
            logger.debug("第" + curr_dept + "层 : " + views);
            curr_dept ++;
        }
        logger.debug("可视化视图树 end");
    }
}
