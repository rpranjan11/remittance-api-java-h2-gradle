package com.ranjan.remittance.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ranjan.remittance.model.ExchangeInfo;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Configuration
public class CurrencyUtil {
	private static final Logger logger = LogManager.getLogger(CurrencyUtil.class);

	public List<ExchangeInfo> getExchangeRate() throws Exception {
		logger.info("Method : getExchangeRate()");

	    final ObjectMapper mapper = new ObjectMapper();
		OkHttpClient client = new OkHttpClient();
        Response response;
        List<ExchangeInfo> exchangeInfo = new ArrayList<ExchangeInfo>();
		try {
	        Request request = new Request.Builder()
	            .url("https://crix-api-cdn.upbit.com/v1/forex/recent?codes=,FRX.KRWJPY,FRX.KRWUSD")
	            .build();
		
			Call call = client.newCall(request);
			response = call.execute();
			
		    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			exchangeInfo = mapper.readValue(response.body().string(), new TypeReference<List<ExchangeInfo>>(){});
		} catch (IOException e) {
			logger.error("Exception occured in getExchangeRate()");
			throw new Exception(e);
		}
		return exchangeInfo;
	}
	
	public int getScaleForCurrency(String currency) throws Exception {
		return Currency.getInstance(currency).getDefaultFractionDigits();
	}
	
}
