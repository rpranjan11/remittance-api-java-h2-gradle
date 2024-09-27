package com.moin.remittance.constants;

public class Constants {
	
	public static String Empty_String = "";
	public static String Result_OK = "OK";
	public static String Id_Type_Individual = "REG_NO";
	public static String Id_Type_Corporate = "BUSINESS_NO";
	public static String Invalid_User_Data = "User already exist OR Invalid User Data";
	public static String No_User = "User not Logged In";
	public static String Login_Denied = "Login Unsuccessfull - Invalid Credentials";
	public static String Empty_Token = "Authentication Error - Token Invalid/Empty";
	public static String Invalid_Token = "Authentication Error - Token Expired";
	public static String Invalid_Amount = "Negative Amount Received";
	public static String Invalid_Currency = "Invalid targetCurrency";
	public static String Invalid_Quote_Id = "Quote_Id Invalid/Empty";
	public static String Invalid_Quote = "No Remittance Quote found with given Quote Id";
	public static String Quote_Status_Unprocessed = "Unprocessed";
	public static String Quote_Status_Processed = "Processed";
	public static String Quote_Expire = "Quote Expired";
	public static String Limit_Exceed = "Daily Limit Exceeded";
	public static String Unknown_Error = "Unknown Error Occured";
	public static String No_Transaction_History = "No Remittance found";
	public static String Currency_USD = "USD";
	public static String Currency_JPY = "JPY";
	public static int JPY_Fixed_Fee = 3000;
	public static double JPY_Commission_Rate = 0.5;
	public static int USD_Lower_Fixed_Fee = 1000;
	public static double USD_Lower_Commission_Rate = 0.2;
	public static int USD_Higher_Fixed_Fee = 3000;
	public static double USD_Higher_Commission_Rate = 0.1;
	public static double Individual_Limit_Per_Day = 1000;
	public static double Corporate_Limit_Per_Day = 5000;

	
}
