package com.mycode.base.retrofitextension.call;

import java.util.Set;

import okhttp3.HttpUrl;
import retrofit2.Call;

/**
 * Created by kyunghoon on 2019-05-21
 *
 * [ 페이징 처리에 대한 예시 ]
 *
 * Request Url 에 {@link com.mycode.base.retrofitextension.annotation.Preload} 주석이 달려 있어도
 * 페이징 파라미터가 들어가있는 Request 라면 첫 번째 페이지에 대해서만 Preload 한다.
 *
 */
class PreloadCallVerifier {

    static boolean isPreload(Call call, PreloadOption option) {
        if (option.isActive()) {
            if (PageParamFinder.hasPageParam(call)) {
                return (PageParamFinder.isFirstPage(call)) ? true : false;
            }
            return true;
        } else {
            return false;
        }
    }


    private static class PageParamFinder {

        private static final String[] PAGE_PARAM_KEYS = {"page", "search.page"};

        static boolean hasPageParam(Call call) {
            if (isValidCall(call)) {
                return false;
            }
            HttpUrl httpUrl = call.request().url();
            Set<String> keys = httpUrl.queryParameterNames();
            for (String key : keys) {
                for (final String PAGE_KEY : PAGE_PARAM_KEYS) {
                    if (PAGE_KEY.equals(key)) {
                        return true;
                    }
                }
            }
            return false;
        }

        static boolean isFirstPage(Call call) {
            if (isValidCall(call)) {
                return false;
            }
            HttpUrl httpUrl = call.request().url();
            Set<String> keys = httpUrl.queryParameterNames();
            for (String key : keys) {
                for (final String PAGE_KEY : PAGE_PARAM_KEYS) {
                    if (PAGE_KEY.equals(key)) {
                        String value = httpUrl.queryParameter(key);
                        return "1".equals(value);
                    }
                }
            }
            return false;
        }

        private static boolean isValidCall(Call call) {
            return call == null ||
                    call.request() == null ||
                    call.request().url() == null;
        }

    }


}
