package com.moin.remittance.util;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.moin.remittance.model.UserDetail;

@Configuration
public class AuthUtil {
//	private static final Logger logger = LogManager.getLogger(AuthUtil.class);
	
	private static final SecureRandom secureRandom = new SecureRandom();
	private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();
	
	@Autowired
	DateUtil dateUtil;

	public String generateNewToken() {
	    byte[] randomBytes = new byte[64];
	    secureRandom.nextBytes(randomBytes);
	    return base64Encoder.encodeToString(randomBytes);
	}
	
	public String retrieveToken(String token) throws Exception {
	      return token.substring(7, token.length());

	}
	
	public boolean isEmptyToken(String token) throws Exception {
		return token.isBlank() || token.isEmpty();
	}
	
	public boolean verifyToken(String token, UserDetail userDetail) throws Exception {
		if(userDetail.getToken().equals(token)) {
			if(dateUtil.isNotExpired(userDetail.getTokenExpiryTime())){
				return true;
			}
			else {
				
				return false;
			}
		}
		else {
			return false;
		}
	}
	
}
