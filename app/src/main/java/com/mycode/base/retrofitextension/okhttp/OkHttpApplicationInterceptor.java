package com.mycode.base.retrofitextension.okhttp;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by kyunghoon on 2019-04-08
 * <p/>
 * 서비스 도메인 특화된 로직 추가 <p/>
 * <p>
 * [ Application interceptors ] <br/>
 * https://github.com/square/okhttp/wiki/Interceptors 참고 <p/>
 * <p>
 * <p>
 * - Don't need to worry about intermediate responses like redirects and retries. <br/>
 * - Are always invoked once, even if the HTTP response is served from the cache. <br/>
 * - Observe the application's original intent. Unconcerned with OkHttp-injected headers like If-None-Match. <br/>
 * - Permitted to short-circuit and not call Chain.proceed(). <br/>
 * - Permitted to retry and make multiple calls to Chain.proceed(). <br/>
 */
class OkHttpApplicationInterceptor implements Interceptor {
    private static final CommonApiRequestBuilder sRequestBuilder = new CommonApiRequestBuilder();
    private static final CommonApiResponseProcessor sApiResponseProcessor = new CommonApiResponseProcessor();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = sRequestBuilder.build(chain.request());
        Response response = chain.proceed(request);
        sApiResponseProcessor.onReceive(response, chain);
        return response;
    }

}
