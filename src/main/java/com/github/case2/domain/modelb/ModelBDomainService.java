package com.github.case2.domain.modelb;

import com.github.case2.domain.modelb.model.ModelB;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * modelB 领域
 */
public class ModelBDomainService {

    private static Map<Long, ModelB> modelB_db = new HashMap<>();
    static {
        ModelB modelB = new ModelB();
        modelB.setId(1L);
        modelB.setInnerAttributeBa(new Object());
        modelB_db.put(1L, modelB);
    }

    public Map<Long, ModelB> batchGetModelBs(List<Long> ids) {
        if(ids == null || ids.size() == 0) {
            return null;
        }

        Map<Long, ModelB> modelMap = new HashMap<>();
        ids.forEach(id -> {
            modelMap.put(id, modelB_db.get(id));
        });
        return modelMap;
    }
}
