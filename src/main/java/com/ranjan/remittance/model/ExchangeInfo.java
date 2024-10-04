package com.ranjan.remittance.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties
public class ExchangeInfo {

	@JsonProperty("code")
	private String code;
	
	@JsonProperty("currencyCode")
	private String currencyCode;
	
	@JsonProperty("basePrice")
	private float basePrice;
	
	@JsonProperty("currencyUnit")
	private long currencyUnit;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public float getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(float basePrice) {
		this.basePrice = basePrice;
	}

	public long getCurrencyUnit() {
		return currencyUnit;
	}

	public void setCurrencyUnit(long currencyUnit) {
		this.currencyUnit = currencyUnit;
	}

	@Override
	public String toString() {
		return "ExchangeInfo [code=" + code + ", currencyCode=" + currencyCode + ", basePrice=" + basePrice
				+ ", currencyUnit=" + currencyUnit + "]";
	}
	
}
