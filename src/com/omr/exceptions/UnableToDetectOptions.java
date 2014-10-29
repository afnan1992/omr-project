package com.omr.exceptions;

public class UnableToDetectOptions extends Exception {

	private static final long serialVersionUID = 6171857837401267512L;
	public static int code = 4;
	public UnableToDetectOptions() {
		super("Error Code"+code);
	}
}
