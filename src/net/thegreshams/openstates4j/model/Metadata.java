package net.thegreshams.openstates4j.model;

import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.thegreshams.openstates4j.service.OpenStates;
import net.thegreshams.openstates4j.service.OpenStatesException;

import org.codehaus.jackson.annotate.JsonProperty;

public class Metadata extends Base {

	private static final long serialVersionUID = 1L;
	

	public		@JsonProperty( "name" )						String				name;
	public		@JsonProperty( "abbreviation" )				String				abbreviation;
	public		@JsonProperty( "legislature_name" )			String				legislatureName;
	public		@JsonProperty( "upper_chamber_name" )		String				upperChamberName;
	public		@JsonProperty( "lower_chamber_name" )		String				lowerChamberName;
	public		@JsonProperty( "upper_chamber_term" )		String				upperChamberTerm;
	public		@JsonProperty( "lower_chamber_term" )		String				lowerChamberTerm;
	public		@JsonProperty( "upper_chamber_title" )		String				upperChamberTitle;
	public		@JsonProperty( "lower_chamber_title" )		String				lowerChamberTitle;
	public		@JsonProperty( "latest_dump_url" )			URL					latestDumpUrl;
	public		@JsonProperty( "latest_dump_date" )			Date				latestDumpDate;
	public		@JsonProperty( "terms" )					List<Term>			terms;
	public		@JsonProperty( "session_details" )			Map<String, Term>	sessionDetails;
	public		@JsonProperty( "feature_flags" )			List<String>		featureFlags;
	

	/**
	 * Term
	 * 
	 * @author Brandon Gresham <brandon@thegreshams.net>
	 */
	public static class Term extends Base {
		
		private static final long serialVersionUID = 1L;
		
		public		@JsonProperty( "start_year" )				String				startYear;
		public		@JsonProperty( "end_year" )					String				endYear;
		public		@JsonProperty( "name" )						String				name;
		public		@JsonProperty( "sessions" )					List<String>		session;
		
		@Override
		public String toString() {
			
			StringBuilder sb = new StringBuilder();
			
			sb.append( Term.class.getSimpleName() )
				.append( " [" )
				.append( "(name:" ).append( this.name ).append( ") " )
				.append( "]" );
			
			return sb.toString();
		}
	}

	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append( Metadata.class.getSimpleName() )
			.append( " [" )
			.append( "(name:" ).append( this.name ).append( ") " )
			.append( "]" );
		
		return sb.toString();
	}
	
	
	public static Metadata get( String stateAbbr ) throws OpenStatesException {
		
		LOGGER.debug( "finding metadata for state(" + stateAbbr + ")" );
		
		StringBuilder sbQueryPath = new StringBuilder( "metadata/" + stateAbbr );
		
		return OpenStates.queryForJsonAndBuildObject( sbQueryPath.toString(), Metadata.class );
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

		System.out.println( "\n*** METADATA ***\n" );
		Metadata metaData = Metadata.get( "ut" );
		System.out.println( "Utah's metadata has the following feature-flags: "+ metaData.featureFlags );
	}
	
}
