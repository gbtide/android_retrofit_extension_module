package com.mycode.base.retrofitextension;

import com.mycode.base.retrofitextension.call.RECallAdapterFactory;
import com.mycode.base.retrofitextension.converter.ResponseConverterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by kyunghoon on 2019-04-03
 *
 */
class RetrofitFactory {

    static Retrofit createRetrofit(String baseUrl, OkHttpClient okHttpClient) {

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RECallAdapterFactory.create())
                .addConverterFactory(new ResponseConverterFactory())
                .build();
        return retrofit;
    }

}
