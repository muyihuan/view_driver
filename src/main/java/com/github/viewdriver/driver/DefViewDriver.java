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

import java.util.*;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

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
        ModelHouse modelHouse = new ModelHouse();
        while(true) {
            if(curr_dept == root_dept) {
                Class<?> modelClass = driverMeta.view_bind_model.get(viewClass);
                if(modelClass.isInstance(inputDataList.get(0))) {
                    inputDataList.forEach(data -> modelHouse.saveModel(modelClass, data, data));
                }
                else {

                }
            }
            else {
                List<ViewTreeNode> nodes = viewTree.getDeptNodes(curr_dept);
                if(nodes == null || nodes.size() == 0) {
                    break;
                }

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

        return Collections.emptyList();
    }

    /**
     * 存储加载好的model.
     *
     * @author yanghuan
     */
    @Data
    private static class ModelHouse {
        Map<Class, Map<Object, Object>> model_1 = new HashMap<>();
        Map<Class, Map<Object, List<Object>>> model_n = new HashMap<>();

        /**
         * 保存model.
         *
         * @param modelClass model类型.
         * @param id model的ID.
         * @param model model.
         */
        private void saveModel(Class modelClass, Object id, Object model) {
            Map<Object, Object> data = model_1.get(modelClass);
            if(data == null) {
                model_1.put(modelClass, new LinkedHashMap<>());

                saveModel(modelClass, id, model);
            }
            else {
                data.put(id, model);
            }
        }

        /**
         * 通过ID获取某类型的所有model.
         *
         * @param modelClass model类型.
         * @param id model的ID.
         * @return model.
         */
        private Object getModelById(Class modelClass, Object id) {
            Map<Object, Object> data = model_1.get(modelClass);
            if(data == null) {
                return null;
            }
            else {
                return data.get(id);
            }
        }

        /**
         * 获取某类型的所有model.
         *
         * @param modelClass model类型.
         * @return model集合.
         */
        private List<Object> getAllModelByType(Class modelClass) {
            Map<Object, Object> data = model_1.get(modelClass);
            if(data == null) {
                return Collections.emptyList();
            }
            else {
                return new ArrayList<>(model_1.values());
            }
        }

        /**
         * 保存model集合.
         *
         * @param modelClass model类型.
         * @param outerId 外键ID.
         * @param models model集合.
         */
        private void saveModelList(Class modelClass, Object outerId, List<Object> models) {
            Map<Object, List<Object>> data = model_n.get(modelClass);
            if(data == null) {
                model_1.put(modelClass, new HashMap<>());

                saveModel(modelClass, outerId, models);
            }
            else {
                data.put(outerId, models);
            }
        }

        /**
         * 通过ID获取某类型的所有model.
         *
         * @param modelClass model类型.
         * @param outerId 外键ID.
         * @return model集合.
         */
        private List<Object> getModelListByOuterId(Class modelClass, Object outerId) {
            Map<Object, List<Object>> data = model_n.get(modelClass);
            if(data == null) {
                return Collections.emptyList();
            }
            else {
                return data.get(outerId);
            }
        }
    }
}
