package net.thegreshams.openstates4j.staticRunner;

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
import java.util.Map;

import net.thegreshams.openstates4j.model.Bill;

import org.codehaus.jackson.map.ObjectMapper;


public class Runner {


	public static void main( String[] args ) throws IOException {

		System.out.println( "Runner#main()...\n" );
		 
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
		System.out.println( "ACTIONS --> " + bill.actions );
		Map<String, Object> map = bill.actions.get(0).optionalProperties;
		Iterator<String> it2 = map.keySet().iterator();
		while( it2.hasNext() ) {
			String key = it2.next();
			System.out.println( "... " + key + " -> " + map.get(key));
		}
		System.out.println("\n\n\nTYPES:\n" + bill.types );
		
	}

}
