package com.github.viewdriver;

import java.util.HashMap;
import java.util.Map;

/**
 * 视图驱动上下文数据.
 *
 * @author yanghuan
 */
public class Context {

    private final Map<String, Object> data;

    public Context() {
        data = new HashMap<>();
    }

    public void put(String key, Object value) {
        data.put(key, value);
    }

    public Object get(String key) {
        return data.get(key);
    }
}
