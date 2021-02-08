package com.mycode.base.retrofitextension.call;

import io.reactivex.disposables.Disposable;
import retrofit2.Call;

/**
 * Created by kyunghoon on 2019-05-17
 */
final class CallDisposable implements Disposable {
    private final Call<?> call;
    private volatile boolean disposed;

    CallDisposable(Call<?> call) {
        this.call = call;
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
}
