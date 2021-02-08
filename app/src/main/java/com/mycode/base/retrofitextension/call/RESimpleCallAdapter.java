package com.mycode.base.retrofitextension.call;

import com.mycode.base.retrofitextension.utility.ApiUtility;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.CallAdapter;

/**
 * Created by kyunghoon on 2019-04-25
 */
class RESimpleCallAdapter implements CallAdapter<Object, Call<?>> {

    final private Type mReturnType;
    final private CallOption mCallOption;

    RESimpleCallAdapter(Type returnType, @NonNull CallOption callOption) {
        this.mReturnType = returnType;
        this.mCallOption = callOption;
    }

    @Override
    public Type responseType() {
        if (!(mReturnType instanceof ParameterizedType)) {
            throw new IllegalArgumentException("Call return type must be parameterized as Call<Foo> or Call<? extends Foo>");
        }
        return ApiUtility.getParameterUpperBound(0, (ParameterizedType) mReturnType);
    }

    @Override
    public Call<Object> adapt(Call<Object> call) {
        return new RECall<>(call, mCallOption);
    }

}
