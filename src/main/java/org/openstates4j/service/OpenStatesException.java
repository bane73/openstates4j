package org.openstates4j.service;

import org.apache.log4j.Logger;

public class OpenStatesException extends Throwable {

	protected static final Logger LOGGER = Logger.getRootLogger();
	
	private static final long serialVersionUID = 1L;

	public OpenStatesException( String message ) {
		super( message );
	}
	
	public OpenStatesException( String message, Throwable cause ) {
		super( message, cause );
	}
	
}
