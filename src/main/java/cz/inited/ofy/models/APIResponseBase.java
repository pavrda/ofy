package cz.inited.ofy.models;

import com.fasterxml.jackson.annotation.JsonInclude;

public class APIResponseBase {
	
	/**
	 * ok nebo error
	 */
	private String status = "ok";

	/**
	 * chybovy kod, strojovy
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String code;
	
	/**
	 * chybova hlaska pro cloveka
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String msg;


	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
