package com.mycode.base.retrofitextension.exception;

import com.google.gson.JsonParseException;
import com.mycode.base.retrofitextension.REApiException;
import com.mycode.base.retrofitextension.REApiExceptionType;
import com.mycode.base.retrofitextension.converter.ResponseParsingException;
import com.mycode.base.retrofitextension.sample.ApiGatewayErrorType;
import com.mycode.base.retrofitextension.sample.ServiceBodyResponseCode;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.UnknownHostException;

import androidx.annotation.NonNull;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Created by kyunghoon on 2021-02-02
 *
 * 메인 서비스 ExceptionMaker 예시
 */
class REApiExceptionMakerForCommon implements REApiExceptionMaker {
    private static final String TAG = "REApiExceptionMakerForCommon";

    @NonNull
    @Override
    public REApiException makeFrom(@NonNull Throwable error) {
        return makeFromInternal(error);
    }

    @NonNull
    private REApiException makeFromInternal(@NonNull Throwable error) {
        if (error instanceof REApiException) {
            return (REApiException) error;
        }

        /**
         * 1. HTTP status == non-2xx
         *
         * [ 처리할 수 있는 내용들 ]
         * - 200이 아닌 게이트웨이 오류
         * - 200이 아닌 서비스 서버 오류
         */
        if (error instanceof HttpException) {
            final HttpException httpException = (HttpException) error;
            final Response response = httpException.response();
            if (response == null) {
                return new REApiException.Builder(REApiExceptionType.INVALID_RESPONSE_BODY, "response is null").build();
            }

            switch (response.code()) {
                // 예시
//                case 403:
//                    String bodyErrorCode = ResponseReader.readBodyErrorCodeFrom(response.errorBody());
//                    // if bodyErrorCode == 특정 에러, return REApiException

                default:
                    REApiException exception = new REApiException.Builder(REApiExceptionType.NOT_HANDLED_NON_2XX_ERROR, response.message())
                            .setHttpStatusCode(response.code())
                            .build();
                    return exception;
            }
        }

        /**
         * 2. HTTP status == 200
         *
         * - 발생 근원지 {@link com.mycode.base.retrofitextension.converter.CommonResponseBodyConverter}
         * - 200이면서 Body 오류가 떨어질 수 있습니다.
         */
        if (error instanceof ResponseParsingException) {
            ResponseParsingException exception = (ResponseParsingException) error;
            switch (exception.getType()) {
                case INVALID_RESPONSE_BODY:
                    return new REApiException.Builder(REApiExceptionType.INVALID_RESPONSE_BODY, exception.getMessage())
                            .setHttpStatusCode(200)
                            .setBodyCode(exception.getServiceBodyResponseCode().getCode())
                            .build();

                case FROM_GATEWAY:
                    ApiGatewayErrorType apiGatewayErrorType = exception.getApiGatewayErrorType();
                    return new REApiException.Builder(ApiGatewayErrorType.Converter.from(apiGatewayErrorType), exception.getMessage())
                            .setHttpStatusCode(200)
                            .build();

                case FROM_SERVICE_SERVER:
                    ServiceBodyResponseCode serviceBodyResponseCode = exception.getServiceBodyResponseCode();
                    return new REApiException.Builder(ServiceBodyResponseCode.Converter.from(serviceBodyResponseCode), exception.getMessage())
                            .setHttpStatusCode(200)
                            .setBodyCode(exception.getServiceBodyResponseCode().getCode())
                            .build();
            }
        }

        if (error instanceof IOException) {
            if (error instanceof UnsupportedEncodingException) {
                return new REApiException.Builder(REApiExceptionType.UNSUPPORTED_ENCODING_ERROR, error.getMessage()).build();
            }
            if (error instanceof JsonParseException) {
                return new REApiException.Builder(REApiExceptionType.JSON_PARSER_ERROR, error.getMessage()).build();
            }
            if (error instanceof UnknownHostException) {
                return new REApiException.Builder(REApiExceptionType.DISCONNECTED_NETWORK_ERROR, error.getMessage()).build();
            }
            if (error instanceof SocketException) {
                return new REApiException.Builder(REApiExceptionType.DISCONNECTED_NETWORK_ERROR, error.getMessage()).build();
            }
        }

        return new REApiException.Builder(REApiExceptionType.UNKNOWN_ERROR, error == null ? null : error.getMessage()).build();
    }

}
