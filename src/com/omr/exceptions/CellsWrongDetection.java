package com.omr.exceptions;

public class CellsWrongDetection extends Exception {
	
	private static final long serialVersionUID = 7508524233546126195L;
	public static int code = 4;
	private int found,total;
	public CellsWrongDetection(int found,int expected){
		super("Error Code"+code);
		this.found = found;
		this.total = expected;
	}
	public String getReason(){
		return "Found Less Coloumns which are "+found+" Expected "+total;
	}
}
