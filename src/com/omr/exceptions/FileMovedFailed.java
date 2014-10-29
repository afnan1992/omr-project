package com.omr.exceptions;

public class FileMovedFailed extends Exception {

	private static final long serialVersionUID = -7500320377007340618L;
	public int code = 11;
	private String filename;
	public FileMovedFailed(String name){
		filename = name;
	}
	public String getReason(){
		return "Failed To Move file "+filename;
	}
}
