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
        objectInfo.setOa("oa");
        objectInfo.setOb("ob");
        modelA.setInnerAttributeAa(objectInfo);
        modelA.setSourceModelAId(2L);
        modelA.setModelBId(1L);
        modelA.setModelDIdList(Arrays.asList(1L, 2L, 3L));
        modelA_db.put(1L, modelA);

        ModelA modelA2 = new ModelA();
        modelA2.setId(2L);
        ObjectInfo objectInfo2 = new ObjectInfo();
        objectInfo2.setOa("oa1");
        objectInfo2.setOb("ob1");
        modelA2.setInnerAttributeAa(objectInfo2);
        modelA2.setSourceModelAId(null);
        modelA2.setModelBId(null);
        modelA2.setModelDIdList(null);
        modelA_db.put(2L, modelA2);
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

    public Map<Long, ObjectInfo> batchGetOuterObject(List<Long> ids) {
        if(ids == null || ids.size() == 0) {
            return null;
        }

        Map<Long, ObjectInfo> objectInfoMap = new HashMap<>();
        ObjectInfo objectInfo = new ObjectInfo();
        objectInfo.setOa("outerOa");
        objectInfo.setOb("outerOb");
        objectInfoMap.put(1L, objectInfo);
        return objectInfoMap;
    }
}
