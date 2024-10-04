package com.ranjan.remittance.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ranjan.remittance.constants.Constants;
import com.ranjan.remittance.model.ExchangeInfo;
import com.ranjan.remittance.model.RemittanceInfo;
import com.ranjan.remittance.model.RemittanceQuote;
import com.ranjan.remittance.model.UserDetail;
import com.ranjan.remittance.repository.RemittanceQuoteRepository;
import com.ranjan.remittance.repository.UserRepository;
import com.ranjan.remittance.util.AuthUtil;
import com.ranjan.remittance.util.CurrencyUtil;
import com.ranjan.remittance.util.DateUtil;
import com.ranjan.remittance.util.EncrpytionUtil;
import com.ranjan.remittance.util.MathUtil;

@Service
public class RemittanceService {
	private static final Logger logger = LogManager.getLogger(RemittanceService.class);
	
	@Autowired
	ServiceHelper serviceHelper;
	
	@Autowired
	private RemittanceQuoteRepository remittanceQuoteRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	AuthUtil authUtil;
	
	@Autowired
	CurrencyUtil currencyUtil;
	
	@Autowired
	DateUtil dateUtil;
	
	@Autowired
	EncrpytionUtil encryptUtil;
	
	@Autowired
	MathUtil mathUtil;
	
	private enum UserType {
		REG_NO, BUSINESS_NO
	}

	public void createAccount(UserDetail signupDetail) {
		logger.info("Method : createAccount()");
		
		try {
			Optional<UserDetail> userDetail = userRepository.findById(signupDetail.getUserId());
			if(userDetail.isEmpty()) {
				if(signupDetail.getIdType().equals(UserType.REG_NO.toString()) 
						|| signupDetail.getIdType().equals(UserType.BUSINESS_NO.toString())) {
					signupDetail.setPassword(encryptUtil.encryptData(signupDetail.getPassword()));
					signupDetail.setIdValue(encryptUtil.encryptData(signupDetail.getIdValue()));
					signupDetail.setAccountCheck(true);
					userRepository.save(signupDetail);
				}
			}
		}
		catch(Exception e) {
			logger.error("Exception occured in createAccount()");
			//throw new Exception(e);
		}
	}

	public UserDetail userLogin(UserDetail loginDetail) throws Exception {
		logger.info("Method : userLogin()");
		try {
			Optional<UserDetail> userDetail = userRepository.findById(loginDetail.getUserId());
			if(!userDetail.isEmpty()) {
				if(encryptUtil.decryptData(userDetail.get().getPassword()).equals(loginDetail.getPassword())) {
					userDetail.get().setToken(authUtil.generateNewToken());
					userDetail.get().setTokenExpiryTime(dateUtil.generateExpiryTime(30));
					userDetail.get().setLoginCheck(true);
					userRepository.save(userDetail.get());
					return userDetail.get();
				}
			}
		}
		catch(Exception e) {
			logger.error("Exception occured in userLogin()");
			throw new Exception(e);
		}
		return loginDetail;
	}

	public void getRemittanceQuote(RemittanceQuote quoteInfo, String token) throws Exception {
		logger.info("Method : getRemittanceQuote()");
		try {
			if(authUtil.isEmptyToken(token)) {
				quoteInfo.setQuoteCheck(false);
				quoteInfo.setQuoteCheckErrorMessage(Constants.Empty_Token);
			}
			else {
				token = authUtil.retrieveToken(token);
				UserDetail userDetail = new UserDetail();
				serviceHelper.userDetailWrapper(token, userDetail);
	
				if(userDetail.getToken() == null) {
					quoteInfo.setQuoteCheck(false);
					quoteInfo.setQuoteCheckErrorMessage(Constants.Empty_Token);
				}
				else if(!authUtil.verifyToken(token, userDetail)) {
					quoteInfo.setQuoteCheck(false);
					quoteInfo.setQuoteCheckErrorMessage(Constants.Invalid_Token);
				}
				else if(quoteInfo.getSourceAmount() < 0) {
					quoteInfo.setQuoteCheck(false);
					quoteInfo.setQuoteCheckErrorMessage(Constants.Invalid_Amount);
				}
				else if(!quoteInfo.getTargetCurrency().equals(Constants.Currency_JPY) 
						&& !quoteInfo.getTargetCurrency().equals(Constants.Currency_USD)) {
					quoteInfo.setQuoteCheck(false);
					quoteInfo.setQuoteCheckErrorMessage(Constants.Invalid_Currency);
				}
				else {
					quoteInfo.setQuoteId((remittanceQuoteRepository.count() + 1) + Constants.Empty_String);
					quoteInfo.setUserId(userDetail.getUserId());
					List<ExchangeInfo> exchangeInfo = currencyUtil.getExchangeRate();
					double exchangeValue;
					double targetAmount;
					double usdAmount;
	
					exchangeValue = exchangeInfo.get(1).getBasePrice() / exchangeInfo.get(1).getCurrencyUnit();
					exchangeValue = mathUtil.round(exchangeValue, currencyUtil.getScaleForCurrency(Constants.Currency_USD));
					quoteInfo.setUsdExchangeRate(exchangeValue);
					
					if(quoteInfo.getTargetCurrency().equals(Constants.Currency_JPY)) {
						quoteInfo.setFee(Constants.JPY_Fixed_Fee);
						
						exchangeValue = exchangeInfo.get(0).getBasePrice() / exchangeInfo.get(0).getCurrencyUnit();
						exchangeValue = mathUtil.round(exchangeValue, currencyUtil.getScaleForCurrency(Constants.Currency_JPY));
						quoteInfo.setExchangeRate(exchangeValue);
						
						double totalFee = quoteInfo.getSourceAmount() * Constants.JPY_Commission_Rate + Constants.JPY_Fixed_Fee;
						targetAmount = (quoteInfo.getSourceAmount() - totalFee) / quoteInfo.getExchangeRate();
						targetAmount = mathUtil.round(targetAmount, currencyUtil.getScaleForCurrency(Constants.Currency_JPY));
						
						if(quoteInfo.getSourceAmount() < 1000000) {							
							totalFee = quoteInfo.getSourceAmount() * Constants.USD_Lower_Commission_Rate + Constants.USD_Lower_Fixed_Fee;
							usdAmount = (quoteInfo.getSourceAmount() - totalFee) / quoteInfo.getUsdExchangeRate();
						}
						else {							
							totalFee = quoteInfo.getSourceAmount() * Constants.USD_Higher_Commission_Rate + Constants.USD_Higher_Fixed_Fee;
							usdAmount = (quoteInfo.getSourceAmount() - totalFee) / quoteInfo.getUsdExchangeRate();
						}
						usdAmount = mathUtil.round(usdAmount, currencyUtil.getScaleForCurrency(Constants.Currency_USD));
						quoteInfo.setUsdAmount(usdAmount);
					}
					else {
						exchangeValue = exchangeInfo.get(1).getBasePrice() / exchangeInfo.get(1).getCurrencyUnit();
						exchangeValue = mathUtil.round(exchangeValue, currencyUtil.getScaleForCurrency(Constants.Currency_USD));
						quoteInfo.setExchangeRate(exchangeValue);
						
						if(quoteInfo.getSourceAmount() < 1000000) {
							quoteInfo.setFee(Constants.USD_Lower_Fixed_Fee);
							
							double totalFee = quoteInfo.getSourceAmount() * Constants.USD_Lower_Commission_Rate + Constants.USD_Lower_Fixed_Fee;
							targetAmount = (quoteInfo.getSourceAmount() - totalFee) / quoteInfo.getExchangeRate();
						}
						else {
							quoteInfo.setFee(Constants.USD_Higher_Fixed_Fee);
							
							double totalFee = quoteInfo.getSourceAmount() * Constants.USD_Higher_Commission_Rate + Constants.USD_Higher_Fixed_Fee;
							targetAmount = (quoteInfo.getSourceAmount() - totalFee) / quoteInfo.getExchangeRate();
						}
						targetAmount = mathUtil.round(targetAmount, currencyUtil.getScaleForCurrency(Constants.Currency_USD));
						quoteInfo.setUsdAmount(targetAmount);
					}
					
					quoteInfo.setTargetAmount(targetAmount);
					quoteInfo.setRequestDateTime(LocalDateTime.now());
					quoteInfo.setQuoteExpireTime(dateUtil.generateExpiryTime(10));
					quoteInfo.setQuoteStatus(Constants.Quote_Status_Unprocessed);
					remittanceQuoteRepository.save(quoteInfo);
				}
			}
		}
		catch(Exception e) {
			logger.error("Exception occured in getRemittanceQuote()");
			throw new Exception(e);
		}
	}
	
	public void getRemittanceReceipt(RemittanceQuote remittanceReceiptInfo, String token) throws Exception {
		logger.info("Method : getRemittanceReceipt()");
		
		try {
			if(authUtil.isEmptyToken(token)) {
				remittanceReceiptInfo.setRemittanceProcessCheck(false);
				remittanceReceiptInfo.setRemittanceProcessCheckErrorMessage(Constants.Empty_Token);
			}
			else {
				token = authUtil.retrieveToken(token);
				UserDetail userDetail = new UserDetail();
				serviceHelper.userDetailWrapper(token, userDetail);
	
				if(userDetail.getToken() == null) {
					remittanceReceiptInfo.setRemittanceProcessCheck(false);
					remittanceReceiptInfo.setRemittanceProcessCheckErrorMessage(Constants.Empty_Token);
				}
				else if(!authUtil.verifyToken(token, userDetail)) {
					remittanceReceiptInfo.setRemittanceProcessCheck(false);
					remittanceReceiptInfo.setRemittanceProcessCheckErrorMessage(Constants.Invalid_Token);
				}
				else if(remittanceReceiptInfo.getQuoteId().isEmpty() ||remittanceReceiptInfo.getQuoteId() == null) {
					remittanceReceiptInfo.setRemittanceProcessCheck(false);
					remittanceReceiptInfo.setRemittanceProcessCheckErrorMessage(Constants.Invalid_Quote_Id);
				}
				else {
					Optional<RemittanceQuote> remittanceQuote = remittanceQuoteRepository.findById(remittanceReceiptInfo.getQuoteId());
					if(remittanceQuote.isEmpty()) {
						remittanceReceiptInfo.setRemittanceProcessCheck(false);
						remittanceReceiptInfo.setRemittanceProcessCheckErrorMessage(Constants.Invalid_Quote);
					}
					else if(!dateUtil.isNotExpired(remittanceQuote.get().getQuoteExpireTime())) {
						remittanceReceiptInfo.setRemittanceProcessCheck(false);
						remittanceReceiptInfo.setRemittanceProcessCheckErrorMessage(Constants.Quote_Expire);
					}
					else if(serviceHelper.isDailyLimitExceeded(userDetail, remittanceQuote.get().getUsdAmount())) {
						remittanceReceiptInfo.setRemittanceProcessCheck(false);
						remittanceReceiptInfo.setRemittanceProcessCheckErrorMessage(Constants.Limit_Exceed);
					}
					else {
						remittanceQuote.get().setQuoteStatus(Constants.Quote_Status_Processed);
						remittanceQuoteRepository.save(remittanceQuote.get());
					}
				}
			}
		}
		catch(Exception e) {
			logger.error("Exception occured in getRemittanceReceipt()");
			throw new Exception(e);
		}
	}

	public RemittanceInfo retrieveTransactionHistory(String token) throws Exception {
		logger.info("Method : retrieveTransactionHistory()");
		
		RemittanceInfo remittance = new RemittanceInfo();
		try {
			if(authUtil.isEmptyToken(token)) {
				remittance.setRemittanceListCheck(false);
				remittance.setRemittanceListCheckErrorMessage(Constants.Empty_Token);
			}
			else {
				token = authUtil.retrieveToken(token);
				UserDetail userDetail = new UserDetail();
				serviceHelper.userDetailWrapper(token, userDetail);
	
				if(userDetail.getToken() == null) {
					remittance.setRemittanceListCheck(false);
					remittance.setRemittanceListCheckErrorMessage(Constants.Empty_Token);
				}
				else if(!authUtil.verifyToken(token, userDetail)) {
					remittance.setRemittanceListCheck(false);
					remittance.setRemittanceListCheckErrorMessage(Constants.Invalid_Token);
				}
				else {
					remittance.setUserId(userDetail.getUserId());
					remittance.setName(userDetail.getName());
					serviceHelper.retrieveTransactionsDetails(userDetail, remittance);
				}
			}
		}
		catch(Exception e) {
			logger.error("Exception occured in retrieveTransactionHistory()");
			throw new Exception(e);
		}
		return remittance;
	}
	
}
