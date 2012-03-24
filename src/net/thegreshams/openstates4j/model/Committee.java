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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Committee
 * 
 * @author Brandon Gresham <brandon@thegreshams.net>
 */
public class Committee extends Base {

	private static final long serialVersionUID = 1L;
	
	
	
	public		@JsonProperty( "id" )							String			id;
	public		@JsonProperty( "chamber" )						String			chamber;
	public		@JsonProperty( "state" )						String			state;
	public		@JsonProperty( "committee" )					String			committee;
	public		@JsonProperty( "subcommittee" )					String			subCommittee;
	public		@JsonProperty( "parentId" )						String			parentId;
	public		@JsonProperty( "members" )						List<Member>	members;
	public		@JsonProperty( "sources" )						List<Source>	sources;
	
	
	
	
	/**
	 * Member
	 * 
	 * @author Brandon Gresham <brandon@thegreshams.net>
	 */
	public static class Member extends Base {
		
		private static final long serialVersionUID = 1L;
		
		public		@JsonProperty( "legislator" )					String			legislator;
		public		@JsonProperty( "role" )							String			role;
		public		@JsonProperty( "leg_id" )						String			legislatorId;
		
		@Override
		public String toString() {
			
			StringBuilder sb = new StringBuilder();
			
			sb.append( Member.class.getSimpleName() )
				.append( " [" )
				.append( "(legislator:" ).append( this.legislator ).append( ") " )
				.append( "(role:" ).append( this.role ).append( ") " )
				.append( "(leg_id:" ).append( this.legislatorId ).append( ") " )
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
		
		public		@JsonProperty( "source" )						URL				source;
		
		@Override
		public String toString() {
			
			StringBuilder sb = new StringBuilder();
			
			sb.append( Member.class.getSimpleName() )
				.append( " [" )
				.append( "(source:" ).append( this.source ).append( ") " )
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
		URL url = new URL( "http://openstates.org/api/v1/committees/MDC000065/?apikey=" + openStates_apiKey );
		InputStream is = url.openStream();
		Writer writer = new StringWriter();
		char[] buffer = new char[1024];
		Reader reader = new BufferedReader( new InputStreamReader( is, "UTF-8" ) );
		int n;
		while( (n=reader.read(buffer)) != -1 ) {
			writer.write(buffer, 0, n);
		}
		is.close();
		String jsonCommittee = writer.toString();
		System.out.println( "JSON:\n" + jsonCommittee + "\n\n" );
		
		
		// perform the mapping, spit-out the object
		ObjectMapper mapper = new ObjectMapper();	
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		mapper.setDateFormat( sdf );
		Committee committee = mapper.readValue( jsonCommittee, Committee.class );
		System.out.println( "\n\n" + committee );
		Map<String, Object> optionalProps = committee.optionalProperties;
		Iterator<String> it = optionalProps.keySet().iterator();
		System.out.println( "Optional Properties..." );
		while( it.hasNext() ) {
			String key = it.next();
			System.out.println( "   --> " + key + ":" + optionalProps.get(key) );
		}
	}
	
}
