package com.reactive.app.device.control.scheduler.handler;

import org.springframework.stereotype.Component;

@Component
public class CronExpressionGenerator {

	public String expressionForRepeatedDays(int minute, int hour, String daysOfWeek) {
		// "0 MINUTES HOURS ? * DAYOFWEEK *"
		String expression = "0 MINUTES HOURS ? * DAYOFWEEK *";
		expression = expression.replace("MINUTES", String.valueOf(minute));
		expression = expression.replace("HOURS", String.valueOf(hour));
		expression = expression.replace("DAYOFWEEK", daysOfWeek);

		return expression;
	}
}
