package com.mycode.base.retrofitextension.sample;

/**
 * Created by user on 2021-02-08
 *
 * Sample code 입니다.
 */
public class SampleResult {

    private long userNo;
    private String userName;

    public long getUserNo() {
        return userNo;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "SampleResult{" +
                "userNo=" + userNo +
                ", userName='" + userName + '\'' +
                '}';
    }
}
