package com.github.viewdriver;

import org.apache.commons.collections.MapUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 视图驱动上下文数据.
 *
 * @author yanghuan
 */
public class Context {

    private final Map<String, Object> data = new HashMap<>();

    public void put(String key, Object value) {
        data.put(key, value);
    }

    public String getString(String key) {
        return MapUtils.getString(data, key);
    }

    public Integer getInteger(String key) {
        return MapUtils.getInteger(data, key);
    }

    public Long getLong(String key) {
        return MapUtils.getLong(data, key);
    }

    public Double getDouble(String key) {
        return MapUtils.getDouble(data, key);
    }

    public Boolean getBoolean(String key) {
        return MapUtils.getBoolean(data, key);
    }

    public Map getMap(String key) {
        return MapUtils.getMap(data, key);
    }

    public Object getObject(String key) {
        return data.get(key);
    }
}
