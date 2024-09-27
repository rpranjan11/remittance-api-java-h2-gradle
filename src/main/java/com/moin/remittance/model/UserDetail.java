package com.moin.remittance.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "user_detail")
@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class UserDetail {
	
	@Id
	@JsonProperty("userId")
	private String userId;
	
	@JsonProperty("password")
	private String password;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("idType")
	private String idType;
	
	@JsonProperty("idValue")
	private String idValue;
	
	private String token;
	
	private LocalDateTime tokenExpiryTime;
	
	private boolean accountCheck = false;
	
	private boolean loginCheck = false;
	
	private double totalProcessesAmount;

	public UserDetail() {
		
	}
	
	public UserDetail(String userId, String password, String name, String idType, String idValue) {
		super();
		this.userId = userId;
		this.password = password;
		this.name = name;
		this.idType = idType;
		this.idValue = idValue;
	}

	public UserDetail(String userId, String password) {
		super();
		this.userId = userId;
		this.password = password;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdValue() {
		return idValue;
	}

	public void setIdValue(String idValue) {
		this.idValue = idValue;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getTokenExpiryTime() {
		return tokenExpiryTime;
	}

	public void setTokenExpiryTime(LocalDateTime tokenExpiryTime) {
		this.tokenExpiryTime = tokenExpiryTime;
	}

	public boolean isAccountCheck() {
		return accountCheck;
	}

	public void setAccountCheck(boolean accountCheck) {
		this.accountCheck = accountCheck;
	}

	public boolean isLoginCheck() {
		return loginCheck;
	}

	public void setLoginCheck(boolean loginCheck) {
		this.loginCheck = loginCheck;
	}

	public double getTotalProcessesAmount() {
		return totalProcessesAmount;
	}

	public void setTotalProcessesAmount(double totalProcessesAmount) {
		this.totalProcessesAmount = totalProcessesAmount;
	}

	@Override
	public String toString() {
		return "UserDetail [userId=" + userId + ", password=" + password + ", name=" + name + ", idType=" + idType
				+ ", idValue=" + idValue + ", token=" + token + ", tokenExpiryTime=" + tokenExpiryTime
				+ ", accountCheck=" + accountCheck + ", loginCheck=" + loginCheck + "]";
	}
	
}
