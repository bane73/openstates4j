package net.thegreshams.openstates4j.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;

import net.thegreshams.openstates4j.model.District;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class OpenStates {

	protected static final Logger LOGGER = Logger.getRootLogger();
	

	private final String baseUrl = "http://openstates.org/api/v1/";
	private final String apiKey;
	
	public OpenStates( String apiKey ) {
		
		this.apiKey = apiKey;
	}
	
	private String getJsonResponse( String urlQuery ) throws OpenStatesException {
		
		LOGGER.debug( "getting json-response from url-query: " + urlQuery );
		
		String jsonResponse = null;
		InputStream is = null;
		try {
			
			URL url = new URL( urlQuery );
			is = url.openStream();
			Writer writer = new StringWriter();
			char[] buffer = new char[1024];
			Reader reader = new BufferedReader( new InputStreamReader( is, "UTF-8" ) );
			int n;
			while( (n=reader.read(buffer)) != -1 ) {
				writer.write( buffer, 0, n );
			}
			jsonResponse = writer.toString();
			
		} catch( Throwable t ) {
			
			throw new OpenStatesException( "unable to read response from request (" + urlQuery + ")", t );
			
		} finally {
			
			if( is != null ) {
				try {
					is.close();
				} catch( IOException ioe ) {
					LOGGER.warn( "unable to close input-stream", ioe );
				}
			}
			
		}
		
		return jsonResponse;
	}
	
	public List<District> findDistricts( String stateAbbr, String chamber ) throws OpenStatesException {
		
		LOGGER.debug( "finding districts for state(" + stateAbbr + ") and chamber(" + chamber + ")" );
		
		StringBuilder urlQuerySb = new StringBuilder( baseUrl + "districts/" + stateAbbr + "/" );
		if( chamber != null ) {
			urlQuerySb.append( chamber + "/" );
		}
		urlQuerySb.append( "?apikey=" + apiKey );
		
		String jsonResponse = this.getJsonResponse( urlQuerySb.toString() );
		LOGGER.debug( "received json-response: " + jsonResponse );
		
		ObjectMapper mapper = new ObjectMapper();
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		mapper.setDateFormat( sdf );
		
		List<District> result = null;
		try {
			
			result = mapper.readValue( jsonResponse, new TypeReference<List<District>>(){} );
			
		} catch( Throwable t ) {
			
			throw new OpenStatesException( "unable to map json to " + District.class.getCanonicalName(), t );
			
		}
		
		LOGGER.debug( (result == null ?
										"json-response mapped to a NULL List<District>"
										:
										"json-response mapped to a List<District> with " + result.size() + " elements" ) );
		
		return result;
	}
	
	public static void main( String[] args ) {

		// get the API key
		String apiKey = null;
		for( String s : args ) {

			if( s == null || s.trim().isEmpty() ) continue;
			if( s.trim().split( "=" )[0].equals( "apiKey" ) ) {
				apiKey = s.trim().split( "=" )[1];
			}
		}
		if( apiKey == null || apiKey.trim().isEmpty() ) {
			throw new IllegalArgumentException( "Program-argument 'apiKey' not found but required" );
		}
		
		OpenStates os = new OpenStates( apiKey );
		try {
			List<District> allUtahDistricts = os.findDistricts( "ut", null );
			List<District> utahLowerDistricts = os.findDistricts( "ut", "lower" );
			List<District> utahUpperDistricts = os.findDistricts( "ut", "upper" );
			
			
			System.out.println( "Utah's lower house has " + utahLowerDistricts.size() + " districts" );
			System.out.println( "Utah's upper house has " + utahUpperDistricts.size() + " districts" );
			System.out.println( "Utah's has " + allUtahDistricts.size() + " total districts" );
			
		} catch( OpenStatesException e ) {
			e.printStackTrace();
		}
	}
	
}
