package com.mycode.base.retrofitextension;

import com.google.gson.Gson;

/**
 * Created by kyunghoon on 2019-04-08
 */
public final class GsonInstance {
    private static Gson sInstance;
    private static final Object KEY = new Object();

    // kyunghoon memo.
    //
    // https://sites.google.com/site/gson/gson-user-guide#TOC-Using-Gson
    //
    // The Gson instance does not maintain any state while invoking Json operations.
    // So, you are free to reuse the same object for multiple Json serialization and deserialization operations.
    public static Gson get() {
        if (sInstance == null) {
            synchronized (KEY) {
                if (sInstance == null) {
//                    sInstance = new GsonBuilder()
//                            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
//                            .setLenient()
//                            .create();
                    sInstance = new Gson();
                    // 각 프로젝트에서 적절하게 Gson 정책을 수정해줄 것
                }
            }
        }
        return sInstance;
    }

}
