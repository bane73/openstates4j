package net.thegreshams.openstates4j.model;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.thegreshams.openstates4j.service.OpenStates;
import net.thegreshams.openstates4j.service.OpenStatesException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;

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
	

	public Committee(String committeeName) {
		this.committee = committeeName;
	}
	
	public Committee(
		@JsonProperty( "id" )							String			id, 
		@JsonProperty( "chamber" )						String			chamber, 
		@JsonProperty( "state" )						String			state, 
		@JsonProperty( "committee" )					String			committee, 
		@JsonProperty( "subcommittee" )					String			subCommittee, 
		@JsonProperty( "parentId" )						String			parentId, 
		@JsonProperty( "members" )						List<Member>	members, 
		@JsonProperty( "sources" )						List<Source>	sources 
	) {
		this.id = id;
		this.chamber = chamber;
		this.state = state;
		this.committee = committee;
		this.subCommittee = subCommittee;
		this.parentId = parentId;
/*		
		this.members = new ArrayList<Member>(members.size());
		Collections.copy(this.members, members);
		this.sources = new ArrayList<Source>(sources.size());
		Collections.copy(this.sources, sources);
*/
		if ( members != null ) {
			this.members = new ArrayList<Member>();
			this.members.addAll(members);
		}
		if ( sources != null ) {
			this.sources = new ArrayList<Source>();
			this.sources.addAll(sources);
		}
	}
	
	/**
	 * Member
	 * 
	 * @author Brandon Gresham <brandon@thegreshams.net>
	 */
	public static class Member extends Base implements Comparable<Member> {
		
		private static final long serialVersionUID = 1L;
/*		
		public		@JsonProperty( "legislator" )					String			legislator;
		public		@JsonProperty( "role" )							String			role;
		public		@JsonProperty( "leg_id" )						String			legislatorId;
*/
		public Legislator legislator;
		public String role;

//		public Member() {}
		public Member(
			@JsonProperty( "name" ) String name, 
			@JsonProperty( "role" ) String role, 
			@JsonProperty( "leg_id" ) String leg_id
		) {
			this.legislator = new Legislator();
			this.legislator.fullName = name;
			this.role = role;
			this.legislator.id = leg_id;
		}
		/**
		 * For temporary construction only. After constructing Committee.Member you can 
		 * then pass it to Collections.binarySearch assuming that you have 
		 * used BulkData.LoadState.
		 * 
		 * @param legislatorId
		 */
		public Member(String legislatorId) {
			this.legislator = new Legislator();
			this.legislator.id = legislatorId;
		}
		
		@Override
		public String toString() {
			
			StringBuilder sb = new StringBuilder();
			
			sb.append( Member.class.getSimpleName() )
				.append( " [" )
				.append( "(legislator:" ).append( this.legislator ).append( ") " )
				.append( "(role:" ).append( this.role ).append( ") " )
				.append( "(leg_id:" ).append( this.legislator.id ).append( ") " )
				.append( "]" );
			
			return sb.toString();
		}

		@Override
		public int compareTo(Member o) {
			if ( legislator.id == null && o.legislator.id != null ) return -1;
			else if ( legislator.id == null && o.legislator.id == null ) return 0;
			else if ( legislator.id != null && o.legislator.id == null ) return 1;
			else return legislator.id.compareTo(o.legislator.id);
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
	
	public static List<Committee> find( Map<String, String> queryParameters ) throws OpenStatesException, UnsupportedEncodingException, URISyntaxException {
		
		LOGGER.debug( "getting committees using query-parameters: " + queryParameters );
		
		StringBuilder sbQueryPath = new StringBuilder( "committees" );
		
		return OpenStates.queryForJsonAndBuildObject( sbQueryPath.toString(), queryParameters, new TypeReference<List<Committee>>(){} );
	}
	
	public static Committee get( String committeeId ) throws OpenStatesException, UnsupportedEncodingException, URISyntaxException {
		
		LOGGER.debug( "getting committee for committee-id(" + committeeId + ")" );
		
		StringBuilder sbQueryPath = new StringBuilder( "committees/" + committeeId );
		
		return OpenStates.queryForJsonAndBuildObject( sbQueryPath.toString(), Committee.class );
	}
	


////////////////////////////////////////////////////////////////////////////////

	
	public static void main( String[] args) throws OpenStatesException, UnsupportedEncodingException, URISyntaxException {

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
		

		System.out.println( "\n*** COMMITTEES ***\n" );
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put( "state", "ut" );
		List<Committee> committees = Committee.find( queryParams );
		System.out.println( "Utah has " + committees.size() + " committees" );
		System.out.println( "One of Utah's committees is: " + committees.get(0).committee );
		queryParams.put( "chamber", "upper" );
		committees = Committee.find( queryParams );
		System.out.println( "Utah's upper house has " + committees.size() + " committees" );
		
		String targetCommitteeId = committees.get(0).id;
		System.out.println( "\nGetting committee: " + targetCommitteeId );
		Committee targetCommittee = Committee.get( targetCommitteeId );
		System.out.println( "Found committee... " + targetCommittee.committee );
			
	}
	
}
