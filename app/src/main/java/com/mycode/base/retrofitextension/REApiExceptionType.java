package com.mycode.base.retrofitextension;

import androidx.annotation.StringRes;

/**
 * Created by kyunghoon on 2019-05-02
 */
public enum REApiExceptionType {

    // [ 1 ] COMMON
    //
    /**
     * Body 가 약속한 형태로 내려오지 않았습니다.
     */
    INVALID_RESPONSE_BODY(From.COMMON, R.string.service_api_error_invalid_response_body, false),

    UNSUPPORTED_ENCODING_ERROR(From.COMMON, R.string.service_api_error_unsupported_encoding, false),

    JSON_PARSER_ERROR(From.COMMON, R.string.service_api_error_json_parser_error, false),

    /**
     * NON 2XX 인데, 특별히 처리하지 않은 케이스. REApiExceptionMaker 참조
     */
    NOT_HANDLED_NON_2XX_ERROR(From.COMMON, R.string.service_api_error_not_handled_non_2xx_error, true),

    /**
     * {@link java.net.UnknownHostException} 네트워크 끊어졌을 때 떨어지는 에러
     */
    DISCONNECTED_NETWORK_ERROR(From.COMMON, R.string.service_api_error_access_denied_by_gateway, true),

    UNKNOWN_ERROR(From.COMMON, R.string.service_api_error_unknown_error, true),


    // [ 2 ] GATEWAY
    //
    ACCESS_DENIED(From.GATEWAY, R.string.service_api_error_access_denied_by_gateway, false),


    // [ 3 ] SERVICE SERVER
    //
    NEED_TO_LOGIN(From.SERVICE, R.string.service_api_error_need_to_login, false),

    SYSTEM_CHECKING(From.SERVICE, R.string.service_api_error_system_checking, false),
    ;

    /**
     * 어디서 발생한 에러인지 식별하기 위한 변수.
     */
    private From from;

    private @StringRes int errorMessageResId;

    private boolean defaultRetryEnabled;

    REApiExceptionType(From from, int errorMessageResId, boolean defaultRetryEnabled) {
        this.from = from;
        this.errorMessageResId = errorMessageResId;
        this.defaultRetryEnabled = defaultRetryEnabled;
    }

    public int getErrorMessageResId() {
        return errorMessageResId;
    }

    public boolean isDefaultRetryEnabled() {
        return defaultRetryEnabled;
    }

    private enum From {
        COMMON,
        GATEWAY,
        SERVICE
        ;
    }

}
