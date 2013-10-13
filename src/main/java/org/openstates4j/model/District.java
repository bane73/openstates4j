package org.openstates4j.model;

import java.util.List;

import org.openstates4j.service.OpenStates;
import org.openstates4j.service.OpenStatesException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;


/**
 * District
 * 
 * @author Brandon Gresham <brandon@thegreshams.net>
 */
public class District extends Base {
	
	private static final long serialVersionUID = 1L;
	
	
	public		@JsonProperty( "name" )						String				name;
	public		@JsonProperty( "chamber" )					String				chamber;
	public		@JsonProperty( "abbr" )						String				abbr;
	public		@JsonProperty( "num_seats" )				int					numSeats;
	public		@JsonProperty( "boundary_id" )				String				boundaryId;
	public		@JsonProperty( "legislators" )				List<Legislator>	legislators;
	
	
	/**
	 * Legislator
	 * 
	 * @author Brandon Gresham <brandon@thegreshams.net>
	 */
	public static class Legislator extends Base {
		
		private static final long serialVersionUID = 1L;
		
		public		@JsonProperty( "leg_id" )					String				legislatorId;
		public		@JsonProperty( "full_name" )				String				fullName;
		
		@Override
		public String toString() {
			
			StringBuilder sb = new StringBuilder();
			
			sb.append( Legislator.class.getSimpleName() )
				.append( " [" )
				.append( "(id:" ).append( this.legislatorId ).append( ") " )
				.append( "(fullName:" ).append( this.fullName ).append( ") " )
				.append( "]" );
			
			return sb.toString();
		}
	}
	
	
	/**
	 * Boundary
	 * 
	 * @author Brandon Gresham <brandon@thegreshams.net>	
	 */
	public static class Boundary extends Base {
		
		private static final long serialVersionUID = 1L;
		
		public 		@JsonProperty( "name" )						String				name;
		public		@JsonProperty( "chamber" )					String				chamber;
		public		@JsonProperty( "abbr" ) 					String				abbr;
		public		@JsonProperty( "boundary_id" )				String				boundaryId;
		public		@JsonProperty( "num_seats" )				int					numSeats;
		public		@JsonProperty( "region" )					Region				region;
		public		@JsonProperty( "shape" )					String[][][][]		shape;
		
		
		/**
		 * Region
		 * 
		 * @author Brandon Gresham <brandon@thegreshams.net>
		 */
		public static class Region extends Base {
			
			private static final long serialVersionUID = 1L;
			
			public		@JsonProperty( "lon_delta" )				String				lonDelta;
			public		@JsonProperty( "center_lon" )				String				lonCenter;
			public		@JsonProperty( "lat_delta" )				String				latDelta;
			public		@JsonProperty( "center_lat" )				String				latCenter;
		}
		
		
		/**
		 * Shape
		 * 
		 * @author Brandon Gresham <brandon@thegreshams.net>
		 */
		public static class Shape extends Base {
			
			private static final long serialVersionUID = 1L;			
		}
		
					
		public static Boundary get( District district ) throws OpenStatesException {
			return Boundary.get( district.boundaryId );
		}
		public static Boundary get( String boundaryId ) throws OpenStatesException {
			
			LOGGER.debug( "getting boundary for boundary-id(" + boundaryId + ")" );
			
			StringBuilder sbQueryPath = new StringBuilder( "districts/boundary/" + boundaryId );
			
			return OpenStates.queryForJsonAndBuildObject( sbQueryPath.toString(),  Boundary.class );
		}
		
	}
	
	
	/**
	 * toString()
	 */
	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		
		sb.append( District.class.getSimpleName() )
			.append( " [ " )
			.append( "(name:" ).append( this.name ).append( ") " )
			.append( "(chamber:" ).append( this.chamber ).append( ") " )
			.append( "(abbr:" ).append( this.abbr ).append( ") " )
			.append( "]" );
		
		return sb.toString();
	}

	public static List<District> find( String stateAbbr ) throws OpenStatesException {
		return District.find( stateAbbr, null );
	}
	public static List<District> find( String stateAbbr, String chamber ) throws OpenStatesException {
		
		LOGGER.debug( "finding districts for state(" + stateAbbr + ") and chamber(" + chamber + ")" );
		
		StringBuilder sbQueryPath = new StringBuilder( "districts/" + stateAbbr );
		if( chamber != null ) {
			sbQueryPath.append( "/" + chamber );
		}
		return OpenStates.queryForJsonAndBuildObject( sbQueryPath.toString(), new TypeReference<List<District>>(){} );
	}


	
	public static void main( String[] args ) throws OpenStatesException {
 
		// get the API key
		String openStates_apiKey = null;
		for( String s : args ) {

			if( s == null || s.trim().isEmpty() ) continue;
			if( s.trim().split( "=" )[0].equals( "apiKey" ) ) {
				openStates_apiKey = s.trim().split( "=" )[1];
			}
		}
		if( openStates_apiKey == null || openStates_apiKey.trim().isEmpty() ) {
			throw new IllegalArgumentException( "Program-argument 'apiKey' not found but required" );
		}
		OpenStates.setApiKey( openStates_apiKey );

		System.out.println( "\n*** DISTRICTS ***\n" );
		List<District> allUtahDistricts = District.find( "ut" );
		List<District> utahLowerDistricts = District.find( "ut", "lower" );
		List<District> utahUpperDistricts = District.find( "ut", "upper" );
		
		
		System.out.println( "Utah's lower house has " + utahLowerDistricts.size() + " districts" );
		System.out.println( "Utah's upper house has " + utahUpperDistricts.size() + " districts" );
		System.out.println( "Utah has " + allUtahDistricts.size() + " total districts" );

		Boundary boundary = Boundary.get( utahUpperDistricts.get(5) );
		System.out.println( "Utah's " + boundary.chamber + " district " + boundary.name + " has " + boundary.numSeats + " seat(s) (" + boundary.boundaryId + ")" );
		
		
	}

}




