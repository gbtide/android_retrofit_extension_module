package com.mycode.base.retrofitextension.okhttp;

import com.mycode.base.retrofitextension.Initializer;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by kyunghoon on 2019-04-08
 */
public class OkHttpClientFactory {
    private static final int DEFAULT_TIMEOUT_SECONDS = 10;

    public static OkHttpClient create() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new OkHttpApplicationInterceptor());
        builder.addNetworkInterceptor(new OkHttpNetworkInterceptor());
        if (Initializer.getInstance().isDebug()) {
            builder.addInterceptor(new OkHttpLoggerInterceptor());
        }

        builder.connectTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        return builder.build();
    }


}
