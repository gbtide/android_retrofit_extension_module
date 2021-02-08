package com.mycode.base.retrofitextension.converter;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.mycode.base.retrofitextension.GsonInstance;
import com.mycode.base.retrofitextension.sample.AbTestResponse;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by kyunghoon on 2021-02-02
 *
 * 3rd party 예시. 메인 서비스 ResponseBodyConverter 보다는 간단히 정의.
 */
class ABTestResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            Gson gson = GsonInstance.get();
            JsonReader jsonReader = gson.newJsonReader(value.charStream());
            T rawResponse = gson.fromJson(jsonReader, AbTestResponse.class);
            return rawResponse;
        } catch (Throwable e) {
            throw new ResponseParsingException.Builder("invalid response body",
                    ResponseParsingException.Type.INVALID_RESPONSE_BODY).build();
        } finally {
            value.close();
        }
    }

}
