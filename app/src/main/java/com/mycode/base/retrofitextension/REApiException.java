package com.mycode.base.retrofitextension;

import java.io.IOException;


/**
 * Created by kyunghoon on 2019-05-02
 * <p>
 * memo. Retrofit 의 Call.publish 중 에러 발생 시 Throwable 이 아닌 IOException 을 던져서 상속 구조를 아래와 같이 잡음.
 */
public class REApiException extends IOException {
    private final REApiExceptionType mType;
    private int httpStatusCode;
    private String bodyCode;

    private REApiException(String message,
                           REApiExceptionType type,
                           int httpStatusCode,
                           String bodyCode) {
        super(message);
        this.mType = type;
        this.httpStatusCode = httpStatusCode;
        this.bodyCode = bodyCode;
    }

    public REApiExceptionType getType() {
        return mType;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getBodyCode() {
        return bodyCode;
    }

    public static class Builder {
        private REApiExceptionType type;
        private String message;
        private int httpStatusCode;
        private String bodyCode;

        public Builder(REApiExceptionType exceptionType, String message) {
            this.type = exceptionType;
            this.message = message;
        }

        public Builder setBodyCode(String bodyCode) {
            this.bodyCode = bodyCode;
            return this;
        }

        public Builder setHttpStatusCode(int httpStatusCode) {
            this.httpStatusCode = httpStatusCode;
            return this;
        }

        public REApiException build() {
            return new REApiException(message, type, httpStatusCode, bodyCode);
        }
    }

}
