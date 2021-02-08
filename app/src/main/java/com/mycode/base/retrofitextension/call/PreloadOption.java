package com.mycode.base.retrofitextension.call;

/**
 * Created by kyunghoon on 2019-05-16
 */
class PreloadOption {
    private static final boolean DEFAULT_PRELOAD_ACTIVATION = false;

    private boolean active = DEFAULT_PRELOAD_ACTIVATION;

    private Class<?> rawType = null;

    private boolean disableFileload = false;

    PreloadOption() {}

    PreloadOption(boolean active, Class<?> rawType, boolean disableFileload) {
        this.active = active;
        this.rawType = rawType;
        this.disableFileload = disableFileload;
    }

    public boolean isActive() {
        return active;
    }

    public Class<?> getRawType() {
        return rawType;
    }

    public boolean isFileloadDisabled() {
        return disableFileload;
    }
}
