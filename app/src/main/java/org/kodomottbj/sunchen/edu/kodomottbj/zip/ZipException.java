package org.kodomottbj.sunchen.edu.kodomottbj.zip;

public class ZipException extends Exception {
	private static final long serialVersionUID = -5313116368532927624L;
	public ZipException(String message, Throwable cause) {
		super(message, cause);
	}

	public ZipException(String format, Object... args) {
		super(String.format(format, args));
	}

	public ZipException(String message) {
		super(message);
	}

	public ZipException(Throwable cause) {
		super(cause);
	}
}
