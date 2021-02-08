package com.mycode.base.retrofitextension.call;

import android.util.Log;

import com.mycode.base.retrofitextension.REApiException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by kyunghoon on 2019-05-17
 */
abstract class PreloadRxAction<T> {
    private static final String TAG = "PreloadRxAction";

    private static final Object KEY = new Object();

    private PreloadFileIOAction<T> mPreloadFileIOAction;

    private Scheduler.Worker mWorker;

    PreloadRxAction(PreloadFileIOAction<T> preloadFileIOAction) {
        this.mPreloadFileIOAction = preloadFileIOAction;
        mWorker = Schedulers.computation().createWorker();
    }

    public void execute(PreloadCallback<T> callback) {

        Observable<PreloadResponse<T>> fileObservable = Observable.defer(() -> {
            Response<T> response = mPreloadFileIOAction.load();
            PreloadResponse<T> preloadResponse = new PreloadResponse<>(response, PreloadResponse.From.FILE);
            return Observable.just(preloadResponse);
        });

        Observable<PreloadResponse<T>> networkObservable = Observable.defer(() -> {
            Response<T> response = executeCallByNetwork();
            PreloadResponse<T> preloadResponse = new PreloadResponse<>(response, PreloadResponse.From.NETWORK);
            return Observable.just(preloadResponse);
        });

        Observable.mergeDelayError(fileObservable, networkObservable)
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultObserver<PreloadResponse<T>>() {
                    private boolean mNetworkLoadSuccess = false;

                    @Override
                    public void onNext(PreloadResponse<T> t) {
                        synchronized (KEY) {
                            switch (t.getFrom()) {
                                case FILE:
                                    if (mNetworkLoadSuccess || t.getResponse() == null) {
                                        return;
                                    }
                                    break;

                                case NETWORK:
                                    mNetworkLoadSuccess = true;
                                    break;
                            }
                        }

                        switch (t.getFrom()) {
                            case FILE:
                                Log.d(TAG, "###N [ preload ] success from file : onNext - " + t);
                                callback.onNextFromFile(t.getResponse());
                                break;

                            case NETWORK:
                                Log.d(TAG, "###N [ preload ] success from network : onNext - " + t);
                                callback.onNextFromNetwork(t.getResponse());
                                mPreloadFileIOAction.saveAsync(t.getResponse());
                                break;
                        }
                    }

                    // memo. http://reactivex.io/documentation/operators/merge.html
                    //
                    // In many ReactiveX implementations there is a second operator, MergeDelayError, that changes this behavior —
                    // reserving onError notifications until all of the merged Observables complete and only then passing it along to the observers:
                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "###N [ preload ] error : " + e);

                        // API 콜에서 발생한 오류만 바깥으로 알려줌
                        if (e instanceof REApiException) {
                            // 이슈 : https://github.com/ReactiveX/RxJava/issues/2887
                            // onNext 바로 뒤에 onError가 불리면 구독측에서 onNext를 못 받을 수 있음.
                            // mergeDelayError 이기 때문에 onNext 후 간격이 보장됨.
                            mWorker.schedule(() -> {
                                callback.onError(e);
                            }, 800, TimeUnit.MILLISECONDS);
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "###N [ preload ] complete");
                        callback.onComplete();
                    }
                });
    }

    abstract Response<T> executeCallByNetwork() throws IOException;

    interface PreloadCallback<T> {
        void onNextFromFile(Response<T> response);

        void onNextFromNetwork(Response<T> response);

        void onError(Throwable e);

        void onComplete();

    }

}
