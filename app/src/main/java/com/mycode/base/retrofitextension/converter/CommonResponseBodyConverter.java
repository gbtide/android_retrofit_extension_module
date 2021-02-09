package com.mycode.base.retrofitextension.converter;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.mycode.base.retrofitextension.GsonInstance;
import com.mycode.base.retrofitextension.XmlSerializerInstance;
import com.mycode.base.retrofitextension.sample.ApiGatewayErrorType;
import com.mycode.base.retrofitextension.sample.BaseJsonObject;
import com.mycode.base.retrofitextension.sample.BaseXmlObject;
import com.mycode.base.retrofitextension.sample.ServiceBodyResponseCode;
import com.mycode.base.retrofitextension.utility.StringUtility;

import org.apache.commons.lang3.reflect.TypeUtils;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by kyunghoon on 2021-02-02
 * <p>
 * 메인 서비스 ResponseBodyConverter 예시
 */
class CommonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private static final String TAG = "CommonResponseBodyConverter";

    private final Type type;

    public CommonResponseBodyConverter(Type type) {
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            return parse(value);
        } finally {
            value.close();
        }
    }

    private T parse(ResponseBody value) throws IOException {
        // response 파싱까지 내려왔다면 http status code 는 200
        // 그러나 http status code 200 이어도 Error Body 를 내려줄 수 있는 경우가 있음.
        // apigw 오류는 xml, json 포맷 둘다 가능함을 가정.
        if (StringUtility.equalsIgnoreCase(value.contentType().subtype(), "xml")) {
            BaseXmlObject xmlBody = null;
            String responseString = "";
            try {
                responseString = value.string();
                xmlBody = XmlSerializerInstance.get().read(BaseXmlObject.class, responseString, true);
            } catch (Exception e) {
//                Log.e(TAG, e.getMessage(), e);
            }

            if (xmlBody == null) {
                // 1) 예상치 못한 포멧
                throw new ResponseParsingException.Builder(responseString,
                        ResponseParsingException.Type.INVALID_RESPONSE_BODY)
                        .build();

            } else {
                // 2) 게이트웨이에서 약속된 에러 포멧을 내려줌
                String message = xmlBody.toString();
                throw new ResponseParsingException.Builder(message,
                        ResponseParsingException.Type.FROM_GATEWAY)
                        .setApiGatewayErrorType(ApiGatewayErrorType.findBy(xmlBody.getGatewayErrorCode()))
                        .build();
            }

        } else {
            Gson gson = GsonInstance.get();
            BaseJsonObject<T> jsonBody = null;

            try {
                JsonReader jsonReader = gson.newJsonReader(value.charStream());
                jsonBody = gson.fromJson(jsonReader, TypeUtils.parameterize(BaseJsonObject.class, type));
            } catch (Exception e) {
//                Log.e(TAG, e.getMessage(), e);
            }

            // 1) 예상하지 못한 포멧
            if (jsonBody == null) {
                throw new ResponseParsingException.Builder("unexpected response type (json)",
                        ResponseParsingException.Type.INVALID_RESPONSE_BODY)
                        .build();
            }

            // 2) 게이트웨이에서 약속된 에러 포멧을 내려줌
            if (!StringUtility.isNullOrEmpty(jsonBody.getGatewayErrorCode())) {
                String message = jsonBody.getGatewayErrorCode();
                throw new ResponseParsingException.Builder(message,
                        ResponseParsingException.Type.FROM_GATEWAY)
                        .setApiGatewayErrorType(ApiGatewayErrorType.findBy(jsonBody.getGatewayErrorCode()))
                        .build();
            }

            // 3) 서비스 서버로부터 리스폰스 도착
            if (jsonBody.getServiceErrorCode() == null) {
                // 3-1) 성공!
                return jsonBody.getMessage().getResult();
            } else {
                // 3-2) 서비스 서버에서 약속된 에러 포멧을 내려줌
                ServiceBodyResponseCode responseCode = ServiceBodyResponseCode.findBy(jsonBody.getServiceErrorCode());
                throw new ResponseParsingException.Builder(responseCode.name(),
                        ResponseParsingException.Type.FROM_SERVICE_SERVER)
                        .setServiceBodyResponseCode(responseCode)
                        .build();
            }
        }
    }


}
