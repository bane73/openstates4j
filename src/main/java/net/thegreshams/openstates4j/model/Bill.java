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
 * Bill
 * 
 * @author Brandon Gresham <brandon@thegreshams.net>
 */
public final class Bill extends Base {
	
	private static final long serialVersionUID = 1L;
	

	public 		@JsonProperty( "bill_id" ) 				String 					id;	
	public 		@JsonProperty( "title" ) 				String 					title;
	public 		@JsonProperty( "updated_at" ) 			Date 					updatedAt;
	public 		@JsonProperty( "state" ) 				String 					state;
	public 		@JsonProperty( "alternate_titles" ) 	List<String> 			alternateTitles;
	public 		@JsonProperty( "session" ) 				String 					session;
	public 		@JsonProperty( "chamber" ) 				String 					chamber;
	public 		@JsonProperty( "type" ) 				List<String> 			types;
	public 		@JsonProperty( "sources" ) 				List<Source> 			sources;
	public 		@JsonProperty( "documents" ) 			List<Document> 			documents;
	public 		@JsonProperty( "versions" ) 			List<Version> 			versions;
	public 		@JsonProperty( "votes" ) 				List<Vote> 				votes;
	public 		@JsonProperty( "sponsors" ) 			List<Sponsor> 			sponsors;
	public 		@JsonProperty( "actions" ) 				List<Action> 			actions;
	
	
	/**
	 * toString()
	 */
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append( Bill.class.getSimpleName() )
			.append( " [ " )
			.append( "(id:" ).append( this.id ).append( ") " )
			.append( "(title:" ).append( this.title ).append( ") " )
			.append( "(updated-at:" ).append( this.updatedAt ).append( ") " )
			.append( "]" );
		
		return sb.toString();
	}
	
	
	/**
	 * Source
	 * 
	 * @author Brandon Gresham <brandon@thegreshams.net>
	 */
	public static class Source extends Base {
		
		private static final long serialVersionUID = 1L;
		
		public		@JsonProperty( "url" )					URL						url;

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
	 * Action
	 * 
	 * @author Brandon Gresham <brandon@thegreshams.net>
	 */
	public static class Action extends Base {
		
		private static final long serialVersionUID = 1L;
		
		public		@JsonProperty( "date" )					Date					date;
		public		@JsonProperty( "actor" )				String					actor;
		public		@JsonProperty( "action" )				String					action;
		public		@JsonProperty( "type" )					List<String>			types;

		@Override
		public String toString() {
			
			StringBuilder sb = new StringBuilder();
			
			sb.append( Member.class.getSimpleName() )
				.append( " [" )
				.append( "(actor:" ).append( this.actor ).append( ") " )
				.append( "(action:" ).append( this.action ).append( ") " )
				.append( "(date:" ).append( this.date ).append( ") " )
				.append( "]" );
			
			return sb.toString();
		}
	}
	
	
	/**
	 * Document
	 * 
	 * @author Brandon Gresham <brandon@thegreshams.net>
	 */
	public static class Document extends Base {
		
		private static final long serialVersionUID = 1L;
		
		public		@JsonProperty( "url" )					URL						url;
		public		@JsonProperty( "name" )					String					name;

		@Override
		public String toString() {
			
			StringBuilder sb = new StringBuilder();
			
			sb.append( Member.class.getSimpleName() )
				.append( " [" )
				.append( "(name:" ).append( this.name ).append( ") " )
				.append( "(url:" ).append( this.url ).append( ") " )
				.append( "]" );
			
			return sb.toString();
		}
	}
	
	
	/**
	 * Version
	 * 
	 * @author Brandon Gresham <brandon@thegreshams.net>
	 */
	public static class Version extends Base {
		
		private static final long serialVersionUID = 1L;
		
		public		@JsonProperty( "url" )					URL						url;
		public		@JsonProperty( "name" )					String					name;

		@Override
		public String toString() {
			
			StringBuilder sb = new StringBuilder();
			
			sb.append( Member.class.getSimpleName() )
				.append( " [" )
				.append( "(name:" ).append( this.name ).append( ") " )
				.append( "(url:" ).append( this.url ).append( ") " )
				.append( "]" );
			
			return sb.toString();
		}
	}
	
	
	/**
	 * Sponsor
	 * 
	 * @author Brandon Gresham <brandon@thegreshams.net>
	 */
	public static class Sponsor extends Base {
		
		private static final long serialVersionUID = 1L;
		
		public		@JsonProperty( "leg_id" )				String					legislatorId;
		public		@JsonProperty( "name" )					String					name;
		public		@JsonProperty( "type" )					String					type;

		@Override
		public String toString() {
			
			StringBuilder sb = new StringBuilder();
			
			sb.append( Member.class.getSimpleName() )
				.append( " [" )
				.append( "(id:" ).append( this.legislatorId ).append( ") " )
				.append( "(name:" ).append( this.name ).append( ") " )
				.append( "(type:" ).append( this.type ).append( ") " )
				.append( "]" );
			
			return sb.toString();
		}
	}
	
	
	/**
	 * Vote
	 * 
	 * @author Brandon Gresham <brandon@thegreshams.net>
	 */
	public static class Vote extends Base {
		
		private static final long serialVersionUID = 1L;
		
		public		@JsonProperty( "date" )					Date					date;
		public		@JsonProperty( "chamber" )				String					chamber;
		public		@JsonProperty( "motion" )				String					motion;
		public		@JsonProperty( "passed" )				boolean					passed;
		public		@JsonProperty( "type" )					String					type;
		public		@JsonProperty( "yes_count" )			int						yesCount;
		public		@JsonProperty( "no_count" )				int						noCount;
		public		@JsonProperty( "other_count" )			int						otherCount;
		public		@JsonProperty( "yes_votes" )			List<Legislator>		yesVotes;
		public		@JsonProperty( "no_votes" )				List<Legislator>		noVotes;
		public		@JsonProperty( "other_votes" )			List<Legislator>		otherVotes;

		@Override
		public String toString() {
			
			StringBuilder sb = new StringBuilder();
			
			sb.append( Member.class.getSimpleName() )
				.append( " [" )
				.append( "(date:" ).append( this.date ).append( ") " )
				.append( "(chamber:" ).append( this.chamber ).append( ") " )
				.append( "(motion:" ).append( this.motion ).append( ") " )
				.append( "(passed:" ).append( this.passed ).append( ") " )
				.append( "]" );
			
			return sb.toString();
		}
	}
	

	public static List<Bill> find( Map<String, String> queryParameters ) throws OpenStatesException, URISyntaxException {
		
		LOGGER.debug( "getting bills using query-parameters: " + queryParameters );
	
		StringBuilder sbQueryPath = new StringBuilder( "bills" );
		
		return OpenStates.queryForJsonAndBuildObject( sbQueryPath.toString(), queryParameters, new TypeReference<List<Bill>>(){} );
	}
	
	public static Bill get( String stateAbbr, String session, String billId ) throws OpenStatesException, URISyntaxException {
		return Bill.get( stateAbbr, session, billId, null );
	}
	public static Bill get( String stateAbbr, String session, String billId, String chamber ) throws OpenStatesException, URISyntaxException {
		
		LOGGER.debug( "getting bill for bill-id(" + billId + "), state(" + stateAbbr + "), session(" + session + ")" +
						( chamber == null ? "" : (", chamber(" + chamber + ")") ) );
		
		StringBuilder sbQueryPath = new StringBuilder( "bills/" + stateAbbr + "/" + session + "/" );
		if( chamber != null ) {
			sbQueryPath.append( chamber + "/" );
		}
		sbQueryPath.append( billId );
		
		return OpenStates.queryForJsonAndBuildObject( sbQueryPath.toString(), Bill.class );
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

		System.out.println( "\n*** BILLS ***\n" );
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put( "state", "ut" );
		queryParams.put( "updated_since", "2012-01-01" );
		queryParams.put( "chamber", "upper" );
		List<Bill> bills = Bill.find( queryParams );
		System.out.println( "Utah's upper-house has " + bills.size() + " bills that have been updated in 2012" );
		System.out.println( "One of Utah's 2012 upper-house bills is: " + bills.get(0).title );
		
		Bill bill = bills.get(0);
		System.out.println( "\nGetting bill: " + bill.id );
		Bill targetBill = Bill.get( bill.state, bill.session, bill.id );
		System.out.println( "Found bill... " + targetBill.title );
	}
	
}
