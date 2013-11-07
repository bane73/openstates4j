package net.thegreshams.openstates4j.model;

import java.net.URL;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Source
 * 
 * @author Brandon Gresham <brandon@thegreshams.net>
 */
public class Source extends Base {
	
	private static final long serialVersionUID = 1L;
	
	public		@JsonProperty( "url" )				URL						url;

	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append( Source.class.getSimpleName() )
			.append( " [" )
			.append( "(url:" ).append( this.url ).append( ") " )
			.append( "]" );
		
		return sb.toString();
	}
}


