package com.mycode.base.retrofitextension.sample;

import com.mycode.base.retrofitextension.REApiExceptionType;
import com.mycode.base.retrofitextension.utility.StringUtility;

/**
 * Created by kyunghoon on 2021-02-02
 *
 * 예제 코드입니다.
 */
public enum ServiceBodyResponseCode {

    SUCCESS("200"),

    /**
     * 로그인 필요
     */
    NEED_TO_LOGIN("3001"),

    /**
     * 시스템 점검
     */
    SYSTEM_CHECKING("4000"),

    /**
     * 기타 서버 오류 외, 정의되지 않은 에러
     */
    UNKNOWN_ERROR("9999");

    private String code;

    ServiceBodyResponseCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static ServiceBodyResponseCode findBy(String code) {
        for (ServiceBodyResponseCode each : ServiceBodyResponseCode.values()) {
            if (StringUtility.equals(each.getCode(), code)) {
                return each;
            }
        }
        return UNKNOWN_ERROR;
    }


    public static class Converter {

        public static REApiExceptionType from(ServiceBodyResponseCode responseCode) {
            switch (responseCode) {
                case NEED_TO_LOGIN:
                    // memo. 아래는 카페 서버 로그인 에러와 같습니다.
                    // 현재 스펙상 특별히 카페 서버 네이버 로그인 에러와 분기를 둘 필요는 없고,
                    // 모듈 내에서 일괄적으로 처리하는 에러라 아래와 같이 설정함.
                    return REApiExceptionType.NEED_TO_LOGIN;

                case SYSTEM_CHECKING:
                    return REApiExceptionType.SYSTEM_CHECKING;

                case UNKNOWN_ERROR:
                default:
                    return REApiExceptionType.UNKNOWN_ERROR;
            }
        }

    }

}
