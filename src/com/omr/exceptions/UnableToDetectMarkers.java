package com.omr.exceptions;

public class UnableToDetectMarkers extends Exception {

	private static final long serialVersionUID = -7030596052846131535L;
	public static int code = 2;
	private String region;
	private int objects;
	public UnableToDetectMarkers(int objects,String region){
		super("Error Code"+code);
		this.region = region;
		this.objects = objects;
	}
	public String getReason(){
		return "Found Objects "+objects+" In "+region+" Area";
	}
}
