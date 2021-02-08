package com.mycode.base.retrofitextension.sample;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import androidx.annotation.Keep;

@Keep
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message<T> {
    public int status;
    public T result;
    public Error error;

    public int getStatus() {
        return status;
    }

    public T getResult() {
        return result;
    }

    public Error getError() {
        return error;
    }

    @Keep
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Error {
        String code;

        public String getCode() {
            return code;
        }
    }

}