package com.mycode.base.retrofitextension.sample;

import android.text.TextUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import androidx.annotation.Keep;

/**
 * Response 예시 입니다.
 */
@Keep
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseJsonObject<T> {

	public Message<T> message;

	public Message<T> getMessage() {
		return message;
	}

	@SerializedName("error_code")
	@JsonProperty("error_code")
	private String gatewayErrorCode;

	public String getServiceErrorCode() {
		if (message == null
				|| message.getError() == null
				|| TextUtils.isEmpty(message.getError().getCode())) {
			return null;
		} else {
			return message.getError().getCode();
		}
	}

	public String getGatewayErrorCode() {
		return gatewayErrorCode;
	}

}
