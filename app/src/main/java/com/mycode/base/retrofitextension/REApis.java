package com.mycode.base.retrofitextension;

import com.mycode.base.retrofitextension.okhttp.OkHttpClientFactory;

import retrofit2.Retrofit;

/**
 * Created by kyunghoon on 2019-04-19
 *
 * RE : Retrofit Extension
 */
public class REApis {
    private static final Object KEY = new Object();
    private static REApis sInstance;
    private Retrofit mRetrofit;
    private RetrofitProxyServiceContainer mProxyServiceContainer;

    public static REApis getInstance() {
        if (sInstance == null) {
            synchronized (KEY) {
                if (sInstance == null) {
                    sInstance = new REApis();
                }
            }
        }
        return sInstance;
    }

    private REApis() {
        String baseUrl = Initializer.getInstance().getApiBaseUrl();
        mRetrofit = RetrofitFactory.createRetrofit(baseUrl, OkHttpClientFactory.create());
        mProxyServiceContainer = new REApiServiceContainer();
    }

    public <T> T get(Class<T> service) {
        return mProxyServiceContainer.get(service.getSimpleName(), service);
    }

    class REApiServiceContainer extends RetrofitProxyServiceContainer {

        @Override
        <T> T createProxyService(Class<T> service) {
            return mRetrofit.create(service);
        }
    }

}
