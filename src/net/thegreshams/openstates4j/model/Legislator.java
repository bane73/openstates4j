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
import org.codehaus.jackson.type.TypeReference;

public class Legislator extends Base {

	private static final long serialVersionUID = 1L;
	
	
	public 		@JsonProperty( "leg_id" )					String				id;
	public		@JsonProperty( "full_name" )				String				fullName;
	

	
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
	

	public static void main( String[] args ) throws IOException {

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
		URL url = new URL( "http://openstates.org/api/v1/legislators/?state=ca&party=democratic&first_name=Bob&active=true&apikey=" + openStates_apiKey );
		InputStream is = url.openStream();
		Writer writer = new StringWriter();
		char[] buffer = new char[1024];
		Reader reader = new BufferedReader( new InputStreamReader( is, "UTF-8" ) );
		int n;
		while( (n=reader.read(buffer)) != -1 ) {
			writer.write(buffer, 0, n);
		}
		is.close();
		String jsonEvent = writer.toString();
		System.out.println( "JSON:\n" + jsonEvent + "\n\n" );
		
		
		// perform the mapping, spit-out the object
		ObjectMapper mapper = new ObjectMapper();	
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		mapper.setDateFormat( sdf );
		List<Legislator> legislators = mapper.readValue( jsonEvent, new TypeReference<List<Legislator>>(){} );
		Legislator legislator1 = legislators.get(0);
		System.out.println( "\n\n" + legislator1 );
		Map<String, Object> optionalProps = legislator1.optionalProperties;
		Iterator<String> it = optionalProps.keySet().iterator();
		System.out.println( "Optional Properties..." );
		while( it.hasNext() ) {
			String key = it.next();
			System.out.println( "   --> " + key + ":" + optionalProps.get(key) );
		}
	}
	
}
