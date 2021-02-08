package com.mycode.base.retrofitextension.call;

import java.lang.reflect.Type;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.plugins.RxJavaPlugins;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;

/**
 * Created by kyunghoon on 2019-04-19
 */
class RERxCallAdapter<R> implements CallAdapter<R, Object> {

    private final Type mResponseType;

    @Nullable
    private final Scheduler mScheduler;
    private final boolean mIsAsync;
    private final boolean mIsResult;
    private final boolean mIsBody;
    private final boolean mIsFlowable;
    private final boolean mIsSingle;
    private final boolean mIsMaybe;
    private final boolean mIsCompletable;
    private final CallOption mCallOption;

    RERxCallAdapter(Type responseType, @Nullable Scheduler scheduler, boolean isAsync,
                    boolean isResult, boolean isBody, boolean isFlowable, boolean isSingle, boolean isMaybe,
                    boolean isCompletable, @NonNull CallOption callOption) {
        this.mResponseType = responseType;
        this.mScheduler = scheduler;
        this.mIsAsync = isAsync;
        this.mIsResult = isResult;
        this.mIsBody = isBody;
        this.mIsFlowable = isFlowable;
        this.mIsSingle = isSingle;
        this.mIsMaybe = isMaybe;
        this.mIsCompletable = isCompletable;
        this.mCallOption = callOption;
    }

    @Override
    public Type responseType() {
        return mResponseType;
    }

    @Override
    public Object adapt(Call<R> call) {
        RECall reCall = new RECall<>(call, mCallOption);
        Observable<Response<R>> responseObservable = mIsAsync
                ? new CallEnqueueObservable<>(reCall, mCallOption)

                // 현재 설정은 아래를 탄다.
                : new CallExecuteObservable<>(reCall, mCallOption);

        Observable<?> observable;
        if (mIsResult) {
            // Return Parameter value 가 retrofit2.adapter.rxjava2.Result 일 때
            observable = new ResultObservable<>(responseObservable);
        } else if (mIsBody) {
            // Return Parameter value 가 일반 객체 일 때
            observable = new BodyObservable<>(responseObservable);
        } else {
            // Return Parameter value 가 retrofit2.Response 일 때
            observable = responseObservable;
        }

        if (mScheduler != null) {
            observable = observable.subscribeOn(mScheduler);
        }
        if (mIsFlowable) {
            return observable.toFlowable(BackpressureStrategy.LATEST);
        }
        if (mIsSingle) {
            return observable.singleOrError();
        }
        if (mIsMaybe) {
            return observable.singleElement();
        }
        if (mIsCompletable) {
            return observable.ignoreElements();
        }
        return RxJavaPlugins.onAssembly(observable);
    }


}
