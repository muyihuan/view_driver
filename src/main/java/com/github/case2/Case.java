package com.github.case2;

import com.github.case2.domain.modela.ModelADomainService;
import com.github.case2.domain.modela.model.ModelA;
import com.github.case2.domain.modela.model.ObjectA;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Case {

    private ModelADomainService modelADomainService = new ModelADomainService();

    private ModelBDomainService modelBDomainService = new ModelBDomainService();

    private ModelCDomainService modelCDomainService = new ModelCDomainService();

    private ModelDDomainService modelDDomainService = new ModelDDomainService();

    private ModelEDomainService modelEDomainService = new ModelEDomainService();

    private ModelFDomainService modelFDomainService = new ModelFDomainService();

    public List<ViewA> buildById(List<Long> ids) {
        // 1 ：1 的 查询结果是输入 一组参数 输出 1个model
        // 1.model数据需要通过收集model的id获取
        // 2.model数据需要通过收集参数获取
        // 3.model数据来自已加载的model的属性

        // 1 ：n 的 查询结果是输入 一组参数 输出 n个model
        // 1.model数据需要通过收集参数获取 参数
        // 2.model数据来自已加载的model的属性


        // 通过输入的id查询
        Map<Long, ModelA> modelAMap = modelADomainService.batchGetModelAs(ids);
        Map<Long, ViewA> viewAMap = new HashMap<>();

        // ViewA存在自己依赖自己所以也需要参与构建，大多数业务只有一层依赖，目前只支持一层依赖处理
        List<ModelA> copy = new ArrayList<>(modelAMap.values());
        modelAMap.values().forEach(modelA -> {
            if(modelA.getId() != null) {
//                copy.add(0, modelA.getId());
            }
        });

        List<Long> modelAIds = copy.stream().map(ModelA::getId).collect(Collectors.toList());
        List<Long> modelBIds = copy.stream().map(ModelA::getModelBId).collect(Collectors.toList());

        // 通过收集的modelAIds获取
        Map<Long, List<ModelC>> modelCListMap = modelCDomainService.queryModelCList(modelAIds, 0,10);
        modelCListMap.values().forEach(modelCS -> {
            modelCS.forEach(modelC -> {
                modelBIds.add(modelC.getModelBId());
            });
        });

        List<Long> modelDIds = new ArrayList<>();
        copy.forEach(modelA -> {
            modelDIds.addAll(modelA.getModelDIdList());
        });

        // 通过ModelA::getModelBId和modelC.getModelBId()收集的modelBIds获取
        Map<Long, ModelB> modelBMap = modelBDomainService.batchGetModelBs(modelBIds);
        // 通过ModelA::getModelDIdList收集的modelDIds获取
        Map<Long, ModelD> modelDMap = modelDDomainService.batchGetModelDs(modelDIds);
        // 通过ModelA::getModelBId和modelC.getModelBId()收集的modelBIds获取
        Map<Long, ModelF> modelFMap = modelFDomainService.batchGetModelFs(modelBIds);
        // 通过收集的modelAIds获取
        Map<Long, ModelE> modelEMap = modelEDomainService.batchGetModelEs(modelAIds);

        for(ModelA modelA : copy) {
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

//                ModelC modelC1 = modelC.getSourceModelCId();
//                if( != null) {
//                    ViewC dependentViewC = new ViewC();
//                    dependentViewC.setViewCId(modelC.getDependentModelC().toString());
//
//                    ModelB modelB2 = modelBMap.get(modelC.getDependentModelC().getModelBId());
//                    ViewB viewB2 = new ViewB();
//                    viewB2.setViewBId(modelB2.getId().toString());
//                    viewB2.setInnerAttributeBa(modelB2.getInnerAttributeBa());
//                    ModelF modelF2 = modelFMap.get(modelB2.getId());
//                    ViewF viewF2 = new ViewF();
//                    viewF1.setInnerAttributeFa(modelF2.getInnerAttributeFa());
//                    viewF1.setInnerAttributeFb(modelF2.getInnerAttributeFb());
//                    viewB1.setViewF(viewF2);
//                    dependentViewC.setViewB(viewB2);
//                    dependentViewC.setDependentViewC(null);
//                    viewC.setDependentViewC(dependentViewC);
//                }

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
