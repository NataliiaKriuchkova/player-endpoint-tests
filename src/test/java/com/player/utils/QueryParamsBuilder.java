package com.player.utils;

import java.util.HashMap;
import java.util.Map;

public class QueryParamsBuilder {
    private final Map<String, Object> params = new HashMap<>();

    public QueryParamsBuilder add(String key, Object value) {
        if (value != null) {
            params.put(key, value);
        }
        return this;
    }

    public Map<String, Object> build() {
        return params;
    }
}
