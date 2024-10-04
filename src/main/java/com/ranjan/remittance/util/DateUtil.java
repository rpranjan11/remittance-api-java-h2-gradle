package com.ranjan.remittance.util;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Configuration;

@Configuration
public class DateUtil {
//	private static final Logger logger = LogManager.getLogger(DateUtil.class);

	public LocalDateTime generateExpiryTime(long minutes) throws Exception {
		return LocalDateTime.now().plusMinutes(minutes);
	}
	
	public boolean isNotExpired(LocalDateTime expiryTime) throws Exception {
		return LocalDateTime.now().isBefore(expiryTime);
	}
	
	public boolean isToday(LocalDateTime dateTime) {
		LocalDateTime now = LocalDateTime.now();
		return now.getDayOfYear() == (dateTime.getDayOfYear());
	}

}
