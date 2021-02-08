package com.mycode.base.retrofitextension.okhttp;

import android.net.Uri;
import android.text.TextUtils;

import com.mycode.base.retrofitextension.Initializer;
import com.mycode.base.retrofitextension.utility.StringUtility;

import java.util.UUID;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * Created by kyunghoon on 2019-04-30
 */
class CommonApiRequestBuilder {
    private static String sReferer;
    private static String sUserAgent;

    Request build(Request request) {
        Request.Builder builder = request.newBuilder().method(request.method(), request.body());
        addHeader(builder);

        String url = addCommonParameter(request);
        encryptUrl(builder, url);

        return builder.build();
    }

    private void addHeader(Request.Builder builder) {
        if (!StringUtility.isEmpty(getUserAgent())) {
            builder.addHeader("User-Agent", getUserAgent());
        }

        if (!StringUtility.isEmpty(getReferer())) {
            builder.addHeader("Referer", getReferer());
        }

        String userId = Initializer.getInstance().getUserUniqueId();
        if (!TextUtils.isEmpty(userId)) {
            builder.addHeader("UserId", userId);
        }

//        String cookie = ServiceCookieManager.getCookie();
//        if (!TextUtils.isEmpty(cookie)) {
//            builder.addHeader("Cookie", cookie);
//        }
    }

    private String getReferer() {
        if (sReferer == null) {
            sReferer = Initializer.getInstance().getApiBaseUrl();
        }
        return sReferer;
    }

    private String getUserAgent() {
        if (sUserAgent == null) {
            sUserAgent = "user-agent-sample";
//            sUserAgent = UserAgentHelper.get();
        }
        return sUserAgent;
    }

    private String addCommonParameter(Request request) {
        String url = request.url().toString();
        Uri.Builder uriBuilder = Uri.parse(url).buildUpon();
        uriBuilder.appendQueryParameter("uId", Initializer.getInstance().getUserUniqueId());
        uriBuilder.appendQueryParameter("tag", UUID.randomUUID().toString());
        return uriBuilder.toString();
    }

    /**
     * 필요하면 해줄 것!
     */
    private void encryptUrl(Request.Builder builder, String url) {
//        builder.url(HttpUrl.parse(MACHelper.encryptUrl(url)));
    }

}
