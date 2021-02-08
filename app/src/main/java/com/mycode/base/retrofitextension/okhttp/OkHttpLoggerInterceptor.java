package com.mycode.base.retrofitextension.okhttp;

import android.util.Log;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by kyunghoon on 2019-04-08 <p/>
 * <p>
 * <p>
 * Debug 모드 시에만 Intercept 로 추가됨. <br/>
 * OkHttp에서 추가하는 데이터 (such as the Accept-Encoding: gzip header), 실제 Redirect 정보까지 모두 보기 위해 NetworkInterceptor 에 추가합니다. <br/>
 * 참고 : https://github.com/square/okhttp/wiki/Interceptors <br/>
 */
class OkHttpLoggerInterceptor implements Interceptor {
    private static final String TAG = "OkHttpLoggerInterceptor";

    /**
     * Cookie 생략
     */
    private static final String[] REQUEST_HEADER_LIST_TO_BE_SHOWN = {"User-Agent", "UserId", "Content-Type"};

    /**
     * p3p, set-cookie, access-control*, cache-control, pragma, expires, x-xss-protection, x-frame-options 등은 생략
     */
    private static final String[] RESPONSE_HEADER_LIST_TO_BE_SHOWN = {"server", "date", "content-type"};

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        StringBuilder builder = new StringBuilder();
        log(builder, request);

        Response response = chain.proceed(request);
        log(builder, response);
        Log.d(TAG, builder.toString());

        return response;
    }

    private void log(StringBuilder builder, Request request) {
        builder.append("\n===== [START]");
        builder.append("\n");
        builder.append("\n [ Request ]");
        builder.append("\n");
        builder.append("\n (1) Request from : " + request.url());
        builder.append("\n (2) Request Method : " + request.method());
        builder.append("\n (3) Request Header");

        for (String key : request.headers().names()) {
            for (String headerKey : REQUEST_HEADER_LIST_TO_BE_SHOWN) {
                if (headerKey.equals(key)) {
                    builder.append("\n   - ");
                    builder.append(key);
                    builder.append(" : ");
                    builder.append(request.headers().get(key));
                }
            }
        }

        if (request.body() != null) {
            try {
                builder.append("\n (4) Request Body : ");
                builder.append(getBodyStringFrom(request));
            } catch (Exception e) {
                builder.append("\n (4) Request Body : error - ");
                builder.append(e.toString());
            }
        }
    }

    private void log(StringBuilder builder, Response response) {
        long requestTime = response.sentRequestAtMillis();
        long responseTime = response.receivedResponseAtMillis();
        Headers responseHeaders = response.headers();

        builder.append("\n");
        builder.append("\n [ Response ]");
        builder.append("\n");
        builder.append("\n (1) Response from : ");
        builder.append(response.request().url());

        builder.append("\n (2) Response Time : ");
        builder.append((responseTime - requestTime));
        builder.append("ms");

        builder.append("\n (3) Response Code : ");
        builder.append(response.code());

        builder.append("\n (4) Response Message : ");
        builder.append(response.message());

        builder.append("\n (5) Response Headers : ");
        for (int i = 0, len = responseHeaders.size(); i < len; i++) {
            String name = responseHeaders.name(i);
            String value = responseHeaders.value(i);
            if (name != null) {
                for (String headerKey : RESPONSE_HEADER_LIST_TO_BE_SHOWN) {
                    if (headerKey.equals(name)) {
                        builder.append("\n   - ");
                        builder.append(name);
                        builder.append(" : ");
                        builder.append(value);
                    }
                }
            }
        }

        try {
            builder.append("\n (6) Response Body : ");
            builder.append(getBodyStringFrom(response));
        } catch (Exception e) {
            builder.append("\n (6) Response Body : error - ");
            builder.append(e.toString());
        }
        builder.append("\n");
        builder.append("\n===== [END]\n");
    }

    /**
     * response.body().string() 은 1회 밖에 call 못한다. (Response 메모리 이슈)
     * 즉, 로깅을 위해 source buffer 를 clone 해서 사용한다.
     */
    private String getBodyStringFrom(Response rawResponse) throws Exception {
        ResponseBody responseBody = rawResponse.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // request the entire body.
        Buffer buffer = source.buffer();
        String responseBodyString = buffer.clone().readString(StandardCharsets.UTF_8);
        return responseBodyString != null ? responseBodyString : "";
    }

    private String getBodyStringFrom(Request request) throws Exception {
        Request copy = request.newBuilder().build();
        Buffer buffer = new Buffer();
        copy.body().writeTo(buffer);
        return buffer.readString(StandardCharsets.UTF_8);
    }
}
