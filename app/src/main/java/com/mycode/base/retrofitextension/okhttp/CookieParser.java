package com.mycode.base.retrofitextension.okhttp;

import com.mycode.base.retrofitextension.utility.StringUtility;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;

/**
 * Created by kyunghoon on 2019-07-08
 */
class CookieParser {
    private static final String COOKIE_SEPARATOR = ";";
    private static final String HEADER_KEY_SET_COOKIE = "set-cookie";

    @Nullable
    static Map<String, String> parse(String cookie) {
        if (!StringUtility.isNullOrEmpty(cookie)) {
            String[] newValues = cookie.split(COOKIE_SEPARATOR);
            Map<String, String> cookieMap = new HashMap<>();
            for (String value : newValues) {
                int separatorIndex = value.indexOf("=");
                if (separatorIndex >= 0) {
                    String cookieName = value.substring(0, separatorIndex);
                    String cookieValue = value.substring(separatorIndex + 1);
                    cookieMap.put(cookieName, cookieValue);
                }
            }
            return cookieMap;

        }
        return null;
    }
}
