package com.github.case2.domain.modelc;

import com.github.case2.domain.modelc.model.ModelC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ModelC 领域
 */
public class ModelCDomainService {

    private static Map<Long, List<ModelC>> modelC_list_db = new HashMap<>();
    static {
        List<ModelC> modelCList = new ArrayList<>();
        ModelC modelC1 = new ModelC();
        modelC1.setId(1L);
        modelC1.setModelAId(1L);
        modelC1.setModelBId(1L);
        modelC1.setDependentModelC(null);
        modelCList.add(modelC1);

        ModelC modelC2 = new ModelC();
        modelC2.setId(2L);
        modelC2.setModelAId(1L);
        modelC2.setModelBId(2L);
        modelC2.setDependentModelC(null);
        modelCList.add(modelC2);

        ModelC modelC3 = new ModelC();
        modelC3.setId(3L);
        modelC3.setModelAId(1L);
        modelC3.setModelBId(3L);
        modelC3.setDependentModelC(null);
        modelCList.add(modelC3);
        modelC_list_db.put(1L, modelCList);
    }

    public Map<Long, List<ModelC>> queryModelCList(List<Long> ids, Integer page, Integer count) {
        if(ids == null || ids.size() == 0) {
            return null;
        }

        Map<Long, List<ModelC>> modelMap = new HashMap<>();
        ids.forEach(id -> {
            modelMap.put(id, modelC_list_db.get(id));
        });
        return modelMap;
    }

    public Map<Long, List<ModelC>> queryModelC2List(List<Long> ids, Integer page, Integer count) {
        return queryModelCList(ids, page, count);
    }
}
