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
