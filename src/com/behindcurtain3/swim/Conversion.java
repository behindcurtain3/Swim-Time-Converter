package com.behindcurtain3.swim;

public abstract class Conversion {
	abstract double toScy(double t, String e);
	abstract double toScm(double t, String e);
	abstract double toLcm(double t, String e);
	
	// Conversion factors
	public static double conversionFactor = 1.11;
	
	// 50's
	public static double fiftyBreast = 1.0;
	public static double fiftyBack = 0.6;
	public static double fiftyFly = 0.7;
	public static double fiftyFree = 0.8;
	
	// 100's
	public static double hundredFly = 1.4;
	public static double hundredBack = 1.2;
	public static double hundredBreast = 2.0;
	public static double hundredFree = 1.6;
	
	// 200's
	public static double twoHundredFly = 2.8;
	public static double twoHundredBack = 2.4;
	public static double twoHundredBreast = 4.0;
	public static double twoHundredFree = 3.2;
	public static double twoHundredIM = 3.2;
	
	// 400
	public static double fourHundredIM = 6.4;
	
	// Distance, uses different calculation
	public static double fourHundredEq = 0.8925;
	public static double eightHundredEq = 0.8925;
	public static double fifteenHundredEq = 1.02;
	public static double fourHundred = 6.4;
	public static double eightHundred = 12.8;
	public static double fifteenHundred = 24.0;
}
