package com.mycode.base.retrofitextension.call;

import android.util.Log;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.plugins.RxJavaPlugins;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by kyunghoon on 2019-04-20
 */
final class CallExecuteObservable<T> extends Observable<Response<T>> {
    private static final String TAG = "CallExecuteObservable";

    private final Call<T> mOriginalCall;
    private final RxCallOption mCallOption;

    CallExecuteObservable(Call<T> originalCall, RxCallOption callOption) {
        this.mOriginalCall = originalCall;
        this.mCallOption = callOption;
    }

    @Override
    protected void subscribeActual(Observer<? super Response<T>> observer) {
        // memo. clone 을 왜?
        // Since Call is a one-shot type, clone it for each new observer.
        Call<T> call = mOriginalCall.clone();

        CallDisposable disposable = new CallDisposable(call);
        observer.onSubscribe(disposable);
        if (disposable.isDisposed()) {
            return;
        }

        boolean terminated = false;
        try {
            // A. 프리로드 요청
            if (PreloadCallVerifier.isPreload(call, mCallOption.getPreload())) {
                Log.d(TAG, "###N request by preload : " + call.request().url());
                executeByPreload(observer, call, disposable);
                return;
            }

            // B. 파일로드 요청
            if (mCallOption.getFileload().isActive()) {
                Log.d(TAG, "###N request by file load : " + call.request().url());
                Class<?> clazz = mCallOption.getFileload().getRawType();
                String url = call.request().url().toString();
                new FileloadRxAction<>(clazz, url, disposable, observer).execute();
                return;
            }

            // C. 일반 요청
            Response<T> response = call.execute();
            if (!disposable.isDisposed()) {
                observer.onNext(response);
            }
            if (!disposable.isDisposed()) {
                terminated = true;
                observer.onComplete();
            }

        } catch (Throwable t) {
            Exceptions.throwIfFatal(t);
            if (terminated) {
                RxJavaPlugins.onError(t);
            } else if (!disposable.isDisposed()) {
                try {
                    observer.onError(t);
                } catch (Throwable inner) {
                    Exceptions.throwIfFatal(inner);
                    RxJavaPlugins.onError(new CompositeException(t, inner));
                }
            }
        }
    }

    private void executeByPreload(Observer<? super Response<T>> observer, Call<T> call, CallDisposable disposable) throws IOException {
        Class<?> clazz = mCallOption.getPreload().getRawType();
        String url = call.request().url().toString();
        PreloadRxAction<T> preload = new PreloadRxAction(new PreloadFileIOAction<T>(url, clazz, mCallOption.getPreload().isFileloadDisabled())) {
            @Override
            protected Response<T> executeCallByNetwork() throws IOException {
                return call.execute();
            }
        };
        preload.execute(new PreloadRxAction.PreloadCallback<T>() {

            @Override
            public void onNextFromFile(Response<T> response) {
                if (isValidContext()) {
                    observer.onNext(response);
                }
            }

            @Override
            public void onNextFromNetwork(Response<T> response) {
                if (isValidContext()) {
                    observer.onNext(response);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (isValidContext()) {
                    observer.onError(e);
                }
            }

            @Override
            public void onComplete() {
                if (isValidContext()) {
                    observer.onComplete();
                }
            }

            private boolean isValidContext() {
                return (!disposable.isDisposed() && !call.isCanceled());
            }
        });
    }

}

