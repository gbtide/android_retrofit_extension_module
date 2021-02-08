package com.mycode.base.retrofitextension.call;

import android.content.Context;

import com.mycode.base.retrofitextension.utility.BaseSharedPrefModel;

/**
 * Created by kyunghoon on 2019-05-17
 */
class PreloadPreference extends BaseSharedPrefModel {
    private static final String PREFERENCE_ID = "ApiPreload";
    private static final String KEY_FOLDER_PATH = "KEY_FOLDER_PATH_V1";
    private static final String KEY_VERSION_CODE = "KEY_VERSION_CODE";
    private static final Object KEY = new Object();

    private static PreloadPreference instance;

    public static PreloadPreference getInstance() {
        if (instance == null) {
            synchronized (KEY) {
                if (instance == null) {
                    instance = new PreloadPreference();
                }
            }
        }
        return instance;
    }

    private PreloadPreference() {
    }

    @Override
    public String getPrefName() {
        return PREFERENCE_ID;
    }

    @Override
    public int getPrefMode() {
        return Context.MODE_PRIVATE;
    }

    public void savePreloadFolderPath(String value) {
        put(KEY_FOLDER_PATH, value);
    }

    public String getPreloadFolderPath() {
        return get(KEY_FOLDER_PATH, "");
    }

    public void saveVersionCode(int value) {
        put(KEY_VERSION_CODE, value);
    }

    public int getVersionCode() {
        return get(KEY_VERSION_CODE, 0);
    }

}
