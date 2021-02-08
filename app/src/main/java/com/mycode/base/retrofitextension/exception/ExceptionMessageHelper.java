package com.mycode.base.retrofitextension.exception;

import android.content.res.Resources;

import com.mycode.base.retrofitextension.Initializer;
import com.mycode.base.retrofitextension.REApiException;
import com.mycode.base.retrofitextension.REApiExceptionType;
import com.mycode.base.retrofitextension.utility.StringUtility;

/**
 * Created by kyunghoon on 2019-06-17
 */
public class ExceptionMessageHelper {

    private static final String UNKOWN_ERROR_MESSAGE = "# Unknown Temporary Error!";

    public static String getErrorMessage(Throwable e) {
        if (e == null) {
            return UNKOWN_ERROR_MESSAGE;
        }

        try {
            return getString(getErrorMessageResId(e));
        } catch (Resources.NotFoundException err) {
            // 서버에서 내려준 메시지를 기본으로 노출
            return StringUtility.isNullOrEmpty(e.getMessage()) ?
                    UNKOWN_ERROR_MESSAGE :
                    e.getMessage();

        }
    }

    private static int getErrorMessageResId(Throwable e) throws Resources.NotFoundException {
        if (e instanceof REApiException) {
            REApiException exception = (REApiException) e;
            if (exception.getType() == REApiExceptionType.UNKNOWN_ERROR
                    || exception.getType() == REApiExceptionType.NOT_HANDLED_NON_2XX_ERROR) {
                throw new Resources.NotFoundException();
            }
            return exception.getType().getErrorMessageResId();

        }
        throw new Resources.NotFoundException();
    }

    private static String getString(int resId) {
        return Initializer.getInstance().getContext().getString(resId);
    }

}
