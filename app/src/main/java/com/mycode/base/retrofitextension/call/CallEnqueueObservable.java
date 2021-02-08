package com.mycode.base.retrofitextension.call;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kyunghoon on 2019-04-20
 *
 */
final class CallEnqueueObservable<T> extends Observable<Response<T>> {
    private static final String TAG = "CallEnqueueObservable";

    private static final Object KEY = new Object();
    private final RxCallOption mCallOption;
    private final Call<T> mOriginalCall;

    CallEnqueueObservable(Call<T> originalCall, RxCallOption callOption) {
        this.mOriginalCall = originalCall;
        this.mCallOption = callOption;
    }

    @Override
    protected void subscribeActual(Observer<? super Response<T>> observer) {
        Call<T> call = mOriginalCall.clone();
        CallCallback<T> callback = new CallCallback<>(call, observer);
        observer.onSubscribe(callback);

        if (PreloadCallVerifier.isPreload(call, mCallOption.getPreload())) {
            Log.d(TAG, "###N request by preload : " + call.request().url());
            enqueueByPreload(observer, call, callback);
            return;
        }

        if (mCallOption.getFileload().isActive()) {
            Class<?> clazz = mCallOption.getFileload().getRawType();
            String url = call.request().url().toString();
            new FileloadRxAction<>(clazz, url, callback, observer).execute();
            return;
        }

        if (!callback.isDisposed()) {
            call.enqueue(callback);
        }
    }

    private static final class CallCallback<T> implements Disposable, Callback<T> {
        private final Call<?> call;
        private final Observer<? super Response<T>> observer;
        private volatile boolean disposed;
        boolean terminated = false;
        boolean hasResponseArrived = false;

        CallCallback(Call<?> call, Observer<? super Response<T>> observer) {
            this.call = call;
            this.observer = observer;
        }

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            synchronized (KEY) {
                hasResponseArrived = true;
            }

            if (disposed) return;

            try {
                observer.onNext(response);

                if (!disposed) {
                    terminated = true;
                    observer.onComplete();
                }
            } catch (Throwable t) {
                if (terminated) {
                    RxJavaPlugins.onError(t);
                } else if (!disposed) {
                    try {
                        observer.onError(t);
                    } catch (Throwable inner) {
                        Exceptions.throwIfFatal(inner);
                        RxJavaPlugins.onError(new CompositeException(t, inner));
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            if (call.isCanceled()) return;

            try {
                observer.onError(t);
            } catch (Throwable inner) {
                Exceptions.throwIfFatal(inner);
                RxJavaPlugins.onError(new CompositeException(t, inner));
            }
        }

        @Override
        public void dispose() {
            disposed = true;
            call.cancel();
        }

        @Override
        public boolean isDisposed() {
            return disposed;
        }

        public boolean hasResponseArrived() {
            return hasResponseArrived;
        }
    }

    private void enqueueByPreload(Observer<? super Response<T>> observer, Call<T> call, CallCallback<T> callback) {
        Class<?> clazz = mCallOption.getPreload().getRawType();
        String url = call.request().url().toString();

        Observable<Response<T>> observable = Observable.fromCallable(() -> {
            PreloadFileIOAction<T> preloadFileIOAction = new PreloadFileIOAction<>(url, clazz, mCallOption.getPreload().isFileloadDisabled());
            Response<T> response = preloadFileIOAction.load();
            return response;
        });

        observable.subscribeOn(Schedulers.io())
                .subscribe(success -> {
                    synchronized (KEY) {
                        if (callback.isDisposed() || callback.hasResponseArrived() || success == null) {
                            return;
                        }
                    }
                    observer.onNext(success);
                });

        if (!callback.isDisposed()) {
            call.enqueue(callback);
        }
    }

}
