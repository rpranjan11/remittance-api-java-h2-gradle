package com.ranjan.remittance.util;

import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncrpytionUtil {
//	private static final Logger logger = LogManager.getLogger(EncrpytionUtil.class);

	private final AES256TextEncryptor textEncryptor;

    public EncrpytionUtil() throws Exception {
        textEncryptor = new AES256TextEncryptor();
        textEncryptor.setPassword("PBEWITHHMACSHA1ANDAES_128");
    }

    public String encryptData(String data) throws Exception {
        return textEncryptor.encrypt(data);
    }

    public String decryptData(String encryptedData) throws Exception {
        return textEncryptor.decrypt(encryptedData);
    }

    public boolean matchData(String plainText, String encryptedText) throws Exception {
        return plainText.equals(decryptData(encryptedText));
    }
    
}
