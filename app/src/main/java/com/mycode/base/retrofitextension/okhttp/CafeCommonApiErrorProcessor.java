package com.mycode.base.retrofitextension.okhttp;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by kyunghoon on 2019-04-30
 */
class CafeCommonApiErrorProcessor {
    Response onError(Response response, Interceptor.Chain chain) throws IOException {
        // do something if needed
        return response;
    }


}
