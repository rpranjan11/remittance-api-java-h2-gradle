package com.ranjan.remittance.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties
public class TransactionHistory {

	@JsonProperty("sourceAmount")		
	private long sourceAmount;
	
	@JsonProperty("fee")		
	private double fee;
	
	@JsonProperty("usdExchangeRate")
	private double usdExchangeRate;
	
	@JsonProperty("usdAmount")
	private double usdAmount;
	
	@JsonProperty("targetCurrency")
	private String targetCurrency;
	
	@JsonProperty("exchangeRate")
	private double exchangeRate;
	
	@JsonProperty("targetAmount")
	private double targetAmount;
	
	@JsonProperty("requestedDate")
	private LocalDateTime requestedDate;

	public long getSourceAmount() {
		return sourceAmount;
	}

	public void setSourceAmount(long sourceAmount) {
		this.sourceAmount = sourceAmount;
	}

	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public double getUsdExchangeRate() {
		return usdExchangeRate;
	}

	public void setUsdExchangeRate(double usdExchangeRate) {
		this.usdExchangeRate = usdExchangeRate;
	}

	public double getUsdAmount() {
		return usdAmount;
	}

	public void setUsdAmount(double usdAmount) {
		this.usdAmount = usdAmount;
	}

	public String getTargetCurrency() {
		return targetCurrency;
	}

	public void setTargetCurrency(String targetCurrency) {
		this.targetCurrency = targetCurrency;
	}

	public double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public double getTargetAmount() {
		return targetAmount;
	}

	public void setTargetAmount(double targetAmount) {
		this.targetAmount = targetAmount;
	}

	public LocalDateTime getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(LocalDateTime requestedDate) {
		this.requestedDate = requestedDate;
	}

	@Override
	public String toString() {
		return "TransactionHistory [sourceAmount=" + sourceAmount + ", fee=" + fee + ", usdExchangeRate="
				+ usdExchangeRate + ", usdAmount=" + usdAmount + ", targetCurrency=" + targetCurrency
				+ ", exchangeRate=" + exchangeRate + ", targetAmount=" + targetAmount + ", requestedDate="
				+ requestedDate + "]";
	}
	
}
