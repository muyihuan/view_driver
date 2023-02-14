package com.wb.zeus.tools.viewdriver.driver;

import com.wb.zeus.tools.viewdriver.Context;
import com.wb.zeus.tools.viewdriver.ViewDriver;
import com.wb.zeus.tools.viewdriver.driver.executor.MultiThreadExecutor;
import com.wb.zeus.tools.viewdriver.driver.metadata.ViewDriverMetaData;
import com.wb.zeus.tools.viewdriver.driver.tree.ViewTree;
import com.wb.zeus.tools.viewdriver.driver.tree.ViewTreeNode;
import com.wb.zeus.tools.viewdriver.driver.exception.MetaDataIsNullException;
import com.wb.zeus.tools.viewdriver.driver.tree.ViewTreeParser;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.function.Function;

/**
 * 默认视图驱动器.
 *
 * @author yanghuan
 */
public class DefViewDriver implements ViewDriver {

    private final ViewDriverMetaData driverMeta;
    private final Config config;
    private final Executor executor;
    private final ViewTreeParser viewParser;

    /**
     * Create a new instance
     *
     * @param driverMeta 视图驱动元数据.
     * @param config 视图驱动相关配置.
     */
    public DefViewDriver(ViewDriverMetaData driverMeta, Config config) {
        this(driverMeta, config, new MultiThreadExecutor("view_driver_executor", config), new ViewTreeParser());
    }

    /**
     * Create a new instance
     *
     * @param driverMeta 视图驱动元数据.
     * @param config 视图驱动相关配置.
     * @param executor 任务执行器.
     * @param viewTreeParser 视图解析器.
     */
    public DefViewDriver(ViewDriverMetaData driverMeta, Config config, Executor executor, ViewTreeParser viewTreeParser) {
        this.driverMeta = driverMeta;
        this.config = config;
        this.executor = executor;
        this.viewParser = viewTreeParser;
    }

    @Override
    public <V> List<V> mapView(List models, Class<V> viewClass, Context context) throws Exception {
        if(driverMeta == null) {
            throw new MetaDataIsNullException();
        }

        if(models == null || models.size() == 0) {
            return Collections.emptyList();
        }

        // 生成视图树
        ViewTree viewTree = viewParser.generateViewTree(viewClass);

        // 获取视图绑定的model view_model.get(viewClass)
        Class<?> modelClass = null;
        List topModels;
        if(modelClass.isInstance(models.get(0))) {
            topModels = models;
        }
        else {
            // 获取model加载器 model_loader.get(modelClass)
            Function<List, Map> modelLoader = null;
            Map map = modelLoader.apply(models);
            topModels = new ArrayList(map.values());
        }
        Map<Class<?>, List> topData = new HashMap<>();
        topData.put(modelClass, topModels);
        viewTree.setDeptLoadModel(0, topData);

        int dept = 1;
        while(true) {
            int toDept = dept;
            dept ++;
            List<ViewTreeNode> nodes = viewTree.getDeptNodes(toDept);
            if(nodes == null || nodes.size() == 0) {
                break;
            }

            Map<Class<?>, List> deptModels = viewTree.getDeptLoadModel(toDept - 1);
            if(deptModels == null || deptModels.size() == 0) {
                break;
            }

            Map<ViewTreeNode, List> collectIdsMap = new HashMap<>();
            // 抽取
            for(ViewTreeNode node : nodes) {
                List<Object> collectIds = new ArrayList<>();

                // model id 抽取 id_getter.get(node.getNeed())
                Map<Class<?>, Function<Object, Object>> getters = null;
                getters.keySet().forEach(mClass -> {
                    Function<Object, Object> get = getters.get(mClass);
                    List list = deptModels.get(mClass);
                    list.forEach(ids -> {
                        if(ids instanceof Collection) {
                            ((Collection) ids).forEach(id -> {
                                collectIds.add(get.apply(ids));
                            });
                        }
                        else {
                            collectIds.add(get.apply(ids));
                        }
                    });
                });

                collectIdsMap.put(node, collectIds);
            }

            // model加载
            for(ViewTreeNode node : collectIdsMap.keySet()) {

            }
        }

        // 通用动态代理实现视图渲染
        List<V> viewResult = new ArrayList<>(models.size());
        for(int i = 0; i < models.size(); i ++) {
            V view = viewClass.newInstance();

        }

        return Collections.emptyList();
    }
}
