package com.mycode.base.retrofitextension.call;

/**
 * Created by kyunghoon on 2019-05-30
 */
public class CallOptionUtility {

    public static boolean isGetRequest(CallOption callOption) {
        return (callOption != null && CallOption.CallType.GET == callOption.getCallType());
    }

    public static boolean notSetRetryCount(CallOption callOption) {
        return (callOption == null ||
                callOption.getRetry() == null ||
                callOption.getRetry().maxCount == RetryOption.NOT_SET_RETRY_COUNT);
    }

}
