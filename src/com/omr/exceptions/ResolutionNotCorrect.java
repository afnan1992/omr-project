package com.omr.exceptions;

public class ResolutionNotCorrect extends Exception {
	private static final long serialVersionUID = 4123116531396962025L;
	private int dpi;
	public final static int code = 1;
	public ResolutionNotCorrect(int dpi) {
		super("Error Code"+code);
		this.dpi = dpi;
	}
	public String getReason(){
		return "Expected Resolution is 150dpi, found "+dpi+"dpi";
	}

}
