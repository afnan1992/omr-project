package com.omr.exceptions;

public class MappingNotCorrect extends Exception {


	private static final long serialVersionUID = 9062196853834940390L;
	public static int code = 14;
	public MappingNotCorrect(){
		super("Error Code"+code);
	}
	public String getReason(){
		return "Page layout is not correct";
	}
}
