package com.sc.ratelimit.domain;

public class DefaultLimits {

	String user;
	Integer timeInMin;
	Integer limit;
	String api;
	
	public String getApi() {
		return api;
	}
	public void setApi(String api) {
		this.api = api;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public Integer getTimeInMin() {
		return timeInMin;
	}
	public void setTimeInMin(Integer timeInMin) {
		this.timeInMin = timeInMin;
	}
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
}
