package com.github.viewdriver.driver;

import com.github.viewdriver.Context;
import com.github.viewdriver.ViewDriver;
import com.github.viewdriver.driver.exception.MetaDataIsNullException;
import com.github.viewdriver.driver.exception.NotViewException;
import com.github.viewdriver.driver.executor.ConfusedExecutor;
import com.github.viewdriver.driver.executor.IsolatedExecutor;
import com.github.viewdriver.driver.metadata.ViewDriverMetaData;
import com.github.viewdriver.driver.tree.ViewTree;
import com.github.viewdriver.driver.tree.ViewTreeLine;
import com.github.viewdriver.driver.tree.ViewTreeNode;
import com.github.viewdriver.driver.tree.ViewTreeParser;
import com.github.viewdriver.lambda.FieldGetter;
import lombok.Data;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.function.Function;

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
        if(viewTree.isNull()) {
            throw new NotViewException();
        }

        if(context == null) {
            context = new Context();
        }

        ViewTreeNode root_node = viewTree.getRoot();
        ModelHouse model_house = new ModelHouse();
        // 数据加载
        load_mode(root_node, true, inputDataList, context, model_house);



//        // 通用动态代理实现视图渲染
//        List<V> viewResult = new ArrayList<>(inputDataList.size());
//        for(int i = 0; i < inputDataList.size(); i ++) {
//            V view = viewClass.newInstance();
//
//        }

        return Collections.emptyList();
    }

    /**
     * model 加载.
     *
     * @param node 节点.
     * @param is_root 是否是根节点.
     * @param inputDataList 输入数据.
     */
    private void load_mode(ViewTreeNode node, boolean is_root, List inputDataList, Context context, ModelHouse model_house) {
        if(is_root) {
            Class<?> model_class = driverMeta.view_bind_model.get(node.getNodeClass());
            Function idGetter = driverMeta.model_id_getter.get(model_class);
            if(model_class.isInstance(inputDataList.get(0))) {
                inputDataList.forEach(data -> {
                    model_house.saveModel(model_class, idGetter.apply(data), data);

                    if(node.isDependSelf()) {
                        FieldGetter _getter = driverMeta.model_relation_by_outer_id.get(new ViewDriverMetaData.TwoModel(model_class, model_class));
                        if(_getter != null) {
                            model_house.saveModel(model_class, idGetter.apply(_getter.apply(data)), _getter.apply(data));
                        }
                    }
                });
            }
            else {
                BiFunction modelLoader = driverMeta.model_loader_by_id.get(model_class);
                Map modelMap = (Map) modelLoader.apply(inputDataList, config);
                modelMap.forEach((key, value) -> {
                    model_house.saveModel(model_class, key, value);

                    if(node.isDependSelf()) {
                        FieldGetter _getter = driverMeta.model_relation_by_outer_id.get(new ViewDriverMetaData.TwoModel(model_class, model_class));
                        if(_getter != null) {
                            model_house.saveModel(model_class, idGetter.apply(_getter.apply(value)), _getter.apply(value));
                        }
                    }
                });
            }

            List<ViewTreeNode> nodes = node.getChildNodes();
            for(ViewTreeNode _node : nodes) {
                executor.execute(() -> {
                    load_mode(_node, false, inputDataList, context, model_house);
                });
            }
        }
        else {
            if(node.getType() == 0) {
                // 父节点的model信息
                ViewTreeLine from_parent_line = node.getFromParentLine();
                ViewTreeNode parent_node = from_parent_line.getLeft();
                Class<?> parent_view_class = parent_node.getNodeClass();
                Class<?> parent_model_class = driverMeta.view_bind_model.get(parent_view_class);
                List parent_models = model_house.getAllModelByType(parent_model_class);
                if(parent_models != null && parent_models.size() > 0) {
                    // 需要加载该 modelClass
                    Class<?> need_model_class = driverMeta.view_bind_model.get(node.getNodeClass());

                    // 计算如何加载
                    boolean is_one_to_n = from_parent_line.is_one_to_n();
                    if(is_one_to_n) {

                    }
                    else {
                        FieldGetter getter = driverMeta.model_relation_by_outer_id.get(new ViewDriverMetaData.TwoModel(parent_model_class, need_model_class));
                        List collect_ids = new ArrayList();

                    }


                    // 收集查询 modelClass 的ID
                }
            }
            else {

            }
        }
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
