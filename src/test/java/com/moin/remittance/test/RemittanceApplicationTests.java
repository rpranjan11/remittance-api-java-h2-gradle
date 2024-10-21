package com.moin.remittance.test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.moin.remittance.controller.RemittanceController;
import com.moin.remittance.model.RemittanceQuote;
import com.moin.remittance.model.UserDetail;

@RunWith(MockitoJUnitRunner.class)
class RemittanceApplicationTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	ObjectMapper mapper = new ObjectMapper();
	ObjectWriter writer = mapper.writer(); 
	String token = new String();
	
	@InjectMocks
	private RemittanceController remittanceController;
	
	//Incorrect Response - Invalid User Data
	@Test
	public void testcreateUserAccount3() throws Exception {
		
		UserDetail userDetail = new UserDetail("rpranjan11@gmail.com", "Password1", "Ranjan", "", "123456-5820040");
		String content = writer.writeValueAsString(userDetail);
		
		mockMvc.perform(MockMvcRequestBuilders
				.post("/user/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(content))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.resultCode").value(200))
		.andExpect(jsonPath("$.resultMsg").value("User already exist OR Invalid User Data"));

	}

	//Correct Response
	@Test
	public void testcreateUserAccount() throws Exception {
		
		UserDetail userDetail = new UserDetail("rpranjan11@gmail.com", "Password1", "Ranjan", "REG_NO", "123456-5820040");
		String content = writer.writeValueAsString(userDetail);
		
		mockMvc.perform(MockMvcRequestBuilders
				.post("/user/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(content))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.resultCode").value(200))
		.andExpect(jsonPath("$.resultMsg").value("OK"));

	}
	
	//Incorrect Response - User already Exist
	@Test
	public void testcreateUserAccount2() throws Exception {
		
		UserDetail userDetail = new UserDetail("rpranjan11@gmail.com", "Password1", "Ranjan", "REG_NO", "123456-5820040");
		String content = writer.writeValueAsString(userDetail);
		
		mockMvc.perform(MockMvcRequestBuilders
				.post("/user/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(content))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.resultCode").value(200))
		.andExpect(jsonPath("$.resultMsg").value("User already exist OR Invalid User Data"));
	
	}

	//Unsuccess Response
	@Test
	public void testUserLogin2() throws Exception {
		
		UserDetail userDetail = new UserDetail("rpranjan11@gmail.com", "Pass");
		String content = writer.writeValueAsString(userDetail);
		
		mockMvc.perform(MockMvcRequestBuilders
				.post("/user/login")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(content))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.resultCode").value(200))
		.andExpect(jsonPath("$.resultMsg").value("Login Unsuccessfull - Invalid Credentials"));
	}
			
	//Success Response
	@Test
	public void testUserLogin() throws Exception {
		
		UserDetail userDetail = new UserDetail("rpranjan11@gmail.com", "Password1");
		String content = writer.writeValueAsString(userDetail);
		
		mockMvc.perform(MockMvcRequestBuilders
				.post("/user/login")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(content))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.resultCode").value(200))
		.andExpect(jsonPath("$.resultMsg").value("OK"))
		.andExpect(jsonPath("$.token").value("<Random Generated Token Value>"));

	}
		
	//Unsuccess Response
	@Test
	public void testRetrieveTransactionHistory2() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/transfer/list")
				.header("Authorization", "Bearer " + token))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.resultCode").value(200))
		.andExpect(jsonPath("$.resultMsg").value("No Remittance found"));
	}
		
	//Success Response
	@Test
	public void testGetRemittanceQuote() throws Exception {
		
		RemittanceQuote quoteInfo = new RemittanceQuote(500000, "JPY");
		String content = writer.writeValueAsString(quoteInfo);
		
		mockMvc.perform(MockMvcRequestBuilders
				.post("/transfer/quote")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(content)
				.header("Authorization", "Bearer " + token))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.resultCode").value(200))
		.andExpect(jsonPath("$.resultMsg").value("OK"))
		.andExpect(jsonPath("$.quoteId").value("1"))
		.andExpect(jsonPath("$.exchangeRate").value(9.0))
		.andExpect(jsonPath("$.expireTime").value("2024-07-03T12:51:03.676176"))
		.andExpect(jsonPath("$.targetAmount").value(27444.0));
	}
	
	//Unsuccess Response
	@Test
	public void testGetRemittanceQuote2() throws Exception {
		
		RemittanceQuote quoteInfo = new RemittanceQuote(500000, "");
		String content = writer.writeValueAsString(quoteInfo);
		
		mockMvc.perform(MockMvcRequestBuilders
				.post("/transfer/quote")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(content)
				.header("Authorization", "Bearer " + token))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.resultCode").value(200))
		.andExpect(jsonPath("$.resultMsg").value("Invalid targetCurrency"));
	}
		
	//Success Response
	@Test
	public void testGetRemittanceReceipt() throws Exception {
		
		RemittanceQuote quoteInfo = new RemittanceQuote("1");
		String content = writer.writeValueAsString(quoteInfo);
		
		mockMvc.perform(MockMvcRequestBuilders
				.post("/transfer/request")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(content)
				.header("Authorization", "Bearer " + token))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.resultCode").value(200))
		.andExpect(jsonPath("$.resultMsg").value("OK"));
	}
	
	
	//Unsuccess Response
	@Test
	public void testGetRemittanceReceipt2() throws Exception {
		
		RemittanceQuote quoteInfo = new RemittanceQuote("10");
		String content = writer.writeValueAsString(quoteInfo);
		
		mockMvc.perform(MockMvcRequestBuilders
				.post("/transfer/request")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(content)
				.header("Authorization", "Bearer " + token))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.resultCode").value(200))
		.andExpect(jsonPath("$.resultMsg").value("No Remittance Quote found with given Quote Id"));
	}
		
	//Success Response
	@Test
	public void testRetrieveTransactionHistory() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/transfer/list")
				.header("Authorization", "Bearer " + token))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.resultCode").value(200))
		.andExpect(jsonPath("$.resultMsg").value("OK"))
		.andExpect(jsonPath("$.userId").value("rpranjan11@gmail.com"))
		.andExpect(jsonPath("$.name").value("Ranjan"))
		.andExpect(jsonPath("$.todayTransferCount").value(1))
		.andExpect(jsonPath("$.todayTransferUsdAmount").value(193.48))
		.andExpect(jsonPath("$.sourceAmount").value(500000))
		.andExpect(jsonPath("$.fee").value(3000))
		.andExpect(jsonPath("$.usdExchangeRate").value(1390.3))
		.andExpect(jsonPath("$.usdAmount").value(15.82))
		.andExpect(jsonPath("$.targetCurrency").value("JPY"))
		.andExpect(jsonPath("$.exchangeRate").value(9.0))
		.andExpect(jsonPath("$.targetAmount").value(2444.0))
		.andExpect(jsonPath("$.requestedDate").value("2024-07-03T12:40:32.717131"));
	}
		
}
