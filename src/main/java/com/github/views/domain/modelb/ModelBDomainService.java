package com.github.views.domain.modelb;

import com.github.views.domain.modelb.model.ModelB;

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
        modelB.setInnerAttributeBa("内部属性Ba-1");
        modelB_db.put(1L, modelB);

        ModelB modelB2 = new ModelB();
        modelB2.setId(2L);
        modelB2.setInnerAttributeBa("内部属性Ba-2");
        modelB_db.put(2L, modelB2);

        ModelB modelB3 = new ModelB();
        modelB3.setId(3L);
        modelB3.setInnerAttributeBa("内部属性Ba-3");
        modelB_db.put(3L, modelB3);
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
