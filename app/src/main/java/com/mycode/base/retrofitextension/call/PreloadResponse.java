package com.mycode.base.retrofitextension.call;

import retrofit2.Response;

/**
 * Created by kyunghoon on 2019-05-17
 */
final class PreloadResponse<T> {

    private Response<T> response;

    private From from;

    PreloadResponse(Response<T> response, From from) {
        this.response = response;
        this.from = from;
    }

    public Response<T> getResponse() {
        return response;
    }

    public From getFrom() {
        return from;
    }

    public boolean fromNetwork() {
        return from == From.NETWORK;
    }

    enum From {
        FILE,
        NETWORK
    }

}
