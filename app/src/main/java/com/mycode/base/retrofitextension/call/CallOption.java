package com.mycode.base.retrofitextension.call;

import com.mycode.base.retrofitextension.annotation.Fileload;
import com.mycode.base.retrofitextension.annotation.Preload;
import com.mycode.base.retrofitextension.annotation.ResponseType;
import com.mycode.base.retrofitextension.annotation.Retry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by kyunghoon on 2019-05-16
 *
 * <p>
 * {@link RECallAdapterFactory} 에서 Request 요청에 적합한 {@link retrofit2.CallAdapter} 를 뽑기 위해
 * 현 요청에 대한 return 타입과 어노테이션을 받는다. ( 참고 : {@link RECallAdapterFactory#get(Type, Annotation[], Retrofit)}) <br/>
 * 이 때 return 타입{@link Type}과 어노테이션{@link Annotation}을 파싱해서 {@link CallOption} 을 만들고,
 * 생성된 콜옵션은 {@link retrofit2.CallAdapter}에 들어가서 {@link RECall}에 inject 된다.
 */
public class CallOption implements RxCallOption {

    private CallType callType;

    private RetryOption retry = new RetryOption();

    private PreloadOption preload = new PreloadOption();

    private FileloadOption fileload = new FileloadOption();

    private ResponseType.Value responseType = ResponseType.Value.getDefault();


    public RetryOption getRetry() {
        return retry;
    }

    @Override
    public PreloadOption getPreload() {
        return preload;
    }

    public CallType getCallType() {
        return callType;
    }

    @Override
    public FileloadOption getFileload() {
        return fileload;
    }

    public ResponseType.Value getResponseType() {
        return responseType;
    }

    public void setCallType(CallType callType) {
        this.callType = callType;
    }

    public void setRetry(RetryOption retry) {
        this.retry = retry;
    }

    public void setPreload(PreloadOption preload) {
        this.preload = preload;
    }

    public void setFileload(FileloadOption fileload) {
        this.fileload = fileload;
    }

    public void setResponseType(ResponseType.Value responseType) {
        this.responseType = responseType;
    }

    enum CallType {
        GET,
        POST
    }

}
