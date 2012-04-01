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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.thegreshams.openstates4j.model.Committee;
import net.thegreshams.openstates4j.model.District;
import net.thegreshams.openstates4j.model.District.Boundary;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class OpenStates {

	protected static final Logger LOGGER = Logger.getRootLogger();
	

	private final String baseUrl = "http://openstates.org/api/v1/";
	private final String apiKey;
	

	private final ObjectMapper mapper;
	
	
	
///////////////////////////////////////////////////////////////////////////////
//
// API
//
///////////////////////////////////////////////////////////////////////////////
	
	
	public OpenStates( String apiKey ) {
		
		this.apiKey = apiKey;
		
		this.mapper = new ObjectMapper();
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		this.mapper.setDateFormat( sdf );
	}

	////////////
	//
	// DISTRICTS
	//
	////////////

	public List<District> findDistricts( String stateAbbr ) throws OpenStatesException {
		return this.findDistricts( stateAbbr, null );
	}
	public List<District> findDistricts( String stateAbbr, String chamber ) throws OpenStatesException {
		
		LOGGER.debug( "finding districts for state(" + stateAbbr + ") and chamber(" + chamber + ")" );
		
		StringBuilder sbQueryPath = new StringBuilder( "districts/" + stateAbbr );
		if( chamber != null ) {
			sbQueryPath.append( "/" + chamber );
		}
		String urlQueryString = getUrlQueryString( sbQueryPath.toString() );
		
		String jsonResponse = this.getJsonResponse( urlQueryString );
				
		List<District> result = this.mapObject( jsonResponse, new TypeReference<List<District>>(){} );
		
		return result;
	}
	
	public Boundary getBoundary( District district ) throws OpenStatesException {
		return this.getBoundary( district.boundaryId );
	}
	public Boundary getBoundary( String boundaryId ) throws OpenStatesException {
		
		LOGGER.debug( "getting boundary for boundary-id(" + boundaryId + ")" );
		
		StringBuilder sbQueryPath = new StringBuilder( "districts/boundary/" + boundaryId );
		String urlQueryString = getUrlQueryString( sbQueryPath.toString() );
		
		String jsonResponse = this.getJsonResponse( urlQueryString );
		
		Boundary result = this.mapObject( jsonResponse, Boundary.class );
		
		return result;
	}

	/////////////
	//
	// COMMITTEES
	//
	/////////////
	
	public List<Committee> findCommittees( Map<String, String> queryParameters ) throws OpenStatesException {
		
		LOGGER.debug( "getting committees using query-parameters: " + queryParameters );
		
		
		StringBuilder sbQueryPath = new StringBuilder( "committees" );
		String urlQueryString = getUrlQueryString( sbQueryPath.toString(), queryParameters );
		
		String jsonResponse = this.getJsonResponse( urlQueryString );
		
		List<Committee> result = this.mapObject( jsonResponse, new TypeReference<List<Committee>>(){} );
		
		return result;
	}
	
	public Committee getCommittee( String committeeId ) throws OpenStatesException {
		
		LOGGER.debug( "getting committee for committee-id(" + committeeId + ")" );
		
		StringBuilder sbQueryPath = new StringBuilder( "committees/" + committeeId );
		String urlQueryString = getUrlQueryString( sbQueryPath.toString() );

		String jsonResponse = this.getJsonResponse( urlQueryString );
		
		Committee result = this.mapObject( jsonResponse, Committee.class );

		return result;
	}
	
	
	
///////////////////////////////////////////////////////////////////////////////
//
// PRIVATE HELPERS
//
///////////////////////////////////////////////////////////////////////////////

	
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

		LOGGER.debug( "received json-response: " + jsonResponse );
		
		return jsonResponse;
	}
	
	private String getUrlQueryString( String queryPath ) {
		return this.getUrlQueryString( queryPath, null );
	}
	private String getUrlQueryString( String queryPath, Map<String, String> queryParameters ) {
		
		StringBuilder sb = new StringBuilder( baseUrl );
		
		sb.append( queryPath + "/" );
		sb.append( "?apikey=" + this.apiKey );
		
		if( queryParameters != null ) {
			Iterator<String> it = queryParameters.keySet().iterator();
			while( it.hasNext() ) {
				String key = it.next();
				String value = queryParameters.get(key);
				sb.append( "&" + key + "=" + value );
			}
		}
		
		return sb.toString();
	}
	
	private <T> T mapObject( String json, Class<T> valueType ) throws OpenStatesException {
		
		T result = null;
		try {
			
			result = this.mapper.readValue( json, valueType );
			
		} catch( Throwable t ) {
			
			String msg = "error mapping to object-type(" +
							valueType.getCanonicalName() +
							") from json: " +
							json;
			LOGGER.warn( msg );
			throw new OpenStatesException( msg, t );
			
		}
		
		//log
		StringBuilder sbLogMsg = new StringBuilder( "json mapped to " );
		if( result == null ) {
			sbLogMsg.append( "NULL" );
		}
		else {
			sbLogMsg.append( "type " + valueType.getCanonicalName() );
		}
		LOGGER.debug( sbLogMsg.toString() );
		
		return result;
	}
	
	private <T> T mapObject( String json, TypeReference<?> valueTypeRef ) throws OpenStatesException {

		T result = null;
		try {
			
			result = this.mapper.readValue( json, valueTypeRef );
			
		} catch( Throwable t ) {

			String msg = "error mapping to object-type(" +
							valueTypeRef.getType() +
							") from json: " +
							json;
			LOGGER.warn( msg );
			throw new OpenStatesException( msg, t );
		}
		
		// log
		StringBuilder sbLogMsg = new StringBuilder( "json mapped to " );		
		if( result == null ) {
			sbLogMsg.append( "NULL" );
		}
		else if( result instanceof Collection ) {
			int size = ((Collection<?>) result).size();
			sbLogMsg.append( "type " + valueTypeRef.getType() + " with " + size + " elements" );
		} 
		else {
			sbLogMsg.append( "type " + valueTypeRef.getType() );
		}		
		LOGGER.debug( sbLogMsg.toString() );
		
		return result;
	}

	
	
///////////////////////////////////////////////////////////////////////////////
//
// MAIN
//
///////////////////////////////////////////////////////////////////////////////

	
	public static void main( String[] args ) throws OpenStatesException {

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
			
		System.out.println( "*** DISTRICTS ***\n" );
		List<District> allUtahDistricts = os.findDistricts( "ut" );
		List<District> utahLowerDistricts = os.findDistricts( "ut", "lower" );
		List<District> utahUpperDistricts = os.findDistricts( "ut", "upper" );
		
		
		System.out.println( "Utah's lower house has " + utahLowerDistricts.size() + " districts" );
		System.out.println( "Utah's upper house has " + utahUpperDistricts.size() + " districts" );
		System.out.println( "Utah has " + allUtahDistricts.size() + " total districts" );
		
		Boundary boundary = os.getBoundary( utahUpperDistricts.get(5) );
		System.out.println( "Utah's " + boundary.chamber + " district " + boundary.name + " has " + boundary.numSeats + " seat(s) (" + boundary.boundaryId + ")" );
		
		System.out.println( "\n*** COMMITTEES ***\n" );
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put( "state", "ut" );
		List<Committee> committees = os.findCommittees( queryParams );
		System.out.println( "Utah has " + committees.size() + " committees" );
		System.out.println( "One of Utah's committees is: " + committees.get(0).committee );
		queryParams.put( "chamber", "upper" );
		committees = os.findCommittees( queryParams );
		System.out.println( "Utah's upper house has " + committees.size() + " committees" );
		
		String targetCommitteeId = committees.get(0).id;
		System.out.println( "\nGetting committee: " + targetCommitteeId );
		Committee targetCommittee = os.getCommittee( targetCommitteeId );
		System.out.println( "Found committee... " + targetCommittee.committee );
			
	}
	
}
