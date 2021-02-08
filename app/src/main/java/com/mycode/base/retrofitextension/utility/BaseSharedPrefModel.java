package com.mycode.base.retrofitextension.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.mycode.base.retrofitextension.Initializer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.Nullable;

@SuppressLint("CommitPrefEdits")
public abstract class BaseSharedPrefModel {
    private AtomicBoolean commitFileImmediately = new AtomicBoolean(true);
    private AtomicBoolean commitEditorSynchronously = new AtomicBoolean(false);

    private Map<String, Object> dataMap;
    private Map<String, Object> dirtyMap;

    public abstract String getPrefName();

    public abstract int getPrefMode();

    public void setCommitFileImmediately(boolean commitFileImmediately) {
        this.commitFileImmediately.set(commitFileImmediately);
    }

    public void setCommitEditorSynchronously(boolean commitEditorSynchronously) {
        this.commitEditorSynchronously.set(commitEditorSynchronously);
    }

    protected Context getContext() {
        Context context = Initializer.getInstance().getContext();
        if (context == null) {
            Log.e(BaseSharedPrefModel.class.getSimpleName(), "BaseSharedPrefModel Initializer.getContext() is null.");
        }
        return context;
    }

    private SharedPreferences getSharedPreferences() {
        Context context = getContext();
        return context.getSharedPreferences(getPrefName(), getPrefMode());
    }

    protected Map<String, Object> getDataMap() {
        if (dataMap == null) {
            dataMap = new HashMap<String, Object>();
        }
        return dataMap;
    }

    protected Map<String, Object> getDirtyMap() {
        if (dirtyMap == null) {
            dirtyMap = new HashMap<String, Object>();
        }

        return dirtyMap;
    }

    @Nullable
    public <T> T get(String key) {
        return get(key, null);
    }

    public <T> T get(String key, T defaultValue) {
        if (!getDataMap().containsKey(key)) {
            SharedPreferences pref = getSharedPreferences();
            if (pref.contains(key)) {
                Map<String, ?> prefData = pref.getAll();

                for (Map.Entry<String, ?> entry : prefData.entrySet()) {
                    if (!getDataMap().containsKey(entry.getKey())) {
                        getDataMap().put(entry.getKey(), entry.getValue());
                        getDirtyMap().put(entry.getKey(), entry.getValue());
                    }
                }
            }
        }

        if (getDataMap().containsKey(key)) {
            return (T) getDataMap().get(key);
        }

        return defaultValue;
    }

    public void put(String key, boolean value) {
        put(key, Boolean.valueOf(value));
    }

    public void put(String key, int value) {
        put(key, Integer.valueOf(value));
    }

    public void put(String key, long value) {
        put(key, Long.valueOf(value));
    }

    public void put(final String key, final Object value) {
        if (commitFileImmediately.get()) {
            SharedPreferences pref = getSharedPreferences();

            SharedPreferences.Editor editor = pref.edit();
            putData(editor, key, value);

            commitEditor(editor);
        }

        getDataMap().put(key, value);
    }

    private void putData(SharedPreferences.Editor editor, String key, Object value) {
        if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else {
            if (value != null) {
                editor.putString(key, value.toString());
            } else {
                editor.putString(key, null);
            }
        }
    }

    private void commitEditor(SharedPreferences.Editor editor) {
        if (!commitEditorSynchronously.get()) {
            // apply를 쓰는 것은 memory shared preference에 저장하므로 타이밍 안좋게 날라갈 염려가 있다.
            editor.apply();
            return;
        }
        editor.commit();
    }

    public void commit() {
        if (commitFileImmediately.get()) {
            return;
        }

        SharedPreferences pref = getSharedPreferences();
        SharedPreferences.Editor editor = pref.edit();

        for (Map.Entry<String, Object> entry : getDataMap().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (!getDirtyMap().containsKey(key) || getDirtyMap().get(key) != value) {
                putData(editor, key, value);
                getDirtyMap().put(key, value);
            }
        }

        commitEditor(editor);
        setCommitFileImmediately(true);
    }

    public void clear() {
        SharedPreferences pref = getSharedPreferences();
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        commitEditor(editor);

        getDataMap().clear();
        getDirtyMap().clear();
        setCommitFileImmediately(true);
    }

    public void remove(String key) {
        SharedPreferences pref = getSharedPreferences();
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        commitEditor(editor);

        setCommitFileImmediately(true);
    }

}
