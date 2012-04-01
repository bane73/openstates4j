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

import net.thegreshams.openstates4j.model.Bill;
import net.thegreshams.openstates4j.model.Committee;
import net.thegreshams.openstates4j.model.District;
import net.thegreshams.openstates4j.model.District.Boundary;
import net.thegreshams.openstates4j.model.District.Legislator;
import net.thegreshams.openstates4j.model.Event;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class OpenStates {

	protected static final Logger LOGGER = Logger.getRootLogger();
	

	private static final String BASE_URL = "http://openstates.org/api/v1/";
	private static final ObjectMapper MAPPER;
	static {
		MAPPER = new ObjectMapper();
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		MAPPER.setDateFormat( sdf );
	}
	
	private OpenStates() { /* DO NOTHING */ }
	
	
	
///////////////////////////////////////////////////////////////////////////////
//
// PUBLIC API
//
///////////////////////////////////////////////////////////////////////////////

	
	private static String apiKey;
	public static void setApiKey( String apiKey ) throws OpenStatesException {
		
		if( apiKey == null || apiKey.trim().length() != 32 ) {
			String msg = "apiKey cannot be null & must be 32-characters long; was: '" + apiKey + "'";
			LOGGER.error( msg );
			throw new OpenStatesException( msg );
		}
		OpenStates.apiKey = apiKey.trim();		
	}

	public static <T> T queryForJsonAndBuildObject( String queryPath, Class<T> valueType ) throws OpenStatesException {
		return OpenStates.queryForJsonAndBuildObject( queryPath, null, valueType );
	}
	
	public static <T> T queryForJsonAndBuildObject( String queryPath, TypeReference<?> valueTypeRef ) throws OpenStatesException {
		return OpenStates.queryForJsonAndBuildObject( queryPath, null, valueTypeRef );
	}
	
	public static <T> T queryForJsonAndBuildObject( String queryPath, Map<String, String> queryParams, TypeReference<?> valueTypeRef ) throws OpenStatesException {

		String jsonResponse = OpenStates.buildUrlQueryStringAndGetJsonResponse( queryPath, queryParams );
		
		return OpenStates.mapObject( jsonResponse, valueTypeRef );
	}


	
///////////////////////////////////////////////////////////////////////////////
//
// PRIVATE API
//
///////////////////////////////////////////////////////////////////////////////
	

	private static <T> T queryForJsonAndBuildObject( String queryPath, Map<String, String> queryParams, Class<T> valueType ) throws OpenStatesException {

		String jsonResponse = OpenStates.buildUrlQueryStringAndGetJsonResponse( queryPath, queryParams );
		
		return OpenStates.mapObject( jsonResponse, valueType );
	}
	
	private static String buildUrlQueryStringAndGetJsonResponse( String queryPath, Map<String, String> queryParameters ) throws OpenStatesException {

		String urlQueryString = OpenStates.buildUrlQueryString( queryPath, queryParameters );
		String jsonResponse = OpenStates.getJsonResponse( urlQueryString );
		
		return jsonResponse;
	}

	private static <T> T mapObject( String json, Class<T> valueType ) throws OpenStatesException {
		
		T result = null;
		try {
			
			result = OpenStates.MAPPER.readValue( json, valueType );
			
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
	
	private static <T> T mapObject( String json, TypeReference<?> valueTypeRef ) throws OpenStatesException {

		T result = null;
		try {
			
			result = OpenStates.MAPPER.readValue( json, valueTypeRef );
			
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
	
	private static String getJsonResponse( String urlQuery ) throws OpenStatesException {
		
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
	
	private static String buildUrlQueryString( String queryPath, Map<String, String> queryParameters ) throws OpenStatesException {
		
		if( OpenStates.apiKey == null ) {
			String msg = "you must first set your api-key like this: OpenStates.setApiKey( {YOUR_API_KEY} );" +
							" you can obtain a key by visiting: http://services.sunlightlabs.com/";
			LOGGER.fatal( msg );
			throw new OpenStatesException( msg );
		}
		
		StringBuilder sb = new StringBuilder( OpenStates.BASE_URL );
		
		sb.append( queryPath + "/" );
		sb.append( "?apikey=" + OpenStates.apiKey );
		
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
	
}
