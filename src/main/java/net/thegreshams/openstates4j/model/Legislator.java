package net.thegreshams.openstates4j.model;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.thegreshams.openstates4j.model.Committee.Member;
import net.thegreshams.openstates4j.service.OpenStates;
import net.thegreshams.openstates4j.service.OpenStatesException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;

public class Legislator extends Base implements Comparable<Legislator> {

	private static final long serialVersionUID = 1L;
	
	
	public 		@JsonProperty( "leg_id" )					String				id;
	public		@JsonProperty( "full_name" )				String				fullName;
	public		@JsonProperty( "first_name" )				String				firstName;
	public		@JsonProperty( "last_name" )				String				lastName;
	public		@JsonProperty( "middle_name" )				String				middleName;
	public		@JsonProperty( "suffixes" )					String				suffixes;
	public		@JsonProperty( "url" )						URL					url;
	public		@JsonProperty( "email" )					String				email;
	public		@JsonProperty( "photo_url" )				URL					photoUrl;
	public		@JsonProperty( "active" )					boolean				isActive;
	public		@JsonProperty( "state" )					String				state;
	public		@JsonProperty( "chamber" )					String				chamber;
	public		@JsonProperty( "district" )					String				district;
	public		@JsonProperty( "party" )					String				party;
	public		@JsonProperty( "roles" )					List<Role>			roles;
	public		@JsonProperty( "old_roles" )				Map<String, List<Role>>	oldRoles;
	public		@JsonProperty( "sources" )					List<Source>		sources;
	

	@Override
	public int compareTo(Legislator o) {
		return this.id.compareTo(o.id);
	}
	
	
	/**
	 * Source
	 * 
	 * @author Brandon Gresham <brandon@thegreshams.net>
	 */
	public static class Source extends Base {
		
		private static final long serialVersionUID = 1L;
		
		public		@JsonProperty( "url" )						URL					url;

		@Override
		public String toString() {
			
			StringBuilder sb = new StringBuilder();
			
			sb.append( Member.class.getSimpleName() )
				.append( " [" )
				.append( "(url:" ).append( this.url ).append( ") " )
				.append( "]" );
			
			return sb.toString();
		}
	}

	/**
	 * Role
	 * 
	 * @author Brandon Gresham <brandon@thegreshams.net>
	 */
	public static class Role extends Base {
		
		private static final long serialVersionUID = 1L;
		
		public		@JsonProperty( "type" )						String				type;
		public		@JsonProperty( "term" )						String				term;
		public		@JsonProperty( "chamber" )					String				chamber;
		public		@JsonProperty( "district" )					String				district;
		public		@JsonProperty( "party" )					String				party;
		public		@JsonProperty( "committee" )				String				committee;

		@Override
		public String toString() {
			
			StringBuilder sb = new StringBuilder();
			
			sb.append( Member.class.getSimpleName() )
				.append( " [" )
				.append( "(type:" ).append( this.type ).append( ") " )
				.append( "]" );
			
			return sb.toString();
		}
	}

	
	/**
	 * toString()
	 */
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append( Legislator.class.getSimpleName() )
			.append( " [ " )
			.append( "(id:" ).append( this.id ).append( ") " )
			.append( "(name:" ).append( this.fullName ).append( ") " )
			.append( "]" );
		
		return sb.toString();
	}
	
	public static List<Legislator> find( Map<String, String> queryParameters ) throws OpenStatesException, URISyntaxException {
		
		LOGGER.debug( "getting legislators using query-parameters: " + queryParameters );
		
		StringBuilder sbQueryPath = new StringBuilder( "legislators" );
		
		return OpenStates.queryForJsonAndBuildObject( sbQueryPath.toString(), queryParameters, new TypeReference<List<Legislator>>(){} );
	}
	
	public static Legislator get( String legislatorId ) throws OpenStatesException, URISyntaxException {
		
		LOGGER.debug( "getting legislator for legislator-id(" + legislatorId + ")" );
		
		StringBuilder sbQueryPath = new StringBuilder( "legislators/" + legislatorId );
		
		return OpenStates.queryForJsonAndBuildObject( sbQueryPath.toString(), Legislator.class );
	}
	
	public static List<Legislator> find( String longitude, String latitude ) throws OpenStatesException, URISyntaxException {
		
		LOGGER.debug( "getting legislators using longitude(" + longitude + ") and latitude(" + latitude + ")" );

		StringBuilder sbQueryPath = new StringBuilder( "legislators/geo" ); 
		
		Map<String, String> geoParams = new HashMap<String, String>();
		geoParams.put( "long", longitude );
		geoParams.put( "lat", latitude );
		
		return OpenStates.queryForJsonAndBuildObject( sbQueryPath.toString(), geoParams, new TypeReference<List<Legislator>>(){} );
	}
	
	

	public static void main( String[] args ) throws OpenStatesException, URISyntaxException {

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

		System.out.println( "\n*** LEGISLATORS ***\n" );
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put( "state", "ut" );
		List<Legislator> legislators = Legislator.find( queryParams );
		System.out.println( "Utah has " + legislators.size() + " legislators" );
		queryParams.put( "party", "Republican" );
		List<Legislator> repLegs = Legislator.find( queryParams );
		queryParams.put( "party", "Democratic" );
		List<Legislator> demLegs = Legislator.find( queryParams );
		System.out.println( "Utah has " + repLegs.size() + " republican legislators and " + demLegs.size() + " democrat legislators");
		
		String targetLegislatorId = legislators.get(0).id;
		System.out.println(  "\nGetting legislator: " + targetLegislatorId );
		Legislator targetLegislator = Legislator.get( targetLegislatorId );
		System.out.println( "Found legislator... " + targetLegislator.fullName );
		
		String targetLong = "-78.76648";
		String targetLat = "35.81336";
		System.out.println( "Getting legislators in specific geo-area..." );
		List<Legislator> geoLegislators = Legislator.find( targetLong, targetLat );
		System.out.println( "That geo-area has " + geoLegislators.size() + " legislators" );
		System.out.println( "One of them is: " + geoLegislators.get(0).fullName );
	}
		
}
