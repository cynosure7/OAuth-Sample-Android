package com.ecs.android.sample.oauth2;


public class Response {
	private String respMsg;
	public Response(String respMsg, String httpLog) {
		super();
		this.respMsg = respMsg;
		this.httpLog = httpLog;
	}
	public String getRespMsg() {
		return respMsg;
	}
	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}
	public String getHttpLog() {
		return httpLog;
	}
	public void setHttpLog(String httpLog) {
		this.httpLog = httpLog;
	}
	private String httpLog;
}
