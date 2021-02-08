package com.mycode.base.retrofitextension.handler;

import com.mycode.base.retrofitextension.exception.ExceptionMessageHelper;
import com.mycode.base.retrofitextension.Initializer;
import com.mycode.base.retrofitextension.REApiException;
import com.mycode.base.retrofitextension.REApiExceptionType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kyunghoon on 2019-04-24
 */
public class REApiExceptionHandler {
    private Throwable mException;
    private ErrorInterceptor mInterceptor;
    private List<ExceptionHandler> mExceptionHandlerList = new ArrayList<>();

    private ToastExceptionHandler mToastExceptionHandler = new ToastExceptionHandler();


    public REApiExceptionHandler(Throwable exception) {
        this.mException = exception;

        // 먼저 처리되는 것이 우선 순위가 높습니다.
        // Login 처리 -> Toast 처리
        this.mExceptionHandlerList.add(new LoginApiExceptionHandler());
        this.mExceptionHandlerList.add(mToastExceptionHandler);

    }

    /**
     * 특정 에러만 처리하고, 나머지는 공통 처리 모듈에 맡깁니다.
     */
    public REApiExceptionHandler setInterceptor(ErrorInterceptor interceptor) {
        this.mInterceptor = interceptor;
        return this;
    }

    public REApiExceptionHandler setCustomExceptionHandlers(List<ExceptionHandler> list) {
        this.mExceptionHandlerList = list;
        return this;
    }


    public REApiExceptionHandler setToastOff() {
        if (Initializer.getInstance().isDebug()) {
            return this;
        }
        mToastExceptionHandler.setToastOff();
        return this;
    }

    public REApiExceptionHandler setToastOn() {
        mToastExceptionHandler.setToastOn();
        return this;
    }

    /**
     * @param specialTypes : 정의된 타입 외 나머지 토스트는 Off 합니다.
     * @return
     */
    public REApiExceptionHandler setToastOnOnly(REApiExceptionType[] specialTypes) {
        mToastExceptionHandler.setToastOnOnly(specialTypes);
        return this;
    }

    /**
     * 에러 문구만 빼고 싶을 때 사용
     */
    public String getErrorMessage() {
        return ExceptionMessageHelper.getErrorMessage(mException);
    }

    public void handle() {
        if (handleByInterceptor()) {
            return;
        }

        if (mExceptionHandlerList != null) {
            for (ExceptionHandler handler : mExceptionHandlerList) {
                if (handler.handle(mException)) {
                    return;
                }
            }
        }
    }

    private boolean handleByInterceptor() {
        if (mException instanceof REApiException
                && ((REApiException) mException).getType() != null
                && mInterceptor != null) {
            return mInterceptor.handle(((REApiException) mException).getType());
        }
        return false;
    }

    public interface ErrorInterceptor {
        boolean handle(REApiExceptionType type);
    }

    public interface ExceptionHandler {
        boolean handle(Throwable e);

    }

}
