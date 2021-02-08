package com.mycode.base.retrofitextension.sample;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import androidx.annotation.Keep;

/**
 * Response 예시 입니다.
 */
@Keep
@Root(name = "", strict = false)
public class BaseXmlObject {

	@Path("error")
	@Element(name = "error_code", required = false)
	public String serviceErrorCode;

	@Path("error")
	@Element(name = "message", required = false)
	public String serviceErrorMessage;

	@Element(name = "error_code", required = false)
	public String gatewayErrorCode;

	@Element(name = "message", required = false)
	public String gatewayErrorMessage;

	public String getServiceErrorCode() {
		return serviceErrorCode;
	}

	public String getServiceErrorMessage() {
		return serviceErrorMessage;
	}

	public String getGatewayErrorCode() {
		return gatewayErrorCode;
	}

	public String getGatewayErrorMessage() {
		return gatewayErrorMessage;
	}

}
