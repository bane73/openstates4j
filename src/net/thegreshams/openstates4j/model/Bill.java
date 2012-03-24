package net.thegreshams.openstates4j.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.thegreshams.openstates4j.model.Committee.Member;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;


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
	
	

	
	
////////////////////////////////////////////////////////////////////////////////
	
	 
	
	public static void main( String[] args) throws IOException {

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
				
		// get an Event
		URL url = new URL( "http://openstates.org/api/v1/bills/ca/20092010/AB%20667/?apikey=" + openStates_apiKey );
		InputStream is = url.openStream();
		Writer writer = new StringWriter();
		char[] buffer = new char[1024];
		Reader reader = new BufferedReader( new InputStreamReader( is, "UTF-8" ) );
		int n;
		while( (n=reader.read(buffer)) != -1 ) {
			writer.write(buffer, 0, n);
		}
		is.close();
		String jsonBill = writer.toString();
		System.out.println( "JSON:\n" + jsonBill + "\n\n" );
	
		// perform the mapping, spit-out the object
		ObjectMapper mapper = new ObjectMapper();	
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		mapper.setDateFormat( sdf );
		Bill bill = mapper.readValue( jsonBill, Bill.class );
		System.out.println( "\n\n" + bill );
		System.out.println( "SOURCES: " + bill.sources );
		Map<String, Object> optionalProps = bill.optionalProperties;
		Iterator<String> it = optionalProps.keySet().iterator();
		System.out.println( "Optional Properties..." );
		while( it.hasNext() ) {
			String key = it.next();
			System.out.println( "   --> " + key + ":" + optionalProps.get(key) );
		}
		System.out.println( "SPONSORS --> " + bill.sponsors );
	}
	
}