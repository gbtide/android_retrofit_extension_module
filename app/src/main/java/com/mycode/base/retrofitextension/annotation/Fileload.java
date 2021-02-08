package com.mycode.base.retrofitextension.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by kyunghoon on 2020-03-02
 *
 * <p>
 * 네트워크가 아닌 파일만으로 데이터를 로딩합니다.  <br/>
 * {@link Fileload} 를 Api 에 쓰려면 동일 Api 를 한 번이라도 {@link Preload} 한 적이 있어야 합니다.  <p/>
 *
 * Sample : <br/>
 * 프리로드를 쓰고 싶으나, Response 응답 횟수가 중요한 Api (예를들면, Response 가 올 때마다 Paging 을 카운팅 한다든가)
 * 에는 아래와 같이 쓸 수 있습니다. <p/>
 *
 * @Preload(disableFileload = true) <br/>
 * @GET("/serviceapps/v3/aa") Observable<AAResponse> getAA(); <p/>
 *
 * @Fileload <br/>
 * @GET("/serviceapps/v3/aa") Observable<AAResponse> getAAFromFile(); <p/>
 *
 * 위 예제에서는 화면 진입 시, 파일로드를 먼저 콜하고 세팅한 후, 적절한 시점에 프리로딩을 콜하면 됩니다. <br/>
 *
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Fileload {
}
