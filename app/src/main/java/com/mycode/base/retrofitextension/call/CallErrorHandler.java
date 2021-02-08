package com.mycode.base.retrofitextension.call;

import android.util.Log;

import com.mycode.base.retrofitextension.REApiException;
import com.mycode.base.retrofitextension.exception.REApiExceptionMaker;
import com.mycode.base.retrofitextension.exception.REApiExceptionMakerFactory;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kyunghoon on 2019-05-03
 */
class CallErrorHandler<T> {
    private static final String TAG = "CallErrorHandler";

    private final CallOption mCallOption;
    private final Call<T> mOriginalCall;

    CallErrorHandler(@NonNull Call<T> call, @NonNull CallOption callOption) {
        this.mOriginalCall = call;
        this.mCallOption = callOption;
    }

    public Response<T> handle(Throwable e) throws REApiException {
        return handleInternal(e);
    }

    public void handleAsync(Throwable e, Call call, Callback<T> originCallback) {
        Observable<Response<T>> observable = Observable.defer(() -> {
            Response<T> response = handleInternal(e);
            return Observable.just(response);
        });

        observable.subscribeOn(Schedulers.io())
                .subscribe(response -> {
                    originCallback.onResponse(call, response);
                }, error -> {
                    originCallback.onFailure(call, error);
                });
    }

    /**
     * memo.
     * 공통으로 처리해야할 에러가 있다면 여기에서 처리!
     */
    private Response<T> handleInternal(Throwable throwable) throws REApiException {
        REApiException exception = makeREApiException(throwable);

        try {
            // 1. 특별한 에러에 대한 처리
            switch (exception.getType()) {
                // case 특정 에러
            }

            // 2. Retry 처리
            if (mCallOption.getRetry().maxCount > 0) {
                return new RetryAction<>(mOriginalCall, mCallOption.getRetry()).retry();

            } else if (CallOptionUtility.notSetRetryCount(mCallOption)) {
                // CallOptionUtility.isGetRequest(mCallOption) 와 같은 조건을 넣을 수 있음.
                if (exception.getType().isDefaultRetryEnabled()) {
                    Log.i(TAG, "###N failed to GET request, so retry " + RetryOption.DEFAULT_RETRY_COUNT_IF_GET_REQUEST + " more time");
                    RetryAction<T> action = new RetryAction<>(mOriginalCall, new RetryOption(RetryOption.DEFAULT_RETRY_COUNT_IF_GET_REQUEST));
                    return action.retry();
                }
            }

        } catch (Throwable err) {
            // failover 처리 중 발생하는 에러
            REApiException e = makeREApiException(err);
            throw e;
        }
        throw exception;
    }

    private REApiException makeREApiException(Throwable t) {
        return REApiExceptionMakerFactory.create(mCallOption.getResponseType()).makeFrom(t);
    }

}
