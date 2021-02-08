package com.mycode.base.retrofitextension.utility;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.mycode.base.retrofitextension.GsonInstance;
import com.mycode.base.retrofitextension.XmlSerializerInstance;
import com.mycode.base.retrofitextension.sample.BaseJsonObject;
import com.mycode.base.retrofitextension.sample.BaseXmlObject;

import java.io.IOException;
import java.nio.charset.Charset;

import androidx.annotation.Nullable;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by kyunghoon on 2019-05-03
 */
public class ResponseReader {

    private static final String XML = "xml";

    public static String readBodyStringFrom(ResponseBody value) throws IOException {
        try {
            if (value == null) {
                return "";
            } else {
                BufferedSource source = value.source();
                source.request(Long.MAX_VALUE); // request the entire body.
                Buffer buffer = source.buffer();
                String responseBodyString = buffer.clone().readString(Charset.forName("UTF-8"));
                return responseBodyString != null ? responseBodyString : "";
            }
        } catch (RuntimeException | IOException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            value.close();
        }
    }

    @Nullable
    public static String readBodyErrorCodeFrom(ResponseBody errorBody) {
        try {
            String bodyString = readBodyStringFrom(errorBody);

            if (XML.equals(errorBody.contentType().subtype())) {
                BaseXmlObject xmlBody = XmlSerializerInstance.get().read(BaseXmlObject.class, bodyString, true);
                return xmlBody.gatewayErrorCode;

            } else {
                Gson gson = GsonInstance.get();
                TypeAdapter<BaseJsonObject> mJsonAdapter = gson.getAdapter(TypeToken.get(BaseJsonObject.class));
                JsonReader jsonReader = gson.newJsonReader(errorBody.charStream());
                BaseJsonObject jsonBody = mJsonAdapter.read(jsonReader);
                String errorCode = jsonBody.getGatewayErrorCode();

                // memo. API gateway 에러를 먼저보고, null 이면 카페 서버로부터 온 에러를 보도록 했습니다.
                return (errorCode == null) ? jsonBody.getServiceErrorCode() : errorCode;
            }

        } catch (Throwable e) {
            return null;
        }
    }

}
