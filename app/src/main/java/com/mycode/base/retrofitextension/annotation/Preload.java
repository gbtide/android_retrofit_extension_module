package com.mycode.base.retrofitextension.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by kyunghoon on 2019-05-16 <p/>
 *
 * 꼭 확인! <p/>
 * 1) RX Response 형태만 지원합니다. <br/>
 * 2) url 을 캐시 파일 이름 키로 쓰고 있습니다. 너무 자주 바뀌는 파라미터가 있으면 키값으로 쓰이기 어려우니,
 * {@link com.mycode.base.retrofitextension.call.PreloadFileNameCreator#INVALID_PARAMETERS_FOR_KEY} 에 추가해주세요.
 *
 *
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Preload {
    /**
     * {@link Fileload} 주석 참고
     */
    boolean disableFileload() default false;

}
