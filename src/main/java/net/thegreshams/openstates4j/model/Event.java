package net.thegreshams.openstates4j.model;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.thegreshams.openstates4j.model.Committee.Member;
import net.thegreshams.openstates4j.service.OpenStates;
import net.thegreshams.openstates4j.service.OpenStatesException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;


/**
 * Event
 * 
 * @author Brandon Gresham <brandon@thegreshams.net>
 */
public final class Event extends Base {
	
	private static final long serialVersionUID = 1L;
	
	
	public 		@JsonProperty( "id" )						String				id;
	public 		@JsonProperty( "description" )				String				description;
	public 		@JsonProperty( "when" )						Date				when;
	public 		@JsonProperty( "end" )						Date				end;
	public 		@JsonProperty( "location" )					String				location;
	public 		@JsonProperty( "type" )						String				type;
	public		@JsonProperty( "state" )					String				state;
	public 		@JsonProperty( "session" )					String				session;
	public		@JsonProperty( "participants" )				List<Participant>	participants;
	public		@JsonProperty( "sources" )					List<Source>		sources;
	
	
	/**
	 * Participant
	 * 
	 * @author Brandon Gresham <brandon@thegreshams.net>
	 */
	public static class Participant extends Base {
		
		private static final long serialVersionUID = 1L;
		
		public		@JsonProperty( "type" )						String				type;
		public		@JsonProperty( "participant" )				String				participant;
		
		@Override
		public String toString() {
			
			StringBuilder sb = new StringBuilder();
			
			sb.append( Participant.class.getSimpleName() )
				.append( " [" )
				.append( "(participant:" ).append( this.participant ).append( ") " )
				.append( "(type:" ).append( this.type ).append( ") " )
				.append( "]" );
			
			return sb.toString();
		}
	}
	

	/**
	 * Source
	 * 
	 * @author Brandon Gresham <brandon@thegreshams.net>
	 */
	public static class Source extends Base {
		
		private static final long serialVersionUID = 1L;
		
		public		@JsonProperty( "url" )				URL						url;

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
	 * toString()
	 */
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append( Event.class.getSimpleName() )
			.append( " [ " )
			.append( "(id:" ).append( this.id ).append( ") " )
			.append( "(description:" ).append( this.description ).append( ") " )
			.append( "]" );
		
		return sb.toString();
	}

	public static List<Event> find( Map<String, String> queryParameters ) throws OpenStatesException, URISyntaxException {
		
		LOGGER.debug( "getting events using query-parameters: " + queryParameters );
		
		StringBuilder sbQueryPath = new StringBuilder( "events" );
		
		return OpenStates.queryForJsonAndBuildObject( sbQueryPath.toString(), queryParameters, new TypeReference<List<Event>>(){} );
	}
	
	public static Event get( String eventId ) throws OpenStatesException, URISyntaxException {
		
		LOGGER.debug( "finding event for event-id(" + eventId + ")" );
		
		StringBuilder sbQueryPath = new StringBuilder( "events/" + eventId );
		
		return OpenStates.queryForJsonAndBuildObject( sbQueryPath.toString(), Event.class );
	}
	
	
////////////////////////////////////////////////////////////////////////////////
	
	 
	
	public static void main( String[] args) throws OpenStatesException, URISyntaxException {

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

		System.out.println( "\n*** EVENTS ***\n" );
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put( "state", "tx" );
		List<Event> events = Event.find( queryParams );
		System.out.println( "Texas has " + events.size() + " events" );
		System.out.println( "One of Texas's events is: " + events.get(0).description );
		
		String targetEventId = events.get(0).id;
		System.out.println( "\nGetting event: " + targetEventId );
		Event targetEvent = Event.get( targetEventId );
		System.out.println( "Found event... " + targetEvent.description );
		
	}
	
}
