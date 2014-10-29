package com.omr.exceptions;

public class CancelException extends Exception {

	private static final long serialVersionUID = -5274868866250328618L;
	public static int code = 15;
	private String imgname;
	public CancelException(String name){
		super("Error Code "+ code);
		imgname = name;
	}
	public String getReason(){
		return "User didn't provided Barcode for image"+imgname;
	}
}
