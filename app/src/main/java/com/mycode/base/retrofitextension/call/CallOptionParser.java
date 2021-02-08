package com.mycode.base.retrofitextension.call;

import com.mycode.base.retrofitextension.annotation.Fileload;
import com.mycode.base.retrofitextension.annotation.Preload;
import com.mycode.base.retrofitextension.annotation.ResponseType;
import com.mycode.base.retrofitextension.annotation.Retry;

import java.lang.annotation.Annotation;

import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by kyunghoon on 2021-01-28
 *
 * {@link CallOption} 주석 참고
 */
public class CallOptionParser {

    public static CallOption parse(Annotation[] annotations) {
        return parse(annotations, null);
    }

    public static CallOption parse(Annotation[] annotations, Class<?> rawType) {
        CallOption callOption = new CallOption();

        for (Annotation annotation : annotations) {
            if (annotation instanceof Retry) {
                Retry retry = (Retry) annotation;
                callOption.setRetry(new RetryOption(retry.count(), retry.interval()));
                continue;
            }

            if (annotation instanceof Preload) {
                Preload preload = (Preload) annotation;
                callOption.setPreload(new PreloadOption(true, rawType, preload.disableFileload()));
                continue;
            }

            if (annotation instanceof Fileload) {
                callOption.setFileload(new FileloadOption(true, rawType));
                continue;
            }

            if (annotation instanceof ResponseType) {
                ResponseType responseType = (ResponseType) annotation;
                if (responseType.value() != null) {
                    callOption.setResponseType(responseType.value());
                }
                continue;
            }

            if (annotation instanceof GET) {
                callOption.setCallType(CallOption.CallType.GET);
            } else if (annotation instanceof POST) {
                callOption.setCallType(CallOption.CallType.POST);
            } else {
                callOption.setCallType(null);
            }
        }

        return callOption;
    }
}
