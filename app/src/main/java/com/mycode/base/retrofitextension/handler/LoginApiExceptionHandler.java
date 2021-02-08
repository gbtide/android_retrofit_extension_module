package com.mycode.base.retrofitextension.handler;

import com.mycode.base.retrofitextension.REApiException;

/**
 * Created by kyunghoon on 2019-05-31
 */
public class LoginApiExceptionHandler implements REApiExceptionHandler.ExceptionHandler {

//    private static Event<Void> sLoginApiErrorEvent = EventFactory.createEvent("loginApiErrorEvent");

    @Override
    public boolean handle(Throwable e) {
        if (!isCafeApiException(e)) {
            return false;
        }

        REApiException exception = (REApiException) e;

        switch (exception.getType()) {
            case NEED_TO_LOGIN:
                // Login 처리 가능한 Base Activity 에서 처리
//                sLoginApiErrorEvent.postValue(null);
                // 토스트는 띄워주는 것이 좋을 것 같아서 return false
                return false;
        }
        return false;
    }

//    public static Event<Void> getEvent() {
//        return sLoginApiErrorEvent;
//    }

    private boolean isCafeApiException(Throwable e) {
        return e instanceof REApiException;
    }

}
