package com.omr.exceptions;

public class AssessmentNotExist extends Exception {

	private static final long serialVersionUID = 5090290439866042829L;
	public static int code = 5;
	private String dkey;
	public AssessmentNotExist(String in){
		super("Error Code"+code);
		dkey = in;
	}
	public String getReason(){
		return "Assessment not found by Dkey"+dkey;
	}
}
