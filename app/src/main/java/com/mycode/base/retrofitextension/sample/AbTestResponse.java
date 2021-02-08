package com.mycode.base.retrofitextension.sample;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kyunghoon on 2021-02-06
 *
 * Sample 예제입니다
 */
public class AbTestResponse {
    @SerializedName("result_code")
    public int resultCode;
    @SerializedName("result_data")
    public List<AbTest> resultData;

    public static class AbTest {
        @SerializedName("abTestNo")
        public long abTestNo;
        @SerializedName("description")
        public String description;
        @SerializedName("extra")
        public String extra;
    }

}
