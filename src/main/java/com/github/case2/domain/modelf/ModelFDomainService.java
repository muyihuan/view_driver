package com.github.case2.domain.modelf;

import com.github.case2.domain.modelf.model.ModelF;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ModelF 领域
 */
public class ModelFDomainService {

    private static Map<Long, ModelF> modelF_db = new HashMap<>();
    static {
        ModelF modelF = new ModelF();
        modelF.setModelBId(1L);
        modelF.setInnerAttributeFa("fa");
        modelF.setInnerAttributeFb("fb");

        modelF_db.put(1L, modelF);
    }

    public Map<Long, ModelF> batchGetModelFs(List<Long> modelBIds) {
        if(modelBIds == null || modelBIds.size() == 0) {
            return null;
        }

        Map<Long, ModelF> modelMap = new HashMap<>();
        modelBIds.forEach(id -> {
            modelMap.put(id, modelF_db.get(id));
        });
        return modelMap;
    }
}
