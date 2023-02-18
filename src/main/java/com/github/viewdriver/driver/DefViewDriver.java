package com.github.viewdriver.driver;

import com.github.viewdriver.Context;
import com.github.viewdriver.ViewDriver;
import com.github.viewdriver.driver.exception.MetaDataIsNullException;
import com.github.viewdriver.driver.executor.ConfusedExecutor;
import com.github.viewdriver.driver.executor.IsolatedExecutor;
import com.github.viewdriver.driver.metadata.ViewDriverMetaData;
import com.github.viewdriver.driver.tree.ViewTree;
import com.github.viewdriver.driver.tree.ViewTreeNode;
import com.github.viewdriver.driver.tree.ViewTreeParser;
import lombok.Data;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * 默认视图驱动器.
 *
 * @author yanghuan
 */
public class DefViewDriver implements ViewDriver {

    private final ViewDriverMetaData driverMeta;
    private final ViewTreeParser viewParser;
    private Config config;
    private Executor executor;

    /**
     * Create a new instance.
     *
     * @param driverMeta 视图驱动元数据.
     * @param config 视图驱动相关配置.
     */
    public DefViewDriver(ViewDriverMetaData driverMeta, Config config) {
        this(driverMeta, config, null, new ViewTreeParser(driverMeta));
    }

    /**
     * Create a new instance.
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

        // 对注册的数据进行校验，校验失败会抛异常.
        if(this.driverMeta != null) {
            this.driverMeta.check();
        }

        // 创建执行器.
        if(this.executor == null) {
            if(this.config == null || this.config.executorConfig == null) {
                this.executor = new ConfusedExecutor("view-driver-executor", null);
            }
            else if(this.config.executorConfig.getGroupCount() < 1) {
                this.executor = new ConfusedExecutor("view-driver-executor", this.config.executorConfig);
            }
            else {
                this.executor = new IsolatedExecutor("view-driver-executor", this.config.executorConfig);
            }
        }
    }

    @Override
    public <V> List<V> mapView(List inputDataList, Class<V> viewClass, Context context) throws Exception {
        if(driverMeta == null) {
            throw new MetaDataIsNullException();
        }

        if(inputDataList == null || inputDataList.size() == 0) {
            return Collections.emptyList();
        }

        ViewTree viewTree = viewParser.generateViewTree(viewClass);

        int root_dept = 1;
        int curr_dept = root_dept;
        Map<Class, Map<Object, Object>> loaded_models = new HashMap<>();
        while(true) {
            List<ViewTreeNode> nodes = viewTree.getDeptNodes(curr_dept);
            if(nodes == null || nodes.size() == 0) {
                return Collections.emptyList();
            }

            if(curr_dept == root_dept) {

            }
            else {

            }

            curr_dept ++;
        }




//        // 获取视图绑定的model view_model.get(viewClass)
//        Class<?> modelClass = Object.class;
//        List topModels;
//        if(modelClass.isInstance(inputDataList.get(0))) {
//            topModels = inputDataList;
//        }
//        else {
//            // 获取model加载器 model_loader.get(modelClass)
//            Function<List, Map> modelLoader = null;
//            Map map = modelLoader.apply(inputDataList);
//            topModels = new ArrayList(map.values());
//        }
//        Map<Class<?>, List> topData = new HashMap<>();
//        topData.put(modelClass, topModels);
//
//        int dept = 1;
//        while(true) {
//            int toDept = dept;
//            dept ++;
//            List<ViewTreeNode> nodes = viewTree.getDeptNodes(toDept);
//            if(nodes == null || nodes.size() == 0) {
//                break;
//            }
//
//            // viewTree.getDeptNodes(toDept - 1)
//            Map<Class<?>, List> deptModels = null;
//            if(deptModels == null || deptModels.size() == 0) {
//                break;
//            }
//
//            Map<ViewTreeNode, List> collectIdsMap = new HashMap<>();
//            // 抽取
//            for(ViewTreeNode node : nodes) {
//                List<Object> collectIds = new ArrayList<>();
//
//                // model id 抽取 id_getter.get(node.getNeed())
//                Map<Class<?>, Function<Object, Object>> getters = null;
//                getters.keySet().forEach(mClass -> {
//                    Function<Object, Object> get = getters.get(mClass);
//                    List list = deptModels.get(mClass);
//                    list.forEach(ids -> {
//                        if(ids instanceof Collection) {
//                            ((Collection) ids).forEach(id -> {
//                                collectIds.add(get.apply(ids));
//                            });
//                        }
//                        else {
//                            collectIds.add(get.apply(ids));
//                        }
//                    });
//                });
//
//                collectIdsMap.put(node, collectIds);
//            }
//
//            // model加载
//            for(ViewTreeNode node : collectIdsMap.keySet()) {
//
//            }
//        }
//
//        // 通用动态代理实现视图渲染
//        List<V> viewResult = new ArrayList<>(inputDataList.size());
//        for(int i = 0; i < inputDataList.size(); i ++) {
//            V view = viewClass.newInstance();
//
//        }

//        return Collections.emptyList();
    }

    /**
     * 需要
     */
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
    }
}
