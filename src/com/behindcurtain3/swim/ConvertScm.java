package com.behindcurtain3.swim;

public class ConvertScm extends Conversion {

	@Override
	double toScy(double t, String e) {
		if (e.equals("400/500"))
    		t = (t + fourHundred) / fourHundredEq;
    	else if (e.equals("800/1000"))
    		t = (t + eightHundred) / eightHundredEq;
    	else if (e.equals("1500/1650"))
    		t = (t + fifteenHundred) / fifteenHundredEq;
    	else
    		t /= conversionFactor;
    	
    	return t;
	}

	@Override
	double toScm(double t, String e) {
		return t;
	}

	@Override
	double toLcm(double t, String e) {
		if(e.equals("50 Fly"))
    		t += fiftyFly;
    	else if (e.equals("50 Back"))
    		t += fiftyBack;
    	else if (e.equals("50 Breast"))
    		t += fiftyBreast;
    	else if (e.equals("50 Free"))
    		t += fiftyFree;
    	else if (e.equals("100 Fly"))
    		t += hundredFly;
    	else if (e.equals("100 Back"))
    		t += hundredBack;
    	else if (e.equals("100 Breast"))
    		t += hundredBreast;
    	else if (e.equals("100 Free"))
    		t += hundredFree;
    	else if (e.equals("200 Fly"))
    		t += twoHundredFly;
    	else if (e.equals("200 Back"))
    		t += twoHundredBack;
    	else if (e.equals("200 Breast"))
    		t += twoHundredBreast;
    	else if (e.equals("200 Free"))
    		t += twoHundredFree;
    	else if (e.equals("200 IM"))
    		t += twoHundredIM;
    	else if (e.equals("400 IM"))
    		t += fourHundredIM;
    	else if (e.equals("400/500"))
    		t += fourHundred;
    	else if (e.equals("800/1000"))
    		t += eightHundred;
    	else if (e.equals("1500/1650"))
    		t += fifteenHundred;
    	
    	return t;
	}

}
