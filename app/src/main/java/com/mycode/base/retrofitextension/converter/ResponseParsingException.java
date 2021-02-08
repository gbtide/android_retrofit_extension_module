package com.mycode.base.retrofitextension.converter;

import com.mycode.base.retrofitextension.sample.ApiGatewayErrorType;
import com.mycode.base.retrofitextension.sample.ServiceBodyResponseCode;

import java.io.IOException;

import androidx.annotation.Nullable;

/**
 * Created by kyunghoon on 2021-02-03
 */
public class ResponseParsingException extends IOException {

    private ApiGatewayErrorType mApiGatewayErrorType;
    private ServiceBodyResponseCode mServiceBodyResponseCode;
    private Type mType;

    private ResponseParsingException(String message,
                                     Type type,
                                     @Nullable ApiGatewayErrorType apiGatewayErrorType,
                                     @Nullable ServiceBodyResponseCode serviceBodyResponseCode) {
        super(message);
        this.mType = type;
        this.mServiceBodyResponseCode = serviceBodyResponseCode;
        this.mApiGatewayErrorType = apiGatewayErrorType;
    }

    @Nullable
    public ApiGatewayErrorType getApiGatewayErrorType() {
        return mApiGatewayErrorType;
    }

    @Nullable
    public ServiceBodyResponseCode getServiceBodyResponseCode() {
        return mServiceBodyResponseCode;
    }

    public Type getType() {
        return mType;
    }

    public enum Type {
        FROM_GATEWAY,
        FROM_SERVICE_SERVER,
        INVALID_RESPONSE_BODY
        ;
    }

    public static class Builder {
        private Type type;
        private String message;
        private ApiGatewayErrorType apiGatewayErrorType;
        private ServiceBodyResponseCode serviceBodyResponseCode;

        public Builder(String message, Type type) {
            this.message = message;
            this.type = type;
        }

        public ResponseParsingException.Builder setApiGatewayErrorType(ApiGatewayErrorType apiGatewayErrorType) {
            this.apiGatewayErrorType = apiGatewayErrorType;
            return this;
        }

        public ResponseParsingException.Builder setServiceBodyResponseCode(ServiceBodyResponseCode serviceBodyResponseCode) {
            this.serviceBodyResponseCode = serviceBodyResponseCode;
            return this;
        }

        public ResponseParsingException build() {
            return new ResponseParsingException(message, type, apiGatewayErrorType, serviceBodyResponseCode);
        }
    }

}
