package com.mycode.base.retrofitextension.call;

/**
 * Created by kyunghoon on 2019-05-16
 */
class FileloadOption {
    private static final boolean DEFAULT_ACTIVATION = false;

    private boolean active = DEFAULT_ACTIVATION;

    private Class<?> rawType = null;

    FileloadOption() {}

    FileloadOption(boolean active, Class<?> rawType) {
        this.active = active;
        this.rawType = rawType;
    }

    public boolean isActive() {
        return active;
    }

    public Class<?> getRawType() {
        return rawType;
    }
}
