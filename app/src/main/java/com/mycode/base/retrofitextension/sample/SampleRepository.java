package com.mycode.base.retrofitextension.sample;

import com.mycode.base.retrofitextension.REApis;

import io.reactivex.Single;

/**
 * Sample Code
 */
public class SampleRepository {

    public static Single<SampleResult> getSample(int id, String ticket) {
        return getApis().getSample(id, ticket);
    }

    public static Single<SampleResult> getSamplePost(int id, String from) {
        return getApis().getSamplePost(id, from);
    }

    private static SampleApis getApis() {
        return REApis.getInstance().get(SampleApis.class);
    }

}
