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
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;


/**
 * District
 * 
 * @author Brandon Gresham <brandon@thegreshams.net>
 */
public class District extends Base {
	
	private static final long serialVersionUID = 1L;
	
	
	public		@JsonProperty( "name" )						String				name;
	public		@JsonProperty( "chamber" )					String				chamber;
	public		@JsonProperty( "abbr" )						String				abbr;
	public		@JsonProperty( "num_seats" )				int					numSeats;
	public		@JsonProperty( "boundary_id" )				String				boundaryId;
	public		@JsonProperty( "legislators" )				List<Legislator>	legislators;
	
	
	/**
	 * Legislator
	 * 
	 * @author Brandon Gresham <brandon@thegreshams.net>
	 */
	public static class Legislator extends Base {
		
		private static final long serialVersionUID = 1L;
		
		public		@JsonProperty( "leg_id" )					String				legislatorId;
		public		@JsonProperty( "full_name" )				String				fullName;
		
		@Override
		public String toString() {
			
			StringBuilder sb = new StringBuilder();
			
			sb.append( Legislator.class.getSimpleName() )
				.append( " [" )
				.append( "(id:" ).append( this.legislatorId ).append( ") " )
				.append( "(fullName:" ).append( this.fullName ).append( ") " )
				.append( "]" );
			
			return sb.toString();
		}
	}
	
	
	/**
	 * Boundary
	 * 
	 * @author Brandon Gresham <brandon@thegreshams.net>	
	 */
	public static class Boundary extends Base {
		
		private static final long serialVersionUID = 1L;
		
		public 		@JsonProperty( "name" )						String				name;
		public		@JsonProperty( "chamber" )					String				chamber;
		public		@JsonProperty( "abbr" ) 					String				abbr;
		public		@JsonProperty( "boundary_id" )				String				boundaryId;
		public		@JsonProperty( "num_seats" )				int					numSeats;
		public		@JsonProperty( "region" )					Region				region;
		public		@JsonProperty( "shape" )					String[][][][]		shape;
		
		
		/**
		 * Region
		 * 
		 * @author Brandon Gresham <brandon@thegreshams.net>
		 */
		public static class Region extends Base {
			
			private static final long serialVersionUID = 1L;
			
			public		@JsonProperty( "lon_delta" )				String				lonDelta;
			public		@JsonProperty( "center_lon" )				String				lonCenter;
			public		@JsonProperty( "lat_delta" )				String				latDelta;
			public		@JsonProperty( "center_lat" )				String				latCenter;
		}
		
		
		/**
		 * Shape
		 * 
		 * @author Brandon Gresham <brandon@thegreshams.net>
		 */
		public static class Shape extends Base {
			
			private static final long serialVersionUID = 1L;
			
			
		}
	}
	
	
	/**
	 * toString()
	 */
	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		
		sb.append( District.class.getSimpleName() )
			.append( " [ " )
			.append( "(name:" ).append( this.name ).append( ") " )
			.append( "(chamber:" ).append( this.chamber ).append( ") " )
			.append( "(abbr:" ).append( this.abbr ).append( ") " )
			.append( "]" );
		
		return sb.toString();
	}
	
	public static List<District> find( String stateAbbr, String chamber, String apikey ) throws IOException {
		
		// build the URL
		StringBuilder query = new StringBuilder( "http://openstates.org/api/v1/districts/" + stateAbbr + "/" );
		if( chamber != null ) {
			query.append( chamber + "/" );
		}
		query.append( "?apikey=" + apikey );
		URL url = new URL( query.toString() );
		
		// fetch the data
		InputStream is = url.openStream();
		Writer writer = new StringWriter();
		char[] buffer = new char[1024];
		Reader reader = new BufferedReader( new InputStreamReader( is, "UTF-8" ) );
		int n;
		while( (n=reader.read(buffer)) != -1 ) {
			writer.write(buffer, 0, n);
		}
		is.close();
		String jsonResponse = writer.toString();
		
		// map to result
		ObjectMapper mapper = new ObjectMapper();
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		mapper.setDateFormat( sdf );
		List<District> result = mapper.readValue( jsonResponse, new TypeReference<List<District>>(){} );
		
		return result;
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
		
		// search by just state
		List<District> districts = District.find( "ut", null, openStates_apiKey );
		System.out.println( "Found " + districts.size() + " districts in Utah" );
		
		// search by both state and lower-house and then by upper-house
		districts = District.find( "ut", "lower", openStates_apiKey );
		System.out.println( "Found " + districts.size() + " districts in Utah's lower-house" );
		districts = District.find( "ut", "upper", openStates_apiKey );
		System.out.println( "Found " + districts.size() + " districts in Utah's upper-house" );
		
		// get one of the boundaries
		District targetDistrict = districts.get(0);
		String boundaryId = targetDistrict.boundaryId;

		// build the URL
		StringBuilder query = new StringBuilder( "http://openstates.org/api/v1/districts/boundary/" + boundaryId + "/" );
		query.append( "?apikey=" + openStates_apiKey );
		URL url = new URL( query.toString() );
		
		// fetch the data
		InputStream is = url.openStream();
		Writer writer = new StringWriter();
		char[] buffer = new char[1024];
		Reader reader = new BufferedReader( new InputStreamReader( is, "UTF-8" ) );
		int n;
		while( (n=reader.read(buffer)) != -1 ) {
			writer.write(buffer, 0, n);
		}
		is.close();
		String jsonResponse = writer.toString();
		
		// map to result
		ObjectMapper mapper = new ObjectMapper();
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		mapper.setDateFormat( sdf );
		Boundary result = mapper.readValue( jsonResponse, Boundary.class );
		System.out.println( "Boundary has " + result.numSeats + " seats" );
		
	}

}




