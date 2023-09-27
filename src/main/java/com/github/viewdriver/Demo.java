package com.github.viewdriver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.viewdriver.builder.FieldBinder;
import com.github.viewdriver.builder.IdBinder;
import com.github.viewdriver.builder.ViewDriverBuilder;
import com.github.viewdriver.driver.Config;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 使用示例.
 *
 * @author yanghuan
 */
@SuppressWarnings("all")
public class Demo {

    /**
     * 全局视图驱动器.
     */
    private static ViewDriver defViewDriver;
    private static ObjectMapper objectMapper = new ObjectMapper();

    private static ModelADomainService modelADomainService = new ModelADomainService();
    private static ModelBDomainService modelBDomainService = new ModelBDomainService();
    private static ModelCDomainService modelCDomainService = new ModelCDomainService();
    private static ModelDDomainService modelDDomainService = new ModelDDomainService();
    private static ModelEDomainService modelEDomainService = new ModelEDomainService();
    private static ModelFDomainService modelFDomainService = new ModelFDomainService();
    private static ModelGDomainService modelGDomainService = new ModelGDomainService();

    static {
        // 构建全局视图驱动器.
        defViewDriver = new ViewDriverBuilder()
                .viewBindModel(ViewA.class, ModelA.class, new FieldBinder<ViewA, ModelA>()
                        .bind(ViewA::getViewAId, ModelA::getId, (r) -> r + " 可被 -> 自定义转化、脱敏、加密等"))
                .viewBindModel(ViewB.class, ModelB.class, new FieldBinder<ViewB, ModelB>()
                        .bind(ViewB::getViewBId, ModelB::getId, String::valueOf))
                .viewBindModel(ViewC.class, ModelC.class, new FieldBinder<ViewC, ModelC>()
                        .bind(ViewC::getViewCId, ModelC::getId, String::valueOf))
                .viewBindModel(ViewD.class, ModelD.class, new FieldBinder<ViewD, ModelD>()
                        .bind(ViewD::getViewDId, ModelD::getId, String::valueOf))
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
                .config(new Config())
                .build();
    }

    public static void main(String[] args) throws Exception {
        Context context = new Context();

        // 输入为id.
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        ids.add(3L);
        for(int i = 0; i < 100; i ++) {
            long start = System.nanoTime();
            List<ViewA> viewAList = defViewDriver.mapView(ids, ViewA.class, context);
            System.out.println("time cost = " + (System.nanoTime() - start) / 1000000.0 + "ms");
            System.out.println("ViewA视图 -> json");
            System.out.println(objectMapper.writeValueAsString(viewAList));
        }

        // 输入为model.
        List<ModelA> modelAList = new ArrayList<>(modelADomainService.batchGetModelAs(Arrays.asList(1L, 2L)).values());
        List<ViewA> viewAList = defViewDriver.mapView(modelAList, ViewA.class, context);
        System.out.println("ViewA视图 -> json");
        System.out.println(objectMapper.writeValueAsString(viewAList));
    }
}
