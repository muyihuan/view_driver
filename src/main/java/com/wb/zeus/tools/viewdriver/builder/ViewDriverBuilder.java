package com.wb.zeus.tools.viewdriver.builder;

import com.wb.zeus.tools.viewdriver.Context;
import com.wb.zeus.tools.viewdriver.ViewDriver;
import com.wb.zeus.tools.viewdriver.builder.lambda.FieldGetter;
import com.wb.zeus.tools.viewdriver.driver.Config;
import com.wb.zeus.tools.viewdriver.driver.DefViewDriver;
import com.wb.zeus.tools.viewdriver.driver.metadata.ViewDriverMetaData;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * 负责构建View驱动器.
 * . View绑定Model
 * . Model的id关联绑定
 * . 主键id的Model加载器
 * . 外键id的Model加载器
 * . 非Model数据加载器
 * . Model过滤器
 *
 * @author yanghuan
 */
public class ViewDriverBuilder {

    private ViewDriverMetaData driverMetaData;
    private Config config;

    public ViewDriverBuilder() {
        driverMetaData = new ViewDriverMetaData();
    }

    /**
     * View绑定Model.
     * @param view 视图 => 视图代表着数据意图展示的数据、数据的哪些内容、数据的展示结构关系.
     * @param model 业务model => 业务领域内的实体.
     * @return ViewDriverBuilder
     */
    public ViewDriverBuilder viewBindModel(Class<?> view, Class<?> model) {
        driverMetaData.view_bind_model.put(view, model);
        return this;
    }

    /**
     * View绑定Model.
     * @param view 视图 => 视图代表着数据意图展示的数据、数据的哪些内容、数据的展示结构关系.
     * @param model model => 业务领域内的实体.
     * @param fieldBinder view和model的属性的对应关系，如果属性的get方法相同 或者 不需要转化处理，那么无需设置.
     * @return ViewDriverBuilder
     */
    public <V, M> ViewDriverBuilder viewBindModel(Class<V> view, Class<M> model, FieldBinder<V, M> fieldBinder) {
        driverMetaData.view_bind_model.put(view, model);

        if(fieldBinder != null) {
            Map<FieldGetter, FieldGetter> fieldGetterBindMap = fieldBinder.fieldGetterBind;
            if(!fieldGetterBindMap.isEmpty()) {
                fieldGetterBindMap.forEach((viewFieldGetter, modelFieldGetter) -> {
                    try {
                        Class<?> viewClass = Class.forName(viewFieldGetter.getClassName());
                        driverMetaData.field_getter_bind.put(new ViewDriverMetaData.ViewAndGetter(viewClass, viewFieldGetter.getMethodName()), modelFieldGetter);
                        if(fieldBinder.fieldDecorator.containsKey(viewFieldGetter)) {
                            driverMetaData.field_decorator.put(new ViewDriverMetaData.ViewAndGetter(viewClass, viewFieldGetter.getMethodName()), fieldBinder.fieldDecorator.get(viewFieldGetter));
                        }
                    }
                    catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }

        return this;
    }

    /**
     * model的唯一id称为主键id，model内其他model的id称为外键id，绑定所有model的主键id与外键id的对应关系.
     * @param model 该model的id被其他model的外键id关联.
     * @param idBinders 外键id绑定列表.
     * @return ViewDriverBuilder
     */
    public <M> ViewDriverBuilder modelIdBind(Class<M> model, IdBinder<M> idBinders) {
        if(idBinders == null) {
            return this;
        }

        Map<FieldGetter<M, Object>, Class<?>> idBindMap = idBinders.idBind;
        if(idBindMap.isEmpty()) {
            return this;
        }

        idBindMap.forEach((idGetter, bindModel) -> {
            if(bindModel.getName().equals(model.getName())) {
                driverMetaData.model_id_getter.put(model, idGetter);
            }
            else {
                driverMetaData.model_relation_by_outer_id.put(new ViewDriverMetaData.TwoModel(model, bindModel), idGetter);
            }
        });

        return this;
    }

    /**
     * 通过model的主键id加载model.
     * @param model 目标model.
     * @param loader model加载器 => 输入model的主键id，查询返回对应的model.
     * @return ViewDriverBuilder
     */
    public <M, I> ViewDriverBuilder modelLoaderById(Class<M> model, BiFunction<List, Context, Map<I, M>> loader) {
        driverMetaData.model_loader_by_id.put(model, loader);
        return this;
    }

    /**
     * 通过model的外键id加载model.
     * @param model 目标model.
     * @param outerId 通过该外键id加载目标model，model的外键id可能有多个，需要指定使用哪一个.
     * @param loader model加载器 => 输入model的外键id，查询返回目标model集合.
     * @return ViewDriverBuilder
     */
    public <M> ViewDriverBuilder modelLoaderByOuterId(Class<M> model, FieldGetter<M, Object> outerId, BiFunction<List, Context, Map<Object, List<M>>> loader) {
        driverMetaData.model_loader_by_outer_id.put(new ViewDriverMetaData.ModelAndGetter(model, outerId.getMethodName()), loader);
        return this;
    }

    /**
     * 通过model的外键id加载model.
     * @param model 目标model.
     * @param outerId 通过该外键id加载目标model，model的外键id可能有多个，需要指定使用哪一个.
     * @param loader model加载器 => 输入model的外键id，查询返回目标model集合.
     * @param bindViewAttribute View的某属性 => View内可能存在多个属性通过同一个外键id加载model，但加载的方法是不同的.
     * @return ViewDriverBuilder
     */
    public <M> ViewDriverBuilder modelLoaderByOuterId(Class<M> model, FieldGetter<M, Object> outerId, BiFunction<List, Context, Map<Object, List<M>>> loader, FieldGetter bindViewAttribute) {
        driverMetaData.model_loader_by_outer_id_bind_field.put(new ViewDriverMetaData.ModelAndGetterAndField(model, outerId.getMethodName(), bindViewAttribute.getFieldName()), loader);
        return this;
    }

    /**
     * 非model的数据加载的加载器 需要绑定具体的View的属性和id抽取的方法.
     * @param viewAttribute 目标View的属性.
     * @param idGetter id抽取方法.
     * @param valueLoader 数据加载器.
     * @param <V> View.
     * @param <M> View绑定的Model.
     * @param <A> View的属性类型.
     * @param <I> 加载的ID类型.
     * @return ViewDriverBuilder
     */
    public <V, M, A, I> ViewDriverBuilder nonModelLoader(FieldGetter<V, A> viewAttribute, FieldGetter<M, I> idGetter, BiFunction<List<I>, Context, Map<I, A>> valueLoader) {
        try {
            ViewDriverMetaData.ViewAndGetter viewAndGetter = new ViewDriverMetaData.ViewAndGetter(Class.forName(viewAttribute.getClassName()), viewAttribute.getMethodName());
            driverMetaData.non_model_id_getter.put(viewAndGetter, idGetter);
            driverMetaData.non_model_loader.put(viewAndGetter, valueLoader);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    /**
     * model过滤器设置，根据设置的model过滤规则，进行model的过滤.
     * @param model 目标model.
     * @param filter 过滤器 => true: 表示命中不需要过滤掉 false: 表示没命中需要过滤掉.
     * @return ViewDriverBuilder
     */
    public <M> ViewDriverBuilder filter(Class<M> model, BiFunction<M, Context, Boolean> filter) {
        driverMetaData.model_filter.put(model, filter);
        return this;
    }

    /**
     * 设置视图驱动器相关配置.
     * @param config 视图驱动器配置.
     * @return ViewDriverBuilder
     */
    public ViewDriverBuilder config(Config config) {
        this.config = config;
        return this;
    }

    /**
     * 通过以上配置生成View驱动器.
     * @return ViewDriver
     */
    public ViewDriver build() {
        // 对注册的数据进行校验，校验失败会抛异常.
        driverMetaData.check();

        return new DefViewDriver(driverMetaData, config);
    }
}
