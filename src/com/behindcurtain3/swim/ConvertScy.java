package com.behindcurtain3.swim;

public class ConvertScy extends Conversion {

	@Override
	double toScy(double t, String e) {
		return t;
	}

	@Override
	double toScm(double t, String e) {
		if (e.equals("400/500"))
    		t = (t * fourHundredEq) - fourHundred;
    	else if (e.equals("800/1000"))
    		t = (t * eightHundredEq) - eightHundred;
    	else if (e.equals("1500/1650"))
    		t = (t * fifteenHundredEq) - fifteenHundred;
    	else
    		t *= conversionFactor;
    	
    	return t;
	}

	@Override
	double toLcm(double t, String e) {
		if(e.equals("50 Fly"))
    		t = (t * conversionFactor) + fiftyFly;
    	else if (e.equals("50 Back"))
    		t = (t * conversionFactor) + fiftyBack;
    	else if (e.equals("50 Breast"))
    		t = (t * conversionFactor) + fiftyBreast;
    	else if (e.equals("50 Free"))
    		t = (t * conversionFactor) + fiftyFree;
    	else if (e.equals("100 Fly"))
    		t = (t * conversionFactor) + hundredFly;
    	else if (e.equals("100 Back"))
    		t = (t * conversionFactor) + hundredBack;
    	else if (e.equals("100 Breast"))
    		t = (t * conversionFactor) + hundredBreast;
    	else if (e.equals("100 Free"))
    		t = (t * conversionFactor) + hundredFree;
    	else if (e.equals("200 Fly"))
    		t = (t * conversionFactor) + twoHundredFly;
    	else if (e.equals("200 Back"))
    		t = (t * conversionFactor) + twoHundredBack;
    	else if (e.equals("200 Breast"))
    		t = (t * conversionFactor) + twoHundredBreast;
    	else if (e.equals("200 Free"))
    		t = (t * conversionFactor) + twoHundredFree;
    	else if (e.equals("200 IM"))
    		t = (t * conversionFactor) + twoHundredIM;
    	else if (e.equals("400 IM"))
    		t = (t * conversionFactor) + fourHundredIM;
    	else if (e.equals("400/500"))
    		t *= fourHundredEq;
    	else if (e.equals("800/1000"))
    		t *= eightHundredEq;
    	else if (e.equals("1500/1650"))
    		t *= fifteenHundredEq;
    	
    	return t;
	}

}
