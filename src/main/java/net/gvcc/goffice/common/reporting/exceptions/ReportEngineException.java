package net.gvcc.goffice.common.reporting.exceptions;

public class ReportEngineException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7780228292073067453L;

	public ReportEngineException(String message, Exception e) {
		super(message, e);
	}
}
