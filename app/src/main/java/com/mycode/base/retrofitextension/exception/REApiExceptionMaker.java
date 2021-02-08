package com.mycode.base.retrofitextension.exception;

import com.mycode.base.retrofitextension.REApiException;

import androidx.annotation.NonNull;

/**
 * Created by kyunghoon on 2021-02-02
 */
public interface REApiExceptionMaker {

    REApiException makeFrom(@NonNull Throwable error);

}
