package com.github.case2.domain.modela;

import com.github.case2.domain.modela.model.ModelA;
import com.github.case2.domain.modela.model.ObjectInfo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * modelA 领域.
 *
 * @author yanghuan
 */
public class ModelADomainService {

    private static Map<Long, ModelA> modelA_db = new HashMap<>();
    static {
        ModelA modelA = new ModelA();
        modelA.setId(1L);
        ObjectInfo objectInfo = new ObjectInfo();
        objectInfo.setOa(new Object());
        objectInfo.setOb(new Object());
        modelA.setInnerAttributeAa(objectInfo);
        ModelA modelA2 = new ModelA();
        {
            modelA2.setId(2L);
            ObjectInfo objectInfo2 = new ObjectInfo();
            objectInfo2.setOa(new Object());
            objectInfo2.setOb(new Object());
            modelA2.setInnerAttributeAa(objectInfo2);
            modelA2.setDependentModelA(null);
            modelA2.setModelBId(null);
            modelA2.setModelDIdList(null);
        }
        modelA.setDependentModelA(modelA2);
        modelA.setModelBId(1L);
        modelA.setModelDIdList(Arrays.asList(1L, 2L, 3L));

        modelA_db.put(1L, modelA);
    }

    public Map<Long, ModelA> batchGetModelAs(List<Long> ids) {
        if(ids == null || ids.size() == 0) {
            return null;
        }

        Map<Long, ModelA> modelMap = new HashMap<>();
        ids.forEach(id -> {
            modelMap.put(id, modelA_db.get(id));
        });
        return modelMap;
    }
}
