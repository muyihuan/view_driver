package com.github.viewdriver.driver.metadata;

import com.github.viewdriver.driver.exception.ParamIsNullException;
import com.github.viewdriver.lambda.FieldGetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 视图驱动的元数据.
 * .view和model绑定关系
 * .view和model对应属性的绑定关系
 * .model之间的关联关系
 * .model加载器
 * .model过滤器
 *
 * @author yanghuan
 */
public class ViewDriverMetaData implements ModelAndView {

    private static final Logger logger = LoggerFactory.getLogger(ViewDriverMetaData.class);

    /**
     * 设置view和model绑定关系.
     *
     * 例如：
     *   view1绑定model1: view1 <=> model1
     *   view2绑定model2: view2 <=> model2
     *
     * view承载着数据意图: 哪个业务实体 + 实体的内的哪些数据.
     */
    public final Map<Class<?>, Class<?>> view_bind_model = new HashMap<>();

    /**
     * 设置view的属性值如何从绑定的model中获取.
     *
     * 例如：
     *    view1的属性a是通过model1的属性a转换后获取的.
     *                       |
     *                      V
     *    field_getter_bind: view1::getA <=> model1::getA
     *    field_decorator: view1::getA <=> (a) -> { return "value=" + a }
     *
     * 如果view和model对应属性的getter方法相同，则不需要设置.相反 需要显式设置对应getter方法对应关系，或者对应的转化方式(包含：类型转换、脱敏等).
     */
    public final Map<ViewAndGetter, FieldGetter> field_getter_bind = new HashMap<>();
    public final Map<ViewAndGetter, Function> field_decorator = new HashMap<>();

    /**
     * 设置model之间的关联关系.
     *
     * 例如：
     *    model1中有主键id为aId、外键id有bId，bId对应的是model2的主键id.
     *                             |
     *                            V
     *          model_relation_by_id: model1.class <=> model1::getAId
     *    model_relation_by_outer_id: model1.class + model2.class <=> model1::getBId (model1 关联 model2 通过 model1的bId属性关联)
     *
     * .设置model主键id.
     * .设置model外键id对应的model.
     */
    public final Map<Class<?>, Function> model_id_getter = new HashMap<>();
    public final Map<TwoModel, FieldGetter> model_relation_by_outer_id = new HashMap<>();

    /**
     * 设置通过model的主键id加载model的加载器.
     *
     * 例如：
     *    model1中有主键id为id，通过id以getModel1ById方法查询对应model1.
     *                             |
     *                            V
     *              model1.class <=> getModel1ById
     *
     */
    public final Map<Class<?>, BiFunction> model_loader_by_id = new HashMap<>();

    /**
     * 设置通过外键id加载model的加载器.
     *
     * 例如：
     *    model1中有外键id有bId，bId对应的是model2的主键id，可以通过bId和getModel1ListBybId方法查询所有对应的model1集合.
     *                                          |
     *                                         V
     *                     model1.class + bId <=> getModel1ListBybId
     *
     */
    public final Map<ModelAndGetter, BiFunction> model_loader_by_outer_id = new HashMap<>();

    /**
     * 设置通过外键id加载model的加载器，当一个View内存在多个属性的类型是同一个View类型并且model加载的方式不同.
     *
     * 例如：
     *    model1中有外键id有bId和cId，bId和cId对应的都是model2的主键id，分别可以通过getModel1ListBybId和getModel1ListBycId方法查询所有对应的model1集合.
     *                                          |
     *                                         V
     *               情况1: model1.class + bId <=> getModel1ListBybId
     *
     *               情况2: model1.class + bId <=> getModel1ListBycId
     *
     */
    public final Map<ModelAndGetterAndField, BiFunction> model_loader_by_outer_id_bind_field = new HashMap<>();

    /**
     * 设置View内未知类型(非View、未绑定Model的属性)的数据加载器.
     *
     * 例如：
     *    View1中有属性x，其数据类型为xObject，xObject的数据通过Model1的id以getXObject方法进行查询.
     *                                           |
     *                                          V
     *    non_model_id_getter: view1.class + x <=> model1::getId
     *
     *    non_model_loader: view1.class + x <=> getXObject
     *
     */
    public final Map<ViewAndGetter, FieldGetter> non_model_id_getter = new HashMap<>();
    public final Map<ViewAndGetter, BiFunction> non_model_loader = new HashMap<>();

    /**
     * model过滤器.
     *
     * 例如：
     *    过滤所有model1为空的model.
     *             |
     *            V
     *    model1.class <=>  model1 -> model1 != null
     *
     */
    public final Map<Class<?>, BiFunction> model_filter = new HashMap<>();

    /**
     * 对注册的数据进行校验，校验失败会抛异常，及时终止进行的流程.
     * 1.注册的 view 和 model 不可以为空!
     * 2.注册的 model 关系不可以为空!
     * 3.注册的 model 加载器不可以为空!
     * 4.注册的 非model 加载器不完整!
     * 5.view 和 model 使用错误，请检查配置!
     * 6.注册的 model 没有设置加载器!
     * 7. ...
     */
    @Override
    public void check() {
        if(view_bind_model.isEmpty()) {
            throw new RuntimeException("注册的 view 和 model 不可以为空!");
        }

        if(model_id_getter.isEmpty()) {
            throw new RuntimeException("注册的 model 关系不可以为空!");
        }

        if(model_loader_by_id.isEmpty()) {
            throw new RuntimeException("注册的 model 加载器不可以为空!");
        }

        if(non_model_id_getter.size() != non_model_loader.size()) {
            throw new RuntimeException("注册的 非model 加载器不完整!");
        }

        view_bind_model.values().forEach(model -> {
            if(model_id_getter.get(model) == null) {
                logger.info("元数据校验 => 注册的 model 没有设置加载器! model={}", model.getName());
            }
        });

        // 更精细的校验：类型是否匹配等.
    }


































    /**
     * 两个model组合.
     *
     * @author yanghuan
     */
    public static class TwoModel {

        private Class<?> firstModel;
        private Class<?> secondModel;

        /**
         * Create a new instance
         *
         * @param firstModel model 1
         * @param secondModel model 2
         */
        public TwoModel(Class<?> firstModel, Class<?> secondModel) {
            if(firstModel == null || secondModel == null) {
                throw new ParamIsNullException("model 参数为空");
            }

            this.firstModel = firstModel;
            this.secondModel = secondModel;
        }

        @Override
        public int hashCode() {
            return (firstModel.getName() + "_" + secondModel.getName()).hashCode();
        }

        @Override
        public boolean equals(Object object) {
            if(object == null) {
                return false;
            }

            if(!(object instanceof TwoModel)) {
                return false;
            }

            TwoModel otherTwoModel = (TwoModel) object;
            return firstModel.getName().equals(otherTwoModel.firstModel.getName()) && secondModel.getName().equals(otherTwoModel.secondModel.getName());
        }
    }

    /**
     * view+getter组合.
     *
     * @author yanghuan
     */
    public static class ViewAndGetter {

        private Class<?> view;
        private String getterName;

        /**
         * Create a new instance
         *
         * @param view 视图
         * @param getterName getter方法名称
         */
        public ViewAndGetter(Class<?> view, String getterName) {
            if(view == null || getterName == null) {
                throw new ParamIsNullException("view or getterName 参数为空");
            }

            this.view = view;
            this.getterName = getterName;
        }

        @Override
        public int hashCode() {
            return (view.getName() + "_" + getterName).hashCode();
        }

        @Override
        public boolean equals(Object object) {
            if(object == null) {
                return false;
            }

            if(!(object instanceof ViewAndGetter)) {
                return false;
            }

            ViewAndGetter otherViewAndGetter = (ViewAndGetter) object;
            return view.getName().equals(otherViewAndGetter.view.getName()) && getterName.equals(otherViewAndGetter.getterName);
        }
    }

    /**
     * model+getter组合.
     *
     * @author yanghuan
     */
    public static class ModelAndGetter {

        private Class<?> model;
        private String getterName;

        /**
         * Create a new instance
         *
         * @param model 实体
         * @param getterName getter方法名称
         */
        public ModelAndGetter(Class<?> model, String getterName) {
            if(model == null || getterName == null) {
                throw new ParamIsNullException("model or getterName 参数为空");
            }

            this.model = model;
            this.getterName = getterName;
        }

        @Override
        public int hashCode() {
            return (model.getName() + "_" + getterName).hashCode();
        }

        @Override
        public boolean equals(Object object) {
            if(object == null) {
                return false;
            }

            if(!(object instanceof ModelAndGetter)) {
                return false;
            }

            ModelAndGetter otherModelAndGetter = (ModelAndGetter) object;
            return model.getName().equals(otherModelAndGetter.model.getName()) && getterName.equals(otherModelAndGetter.getterName);
        }
    }

    /**
     * model+getter+指定属性组合.
     *
     * @author yanghuan
     */
    public static class ModelAndGetterAndField {

        private Class<?> model;
        private String getterName;
        private String fieldName;

        /**
         * Create a new instance
         *
         * @param model 实体
         * @param getterName getter方法名称
         * @param fieldName 属性名称
         */
        public ModelAndGetterAndField(Class<?> model, String getterName, String fieldName) {
            if(model == null || getterName == null) {
                throw new ParamIsNullException("model or getterName or fieldName参数为空");
            }

            this.model = model;
            this.getterName = getterName;
            this.fieldName = fieldName;
        }

        @Override
        public int hashCode() {
            return (model.getName() + "_" + getterName + "_" + fieldName).hashCode();
        }

        @Override
        public boolean equals(Object object) {
            if(object == null) {
                return false;
            }

            if(!(object instanceof ModelAndGetterAndField)) {
                return false;
            }

            ModelAndGetterAndField otherModelAndGetterAndField = (ModelAndGetterAndField) object;
            return model.getName().equals(otherModelAndGetterAndField.model.getName())
                    && getterName.equals(otherModelAndGetterAndField.getterName)
                    && fieldName.equals(otherModelAndGetterAndField.fieldName);
        }
    }
}
