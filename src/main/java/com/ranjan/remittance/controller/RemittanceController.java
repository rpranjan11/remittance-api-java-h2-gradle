package com.ranjan.remittance.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ranjan.remittance.constants.Constants;
import com.ranjan.remittance.model.RemittanceInfo;
import com.ranjan.remittance.model.RemittanceQuote;
import com.ranjan.remittance.model.UserDetail;
import com.ranjan.remittance.service.RemittanceService;

//import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
//@Tag("Remittance")
@RequiredArgsConstructor
public class RemittanceController {

	private static final Logger log = LogManager.getLogger(RemittanceController.class);
	private final RemittanceService remittanceService;

	@PostMapping("/user/signup")
	@ResponseStatus(CREATED)
	public ResponseEntity<String> createUserAccount(@Validated @RequestBody UserDetail request) {
		log.info("Method : createUserAccount()");
		log.debug("Create User Account : {}", request);

		JsonObject jsonResponse = new JsonObject();
		remittanceService.createAccount(request);

		if(request.isAccountCheck()) {
			jsonResponse.addProperty("resultCode", 200);
			jsonResponse.addProperty("resultMsg", Constants.Result_OK);
			return ResponseEntity.status(CREATED).body(jsonResponse.toString());
		}
		else {
			jsonResponse.addProperty("resultCode", 200);
			jsonResponse.addProperty("resultMsg", Constants.Invalid_User_Data);
			return ResponseEntity.status(CREATED).body(jsonResponse.toString());
		}
    }
	
	@PostMapping("/user/login")
    public String userLogin(@RequestBody UserDetail loginDetail) {
		log.info("Method : userLogin()");
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
			log.error("Exception occured in userLogin()");
			e.printStackTrace();
			jsonResponse.addProperty("resultCode", 200);
		    jsonResponse.addProperty("resultMsg", Constants.Unknown_Error);
		    return jsonResponse.toString();
		}
    }
	
	@PostMapping("/transfer/quote")
    public String getRemittanceQuote(@RequestBody RemittanceQuote quoteInfo, HttpServletRequest request) {
		log.info("Method : getRemittanceQuote()");
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
			log.error("Exception occured in getRemittanceQuote()");
			e.printStackTrace();
			jsonResponse.addProperty("resultCode", 200);
		    jsonResponse.addProperty("resultMsg", Constants.Unknown_Error);
		    return jsonResponse.toString();
		}
    }
	
	@PostMapping("/transfer/request")
    public String getRemittanceReceipt(@RequestBody RemittanceQuote remittanceReceiptInfo, HttpServletRequest request) {
		log.info("Method : getRemittanceReceipt()");
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
			log.error("Exception occured in getRemittanceReceipt()");
			e.printStackTrace();
			jsonResponse.addProperty("resultCode", 200);
		    jsonResponse.addProperty("resultMsg", Constants.Unknown_Error);
		    return jsonResponse.toString();
		}
    }
	
	@GetMapping("/transfer/list")
    public String retrieveTransactionHistory(HttpServletRequest request) {
		log.info("Method : retrieveTransactionHistory()");
		
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
			log.error("Exception occured in retrieveTransactionHistory()");
			e.printStackTrace();
			jsonResponse.addProperty("resultCode", 200);
		    jsonResponse.addProperty("resultMsg", Constants.Unknown_Error);
			return jsonResponse.toString();
		}
    }

}
