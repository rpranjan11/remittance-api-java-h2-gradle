package com.moin.remittance.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.context.annotation.Configuration;

@Configuration
public class MathUtil {

	public double round(double value, int places) throws Exception {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(Double.toString(value));
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
}
