package com.omr.exceptions;

public class QrFailedToDetect extends Exception {
	
	private static final long serialVersionUID = 7459962180944972587L;
	public static int code = 16;
	private String img;
	public QrFailedToDetect(String img){
		super("Error code "+code);
		this.img = img;
	}
	public String getReason(){
		return "Failed to Read Qrcode for file name "+img;
	}
}
