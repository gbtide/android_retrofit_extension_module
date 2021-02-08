package com.mycode.base.retrofitextension.call;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import androidx.annotation.Nullable;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.Result;

/**
 * Created by kyunghoon on 2019-04-08
 *
 */
public class RECallAdapterFactory extends CallAdapter.Factory {

    @Nullable
    private final Scheduler scheduler;

    private final boolean isAsync;

    private RECallAdapterFactory(@Nullable Scheduler scheduler, boolean isAsync) {
        this.scheduler = scheduler;
        this.isAsync = isAsync;
    }

    public static RECallAdapterFactory create() {
        return new RECallAdapterFactory(null, false);
    }

    public static RECallAdapterFactory createAsync() {
        return new RECallAdapterFactory(null, true);
    }

    @SuppressWarnings("ConstantConditions")
    public static RECallAdapterFactory createWithScheduler(Scheduler scheduler) {
        if (scheduler == null) throw new NullPointerException("scheduler == null");
        return new RECallAdapterFactory(scheduler, false);
    }

    @Nullable
    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        Type returnParamType = getParameterUpperBound(0, (ParameterizedType) returnType);
        CallOption callOption = CallOptionParser.parse(annotations, getRawType(returnParamType));

        // [1] RX (1) - Completable
        Class<?> rawType = getRawType(returnType);
        if (rawType == Completable.class) {
            return new RERxCallAdapter<>(Void.class, scheduler, isAsync, false, true, false, false, false, true, callOption);
        }

        // [2] 일반 콜
        boolean isFlowable = rawType == Flowable.class;
        boolean isSingle = rawType == Single.class;
        boolean isMaybe = rawType == Maybe.class;
        if (rawType != Observable.class && !isFlowable && !isSingle && !isMaybe) {
            if (rawType != Call.class) {
                return null;
            }
            return new RESimpleCallAdapter(returnType, callOption);
        }

        // [3] RX (2) - Completable 외 나머지 RX
        boolean isResult = false;
        boolean isBody = false;
        Type responseType;
        if (!(returnType instanceof ParameterizedType)) {
            String name = isFlowable ? "Flowable"
                    : isSingle ? "Single"
                    : isMaybe ? "Maybe" : "Observable";
            throw new IllegalStateException(name + " return type must be parameterized as " + name + "<Foo> or " + name + "<? extends Foo>");
        }
        Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);

        Class<?> rawObservableType = getRawType(observableType);
        if (rawObservableType == Response.class) {
            if (!(observableType instanceof ParameterizedType)) {
                throw new IllegalStateException("Response must be parameterized as Response<Foo> or Response<? extends Foo>");
            }
            responseType = getParameterUpperBound(0, (ParameterizedType) observableType);
        } else if (rawObservableType == Result.class) {
            if (!(observableType instanceof ParameterizedType)) {
                throw new IllegalStateException("Result must be parameterized as Result<Foo> or Result<? extends Foo>");
            }
            responseType = getParameterUpperBound(0, (ParameterizedType) observableType);
            isResult = true;
        } else {
            responseType = observableType;
            isBody = true;
        }

        return new RERxCallAdapter(responseType, scheduler, isAsync, isResult, isBody, isFlowable, isSingle, isMaybe, false, callOption);
    }



}
