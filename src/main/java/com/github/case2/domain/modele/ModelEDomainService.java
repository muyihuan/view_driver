package com.github.case2.domain.modele;

import com.github.case2.domain.modele.model.ModelE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ModelE 领域
 */
public class ModelEDomainService {

    private static Map<Long, ModelE> modelE_db = new HashMap<>();
    static {
        ModelE modelE = new ModelE();
        modelE.setModelAId(1L);
        modelE.setInnerAttributeEa("ea");
        modelE.setInnerAttributeEb("eb");

        modelE_db.put(1L, modelE);
    }

    public Map<Long, ModelE> batchGetModelEs(List<Long> modelAIds) {
        if(modelAIds == null || modelAIds.size() == 0) {
            return null;
        }

        Map<Long, ModelE> modelMap = new HashMap<>();
        modelAIds.forEach(id -> {
            modelMap.put(id, modelE_db.get(id));
        });
        return modelMap;
    }
}
