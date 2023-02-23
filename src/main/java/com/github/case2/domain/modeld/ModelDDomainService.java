package com.github.case2.domain.modeld;

import com.github.case2.domain.modeld.model.ModelD;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ModelD 领域
 */
public class ModelDDomainService {

    private static Map<Long, ModelD> modelD_db = new HashMap<>();
    static {
        ModelD modelD1 = new ModelD();
        modelD1.setId(1L);
        modelD1.setInnerAttributeDa("da1");

        ModelD modelD2 = new ModelD();
        modelD2.setId(2L);
        modelD2.setInnerAttributeDa("da2");

        ModelD modelD3 = new ModelD();
        modelD3.setId(3L);
        modelD3.setInnerAttributeDa("da3");

        modelD_db.put(1L, modelD1);
        modelD_db.put(2L, modelD1);
        modelD_db.put(3L, modelD1);
    }

    public Map<Long, ModelD> batchGetModelDs(List<Long> ids) {
        if(ids == null || ids.size() == 0) {
            return null;
        }

        Map<Long, ModelD> modelMap = new HashMap<>();
        ids.forEach(id -> {
            modelMap.put(id, modelD_db.get(id));
        });
        return modelMap;
    }
}
