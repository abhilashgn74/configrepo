package com.sc.ratelimit.rest;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sc.ratelimit.domain.DefaultLimits;
import com.sc.ratelimit.domain.UserApiWindow;

@RestController
@RequestMapping("/ratelimit")
public class RateLimitController {

	@Value("${tom.viewItem.limit}")
	private Integer tomLimitApi1;
	@Value("${tom.viewItem.time}")
	private Integer tomTimeApi1;
	@Value("${tom.addItem.limit}")
	private Integer tomLimitApi2;
	@Value("${tom.addItem.time}")
	private Integer tomTimeApi2;

	@Value("${dick.viewItem.limit}")
	private Integer dickLimitApi1;
	@Value("${dick.viewItem.time}")
	private Integer dickTimeApi1;
	@Value("${dick.addItem.limit}")
	private Integer dickLimitApi2;
	@Value("${dick.addItem.time}")
	private Integer dickTimeApi2;

	@Value("${harry.viewItem.limit}")
	private Integer harryLimitApi1;
	@Value("${harry.viewItem.time}")
	private Integer harryTimeApi1;
	@Value("${harry.addItem.limit}")
	private Integer harryLimitApi2;
	@Value("${harry.addItem.time}")
	private Integer harryTimeApi2;

	private ConcurrentHashMap<String, Integer> limitStore = new ConcurrentHashMap<>();

	private ConcurrentHashMap<String, Object> windowMap = new ConcurrentHashMap<>();

	@PostConstruct
	public void init() {
		initRateLimitWindow();
	}

	@RequestMapping("/viewItem/{userName}")
	@ResponseBody
	public String viewItem(@PathVariable String userName) {
		String message = null;
		if (validateRateLimit("viewItem", userName)) {
			message = "Transaction allowed for user " + userName + " for API viewItem";
			System.out.println(message);
		} else {
			message = "Transaction NOT allowed for user " + userName + " for API viewItem";
			System.out.println(message);
		}
		return message;
	}

	@RequestMapping("/addItem/{userName}")
	@ResponseBody
	public String addItem(@PathVariable String userName) {
		String message = null;
		if (validateRateLimit("addItem", userName)) {
			message = "Transaction allowed for user " + userName + " for API addItem";
			System.out.println(message);
		} else {
			message = "Transaction NOT allowed for user " + userName + " for API addItem";
			System.out.println(message);
		}
		return message;
	}

	private boolean validateRateLimit(String apiName, String userName) {
		boolean isvalid = true;
		try {
			StringBuilder key = new StringBuilder().append(userName).append("-").append(apiName);
			UserApiWindow uaw = (UserApiWindow) windowMap.get(key.toString());
			Calendar cal = Calendar.getInstance();
			Date x = cal.getTime();
			if (x.after(uaw.getStartTime()) && x.before(uaw.getEndTime())) {
				if (!rateLimitTracker(key.toString(), userName)) {
					isvalid = false;
				}
			} else {
				isvalid = false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return isvalid;
	}

	@RequestMapping("/setDefault")
	public String setDefault(@RequestBody DefaultLimits defaultLimits) {
		Calendar cal = Calendar.getInstance();
		String key = defaultLimits.getUser() + "-" + defaultLimits.getApi();
		Date start = ((UserApiWindow) windowMap.get(key)).getStartTime();
		cal.setTime(start);
		if ("Tom".equals(defaultLimits.getUser())) {
			if ("viewItem".equals(defaultLimits.getApi())) {
				this.tomLimitApi1 = defaultLimits.getLimit();
				this.tomTimeApi1 = defaultLimits.getTimeInMin();
				cal.add(Calendar.MINUTE, tomTimeApi1);
			} else if ("addItem".equals(defaultLimits.getApi())) {
				this.tomLimitApi2 = defaultLimits.getLimit();
				this.tomTimeApi2 = defaultLimits.getTimeInMin();
				cal.add(Calendar.MINUTE, tomTimeApi2);
			}
		} else if ("Dick".equals(defaultLimits.getUser())) {
			if ("viewItem".equals(defaultLimits.getApi())) {
				this.dickLimitApi1 = defaultLimits.getLimit();
				this.dickTimeApi1 = defaultLimits.getTimeInMin();
				cal.add(Calendar.MINUTE, dickTimeApi1);
			} else if ("addItem".equals(defaultLimits.getApi())) {
				this.dickLimitApi2 = defaultLimits.getLimit();
				this.dickTimeApi2 = defaultLimits.getTimeInMin();
				cal.add(Calendar.MINUTE, dickTimeApi2);
			}
		} else if ("Harry".equals(defaultLimits.getUser())) {
			if ("viewItem".equals(defaultLimits.getApi())) {
				this.harryLimitApi1 = defaultLimits.getLimit();
				this.harryTimeApi1 = defaultLimits.getTimeInMin();
				cal.add(Calendar.MINUTE, harryTimeApi1);
			} else if ("addItem".equals(defaultLimits.getApi())) {
				this.harryLimitApi2 = defaultLimits.getLimit();
				this.harryTimeApi2 = defaultLimits.getTimeInMin();
				cal.add(Calendar.MINUTE, harryTimeApi2);
			}
		}
		windowMap.put(key, new UserApiWindow(start, cal.getTime()));
		return "SUCCESS";
	}

	private Boolean rateLimitTracker(String key, String userName) {
		int counter = 1;
		boolean success = true;
		if (limitStore.containsKey(key)) {
			counter = limitStore.get(key.toString()) + 1;
			if (key.equals("Tom-viewItem")) {
				if (counter <= tomLimitApi1) {
					limitStore.put(key.toString(), counter);
				} else {
					success = false;
				}
			} else if (key.equals("Tom-addItem")) {
				if (counter < tomLimitApi2) {
					limitStore.put(key.toString(), counter);
				} else {
					success = false;
				}
			} else if (key.equals("Dick-viewItem")) {
				if (counter <= dickLimitApi1) {
					limitStore.put(key.toString(), counter);
				} else {
					success = false;
				}
			} else if (key.equals("Dick-addItem")) {
				if (counter <= dickLimitApi2) {
					limitStore.put(key.toString(), counter);
				} else {
					success = false;
				}
			} else if (key.equals("Harry-viewItem")) {
				if (counter <= harryLimitApi1) {
					limitStore.put(key.toString(), counter);
				} else {
					success = false;
				}
			} else if (key.equals("Harry-addItem")) {
				if (counter < harryLimitApi2) {
					limitStore.put(key.toString(), counter);
				} else {
					success = false;
				}
			}
		} else {
			limitStore.put(key.toString(), counter);
		}
		return success;
	}

	private Map<String, Object> initRateLimitWindow() {
		Calendar cal = Calendar.getInstance();
		Date start = cal.getTime();
		cal.add(Calendar.MINUTE, tomTimeApi1);
		Date tomApi1EndTime = cal.getTime();
		windowMap.put("Tom-viewItem", new UserApiWindow(start, tomApi1EndTime));

		Calendar cal1 = Calendar.getInstance();
		cal1.add(Calendar.MINUTE, dickTimeApi1);
		Date dickApi1EndTime = cal1.getTime();
		windowMap.put("Dick-viewItem", new UserApiWindow(start, dickApi1EndTime));

		Calendar cal2 = Calendar.getInstance();
		cal2.add(Calendar.MINUTE, harryTimeApi1);
		Date harryApi1EndTime = cal2.getTime();
		windowMap.put("Harry-viewItem", new UserApiWindow(start, harryApi1EndTime));

		Calendar cal3 = Calendar.getInstance();
		cal3.add(Calendar.MINUTE, tomTimeApi2);
		Date tomApi2EndTime = cal3.getTime();
		windowMap.put("Tom-addItem", new UserApiWindow(start, tomApi2EndTime));

		Calendar cal4 = Calendar.getInstance();
		cal4.add(Calendar.MINUTE, dickTimeApi2);
		Date dickApi2EndTime = cal4.getTime();
		windowMap.put("Dick-addItem", new UserApiWindow(start, dickApi2EndTime));

		Calendar cal5 = Calendar.getInstance();
		cal5.add(Calendar.MINUTE, harryTimeApi2);
		Date harryApi2EndTime = cal5.getTime();
		windowMap.put("Harry-addItem", new UserApiWindow(start, harryApi2EndTime));
		return windowMap;
	}
}
