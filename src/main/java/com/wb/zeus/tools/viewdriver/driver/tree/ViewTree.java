package com.wb.zeus.tools.viewdriver.driver.tree;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    private ViewTreeNode rootNode;

    public List<ViewTreeNode> getDeptNodes(int dept) {
        return Collections.emptyList();
    }

    public Map<Class<?>, List> getDeptLoadModel(int dept) {
        return Collections.emptyMap();
    }

    public void setDeptLoadModel(int dept, Map<Class<?>, List> models) {
    }
}
