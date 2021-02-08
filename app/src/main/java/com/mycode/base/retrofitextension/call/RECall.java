package com.mycode.base.retrofitextension.call;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.Request;
import okio.Timeout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Created by kyunghoon on 2019-04-21
 *
 */
class RECall<T> implements Call<T> {

    private final Call<T> mOriginalCall;
    private final CallOption mCallOption;
    private final CallErrorHandler<T> mErrorHandler;

    /**
     * @param call : {@link okhttp3.internal.connection.RealCall}을 들고 있는 {@link retrofit2.OkHttpCall}이 넘어옴
     */
    RECall(@NonNull Call<T> call, @NonNull CallOption callOption) {
        this.mOriginalCall = call;
        this.mCallOption = callOption;
        this.mErrorHandler = new CallErrorHandler<>(call, callOption);
    }

    @Override
    public Response<T> execute() throws IOException {
        Response<T> response;
        try {
            response = mOriginalCall.execute();
        } catch (Throwable e) {
            return mErrorHandler.handle(e);
        }

        // memo.
        // NON-2XX 이면, Converter 안거치고, Throwable 안거치고 바로 아래로 들어옴.
        // OkHttpCall.java:186 참조
        if (!response.isSuccessful()) {
            return mErrorHandler.handle(new HttpException(response));
        }
        return response;
    }

    @Override
    public void enqueue(Callback<T> callback) {
        mOriginalCall.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (!response.isSuccessful()) {
                    mErrorHandler.handleAsync(new HttpException(response), call, callback);
                    return;
                }
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                mErrorHandler.handleAsync(t, call, callback);
            }
        });
    }

    @Override
    public boolean isExecuted() {
        return mOriginalCall.isExecuted();
    }

    @Override
    public void cancel() {
        mOriginalCall.cancel();
    }

    @Override
    public boolean isCanceled() {
        return mOriginalCall.isCanceled();
    }

    @Override
    public Call<T> clone() {
        return new RECall<>(mOriginalCall.clone(), mCallOption);
    }

    @Override
    public Request request() {
        return mOriginalCall.request();
    }

    @Override
    public Timeout timeout() {
        return mOriginalCall.timeout();
    }
}
