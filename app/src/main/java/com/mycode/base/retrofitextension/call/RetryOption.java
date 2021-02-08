package com.mycode.base.retrofitextension.call;

/**
 * Created by kyunghoon on 2019-05-16
 */
class RetryOption {
    static final int DEFAULT_RETRY_COUNT_IF_GET_REQUEST = 1;

    /**
     * 0이 설정값일 수 있음
     */
    static final int NOT_SET_RETRY_COUNT = -1;
    static final long DEFAULT_INTERVAL_MILLIS = 100;

    int maxCount = NOT_SET_RETRY_COUNT;
    long interval = DEFAULT_INTERVAL_MILLIS;

    RetryOption() {
    }

    RetryOption(int maxCount) {
        this.maxCount = maxCount;
    }

    RetryOption(int maxCount, long interval) {
        this.maxCount = maxCount;
        this.interval = interval;
    }

}
