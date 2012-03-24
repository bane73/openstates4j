package net.thegreshams.openstates4j.model.subs;

import java.net.URL;

import net.thegreshams.openstates4j.model.Base;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Document
 * 
 * @author Brandon Gresham <brandon@thegreshams.net>
 */
public class Document extends Base {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty( "url" )
	private URL url;
	public URL getUrl() {
		return this.url;
	}
	
	@JsonProperty( "name" )
	private String name;
	public String getName() {
		return this.name;
	}
	
	private Document() { }
	
	@Override
	public String toString() {
		return this.name + "(" + this.url + ")";
	}

}
