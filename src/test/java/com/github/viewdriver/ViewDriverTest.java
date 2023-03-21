package com.github.viewdriver;

import com.github.viewdriver.builder.FieldBinder;
import com.github.viewdriver.builder.IdBinder;
import com.github.viewdriver.builder.ViewDriverBuilder;
import com.github.viewdriver.driver.Config;
import com.github.viewdriver.driver.DefViewDriver;
import com.github.viewdriver.driver.metadata.ViewDriverMetaData;
import com.github.viewdriver.driver.tree.ViewTree;
import com.github.viewdriver.driver.tree.ViewTreeParser;
import com.github.viewdriver.lambda.FieldGetter;
import com.github.views.domain.modela.ModelADomainService;
import com.github.views.domain.modela.model.ModelA;
import com.github.views.domain.modelb.ModelBDomainService;
import com.github.views.domain.modelb.model.ModelB;
import com.github.views.domain.modelc.ModelCDomainService;
import com.github.views.domain.modelc.model.ModelC;
import com.github.views.domain.modeld.ModelDDomainService;
import com.github.views.domain.modeld.model.ModelD;
import com.github.views.domain.modele.ModelEDomainService;
import com.github.views.domain.modele.model.ModelE;
import com.github.views.domain.modelf.ModelFDomainService;
import com.github.views.domain.modelf.model.ModelF;
import com.github.views.domain.modelg.ModelGDomainService;
import com.github.views.domain.modelg.model.ModelG;
import com.github.views.view.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 单元测试.
 *
 * @author yanghuan
 */
@SuppressWarnings("all")
class ViewDriverTest {

    private static ViewDriverBuilder viewDriverBuilder = new ViewDriverBuilder();
    private static ModelADomainService modelADomainService = new ModelADomainService();
    private static ModelBDomainService modelBDomainService = new ModelBDomainService();
    private static ModelCDomainService modelCDomainService = new ModelCDomainService();
    private static ModelDDomainService modelDDomainService = new ModelDDomainService();
    private static ModelEDomainService modelEDomainService = new ModelEDomainService();
    private static ModelFDomainService modelFDomainService = new ModelFDomainService();
    private static ModelGDomainService modelGDomainService = new ModelGDomainService();

    static {
        viewDriverBuilder
                .viewBindModel(ViewA.class, ModelA.class, new FieldBinder<ViewA, ModelA>().bind(ViewA::getViewAId, ModelA::getId, (r) -> "自定义转化"))
                .viewBindModel(ViewB.class, ModelB.class, new FieldBinder<ViewB, ModelB>().bind(ViewB::getViewBId, ModelB::getId, String::valueOf))
                .viewBindModel(ViewC.class, ModelC.class, new FieldBinder<ViewC, ModelC>().bind(ViewC::getViewCId, ModelC::getId, String::valueOf))
                .viewBindModel(ViewD.class, ModelD.class, new FieldBinder<ViewD, ModelD>().bind(ViewD::getViewDId, ModelD::getId, String::valueOf))
                .viewBindModel(ViewE.class, ModelE.class)
                .viewBindModel(ViewF.class, ModelF.class)
                .viewBindModel(ViewG.class, ModelG.class)
                .modelIdBind(ModelA.class, new IdBinder<ModelA>()
                        .bind(ModelA::getId, ModelA.class)
                        .bind(ModelA::getModelBId, ModelB.class)
                        .bind(ModelA::getModelDIdList, ModelD.class)
                        .bind(ModelA::getSourceModelAId, ModelA.class)
                        .bind(ModelA::getId, ModelE.class)
                        .bind(new FieldGetter<ModelA, Object>() {
                            @Override
                            public String getClassName() {
                                return ModelA.class.getName();
                            }
                            @Override
                            public String getMethodName() {
                                return "getObjectB";
                            }
                            @Override
                            public Object apply(ModelA modelA) {
                                return modelA.getObjectB() == null ? null : modelA.getObjectB().getModelGId();
                            }
                        }, ModelG.class))
                .modelIdBind(ModelB.class, new IdBinder<ModelB>()
                        .bind(ModelB::getId, ModelB.class)
                        .bind(ModelB::getId, ModelF.class))
                .modelIdBind(ModelC.class, new IdBinder<ModelC>()
                        .bind(ModelC::getId, ModelC.class)
                        .bind(ModelC::getModelAId, ModelA.class)
                        .bind(ModelC::getModelBId, ModelB.class)
                        .bind(ModelC::getSourceModelCId, ModelC.class))
                .modelIdBind(ModelD.class, new IdBinder<ModelD>()
                        .bind(ModelD::getId, ModelD.class))
                .modelIdBind(ModelE.class, new IdBinder<ModelE>()
                        .bind(ModelE::getModelAId, ModelE.class))
                .modelIdBind(ModelF.class, new IdBinder<ModelF>()
                        .bind(ModelF::getModelBId, ModelF.class))
                .modelLoaderById(ModelA.class, (ids, context) -> modelADomainService.batchGetModelAs(ids))
                .modelLoaderById(ModelB.class, (ids, context) -> modelBDomainService.batchGetModelBs(ids))
                .modelLoaderById(ModelC.class, (ids, context) -> modelCDomainService.batchGetModelCs(ids))
                .modelLoaderById(ModelD.class, (ids, context) -> modelDDomainService.batchGetModelDs(ids))
                .modelLoaderById(ModelE.class, (ids, context) -> modelEDomainService.batchGetModelEs(ids))
                .modelLoaderById(ModelF.class, (ids, context) -> modelFDomainService.batchGetModelFs(ids))
                .modelLoaderById(ModelG.class, (ids, context) -> modelGDomainService.batchGetModelGs(ids))
                .modelLoaderByOuterId(ModelC.class, ModelC::getModelAId, (ids, context) -> modelCDomainService.queryModelCList(ids, context.getInteger("page"), context.getInteger("count")))
                .modelLoaderByOuterId(ModelC.class, ModelC::getModelAId, (ids, context) -> modelCDomainService.queryModelC2List(ids, context.getInteger("page"), context.getInteger("count")), ViewA::getViewC2List)
                .nonModelLoader(ViewA::getOuterAttributeAf, ModelA::getId, (ids, context) -> modelADomainService.batchGetOuterObject(ids))
                .filter(ModelA.class, (modelA, context) -> modelA != null)
                .config(new Config());
    }

    @Test
    void testMeta() throws Exception {
        System.out.println("元数据注册单元测试开始 _");

        Field driverMetaDataField = ViewDriverBuilder.class.getDeclaredField("driverMetaData");
        driverMetaDataField.setAccessible(true);
        ViewDriverMetaData driverMetaData = (ViewDriverMetaData) driverMetaDataField.get(viewDriverBuilder);

        checkViewBindModel(driverMetaData, ViewA.class, ModelA.class, ViewA::getViewAId, ModelA::getId, (r) -> "自定义转化", 2L, "自定义转化");
        checkViewBindModel(driverMetaData, ViewB.class, ModelB.class, ViewB::getViewBId, ModelB::getId, String::valueOf, 1L, "1");
        checkViewBindModel(driverMetaData, ViewC.class, ModelC.class, ViewC::getViewCId, ModelC::getId, String::valueOf, 1L, "1");
        checkViewBindModel(driverMetaData, ViewD.class, ModelD.class, ViewD::getViewDId, ModelD::getId, String::valueOf, 1L, "1");
        checkViewBindModel(driverMetaData, ViewE.class, ModelE.class);
        checkViewBindModel(driverMetaData, ViewF.class, ModelF.class);
        checkViewBindModel(driverMetaData, ViewG.class, ModelG.class);
        checkModelIdBind(driverMetaData, ModelA.class,  true, ModelB.class, "getModelBId", ModelD.class, "getModelDIdList", ModelA.class, "getSourceModelAId", ModelE.class, "getId", ModelG.class, "getObjectB");
        checkModelIdBind(driverMetaData, ModelB.class,  true, ModelF.class, "getId");
        checkModelIdBind(driverMetaData, ModelC.class,  true, ModelA.class, "getModelAId", ModelB.class, "getModelBId", ModelC.class, "getSourceModelCId");
        checkModelIdBind(driverMetaData, ModelD.class,  true);
        checkModelIdBind(driverMetaData, ModelE.class,  true);
        checkModelIdBind(driverMetaData, ModelF.class,  true);
        checkModelLoaderById(driverMetaData, ModelA.class, (ids, context) -> modelADomainService.batchGetModelAs(ids), Collections.singletonList(1L));
        checkModelLoaderById(driverMetaData, ModelB.class, (ids, context) -> modelBDomainService.batchGetModelBs(ids), Collections.singletonList(1L));
        checkModelLoaderById(driverMetaData, ModelC.class, (ids, context) -> modelCDomainService.batchGetModelCs(ids), Collections.singletonList(1L));
        checkModelLoaderById(driverMetaData, ModelD.class, (ids, context) -> modelDDomainService.batchGetModelDs(ids), Collections.singletonList(1L));
        checkModelLoaderById(driverMetaData, ModelE.class, (ids, context) -> modelEDomainService.batchGetModelEs(ids), Collections.singletonList(1L));
        checkModelLoaderById(driverMetaData, ModelF.class, (ids, context) -> modelFDomainService.batchGetModelFs(ids), Collections.singletonList(1L));
        checkModelLoaderById(driverMetaData, ModelG.class, (ids, context) -> modelGDomainService.batchGetModelGs(ids), Collections.singletonList(1L));
        checkModelLoaderByOuterId(driverMetaData, ModelC.class, ModelC::getModelAId, (ids, context) -> modelCDomainService.queryModelCList(ids, context.getInteger("page"), context.getInteger("count")), null, Collections.singletonList(1L));
        checkModelLoaderByOuterId(driverMetaData, ModelC.class, ModelC::getModelAId, (ids, context) -> modelCDomainService.queryModelC2List(ids, context.getInteger("page"), context.getInteger("count")), "ViewC2List".toLowerCase(), Collections.singletonList(1L));
        checkNonModelLoader(driverMetaData, ViewA.class, ViewA::getOuterAttributeAf, ModelA::getId, (ids, context) -> modelADomainService.batchGetOuterObject(ids), Collections.singletonList(1L));

        System.out.println("元数据注册单元测试结束 <");
    }

    @Test
    void testViewTree() throws Exception {
        Field driverMetaDataField = ViewDriverBuilder.class.getDeclaredField("driverMetaData");
        driverMetaDataField.setAccessible(true);
        ViewDriverMetaData driverMetaData = (ViewDriverMetaData) driverMetaDataField.get(viewDriverBuilder);

        ViewTreeParser viewTreeParser = new ViewTreeParser(driverMetaData);
        ViewTree viewATree = viewTreeParser.generateViewTree(ViewA.class);
        ViewTree viewBTree = viewTreeParser.generateViewTree(ViewB.class);
        ViewTree viewCTree = viewTreeParser.generateViewTree(ViewC.class);
        ViewTree viewDTree = viewTreeParser.generateViewTree(ViewD.class);
        ViewTree viewETree = viewTreeParser.generateViewTree(ViewE.class);
        ViewTree viewFTree = viewTreeParser.generateViewTree(ViewF.class);
        ViewTree viewGTree = viewTreeParser.generateViewTree(ViewG.class);

    }

    @Test
    void testLoadModel() throws Exception {
        Field driverMetaDataField = ViewDriverBuilder.class.getDeclaredField("driverMetaData");
        driverMetaDataField.setAccessible(true);
        ViewDriverMetaData driverMetaData = (ViewDriverMetaData) driverMetaDataField.get(viewDriverBuilder);

        DefViewDriver viewDriver = new DefViewDriver(driverMetaData, new Config());
    }

    @Test
    void testMapView() {

    }










    private <V, M, O, I> void checkViewBindModel(ViewDriverMetaData driverMetaData, Class<V> view, Class<M> model, FieldGetter<V, O> viewFieldGetter, FieldGetter<M, I> modelFieldGetter, Function<I, O> decorator, I argument, I expect) {
        Class modelClass = driverMetaData.view_bind_model.get(view);
        if(!model.equals(modelClass)) {
            System.out.println("发现问题=> viewBindModel check fail " + view.getSimpleName() + " <=> " + model.getSimpleName() + ".");
        }

        if(viewFieldGetter != null && modelFieldGetter != null) {
            ViewDriverMetaData.ViewAndGetter viewAndGetter = new ViewDriverMetaData.ViewAndGetter(view, viewFieldGetter.getMethodName());
            FieldGetter fieldGetter = driverMetaData.field_getter_bind.get(viewAndGetter);
            if(fieldGetter == null || !fieldGetter.getClassName().equals(model.getName()) || !fieldGetter.getMethodName().equals(modelFieldGetter.getMethodName())) {
                System.out.println("发现问题=> field_getter_bind check fail " + view.getSimpleName() + "::" + viewFieldGetter.getMethodName() + " <=> " + model.getSimpleName() + "::" + modelFieldGetter.getMethodName() + ".");
            }

            if(decorator != null) {
                Function<I, O> function = driverMetaData.field_decorator.get(viewAndGetter);
                if(function == null) {
                    System.out.println("发现问题=> field_getter_bind check fail null " + view.getSimpleName() + "::" + viewFieldGetter.getMethodName() + " <=> " + model.getSimpleName() + "::" + modelFieldGetter.getMethodName() + ".");
                }
                else {
                    Object temp = new Object();
                    Object value1 = function.apply(argument);
                    value1 = value1 == null ? temp : value1;
                    Object value2 = decorator.apply(argument);
                    value2 = value2 == null ? temp : value2;
                    if(!value1.equals(value2) || !value1.equals(expect)) {
                        System.out.println("发现问题=> field_decorator check fail " + view.getSimpleName() + "::" + viewFieldGetter.getMethodName() + " <=> " + model.getSimpleName() + "::" + modelFieldGetter.getMethodName() + ".");
                    }
                }
            }
        }
    }

    private <V, M, O, I> void checkViewBindModel(ViewDriverMetaData driverMetaData, Class<V> view, Class<M> model) {
        Class modelClass = driverMetaData.view_bind_model.get(view);
        if(!model.equals(modelClass)) {
            System.out.println("发现问题=> viewBindModel check fail " + view.getSimpleName() + " <=> " + model.getSimpleName() + ".");
        }
    }

    private <M> void checkModelIdBind(ViewDriverMetaData driverMetaData, Class<M> model, boolean hasId, Object ... binds) {
        Function function = driverMetaData.model_id_getter.get(model);
        if(hasId && function == null) {
            System.out.println("发现问题=> model_id_getter check fail " + model.getSimpleName() + ".");
        }

        if(binds != null && binds.length > 0) {
            Class bind_model = null;
            for(Object object : binds) {
                if(bind_model == null) {
                    bind_model = (Class) object;
                    continue;
                }

                ViewDriverMetaData.TwoModel twoModel = new ViewDriverMetaData.TwoModel(model, bind_model);
                FieldGetter fieldGetter = driverMetaData.model_relation_by_outer_id.get(twoModel);
                String getter = (String) object;
                if(fieldGetter == null || !getter.equals(fieldGetter.getMethodName())) {
                    System.out.println("发现问题=> model_relation_by_outer_id check fail " + model.getSimpleName() + " <=> " + bind_model.getSimpleName() + ".");
                }

                bind_model = null;
            }
        }
    }

    private <M, P> void checkModelLoaderById(ViewDriverMetaData driverMetaData, Class<M> model, BiFunction<List<P>, Context, Map<P, M>> expect, List<P> arguments) {
        BiFunction<List<P>, Context, Map<P, M>> loader = driverMetaData.model_loader_by_id.get(model);
        if(loader == null) {
            System.out.println("发现问题=> model_loader_by_id check fail " + model.getSimpleName() + ".");
        }

        Map temp = new HashMap();
        Map map1 = loader.apply(arguments, null);
        map1 = map1 == null ? temp : map1;
        Map map2 = expect.apply(arguments, null);
        map2 = map2 == null ? temp : map2;
        Map finalMap = map1;
        map2.forEach((key, value) -> {
            if(!finalMap.containsKey(key) || !finalMap.get(key).equals(value)) {
                System.out.println("发现问题=> model_loader_by_id check not expect " + model.getSimpleName() + ".");
            }
        });
    }

    private <M, I> void checkModelLoaderByOuterId(ViewDriverMetaData driverMetaData, Class<M> model, FieldGetter<M, I> outerId, BiFunction<List<I>, Context, Map<I, List<M>>> expect, String bindViewAttribute, List<I> arguments) {
        if(bindViewAttribute == null) {
            ViewDriverMetaData.ModelAndGetter modelAndGetter = new ViewDriverMetaData.ModelAndGetter(model, outerId.getMethodName());
            BiFunction<List<I>, Context, Map<I, List<M>>> loader = driverMetaData.model_loader_by_outer_id.get(modelAndGetter);
            Map temp = new HashMap();
            Map map1 = loader.apply(arguments, new Context());
            map1 = map1 == null ? temp : map1;
            Map map2 = expect.apply(arguments, new Context());
            map2 = map2 == null ? temp : map2;
            Map finalMap = map1;
            map2.forEach((key, value) -> {
                if(!finalMap.containsKey(key) || !finalMap.get(key).equals(value)) {
                    System.out.println("发现问题=> model_loader_by_outer_id check not expect " + model.getSimpleName() + ".");
                }
            });
        }
        else {
            bindViewAttribute = bindViewAttribute.toLowerCase();
            ViewDriverMetaData.ModelAndGetterAndField modelAndGetterAndField = new ViewDriverMetaData.ModelAndGetterAndField(model, outerId.getMethodName(), bindViewAttribute);
            BiFunction<List<I>, Context, Map<I, List<M>>> loader = driverMetaData.model_loader_by_outer_id_bind_field.get(modelAndGetterAndField);
            Map temp = new HashMap();
            Map map1 = loader.apply(arguments, new Context());
            map1 = map1 == null ? temp : map1;
            Map map2 = expect.apply(arguments, new Context());
            map2 = map2 == null ? temp : map2;
            Map finalMap = map1;
            map2.forEach((key, value) -> {
                if(!finalMap.containsKey(key) || !finalMap.get(key).equals(value)) {
                    System.out.println("发现问题=> model_loader_by_outer_id_bind_field check not expect " + model.getSimpleName() + ".");
                }
            });
        }
    }

    private <V, M, A, I> void checkNonModelLoader(ViewDriverMetaData driverMetaData, Class<V> view, FieldGetter<V, A> viewAttribute, FieldGetter<M, I> idGetter, BiFunction<List<I>, Context, Map<I, A>> expect, List<I> arguments) {
        ViewDriverMetaData.ViewAndGetter viewAndGetter = new ViewDriverMetaData.ViewAndGetter(view, viewAttribute.getMethodName());
        FieldGetter fieldGetter = driverMetaData.non_model_id_getter.get(viewAndGetter);
        if(fieldGetter == null) {
            System.out.println("发现问题=> non_model_id_getter check fail " + view.getSimpleName() + "::" + viewAttribute.getMethodName() + ".");
            return;
        }
        else {
            if(!fieldGetter.getClassName().equals(idGetter.getClassName()) || !fieldGetter.getMethodName().equals(idGetter.getMethodName())) {
                System.out.println("发现问题=> non_model_id_getter check not expect " + view.getSimpleName() + "::" + viewAttribute.getMethodName() + ".");
            }
        }

        BiFunction<List<I>, Context, Map<I, A>> loader = driverMetaData.non_model_loader.get(viewAndGetter);
        if(loader == null) {
            System.out.println("发现问题=> non_model_loader check fail loader is null " + view.getSimpleName() + "::" + viewAttribute.getMethodName() + ".");
        }

        Map temp = new HashMap();
        Map map1 = loader.apply(arguments, new Context());
        map1 = map1 == null ? temp : map1;
        Map map2 = expect.apply(arguments, new Context());
        map2 = map2 == null ? temp : map2;
        Map finalMap = map1;
        map2.forEach((key, value) -> {
            if(!finalMap.containsKey(key) || !finalMap.get(key).equals(value)) {
                System.out.println("发现问题=> non_model_loader check not expect " + view.getSimpleName() + "::" + viewAttribute.getMethodName() + ".");
            }
        });
    }
}
