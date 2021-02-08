package com.mycode.base.retrofitextension.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by kyunghoon on 2021-02-02
 * <p>
 * 명시된 ResponseType 을 기반으로 Validation, Converting 이 진행되고 타입에 맞는 Error 가 발생합니다.
 *
 * <p/>
 * [ 진행 가이드 ]
 * 1. Value 에 타입을 추가한다.
 * 2. {@link com.mycode.base.retrofitextension.converter.ResponseBodyConverterFactory}에 Converter 를 하나 끼워넣는다.
 * 3. {@link com.mycode.base.retrofitextension.exception.REApiExceptionMakerFactory}에 Exception Maker 정의
 * 4. API 위에 @ResponseType 을 붙여서 사용한다.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseType {

    Value value();

    enum Value {
        COMMON,

        // 3rd는 아래와 같이 추가 가능합니다.
//        /**
//         * AB TEST 응답
//         */
//        ABTEST,
//
//        /**
//         * 광고 배너 응답
//         */
//        AD
        ;

        public static Value getDefault() {
            return COMMON;
        }
    }

}
