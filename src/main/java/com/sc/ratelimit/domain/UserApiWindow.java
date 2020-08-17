package com.sc.ratelimit.domain;

import java.util.Date;

public class UserApiWindow {

	Date startTime;
	
	Date endTime;
	
	public UserApiWindow(Date startTime, Date endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

}
