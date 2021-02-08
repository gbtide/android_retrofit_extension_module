package com.mycode.base.retrofitextension.okhttp;

import android.text.TextUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by kyunghoon on 2019-04-09
 * <p>
 * 서비스 도메인과 관계없이 순수 네트워킹 관련 부분만 설정하는 곳입니다<p/>
 *
 * [ Network Interceptors ] <br/>
 *   https://github.com/square/okhttp/wiki/Interceptors 참고 <p/>
 *
 * - Able to operate on intermediate responses like redirects and retries. <br/>
 * - Not invoked for cached responses that short-circuit the network. <br/>
 * - Observe the data just as it will be transmitted over the network. <br/>
 * - Access to the Connection that carries the request. <br/>
 */
class OkHttpNetworkInterceptor implements Interceptor {
    private static final String CONNECT_TIMEOUT = "CONNECT_TIMEOUT";
    private static final String READ_TIMEOUT = "READ_TIMEOUT";
    private static final String WRITE_TIMEOUT = "WRITE_TIMEOUT";


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        resetTimeout(chain, request);

        return chain.proceed(request);
    }

    /**
     * default timeout 외에 해더에 새롭게 정의된 timeout 이 있으면 세팅해줍니다. </br></p>
     * <p>
     * [ 활용 예시 ] </br>
     *
     * @Headers({"CONNECT_TIMEOUT:10000", "READ_TIMEOUT:10000", "WRITE_TIMEOUT:10000"})</br>
     * @POST("/cafemobileapps/cafe/UploadImage") </br>
     * Observable<UploadImageResponse> uploadImage(); </br>
     */
    private void resetTimeout(Chain chain, Request request) {
        int connectTimeout = chain.connectTimeoutMillis();
        int readTimeout = chain.readTimeoutMillis();
        int writeTimeout = chain.writeTimeoutMillis();

        String connectTimeoutFromRequestHeader = request.header(CONNECT_TIMEOUT);
        String readTimeoutFromRequestHeader = request.header(READ_TIMEOUT);
        String writeTimeoutFromRequestHeader = request.header(WRITE_TIMEOUT);

        if (!TextUtils.isEmpty(connectTimeoutFromRequestHeader)) {
            connectTimeout = Integer.valueOf(connectTimeoutFromRequestHeader);
        }
        if (!TextUtils.isEmpty(readTimeoutFromRequestHeader)) {
            readTimeout = Integer.valueOf(readTimeoutFromRequestHeader);
        }
        if (!TextUtils.isEmpty(writeTimeoutFromRequestHeader)) {
            writeTimeout = Integer.valueOf(writeTimeoutFromRequestHeader);
        }

        chain.withConnectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .withReadTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .withWriteTimeout(writeTimeout, TimeUnit.MILLISECONDS);

    }


}
