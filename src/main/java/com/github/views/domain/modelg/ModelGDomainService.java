package com.github.views.domain.modelg;

import com.github.views.domain.modelg.model.ModelG;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ModelG 领域
 */
public class ModelGDomainService {

    private static Map<Long, ModelG> modelG_db = new HashMap<>();
    static {
        ModelG modelG = new ModelG();
        modelG.setId(1L);
        modelG.setInnerAttributeGa("内部属性Ga-1");
        modelG.setInnerAttributeGb("内部属性Gb-1");

        modelG_db.put(1L, modelG);
    }

    public Map<Long, ModelG> batchGetModelGs(List<Long> ids) {
        if(ids == null || ids.size() == 0) {
            return null;
        }

        Map<Long, ModelG> modelMap = new HashMap<>();
        ids.forEach(id -> {
            modelMap.put(id, modelG_db.get(id));
        });
        return modelMap;
    }
}
