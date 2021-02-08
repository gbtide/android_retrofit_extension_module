package com.mycode.base.retrofitextension.exception;

import com.mycode.base.retrofitextension.annotation.ResponseType;

/**
 * Created by kyunghoon on 2021-02-02
 *
 */
public class REApiExceptionMakerFactory {

    public static REApiExceptionMaker create(ResponseType.Value typeValue) {
        switch (typeValue) {
            // 3rd party 추가 될 때마다 정의 가능
//            case ABTEST:
//                return some;
//
//            case AD:
//                return some;

            case COMMON:
            default:
                return new REApiExceptionMakerForCommon();
        }
    }

}
