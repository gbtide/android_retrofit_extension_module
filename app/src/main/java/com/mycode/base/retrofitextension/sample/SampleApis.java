package com.mycode.base.retrofitextension.sample;

import com.mycode.base.retrofitextension.annotation.Preload;
import com.mycode.base.retrofitextension.annotation.Retry;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Sample Code 입니다.
 */
public interface SampleApis {

    @Retry(count = 2)
    @GET("/apps/sample.json")
    Single<SampleResult> getSample(@Query("id") int id, @Query("ticket") String ticket);

    @Preload
    @POST("/apps/cafe/samplePost.json")
    Single<SampleResult> getSamplePost(@Field("id") int id, @Field("from") String from);

}
