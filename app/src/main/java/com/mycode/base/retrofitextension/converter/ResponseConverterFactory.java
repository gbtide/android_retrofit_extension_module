package com.mycode.base.retrofitextension.converter;

import com.mycode.base.retrofitextension.GsonInstance;
import com.mycode.base.retrofitextension.call.CallOption;
import com.mycode.base.retrofitextension.call.CallOptionParser;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import androidx.annotation.Nullable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kyunghoon on 2019-04-03
 */
public class ResponseConverterFactory extends Converter.Factory {
    private static final String TAG = "ResponseConverterFactory";

    private static final GsonConverterFactory sGsonConverterFactory = GsonConverterFactory.create(GsonInstance.get());

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new ResponseBodyConverter<>(type, annotations);
    }

    @Nullable
    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return sGsonConverterFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }


    /**
     * Created by kyunghoon on 2019-04-03
     * modified by bugi9300
     */
    static class ResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private static final String TAG = "ResponseConverterFactory";

        private final Type type;

        private CallOption callOption;

        ResponseBodyConverter(Type type, Annotation[] annotations) {
            this.type = type;
            this.callOption = CallOptionParser.parse(annotations);
        }

        @Nullable
        @Override
        public T convert(ResponseBody value) throws IOException {
            Converter<ResponseBody, T> converter = ResponseBodyConverterFactory.createConverter(type, callOption);
            return converter.convert(value);
        }

    }

}

