package com.mycode.base.retrofitextension.handler;

import android.widget.Toast;

import com.mycode.base.retrofitextension.exception.ExceptionMessageHelper;
import com.mycode.base.retrofitextension.Initializer;
import com.mycode.base.retrofitextension.REApiException;
import com.mycode.base.retrofitextension.REApiExceptionType;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Created by kyunghoon on 2019-06-17
 */
public class ToastExceptionHandler implements REApiExceptionHandler.ExceptionHandler {

    private ToastOption mToastOption = ToastOption.ALL;
    private REApiExceptionType[] mExceptionTypesForToastExposure = null;

    @Override
    public boolean handle(Throwable e) {
        return showToastAccordingToConditions(e);
    }

    private boolean showToastAccordingToConditions(Throwable e) {
        switch (mToastOption) {
            case ALL:
                return showToast(ExceptionMessageHelper.getErrorMessage(e));

            case ALLOW_SPECIAL_TOAST:
                return showSpecialToast(e, ExceptionMessageHelper.getErrorMessage(e));

            case OFF:
                return true;
        }
        return false;
    }

    private boolean showToast(String message) {
        Toast.makeText(Initializer.getInstance().getContext(), message, Toast.LENGTH_SHORT).show();
        return true;
    }

    private boolean showSpecialToast(Throwable e, String message) {
        if (e instanceof REApiException) {
            REApiExceptionType errorType = ((REApiException) e).getType();
            if (!ArrayUtils.isEmpty(mExceptionTypesForToastExposure) && errorType != null) {
                for (REApiExceptionType typeOfToastAllowed : mExceptionTypesForToastExposure) {
                    if (errorType == typeOfToastAllowed) {
                        return showToast(message);
                    }
                }
            }
        }
        return false;
    }

    public void setToastOn() {
        mToastOption = ToastOption.ALL;
    }

    public void setToastOnOnly(REApiExceptionType[] specialTypes) {
        mToastOption = ToastOption.ALLOW_SPECIAL_TOAST;
        mExceptionTypesForToastExposure = specialTypes;
    }

    public void setToastOff() {
        mToastOption = ToastOption.OFF;
    }

    private enum ToastOption {
        // 모든 토스트 활성화
        ALL,

        // 특정 토스트만 활성화
        ALLOW_SPECIAL_TOAST,

        // 토스트 비활성화
        OFF
    }

}
