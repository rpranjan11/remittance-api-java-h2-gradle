package com.moin.remittance.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "remittance_quote")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class RemittanceQuote {
	
	private String userId;
	
	@Id
	@JsonProperty("quoteId")
	private String quoteId;
	
	@JsonProperty("amount")
	private long sourceAmount;
	
	@JsonProperty("targetCurrency")
	private String targetCurrency;
	
	private double exchangeRate;
	
	private double targetAmount;
		
	private double usdExchangeRate;
	
	private double usdAmount;
	
	private double fee;
		
	private LocalDateTime requestDateTime;
	
	private LocalDateTime quoteExpireTime;
	
	private String quoteStatus;
	
	private boolean quoteCheck = true;
	
	private String quoteCheckErrorMessage;
	
	private boolean remittanceProcessCheck = true;
	
	private String remittanceProcessCheckErrorMessage;
	
	public RemittanceQuote(int amount, String targetCurrency) {
		super();
		this.sourceAmount = amount;
		this.targetCurrency = targetCurrency;
	}

	public RemittanceQuote(String quoteId) {
		super();
		this.quoteId = quoteId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}

	public long getSourceAmount() {
		return sourceAmount;
	}

	public void setSourceAmount(long sourceAmount) {
		this.sourceAmount = sourceAmount;
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

	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public LocalDateTime getRequestDateTime() {
		return requestDateTime;
	}

	public void setRequestDateTime(LocalDateTime requestDateTime) {
		this.requestDateTime = requestDateTime;
	}

	public LocalDateTime getQuoteExpireTime() {
		return quoteExpireTime;
	}

	public void setQuoteExpireTime(LocalDateTime quoteExpireTime) {
		this.quoteExpireTime = quoteExpireTime;
	}

	public String getQuoteStatus() {
		return quoteStatus;
	}

	public void setQuoteStatus(String quoteStatus) {
		this.quoteStatus = quoteStatus;
	}

	public boolean isQuoteCheck() {
		return quoteCheck;
	}

	public void setQuoteCheck(boolean quoteCheck) {
		this.quoteCheck = quoteCheck;
	}

	public String getQuoteCheckErrorMessage() {
		return quoteCheckErrorMessage;
	}

	public void setQuoteCheckErrorMessage(String quoteCheckErrorMessage) {
		this.quoteCheckErrorMessage = quoteCheckErrorMessage;
	}

	public boolean isRemittanceProcessCheck() {
		return remittanceProcessCheck;
	}

	public void setRemittanceProcessCheck(boolean remittanceProcessCheck) {
		this.remittanceProcessCheck = remittanceProcessCheck;
	}

	public String getRemittanceProcessCheckErrorMessage() {
		return remittanceProcessCheckErrorMessage;
	}

	public void setRemittanceProcessCheckErrorMessage(String remittanceProcessCheckErrorMessage) {
		this.remittanceProcessCheckErrorMessage = remittanceProcessCheckErrorMessage;
	}

	@Override
	public String toString() {
		return "RemittanceQuote [userId=" + userId + ", quoteId=" + quoteId + ", sourceAmount=" + sourceAmount
				+ ", targetCurrency=" + targetCurrency + ", exchangeRate=" + exchangeRate + ", targetAmount="
				+ targetAmount + ", usdExchangeRate=" + usdExchangeRate + ", usdAmount=" + usdAmount + ", fee=" + fee
				+ ", requestDateTime=" + requestDateTime + ", quoteExpireTime=" + quoteExpireTime + ", quoteStatus="
				+ quoteStatus + ", quoteCheck=" + quoteCheck + ", quoteCheckErrorMessage=" + quoteCheckErrorMessage
				+ ", remittanceProcessCheck=" + remittanceProcessCheck + ", remittanceProcessCheckErrorMessage="
				+ remittanceProcessCheckErrorMessage + "]";
	}
	
}
