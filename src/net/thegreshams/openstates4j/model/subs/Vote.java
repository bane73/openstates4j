package net.thegreshams.openstates4j.model.subs;

import java.util.Date;
import java.util.List;

import net.thegreshams.openstates4j.model.Base;
import net.thegreshams.openstates4j.model.subs.Legislator.SimpleLegislator;

import org.codehaus.jackson.annotate.JsonProperty;

public class Vote extends Base {

	private static final long serialVersionUID = 1L;

	@JsonProperty( "date" )
	private Date date;
	public Date getDate() {
		return this.date;
	}
	
	@JsonProperty( "chamber" )
	private String chamber;
	public String getChamber() {
		return this.chamber;
	}
	
	@JsonProperty( "motion" )
	private String motion;
	public String getMotion() {
		return this.motion;
	}
	
	@JsonProperty( "passed" )
	private boolean passed;
	public boolean getPassed() {
		return this.passed;
	}
	
	@JsonProperty( "type" )
	private String type;
	public String getType() {
		return this.type;
	}
	
	@JsonProperty( "yes_count" )
	private int yesCount;
	public int getYesCount() {
		return this.yesCount;
	}
	
	@JsonProperty( "no_count" )
	private int noCount;
	public int getNoCount() {
		return this.noCount;
	}
	
	@JsonProperty( "other_count" )
	private int otherCount;
	public int getOtherCount() {
		return this.otherCount;
	}
	
	@JsonProperty( "yes_votes" )
	private List<SimpleLegislator> yesVotes;
	public List<SimpleLegislator> getYesVotes() {
		return this.yesVotes;
	}
	
	@JsonProperty( "no_votes" )
	private List<SimpleLegislator> noVotes;
	public List<SimpleLegislator> getNoVotes() {
		return this.noVotes;
	}
	
	@JsonProperty( "other_votes" )
	private List<SimpleLegislator> otherVotes;
	public List<SimpleLegislator> getOtherVotes() {
		return this.otherVotes;
	}
	
	private Vote() { }
	
	@Override
	public String toString() {
		return "(" + ( this.passed ? "PASSED" : "FAILED" ) + ") " + this.motion;
	}
	
}
