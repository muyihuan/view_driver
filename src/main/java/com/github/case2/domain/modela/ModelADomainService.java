package com.github.case2.domain.modela;

import com.github.case2.domain.modela.model.ModelA;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * modelA 领域
 */
public class ModelADomainService {

    public Map<Long, ModelA> batchGetModelAs(List<Long> ids) {
        return new HashMap<Long, ModelA>();
    }
}
