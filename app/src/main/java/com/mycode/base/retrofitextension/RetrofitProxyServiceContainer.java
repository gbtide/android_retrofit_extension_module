package com.mycode.base.retrofitextension;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kyunghoon on 2019-04-30
 */
abstract class RetrofitProxyServiceContainer {
    private Map<String, Object> mServices = new HashMap<>();

    public <T> T get(String key, Class<T> service) {
        T instance = getIfContains(key);
        if (instance != null) {
            return instance;
        }
        instance = createProxyService(service);
        mServices.put(key, instance);
        return instance;
    }


    private <T> T getIfContains(String key) {
        if (mServices.containsKey(key)) {
            return (T) mServices.get(key);
        }
        return null;
    }

    abstract <T> T createProxyService(Class<T> service);

}
