package net.thegreshams.openstates4j.model.subs;

import net.thegreshams.openstates4j.model.Base;

import org.codehaus.jackson.annotate.JsonProperty;

public class Legislator extends Base {

	private static final long serialVersionUID = 1L;
	
	
	public static class SimpleLegislator extends Base {
		
		private static final long serialVersionUID = 1L;
		
		@JsonProperty( "leg_id" )
		private String id;
		public String getId() {
			return this.id;
		}
		
		@JsonProperty( "name" )
		private String name;
		public String getName() {
			return this.name;
		}
		
	}
	
	public static class Sponsor extends Base {
		
		private static final long serialVersionUID = 1L;
		
		@JsonProperty( "leg_id" )
		private String id;
		public String getId() {
			return this.id;
		}
		
		@JsonProperty( "name" )
		private String name;
		public String getName() {
			return this.name;
		}
		
		@JsonProperty( "type" )
		private String type;
		public String getType() {
			return this.type;
		}
		
		@Override
		public String toString() {
			return this.name + ":" + this.type;
		}
	}
	
}
