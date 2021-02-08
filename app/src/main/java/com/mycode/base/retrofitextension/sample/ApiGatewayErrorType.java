package com.mycode.base.retrofitextension.sample;

import com.mycode.base.retrofitextension.REApiExceptionType;
import com.mycode.base.retrofitextension.utility.StringUtility;

/**
 * 예제 코드
 */
public enum ApiGatewayErrorType {

    ACCESS_DENIED("000"),
    UNKNOWN_ERROR("999")
    ;

    private String errorCode;

    ApiGatewayErrorType(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public static ApiGatewayErrorType findBy(String errorCode) {
        for (ApiGatewayErrorType each : ApiGatewayErrorType.values()) {
            if (StringUtility.equals(each.getErrorCode(), errorCode)) {
                return each;
            }
        }
        return UNKNOWN_ERROR;
    }


    public static class Converter {

        public static REApiExceptionType from(String errorCode) {
            return from(ApiGatewayErrorType.findBy(errorCode));
        }

        public static REApiExceptionType from(ApiGatewayErrorType responseCode) {
            switch (responseCode) {
                case ACCESS_DENIED:
                    return REApiExceptionType.ACCESS_DENIED;

                case UNKNOWN_ERROR:
                default:
                    return REApiExceptionType.UNKNOWN_ERROR;
            }
        }

    }

}
