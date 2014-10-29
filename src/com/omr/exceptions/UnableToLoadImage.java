package com.omr.exceptions;

public class UnableToLoadImage extends Exception {
	private static final long serialVersionUID = 5957255427825945015L;
	public static int code = 10;
	private String path;
	public UnableToLoadImage(String path){
		super("Error Code"+code);
		this.path = path;
	}
	public String getReason(){
		return "Unable to open file at "+path;
	}
}
