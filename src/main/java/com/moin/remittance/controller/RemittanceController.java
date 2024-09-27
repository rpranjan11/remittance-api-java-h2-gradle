package com.moin.remittance.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.moin.remittance.constants.Constants;
import com.moin.remittance.model.RemittanceInfo;
import com.moin.remittance.model.RemittanceQuote;
import com.moin.remittance.model.UserDetail;
import com.moin.remittance.service.RemittanceService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/")
public class RemittanceController {
	private static final Logger logger = LogManager.getLogger(RemittanceController.class);
	private final RemittanceService remittanceService;
	
	public RemittanceController(RemittanceService remittanceService) {
        this.remittanceService = remittanceService;
	}
	
	@PostMapping("/user/signup")
    public String createUserAccount(@RequestBody UserDetail signupDetail) {
		logger.info("Method : createUserAccount()");
//		logger.info("UserDetail value received in Request : " + signupDetail);

		JsonObject jsonResponse = new JsonObject();
		try {
			remittanceService.createAccount(signupDetail);
			
			if(signupDetail.isAccountCheck()) {
				jsonResponse.addProperty("resultCode", 200);
			    jsonResponse.addProperty("resultMsg", Constants.Result_OK);
			    return jsonResponse.toString();
			}
			else {
				jsonResponse.addProperty("resultCode", 200);
			    jsonResponse.addProperty("resultMsg", Constants.Invalid_User_Data);
			    return jsonResponse.toString();
			}
		}
		catch(Exception e) {
			logger.error("Exception occured in createUserAccount()");
			e.printStackTrace();
			jsonResponse.addProperty("resultCode", 200);
		    jsonResponse.addProperty("resultMsg", Constants.Unknown_Error);
		    return jsonResponse.toString();
		}
    }
	
	@PostMapping("/user/login")
    public String userLogin(@RequestBody UserDetail loginDetail) {
		logger.info("Method : userLogin()");
//		logger.info("UserDetail value received in Request : " + loginDetail);
		
		JsonObject jsonResponse = new JsonObject();
		try {
			loginDetail = remittanceService.userLogin(loginDetail);
			
			if(loginDetail.isLoginCheck()) {
				jsonResponse.addProperty("resultCode", 200);
			    jsonResponse.addProperty("resultMsg", Constants.Result_OK);
			    jsonResponse.addProperty("token", loginDetail.getToken());
			    return jsonResponse.toString();
			}
			else {
				jsonResponse.addProperty("resultCode", 200);
			    jsonResponse.addProperty("resultMsg", Constants.Login_Denied);
			    return jsonResponse.toString();
			}
		}
		catch(Exception e) {
			logger.error("Exception occured in userLogin()");
			e.printStackTrace();
			jsonResponse.addProperty("resultCode", 200);
		    jsonResponse.addProperty("resultMsg", Constants.Unknown_Error);
		    return jsonResponse.toString();
		}
    }
	
	@PostMapping("/transfer/quote")
    public String getRemittanceQuote(@RequestBody RemittanceQuote quoteInfo, HttpServletRequest request) {
		logger.info("Method : getRemittanceQuote()");
//		logger.info("Remittance Quote received in Request : " + quoteInfo);

		JsonObject jsonResponse = new JsonObject();
		try {
			String token = request.getHeader("Authorization");
			remittanceService.getRemittanceQuote(quoteInfo, token);
			
			if(quoteInfo.isQuoteCheck()) {
				jsonResponse.addProperty("resultCode", 200);
			    jsonResponse.addProperty("resultMsg", Constants.Result_OK);
			    JsonObject innerJsonResponse = new JsonObject();
			    innerJsonResponse.addProperty("quoteId", quoteInfo.getQuoteId());
			    innerJsonResponse.addProperty("exchangeRate", quoteInfo.getExchangeRate());
			    innerJsonResponse.addProperty("expireTime", quoteInfo.getQuoteExpireTime().toString());
			    innerJsonResponse.addProperty("targetAmount", quoteInfo.getTargetAmount());
				jsonResponse.add("quote", innerJsonResponse);
			    return jsonResponse.toString();
			}
			else {
				jsonResponse.addProperty("resultCode", 200);
			    jsonResponse.addProperty("resultMsg", quoteInfo.getQuoteCheckErrorMessage());
			    return jsonResponse.toString();
			}
		}
		catch(Exception e) {
			logger.error("Exception occured in getRemittanceQuote()");
			e.printStackTrace();
			jsonResponse.addProperty("resultCode", 200);
		    jsonResponse.addProperty("resultMsg", Constants.Unknown_Error);
		    return jsonResponse.toString();
		}
    }
	
	@PostMapping("/transfer/request")
    public String getRemittanceReceipt(@RequestBody RemittanceQuote remittanceReceiptInfo, HttpServletRequest request) {
		logger.info("Method : getRemittanceReceipt()");
//		logger.info("Quote Info received in Request : " + remittanceReceiptInfo);

		JsonObject jsonResponse = new JsonObject();
		try {
			String token = request.getHeader("Authorization");
			remittanceService.getRemittanceReceipt(remittanceReceiptInfo, token);
			
			if(remittanceReceiptInfo.isRemittanceProcessCheck()) {
				jsonResponse.addProperty("resultCode", 200);
			    jsonResponse.addProperty("resultMsg", Constants.Result_OK);
			    return jsonResponse.toString();
			}
			else {
				jsonResponse.addProperty("resultCode", 200);
			    jsonResponse.addProperty("resultMsg", remittanceReceiptInfo.getRemittanceProcessCheckErrorMessage());
			    return jsonResponse.toString();
			}
		}
		catch(Exception e) {
			logger.error("Exception occured in getRemittanceReceipt()");
			e.printStackTrace();
			jsonResponse.addProperty("resultCode", 200);
		    jsonResponse.addProperty("resultMsg", Constants.Unknown_Error);
		    return jsonResponse.toString();
		}
    }
	
	@GetMapping("/transfer/list")
    public String retrieveTransactionHistory(HttpServletRequest request) {
		logger.info("Method : retrieveTransactionHistory()");
		
		JsonObject jsonResponse = new JsonObject();
		JsonArray jsonArray = new JsonArray();

        try {
    		String token = request.getHeader("Authorization");
    		RemittanceInfo remittance = remittanceService.retrieveTransactionHistory(token);
    		if(!remittance.isRemittanceListCheck()) {
    			jsonResponse.addProperty("resultCode", 200);
    		    jsonResponse.addProperty("resultMsg", remittance.getRemittanceListCheckErrorMessage());
    			return jsonResponse.toString();
    		}
    		else if(remittance.getTodayTransferCount() < 1) {
    			jsonResponse.addProperty("resultCode", 200);
    		    jsonResponse.addProperty("resultMsg", Constants.No_Transaction_History);
    			return jsonResponse.toString();
    		}
    		else {
	    		jsonResponse.addProperty("resultCode", 200);
			    jsonResponse.addProperty("resultMsg", Constants.Result_OK);
	    		jsonResponse.addProperty("userId", remittance.getUserId());
	    		jsonResponse.addProperty("name", remittance.getName());
	    		jsonResponse.addProperty("todayTransferCount", remittance.getTodayTransferCount());
	    		jsonResponse.addProperty("todayTransferUsdAmount", remittance.getTodayTransferUsdAmount());
	    		
	    		remittance.getTransactionHistory().forEach((remittanceInfo) -> {
		            JsonObject innerJsonResponse = new JsonObject();
		            innerJsonResponse.addProperty("sourceAmount",remittanceInfo.getSourceAmount());
		            innerJsonResponse.addProperty("fee",remittanceInfo.getFee());
		            innerJsonResponse.addProperty("usdExchangeRate",remittanceInfo.getUsdExchangeRate());
		            innerJsonResponse.addProperty("usdAmount",remittanceInfo.getUsdAmount());
		            innerJsonResponse.addProperty("targetCurrency",remittanceInfo.getTargetCurrency());
		            innerJsonResponse.addProperty("exchangeRate",remittanceInfo.getExchangeRate());
		            innerJsonResponse.addProperty("targetAmount",remittanceInfo.getTargetAmount());
		            innerJsonResponse.addProperty("requestedDate",remittanceInfo.getRequestedDate().toString());
		            jsonArray.add(innerJsonResponse);

				});
	    		
	            jsonResponse.add("history", jsonArray);
	            return jsonResponse.toString();
    		}
		} catch(Exception e) {
			logger.error("Exception occured in retrieveTransactionHistory()");
			e.printStackTrace();
			jsonResponse.addProperty("resultCode", 200);
		    jsonResponse.addProperty("resultMsg", Constants.Unknown_Error);
			return jsonResponse.toString();
		}
    }

}
