package com.github.viewdriver;

import com.github.case2.domain.modela.ModelADomainService;
import com.github.case2.domain.modela.model.ModelA;
import com.github.case2.domain.modelb.ModelBDomainService;
import com.github.case2.domain.modelb.model.ModelB;
import com.github.case2.domain.modelc.ModelCDomainService;
import com.github.case2.domain.modelc.model.ModelC;
import com.github.case2.domain.modeld.ModelDDomainService;
import com.github.case2.domain.modeld.model.ModelD;
import com.github.case2.domain.modele.ModelEDomainService;
import com.github.case2.domain.modele.model.ModelE;
import com.github.case2.domain.modelf.ModelFDomainService;
import com.github.case2.domain.modelf.model.ModelF;
import com.github.case2.view.*;
import com.github.viewdriver.builder.FieldBinder;
import com.github.viewdriver.builder.IdBinder;
import com.github.viewdriver.builder.ViewDriverBuilder;
import com.github.viewdriver.driver.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 使用示例.
 *
 * @author yanghuan
 */
public class Demo {

    /**
     * 全局视图驱动器
     */
    private static ViewDriver defViewDriver = null;

    private static ModelADomainService modelADomainService = new ModelADomainService();
    private static ModelBDomainService modelBDomainService = new ModelBDomainService();
    private static ModelCDomainService modelCDomainService = new ModelCDomainService();
    private static ModelDDomainService modelDDomainService = new ModelDDomainService();
    private static ModelEDomainService modelEDomainService = new ModelEDomainService();
    private static ModelFDomainService modelFDomainService = new ModelFDomainService();

    static {
        // 构建全局视图驱动器
        defViewDriver = new ViewDriverBuilder()
                .viewBindModel(ViewA.class, ModelA.class, new FieldBinder<ViewA, ModelA>()
                        .bind(ViewA::getViewAId, ModelA::getId, String::valueOf))
                .viewBindModel(ViewB.class, ModelB.class, new FieldBinder<ViewB, ModelB>()
                        .bind(ViewB::getViewBId, ModelB::getId, String::valueOf))
                .viewBindModel(ViewC.class, ModelC.class, new FieldBinder<ViewC, ModelC>()
                        .bind(ViewC::getViewCId, ModelC::getId, String::valueOf))
                .viewBindModel(ViewD.class, ModelD.class, new FieldBinder<ViewD, ModelD>()
                        .bind(ViewD::getViewDId, ModelD::getId, String::valueOf))
                .viewBindModel(ViewE.class, ModelE.class)
                .viewBindModel(ViewF.class, ModelF.class)
                .modelIdBind(ModelA.class, new IdBinder<ModelA>()
                        .bind(ModelA::getId, ModelA.class)
                        .bind(ModelA::getModelBId, ModelB.class)
                        .bind(ModelA::getModelDIdList, ModelC.class))
                .modelIdBind(ModelB.class, new IdBinder<ModelB>()
                        .bind(ModelB::getId, ModelB.class))
                .modelIdBind(ModelC.class, new IdBinder<ModelC>()
                        .bind(ModelC::getId, ModelC.class)
                        .bind(ModelC::getModelAId, ModelA.class)
                        .bind(ModelC::getModelBId, ModelB.class))
                .modelIdBind(ModelD.class, new IdBinder<ModelD>()
                        .bind(ModelD::getId, ModelD.class))
                .modelIdBind(ModelE.class, new IdBinder<ModelE>()
                        .bind(ModelE::getModelAId, ModelA.class))
                .modelIdBind(ModelF.class, new IdBinder<ModelF>()
                        .bind(ModelF::getModelBId, ModelB.class))
                .modelLoaderById(ModelA.class, (ids, context) -> modelADomainService.batchGetModelAs(ids))
                .modelLoaderById(ModelB.class, (ids, context) -> modelBDomainService.batchGetModelBs(ids))
                .modelLoaderById(ModelD.class, (ids, context) -> modelDDomainService.batchGetModelDs(ids))
                .modelLoaderByOuterId(ModelC.class, ModelC::getModelAId, (ids, context) -> modelCDomainService.queryModelCList(ids, (Integer) context.get("page"), (Integer) context.get("count")))
                .modelLoaderByOuterId(ModelE.class, ModelE::getModelAId, (ids, context) -> modelEDomainService.batchGetModelEs(ids))
                .modelLoaderByOuterId(ModelF.class, ModelF::getModelBId, (ids, context) -> modelFDomainService.batchGetModelFs(ids))
                .nonModelLoader(ViewA::getOuterAttributeAf, ModelA::getId, (ids, context) -> Collections.emptyMap())
                .filter(ModelA.class, (modelA, context) -> modelA != null)
                .config(new Config())
                .build();
    }

    public static void main(String[] args) throws Exception {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        List<ViewA> viewAList = defViewDriver.mapView(ids, ViewA.class, null);

        List<ModelB> modelBList = new ArrayList<>();
        modelBList.add(new ModelB());
        List<ViewB> viewBList = defViewDriver.mapView(modelBList, ViewB.class, null);
    }
}
