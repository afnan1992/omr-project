package com.omr.exceptions;

public class WrongFileAttributes extends Exception {

	private static final long serialVersionUID = -1153918119408614913L;
	public static int code = 12;
	private String path, filename, nameonly,message;
	public WrongFileAttributes(String path, String filename, String nameonly) {
		this.path = path;
		this.filename = filename;
		this.nameonly = nameonly;
	}
	public WrongFileAttributes(String message) {
		super("Error Code"+code);
		this.message = message;
	}
	public String getReason(){
		if(message == null) return "Wrong file attributes found: Path "+path+" filename "+filename+" nameonly "+nameonly;
		return message;
	}

}
