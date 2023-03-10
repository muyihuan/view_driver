package com.github.views;

import com.github.views.domain.modela.ModelADomainService;
import com.github.views.domain.modela.model.ModelA;
import com.github.views.domain.modela.model.ObjectA;
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
import com.github.views.view.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 这里展示的是非视图驱动如何加载和渲染View的流程.
 *
 * @author yanghuan
 */
public class Case {

    private ModelADomainService modelADomainService = new ModelADomainService();
    private ModelBDomainService modelBDomainService = new ModelBDomainService();
    private ModelCDomainService modelCDomainService = new ModelCDomainService();
    private ModelDDomainService modelDDomainService = new ModelDDomainService();
    private ModelEDomainService modelEDomainService = new ModelEDomainService();
    private ModelFDomainService modelFDomainService = new ModelFDomainService();

    /**
     * 这里只是演示，不考虑代码可执行程度.
     *
     * @param ids modelA的id集合.
     * @return 渲染好的ViewA集合.
     */
    public List<ViewA> mapView(List<Long> ids) {
        Map<Long, ModelA> modelAMap = modelADomainService.batchGetModelAs(ids);

        List<Long> collectModelAIds = new ArrayList<>();
        modelAMap.values().forEach(modelA -> {
            if(modelA.getSourceModelAId() != null) {
                collectModelAIds.add(modelA.getSourceModelAId());
            }
        });

        Map<Long, ModelA> innerModelAMap = modelADomainService.batchGetModelAs(collectModelAIds);
        List<ModelA> modelAList = new ArrayList<>(modelAMap.values());
        // 被内部依赖的需要放到前面，提前被渲染.
        modelAList.addAll(0, innerModelAMap.values());

        List<Long> modelAIds = modelAList.stream().map(ModelA::getId).collect(Collectors.toList());
        List<Long> collectModelBIds = modelAList.stream().map(ModelA::getModelBId).collect(Collectors.toList());

        Map<Long, ModelC> modelCMap = new HashMap<>();
        Map<Long, List<ModelC>> modelCListMap = modelCDomainService.queryModelCList(modelAIds, 0,10);
        modelCListMap.values().forEach(modelCS -> {
            modelCS.forEach(modelC -> {
                collectModelBIds.add(modelC.getModelBId());
                modelCMap.put(modelC.getId(), modelC);
            });
        });

        List<Long> collectModelDIds = new ArrayList<>();
        modelAList.forEach(modelA -> {
            collectModelDIds.addAll(modelA.getModelDIdList());
        });

        Map<Long, ModelB> modelBMap = modelBDomainService.batchGetModelBs(collectModelBIds);
        Map<Long, ModelD> modelDMap = modelDDomainService.batchGetModelDs(collectModelDIds);
        Map<Long, ModelF> modelFMap = modelFDomainService.batchGetModelFs(collectModelBIds);
        Map<Long, ModelE> modelEMap = modelEDomainService.batchGetModelEs(modelAIds);

        Map<Long, ViewA> viewAMap = new HashMap<>();
        for(ModelA modelA : modelAList) {
            ViewA viewA = new ViewA();

            // id转化为String
            viewA.setViewAId(String.valueOf(modelA.getId()));

            // 构建内部attributeAa
            ObjectA innerAttributeAa = new ObjectA();
            innerAttributeAa.setOa(modelA.getInnerAttributeAa().getOa());
            innerAttributeAa.setOb(modelA.getInnerAttributeAa().getOa());
            viewA.setInnerAttributeAa(innerAttributeAa);

            // 构建外部查询属性
            viewA.setOuterAttributeAf(null);
            viewA.setOuterAttributeAgs(null);

            // 构建ViewB
            ViewB viewB = new ViewB();
            ModelB modelB = modelBMap.get(modelA.getModelBId());
            viewB.setViewBId(String.valueOf(modelB.getId()));
            viewB.setInnerAttributeBa(modelB.getInnerAttributeBa());
            ModelF modelF = modelFMap.get(modelB.getId());
            ViewF viewF = new ViewF();
            viewF.setInnerAttributeFa(modelF.getInnerAttributeFa());
            viewF.setInnerAttributeFb(modelF.getInnerAttributeFb());
            viewB.setViewF(viewF);
            viewA.setViewB(viewB);

            // 构建ViewE
            ModelE modelE = modelEMap.get(modelA.getId());
            ViewE viewE = new ViewE();
            viewE.setInnerAttributeEa(modelE.getInnerAttributeEa());
            viewE.setInnerAttributeEb(modelE.getInnerAttributeEb());
            viewA.setViewE(viewE);

            // 构建viewCList
            List<ViewC> viewCList = new ArrayList<>();
            modelCListMap.get(modelA.getId()).forEach(modelC -> {
                ViewC viewC = new ViewC();
                viewC.setViewCId(String.valueOf(modelC.getId()));

                ModelB modelB1 = modelBMap.get(modelC.getModelBId());
                ViewB viewB1 = new ViewB();
                viewB1.setViewBId(modelB1.getId().toString());
                viewB1.setInnerAttributeBa(modelB1.getInnerAttributeBa());
                ModelF modelF1 = modelFMap.get(modelB1.getId());
                ViewF viewF1 = new ViewF();
                viewF1.setInnerAttributeFa(modelF1.getInnerAttributeFa());
                viewF1.setInnerAttributeFb(modelF1.getInnerAttributeFb());
                viewB1.setViewF(viewF1);
                viewC.setViewB(viewB1);

                Long innerModelCId = modelC.getSourceModelCId();
                ModelC innerModelC = modelCMap.get(innerModelCId);
                if(innerModelC != null) {
                    ViewC dependentViewC = new ViewC();
                    dependentViewC.setViewCId(modelC.getSourceModelCId().toString());

                    ModelB modelB2 = modelBMap.get(innerModelC.getModelBId());
                    ViewB viewB2 = new ViewB();
                    viewB2.setViewBId(modelB2.getId().toString());
                    viewB2.setInnerAttributeBa(modelB2.getInnerAttributeBa());
                    ModelF modelF2 = modelFMap.get(modelB2.getId());
                    ViewF viewF2 = new ViewF();
                    viewF1.setInnerAttributeFa(modelF2.getInnerAttributeFa());
                    viewF1.setInnerAttributeFb(modelF2.getInnerAttributeFb());
                    viewB1.setViewF(viewF2);
                    dependentViewC.setViewB(viewB2);
                    dependentViewC.setDependentViewC(null);
                    viewC.setDependentViewC(dependentViewC);
                }

                viewCList.add(viewC);
            });
            viewA.setViewCList(viewCList);

            // 构建viewDList
            List<ViewD> viewDList = new ArrayList<>();
            modelA.getModelDIdList().forEach(id -> {
                ModelD modelD = modelDMap.get(id);
                ViewD viewD = new ViewD();
                viewD.setViewDId(String.valueOf(modelD.getId()));
                viewD.setInnerAttributeDa(modelD.getInnerAttributeDa());
                viewDList.add(viewD);
            });
            viewA.setViewDList(viewDList);

            // 构建DependentViewA
            viewA.setDependentViewA(viewAMap.get(modelA.getId()));

            viewAMap.put(modelA.getId(), viewA);
        }

        return new ArrayList<>(viewAMap.values());
    }
}
