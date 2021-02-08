package com.mycode.base.retrofitextension.call;

import android.util.Log;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Created by kyunghoon on 2019-04-26
 */
class RetryAction<T> {
    private static final String TAG = "RetryAction";

    private final Call<T> mOriginalCall;
    private final RetryOption mRetryOption;

    RetryAction(Call<T> originalCall, RetryOption retryOption) {
        this.mOriginalCall = originalCall;
        this.mRetryOption = retryOption;
    }

    public Response<T> retry() throws Throwable {
        Response<T> response;
        int currentCount = 0;
        Throwable exception = new IOException("[ failed retry ] invalid maxCount");
        while (mRetryOption.maxCount > currentCount) {
            try {
                Thread.sleep(mRetryOption.interval);
                Log.d(TAG, "###N Retry call - " + mOriginalCall.request().url());
                currentCount++;
                Call<T> call = mOriginalCall.clone();
                response = call.execute();
                if (!response.isSuccessful()) {
                    // non http exception 은 throw 안함
                    throw new HttpException(response);
                }
                return response;
            } catch (Throwable e) {
                // 고민. 루프의 맨 마지막 에러만 throw 하도록 함.
                exception = e;
            }
        }
        throw exception;
    }

}
