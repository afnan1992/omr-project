package com.omr.exceptions;

public class WrongMarkers extends Exception {
	
	private static final long serialVersionUID = 8591538932915524955L;
	public static int code = 2;
	private int circles;
	public WrongMarkers(int nu) {
		super("Error Code"+code);
		circles = nu;
	}
	public String getReason() {
		return "Wrong Markers Detection Circles found"+circles;
	}

}
