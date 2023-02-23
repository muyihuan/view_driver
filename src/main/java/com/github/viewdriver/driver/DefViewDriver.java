package com.github.viewdriver.driver;

import com.github.viewdriver.Context;
import com.github.viewdriver.ViewDriver;
import com.github.viewdriver.driver.exception.MetaDataIsNullException;
import com.github.viewdriver.driver.exception.NotViewException;
import com.github.viewdriver.driver.exception.ParamIsNullException;
import com.github.viewdriver.driver.executor.ConfusedExecutor;
import com.github.viewdriver.driver.executor.IsolatedExecutor;
import com.github.viewdriver.driver.metadata.ViewDriverMetaData;
import com.github.viewdriver.driver.tree.ViewTree;
import com.github.viewdriver.driver.tree.ViewTreeLine;
import com.github.viewdriver.driver.tree.ViewTreeNode;
import com.github.viewdriver.driver.tree.ViewTreeParser;
import com.github.viewdriver.lambda.FieldGetter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Function;
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

        if(this.config == null) {
            this.config = new Config();
            this.config.setTimeout(3000);
        }

        if(this.driverMeta != null) {
            // 对注册的数据进行校验，校验失败会抛异常.
            this.driverMeta.check();
        }

        if(this.executor == null) {
            if(this.config.executorConfig == null) {
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
        if(viewTree.isEmpty()) {
            throw new NotViewException();
        }

        if(context == null) {
            context = new Context();
        }

        ViewTreeNode root_node = viewTree.getRoot();

        // model加载
        long timeout = config.driverConfig.getTimeout();
        ModelHouse model_house = new ModelHouse();
        load_mode(root_node, null, true, inputDataList, context, model_house);

        // 临时存在
        Thread.sleep(timeout);

        // view渲染
        List<V> views = model_house.getRootModelList().stream()
                .filter(Objects::nonNull)
                .map(model -> (V) view_mapper(root_node, model, model_house)).collect(Collectors.toList());

        return views;
    }

    /**
     * model 加载.
     * todo 研究java线程是怎么玩的.
     *
     * @param node 节点.
     * @param is_root 是否是根节点.
     * @param inputDataList 输入数据.
     */
    private void load_mode(ViewTreeNode node, ViewTreeNode parent_node, boolean is_root, List inputDataList, Context context, ModelHouse model_house) {
        AtomicBoolean is_need_load_child = new AtomicBoolean(false);
        if(is_root) {
            is_need_load_child.set(true);
            Class<?> model_class = driverMeta.view_bind_model.get(node.getNodeClass());
            Function id_getter = driverMeta.model_id_getter.get(model_class);
            if(model_class.isInstance(inputDataList.get(0))) {
                inputDataList.forEach(data -> {
                    model_house.saveModel(model_class, id_getter.apply(data), data);
                });
                model_house.saveRootModelList(inputDataList);
            }
            else {
                BiFunction model_loader = driverMeta.model_loader_by_id.get(model_class);
                Map model_map = (Map) model_loader.apply(inputDataList, context);
                model_map.forEach((key, value) -> {
                    model_house.saveModel(model_class, key, value);
                });
                List<Object> root_models = new ArrayList<>();
                inputDataList.forEach(id -> {
                    root_models.add(model_map.get(id));
                });
                model_house.saveRootModelList(root_models);
            }
        }
        else {
            Map<Method, ViewTreeLine> from_parent_line_with_method = node.getFromParentLine().get(parent_node);
            for(Map.Entry<Method, ViewTreeLine> entry : from_parent_line_with_method.entrySet()) {
                Method _parent_getter = entry.getKey();
                ViewTreeLine from_parent_line = entry.getValue();
                ViewTreeNode _parent_node = from_parent_line.getLeft();
                Class<?> parent_view_class = _parent_node.getNodeClass();
                Class<?> parent_model_class = driverMeta.view_bind_model.get(parent_view_class);
                Collection parent_models = model_house.getAllModelByType(parent_model_class);
                if(parent_models.size() > 0) {
                    if(node.getType() == 0) {
                        Class<?> need_model_class = driverMeta.view_bind_model.get(node.getNodeClass());
                        boolean is_one_to_n = from_parent_line.is_one_to_n();
                        if(is_one_to_n) {
                            Function id_getter = driverMeta.model_id_getter.get(parent_model_class);
                            List collect_ids = new ArrayList();
                            parent_models.forEach(model -> collect_ids.add(id_getter.apply(model)));
                            FieldGetter outer_id_getter = driverMeta.model_relation_by_outer_id.get(new ViewDriverMetaData.TwoModel(need_model_class, parent_model_class));
                            BiFunction model_loader_bind_field = driverMeta.model_loader_by_outer_id_bind_field.get(new ViewDriverMetaData.ModelAndGetterAndField(need_model_class, outer_id_getter.getMethodName(), _parent_getter.getName()));
                            if(model_loader_bind_field != null) {
                                Map<Object, List> data_list_map = (Map<Object, List>) model_loader_bind_field.apply(collect_ids, context);
                                if(data_list_map != null && !data_list_map.isEmpty()) {
                                    data_list_map.forEach((key, value) -> {
                                        boolean is_succeed = model_house.saveModelList(need_model_class, key, value);
                                        if(is_succeed) {
                                            is_need_load_child.set(true);
                                        }
                                    });
                                }
                            }
                            else {
                                BiFunction model_loader = driverMeta.model_loader_by_outer_id.get(new ViewDriverMetaData.ModelAndGetter(need_model_class, outer_id_getter.getMethodName()));
                                if(model_loader != null) {
                                    Map<Object, List> model_list_map = (Map<Object, List>) model_loader.apply(collect_ids, context);
                                    if(model_list_map != null && !model_list_map.isEmpty()) {
                                        model_list_map.forEach((key, value) -> {
                                            boolean is_succeed = model_house.saveModelList(need_model_class, key, value);
                                            if(is_succeed) {
                                                is_need_load_child.set(true);
                                            }
                                        });
                                    }
                                }
                            }
                        }
                        else {
                            FieldGetter relation_id_getter = driverMeta.model_relation_by_outer_id.get(new ViewDriverMetaData.TwoModel(parent_model_class, need_model_class));
                            if(relation_id_getter != null) {
                                List collect_ids = new ArrayList();
                                parent_models.forEach(model -> {
                                    Object _id = relation_id_getter.apply(model);
                                    if(_id != null) {
                                        boolean isArray = _id.getClass().isArray();
                                        boolean isCollection = Collection.class.isAssignableFrom(_id.getClass());
                                        if(isArray) {
                                            collect_ids.addAll(Arrays.asList((Object[]) _id));
                                        }
                                        else if(isCollection) {
                                            collect_ids.addAll((Collection) _id);
                                        }
                                        else {
                                            collect_ids.add(_id);
                                        }
                                    }
                                });
                                BiFunction model_loader = driverMeta.model_loader_by_id.get(need_model_class);
                                if(model_loader != null) {
                                    Map model_list_map = (Map) model_loader.apply(collect_ids, context);
                                    if(model_list_map != null && !model_list_map.isEmpty()) {
                                        model_list_map.forEach((key, value) -> {
                                            boolean is_succeed = model_house.saveModel(need_model_class, key, value);
                                            if(is_succeed) {
                                                is_need_load_child.set(true);
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }
                    else if(node.getType() == 1) {
                        ViewDriverMetaData.ViewAndGetter view_and_getter = new ViewDriverMetaData.ViewAndGetter(parent_view_class, _parent_getter.getName());
                        FieldGetter id_getter = driverMeta.non_model_id_getter.get(view_and_getter);
                        if(id_getter != null) {
                            List collect_ids = new ArrayList();
                            parent_models.forEach(model -> collect_ids.add(id_getter.apply(model)));
                            BiFunction object_loader = driverMeta.non_model_loader.get(view_and_getter);
                            if(object_loader != null) {
                                Map data_map = (Map) object_loader.apply(collect_ids, context);
                                if(data_map != null && !data_map.isEmpty()) {
                                    data_map.forEach((key, value) -> {
                                        boolean is_succeed = model_house.saveObject(node.getNodeClass(), key, value);
                                        if(is_succeed) {
                                            is_need_load_child.set(true);
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            }
        }

        if(is_need_load_child.get()) {
            List<ViewTreeNode> nodes = node.getChildNodes();
            if(nodes != null && nodes.size() > 0) {
                for(ViewTreeNode _node : nodes) {
//                    executor.execute(() -> load_mode(_node, node, false, inputDataList, context, model_house));
                    load_mode(_node, node, false, inputDataList, context, model_house);
                }
            }
        }
    }

    /**
     * view 渲染(通用动态代理实现视图渲染).
     *
     * @param node 视图树节点.
     * @param model model.
     * @param model_house 已加载的所有model.
     * @return 视图对象.
     */
    private Object view_mapper(ViewTreeNode node, Object model, ModelHouse model_house) {
        if(node.getType() != 0) {
            return null;
        }

        Map<String, Object> child_views = new HashMap<>();
        List<ViewTreeNode> child_nodes = node.getChildNodes();
        if(child_nodes != null && child_nodes.size() > 0) {
            for(ViewTreeNode child_node : child_nodes) {
                Class child_node_class = child_node.getNodeClass();
                Map<Method, ViewTreeLine> from_parent_line_with_getter = child_node.getFromParentLine().get(node);
                for(Map.Entry<Method, ViewTreeLine> entry : from_parent_line_with_getter.entrySet()) {
                    Method _parent_getter = entry.getKey();
                    if(child_views.containsKey(_parent_getter.getName())) {
                        continue;
                    }

                    ViewTreeLine from_parent_line = entry.getValue();
                    if(child_node.getType() == 0) {
                        Class child_model_class = driverMeta.view_bind_model.get(child_node_class);
                        if(from_parent_line.is_one_to_n()) {
                            Object _parent_id = model_house.getIdByModel(model.getClass(), model);
                            List<Object> _child_model_list = model_house.getModelListByOuterId(child_model_class, _parent_id);
                            if(_child_model_list != null && _child_model_list.size() > 0) {
                                List<Object> _child_views = new ArrayList<>();
                                for(Object _child_model : _child_model_list) {
                                    Object _child_view = view_mapper(child_node, _child_model, model_house);
                                    _child_views.add(_child_view);
                                }
                                child_views.put(_parent_getter.getName(), _child_views);
                            }
                        }
                        else {
                            FieldGetter outer_id_getter = driverMeta.model_relation_by_outer_id.get(new ViewDriverMetaData.TwoModel(model.getClass(), child_model_class));
                            if(outer_id_getter != null) {
                                Class outer_id_getter_return_type = outer_id_getter.getReturnType();
                                boolean outerIdGetterIsArray = outer_id_getter_return_type.isArray();
                                boolean outerIdGetterIsCollection = Collection.class.isAssignableFrom(outer_id_getter_return_type);
                                if(outerIdGetterIsArray) {
                                    Object[] _ids = (Object[]) outer_id_getter.apply(model);
                                    if(_ids != null && _ids.length > 0) {
                                        List<Object> _child_views = new ArrayList<>();
                                        for(Object _id : _ids) {
                                            Object _child_model = model_house.getModelById(child_model_class, _id);
                                            if(_child_model != null) {
                                                Object _child_view = view_mapper(child_node, _child_model, model_house);
                                                _child_views.add(_child_view);
                                            }
                                        }
                                        child_views.put(_parent_getter.getName(), _child_views);
                                    }
                                }
                                else if(outerIdGetterIsCollection) {
                                    Collection _ids = (Collection) outer_id_getter.apply(model);
                                    if(_ids != null && _ids.size() > 0) {
                                        List<Object> _child_views = new ArrayList<>();
                                        for(Object _id : _ids) {
                                            Object _child_model = model_house.getModelById(child_model_class, _id);
                                            if(_child_model != null) {
                                                Object _child_view = view_mapper(child_node, _child_model, model_house);
                                                _child_views.add(_child_view);
                                            }
                                        }
                                        child_views.put(_parent_getter.getName(), _child_views);
                                    }
                                }
                                else {
                                    Object _id = outer_id_getter.apply(model);
                                    Object _child_model = model_house.getModelById(child_model_class, _id);
                                    if(_child_model != null) {
                                        Object _child_view = view_mapper(child_node, _child_model, model_house);
                                        child_views.put(_parent_getter.getName(), _child_view);
                                    }
                                }
                            }
                        }
                    }
                    else if(child_node.getType() == 1) {
                        Object _parent_id = model_house.getIdByModel(model.getClass(), model);
                        Object _child_object = model_house.getObjectById(child_node_class, _parent_id);
                        if(_child_object != null) {
                            child_views.put(_parent_getter.getName(), _child_object);
                        }
                    }
                }
            }
        }

        ViewMapper view_mapper = new ViewMapper();
        view_mapper.node = node;
        view_mapper.model = model;
        view_mapper.child_view_map = child_views;
        view_mapper.driverMeta = driverMeta;
        return view_mapper.map();
    }

    /**
     * 存储加载好的model.
     *
     * @author yanghuan
     */
    private static class ModelHouse {
        private List<Object> root_models = new ArrayList<>();
        private Map<Class, Map<Object, Object>> id_model_1 = new HashMap<>();
        private Map<Class, Map<Object, Object>> model_id_1 = new HashMap<>();
        private Map<Class, Map<Object, List<Object>>> id_model_n = new HashMap<>();
        private Map<Class, Map<Object, Object>> object_1 = new HashMap<>();

        /**
         * 保存输入的model集合.
         *
         * @param root_models model集合.
         */
        private void saveRootModelList(List<Object> root_models) {
            this.root_models = root_models;
        }

        /**
         * 获取输入的model集合.
         *
         * @return model集合.
         */
        private List<Object> getRootModelList() {
            return root_models;
        }

        /**
         * 保存model.
         *
         * @param modelClass model类型.
         * @param id model的ID.
         * @param model model.
         */
        private boolean saveModel(Class modelClass, Object id, Object model) {
            if(model == null) {
                return false;
            }
            Map<Object, Object> data = id_model_1.get(modelClass);
            if(data == null) {
                id_model_1.put(modelClass, new LinkedHashMap<>());
                model_id_1.put(modelClass, new HashMap<>());

                return saveModel(modelClass, id, model);
            }
            else {
                if(data.containsKey(id)) {
                    return false;
                }

                data.put(id, model);
                model_id_1.get(modelClass).put(model, id);
                return true;
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
            Map<Object, Object> data = id_model_1.get(modelClass);
            if(data == null) {
                return null;
            }
            else {
                return data.get(id);
            }
        }

        /**
         * 通过model获取model的ID.
         *
         * @param modelClass model类型.
         * @param model model.
         * @return model的ID.
         */
        private Object getIdByModel(Class modelClass, Object model) {
            Map<Object, Object> data = model_id_1.get(modelClass);
            if(data == null) {
                return null;
            }
            else {
                return data.get(model);
            }
        }

        /**
         * 获取某类型的所有model.
         *
         * @param modelClass model类型.
         * @return model集合.
         */
        private Collection<Object> getAllModelByType(Class modelClass) {
            Collection<Object> all_models = new ArrayList<>();
            Map<Object, Object> data = id_model_1.get(modelClass);
            if(data != null) {
                all_models.addAll(data.values());
            }

            Map<Object, List<Object>> data_list = id_model_n.get(modelClass);
            if(data_list != null && data_list.size() > 0) {
                data_list.forEach((key, value) -> {
                    if(value != null && value.size() > 0) {
                        all_models.addAll(value);
                    }
                });
            }
            return all_models;
        }

        /**
         * 保存model集合.
         *
         * @param modelClass model类型.
         * @param outerId 外键ID.
         * @param models model集合.
         */
        private boolean saveModelList(Class modelClass, Object outerId, List<Object> models) {
            if(models == null || models.size() == 0) {
                return false;
            }
            Map<Object, List<Object>> data = id_model_n.get(modelClass);
            if(data == null) {
                id_model_n.put(modelClass, new HashMap<>());

                return saveModelList(modelClass, outerId, models);
            }
            else {
                if(data.containsKey(outerId)) {
                    return false;
                }

                data.put(outerId, models);
                return true;
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
            Map<Object, List<Object>> data = id_model_n.get(modelClass);
            if(data == null) {
                return Collections.emptyList();
            }
            else {
                return data.get(outerId);
            }
        }

        /**
         * 保存非model数据.
         *
         * @param objectClass 非model数据类型.
         * @param id 查询ID.
         * @param object 非model数据.
         */
        private boolean saveObject(Class objectClass, Object id, Object object) {
            if(object == null) {
                return false;
            }
            Map<Object, Object> data = object_1.get(objectClass);
            if(data == null) {
                object_1.put(objectClass, new LinkedHashMap<>());

                return saveObject(objectClass, id, object);
            }
            else {
                if(data.containsKey(id)) {
                    return false;
                }

                data.put(id, object);
                return true;
            }
        }

        /**
         * 通过ID获取某类型的所有model.
         *
         * @param objectClass 非model数据类型.
         * @param id 查询ID.
         * @return 非model数据.
         */
        private Object getObjectById(Class objectClass, Object id) {
            Map<Object, Object> data = object_1.get(objectClass);
            if(data == null) {
                return null;
            }
            else {
                return data.get(id);
            }
        }
    }

    /**
     * 通过CGLIB进行view的渲染.
     *
     * @author yanghuan
     */
    private static class ViewMapper<V> implements MethodInterceptor {
        private ViewTreeNode node;
        private Object model;
        private Map<String, Object> child_view_map;
        private ViewDriverMetaData driverMeta;

        /**
         * 生成并获取渲染好的视图对象.
         *
         * @return 视图对象.
         */
        private Object map() {
            if(node == null || model == null || driverMeta == null) {
                throw new ParamIsNullException("node || model || driverMeta 不允许为空!");
            }

            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(node.getNodeClass());
            enhancer.setCallback(this);
            return enhancer.create();
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            String method_name = method.getName();
            ViewTreeNode _child_node = node.getChildNodeByGetter(method_name);
            if(_child_node == null) {
                return methodProxy.invokeSuper(o, objects);
            }

            int type = _child_node.getType();
            if(type == 0 || type == 1) {
                return child_view_map.get(method_name);
            }
            else if(type == 2) {
                ViewDriverMetaData.ViewAndGetter viewAndGetter = new ViewDriverMetaData.ViewAndGetter(node.getNodeClass(), method_name);
                FieldGetter _bind_getter = driverMeta.field_getter_bind.get(viewAndGetter);
                if(_bind_getter != null) {
                    Object _model_value = _bind_getter.apply(model);
                    if(_model_value != null) {
                        Function _decorator = driverMeta.field_decorator.get(viewAndGetter);
                        if(_decorator != null) {
                            return _decorator.apply(_model_value);
                        }
                        else {
                            return _model_value;
                        }
                    }
                }
                else {
                    try {
                        Method _model_getter = model.getClass().getMethod(method_name);
                        if(_model_getter != null) {
                            return _model_getter.invoke(model);
                        }
                    }
                    catch (NoSuchMethodException ignore) {
                        return methodProxy.invokeSuper(o, objects);
                    }
                }
            }

            return null;
        }
    }
}
