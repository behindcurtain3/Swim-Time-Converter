package com.behindcurtain3.swim;

public class ConvertLcm extends Conversion {

	@Override
	double toScy(double t, String e) {
		if(e.equals("50 Fly"))
    		t = (t - fiftyFly) / conversionFactor;
    	else if (e.equals("50 Back"))
    		t = (t - fiftyBack) / conversionFactor;
    	else if (e.equals("50 Breast"))
    		t = (t - fiftyBreast) / conversionFactor;
    	else if (e.equals("50 Free"))
    		t = (t - fiftyFree) / conversionFactor;
    	else if (e.equals("100 Fly"))
    		t = (t - hundredFly) / conversionFactor;
    	else if (e.equals("100 Back"))
    		t = (t - hundredBack) / conversionFactor;
    	else if (e.equals("100 Breast"))
    		t = (t - hundredBreast) / conversionFactor;
    	else if (e.equals("100 Free"))
    		t = (t - hundredFree) / conversionFactor;
    	else if (e.equals("200 Fly"))
    		t = (t - twoHundredFly) / conversionFactor;
    	else if (e.equals("200 Back"))
    		t = (t - twoHundredBack) / conversionFactor;
    	else if (e.equals("200 Breast"))
    		t = (t - twoHundredBreast) / conversionFactor;
    	else if (e.equals("200 Free"))
    		t = (t - twoHundredFree) / conversionFactor;
    	else if (e.equals("200 IM"))
    		t = (t - twoHundredIM) / conversionFactor;
    	else if (e.equals("400 IM"))
    		t = (t - fourHundredIM) / conversionFactor;
    	else if (e.equals("400/500"))
    		t /= fourHundredEq;
    	else if (e.equals("800/1000"))
    		t /= eightHundredEq;
    	else if (e.equals("1500/1650"))
    		t /= fifteenHundredEq;
    	
    	return t;
	}

	@Override
	double toScm(double t, String e) {
		if(e.equals("50 Fly"))
    		t -= fiftyFly;
    	else if (e.equals("50 Back"))
    		t -= fiftyBack;
    	else if (e.equals("50 Breast"))
    		t -= fiftyBreast;
    	else if (e.equals("50 Free"))
    		t -= fiftyFree;
    	else if (e.equals("100 Fly"))
    		t -= hundredFly;
    	else if (e.equals("100 Back"))
    		t -= hundredBack;
    	else if (e.equals("100 Breast"))
    		t -= hundredBreast;
    	else if (e.equals("100 Free"))
    		t -= hundredFree;
    	else if (e.equals("200 Fly"))
    		t -= twoHundredFly;
    	else if (e.equals("200 Back"))
    		t -= twoHundredBack;
    	else if (e.equals("200 Breast"))
    		t -= twoHundredBreast;
    	else if (e.equals("200 Free"))
    		t -= twoHundredFree;
    	else if (e.equals("200 IM"))
    		t -= twoHundredIM;
    	else if (e.equals("400 IM"))
    		t -= fourHundredIM;
    	else if (e.equals("400/500"))
    		t -= fourHundred;
    	else if (e.equals("800/1000"))
    		t -= eightHundred;
    	else if (e.equals("1500/1650"))
    		t -= fifteenHundred;
    	
    	return t;
	}

	/*
	 * (non-Javadoc)
	 * @see com.behindcurtain3.swim.Conversion#convertToLcm(double, java.lang.String)
	 * Returns t as there is no conversion.
	 */
	@Override
	double toLcm(double t, String e) {
		return t;
	}

}
