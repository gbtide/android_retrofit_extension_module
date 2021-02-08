package com.mycode.base.retrofitextension.call;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

/**
 * Created by kyunghoon on 2020-03-02
 */
class FileloadRxAction<T> {

    private Class<?> mClazz;
    private String mUrl;
    private Disposable mDisposable;
    private Observer<? super Response<T>> mObserver;

    FileloadRxAction(Class<?> clazz, String url, Disposable disposable, Observer<? super Response<T>> observer) {
        this.mClazz = clazz;
        this.mUrl = url;
        this.mDisposable = disposable;
        this.mObserver= observer;
    }

    public void execute() {
        PreloadFileIOAction<T> preloadFileIOAction = new PreloadFileIOAction<>(mUrl, mClazz, false);
        Response<T> response = preloadFileIOAction.load();
        if (response != null) {
            if (!mDisposable.isDisposed()) {
                mObserver.onNext(response);
            }
        }
        if (!mDisposable.isDisposed()) {
            mObserver.onComplete();
        }
    }

}
