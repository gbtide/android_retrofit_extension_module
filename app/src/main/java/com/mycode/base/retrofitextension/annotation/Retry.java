package com.mycode.base.retrofitextension.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by kyunghoon on 2019-04-25
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Retry {
    /**
     * @GET 은 기본 1입니다
     * {@link com.mycode.base.retrofitextension.call.RetryOption#DEFAULT_RETRY_COUNT_IF_GET_REQUEST} 참고
     */
    int count() default 0;

    /**
     * @return 단위 : ms
     */
    long interval() default 100;

}
