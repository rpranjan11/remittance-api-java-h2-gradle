package com.moin.remittance.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moin.remittance.constants.Constants;
import com.moin.remittance.model.RemittanceInfo;
import com.moin.remittance.model.RemittanceQuote;
import com.moin.remittance.model.TransactionHistory;
import com.moin.remittance.model.UserDetail;
import com.moin.remittance.repository.RemittanceQuoteRepository;
import com.moin.remittance.repository.UserRepository;
import com.moin.remittance.util.AuthUtil;
import com.moin.remittance.util.CurrencyUtil;
import com.moin.remittance.util.DateUtil;
import com.moin.remittance.util.EncrpytionUtil;
import com.moin.remittance.util.MathUtil;

@Service
public class ServiceHelper {
	private static final Logger logger = LogManager.getLogger(ServiceHelper.class);

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

	public void userDetailWrapper(String token, UserDetail userDetail) throws Exception {
		logger.info("Method : userDetailWrapper()");
		
		try {
			final String fToken = new String(token);
			List<UserDetail> userDetailList = userRepository.findAll();
			if(!userDetailList.isEmpty()) {
				userDetailList.forEach((user) -> {
					if(user.getToken().equals(fToken)) {
						userDetail.setUserId(user.getUserId());
						userDetail.setPassword(user.getPassword());
						userDetail.setName(user.getName());
						userDetail.setIdType(user.getIdType());
						userDetail.setIdValue(user.getIdValue());
						userDetail.setToken(user.getToken());
						userDetail.setTokenExpiryTime(user.getTokenExpiryTime());
						userDetail.setAccountCheck(user.isAccountCheck());
						userDetail.setLoginCheck(user.isLoginCheck());
					}
		        });
			}
		}
		catch(Exception e) {
			logger.error("Exception occured in userDetailWrapper()");
			throw new Exception(e);
		}
	}
	
	public boolean isDailyLimitExceeded(UserDetail userDetail, double amount) throws Exception {
		logger.info("Method : isLimitExceeded()");
		
		try {
			userDetail.setTotalProcessesAmount(amount);
			List<RemittanceQuote> remittanceList = remittanceQuoteRepository.findAll();
			if(!remittanceList.isEmpty()) {
				remittanceList.forEach((remittance) -> {
					if(remittance.getQuoteStatus().equals(Constants.Quote_Status_Processed) 
							&& remittance.getUserId().equals(userDetail.getUserId()) 
							&& dateUtil.isToday(remittance.getRequestDateTime())) {
						userDetail.setTotalProcessesAmount(userDetail.getTotalProcessesAmount() + remittance.getUsdAmount());
					}
				});
				
				if(userDetail.getIdType().equals(Constants.Id_Type_Individual)) {
					if(userDetail.getTotalProcessesAmount() > Constants.Individual_Limit_Per_Day) {
						return true;
					}
				}
				else if(userDetail.getTotalProcessesAmount() > Constants.Corporate_Limit_Per_Day) {
					return true;
				}
			}
		}
		catch(Exception e) {
			logger.error("Exception occured in isDailyLimitExceeded()");
			throw new Exception(e);
		}
		return false;
	}
	
	public void retrieveTransactionsDetails(UserDetail userDetail, RemittanceInfo remittanceInfo) throws Exception {
		logger.info("Method : retrieveTransactions()");
		
		List<TransactionHistory> transactionHistory = new ArrayList<TransactionHistory>();
		try {
			List<RemittanceQuote> remittanceList = remittanceQuoteRepository.findAll();
			if(!remittanceList.isEmpty()) {
				remittanceList.forEach((remittance) -> {
					if(remittance.getQuoteStatus().equals(Constants.Quote_Status_Processed) 
							&& remittance.getUserId().equals(userDetail.getUserId())) {
						TransactionHistory transaction = new TransactionHistory();
						transaction.setSourceAmount(remittance.getSourceAmount());
						transaction.setFee(remittance.getFee());
						transaction.setUsdExchangeRate(remittance.getUsdExchangeRate());
						transaction.setUsdAmount(remittance.getUsdAmount());
						transaction.setTargetCurrency(remittance.getTargetCurrency());
						transaction.setExchangeRate(remittance.getExchangeRate());
						transaction.setTargetAmount(remittance.getTargetAmount());
						transaction.setRequestedDate(remittance.getRequestDateTime());
						transactionHistory.add(transaction);
					}
					
					if(dateUtil.isToday(remittance.getRequestDateTime()) 
							&& remittance.getQuoteStatus().equals(Constants.Quote_Status_Processed)) {
						remittanceInfo.setTodayTransferCount(remittanceInfo.getTodayTransferCount() + 1);
						remittanceInfo.setTodayTransferUsdAmount(remittanceInfo.getTodayTransferUsdAmount() + remittance.getUsdAmount());
					}
				});
				remittanceInfo.setTransactionHistory(transactionHistory);
			}
		}
		catch(Exception e) {
			logger.error("Exception occured in retrieveTransactions()");
			throw new Exception(e);
		}
	}
	
}
