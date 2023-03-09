package com.github.case2.domain.modelc;

import com.github.case2.domain.modelc.model.ModelC;

import java.util.*;

/**
 * ModelC 领域
 */
public class ModelCDomainService {

    private static Map<Long, List<ModelC>> modelC_list_db = new HashMap<>();
    private static Map<Long, ModelC> modelC_db = new HashMap<>();
    static {
        List<ModelC> modelCList = new ArrayList<>();
        ModelC modelC1 = new ModelC();
        modelC1.setId(1L);
        modelC1.setModelAId(1L);
        modelC1.setModelBId(1L);
        modelC1.setSourceModelCId(null);
        modelCList.add(modelC1);

        ModelC modelC2 = new ModelC();
        modelC2.setId(2L);
        modelC2.setModelAId(1L);
        modelC2.setModelBId(2L);
        modelC2.setSourceModelCId(null);
        modelCList.add(modelC2);

        ModelC modelC3 = new ModelC();
        modelC3.setId(3L);
        modelC3.setModelAId(1L);
        modelC3.setModelBId(3L);
        modelC3.setSourceModelCId(1L);
        modelCList.add(modelC3);
        modelC_list_db.put(1L, modelCList);
        modelC_list_db.put(2L, Collections.singletonList(modelC2));

        ModelC modelC4 = new ModelC();
        modelC4.setId(4L);
        modelC4.setModelAId(1L);
        modelC4.setModelBId(2L);
        modelC4.setSourceModelCId(1L);
        modelC_db.put(1L, modelC1);
        modelC_db.put(2L, modelC2);
        modelC_db.put(3L, modelC3);
        modelC_db.put(4L, modelC4);
    }

    public Map<Long, ModelC> batchGetModelCs(List<Long> ids) {
        if(ids == null || ids.size() == 0) {
            return null;
        }

        Map<Long, ModelC> modelMap = new HashMap<>();
        ids.forEach(id -> {
            modelMap.put(id, modelC_db.get(id));
        });
        return modelMap;
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
        Map<Long, List<ModelC>> _modelC_list_db = new HashMap<>();
        List<ModelC> modelCList = new ArrayList<>();

        modelCList.add(modelC_db.get(4L));
        _modelC_list_db.put(1L, modelCList);
        return _modelC_list_db;
    }
}
