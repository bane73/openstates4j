package net.thegreshams.openstates4j.model.subs;

import java.util.Date;
import java.util.List;

import net.thegreshams.openstates4j.model.Base;

import org.codehaus.jackson.annotate.JsonProperty;

public class Action extends Base {

	private static final long serialVersionUID = 1L;
	
	
	@JsonProperty( "date" )
	private Date date;
	public Date getDate() {
		return this.date;
	}
	
	@JsonProperty( "actor" )
	private String actor;
	public String getActor() {
		return this.actor;
	}
	
	@JsonProperty( "action" )
	private String action;
	public String getAction() {
		return this.action;
	}

	@JsonProperty( "type" )
	private List<String> types;
	public List<String> getTypes() {
		return this.types;
	}
	
	@Override
	public String toString() {
		return this.actor + " " + this.action + "(" + this.types + ")";
	}
	
}
