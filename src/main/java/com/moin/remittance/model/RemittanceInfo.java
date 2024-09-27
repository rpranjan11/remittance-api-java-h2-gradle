package com.moin.remittance.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties
public class RemittanceInfo {

	@JsonProperty("userId")
	private String userId;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("todayTransferCount")
	private int todayTransferCount;
	
	@JsonProperty("todayTransferUsdAmount")
	private double todayTransferUsdAmount;
	
	@JsonProperty("history")
	private List<TransactionHistory> transactionHistory;
	
	private boolean remittanceListCheck = true;
	
	private String remittanceListCheckErrorMessage;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTodayTransferCount() {
		return todayTransferCount;
	}

	public void setTodayTransferCount(int todayTransferCount) {
		this.todayTransferCount = todayTransferCount;
	}

	public double getTodayTransferUsdAmount() {
		return todayTransferUsdAmount;
	}

	public void setTodayTransferUsdAmount(double todayTransferUsdAmount) {
		this.todayTransferUsdAmount = todayTransferUsdAmount;
	}

	public List<TransactionHistory> getTransactionHistory() {
		return transactionHistory;
	}

	public void setTransactionHistory(List<TransactionHistory> transactionHistory) {
		this.transactionHistory = transactionHistory;
	}

	public boolean isRemittanceListCheck() {
		return remittanceListCheck;
	}

	public void setRemittanceListCheck(boolean remittanceListCheck) {
		this.remittanceListCheck = remittanceListCheck;
	}

	public String getRemittanceListCheckErrorMessage() {
		return remittanceListCheckErrorMessage;
	}

	public void setRemittanceListCheckErrorMessage(String remittanceListCheckErrorMessage) {
		this.remittanceListCheckErrorMessage = remittanceListCheckErrorMessage;
	}

	@Override
	public String toString() {
		return "RemittanceInfo [userId=" + userId + ", name=" + name + ", todayTransferCount=" + todayTransferCount
				+ ", todayTransferUsdAmount=" + todayTransferUsdAmount + ", transactionHistory=" + transactionHistory
				+ ", remittanceListCheck=" + remittanceListCheck + ", remittanceListCheckErrorMessage="
				+ remittanceListCheckErrorMessage + "]";
	}
	
}
