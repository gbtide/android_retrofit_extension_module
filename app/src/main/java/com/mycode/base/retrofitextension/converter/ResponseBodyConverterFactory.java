package com.mycode.base.retrofitextension.converter;

import com.mycode.base.retrofitextension.call.CallOption;

import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by kyunghoon on 2021-02-02
 */
class ResponseBodyConverterFactory {

    private static final String XML = "xml";

    static <T> Converter<ResponseBody, T> createConverter(Type type, CallOption callOption) {
        switch (callOption.getResponseType()) {
            // 3rd party 예시
//            case ABTEST:
//                return new ABTestResponseBodyConverter<>();

            case COMMON:
            default:
                return new CommonResponseBodyConverter<>(type);
        }

    }

}
