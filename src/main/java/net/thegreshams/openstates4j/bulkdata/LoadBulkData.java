package net.thegreshams.openstates4j.bulkdata;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Enumeration;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;

import net.thegreshams.openstates4j.model.Bill;
import net.thegreshams.openstates4j.model.Committee;
import net.thegreshams.openstates4j.model.Legislator;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Static methods to load the bulk data files from the openstates.org site. 
 * This is in addition to using the API's. These calls and the API calls are 
 * not integrated. In other words, API calls do not fill out these
 * classes. There is no 'caching' done. It may be a good idea to 
 * add this feature, but these classes strictly load up
 * a single bulkdata file. Also, currently there is a method
 * for 'LoadCurrentTerm' For the legislator's, the isActive 
 * flag is checked. For committees, nothing is checked. For
 * bills, a string for the year is passed in and compared to 
 * the directory name within the .zip bulkfile. That's bad form.
 * It should be looking at the session field in the bill.  
 * 
 * @author Karl Nicholas (karlnicholas on github.com)
 *
 */
public final class LoadBulkData {
	protected static final Logger LOGGER = Logger.getRootLogger();
	
	/**
	 * Testing 
	 */
	public static void main(String... args) throws Exception {
		LoadBulkData.LoadCurrentTerm(LoadBulkData.class.getResource("/2013-10-07-ca-json.zip").getFile(), "20132014", TimeZone.getTimeZone("GMT-08:00") );
//		LOGGER.info(Legislators.get("CAL000112"));
//		LOGGER.info(Committees.get("CAC000270").members.get(0));
		LOGGER.info(Legislators.get("CAL000112"));
		LOGGER.info( Committees.get("CAC000270").committee );
		LOGGER.info( Committees.get("CAC000270").members.get(0) );
	}
	
	/**
	 * Note that the currentTerm must be defined as the currentTerm at the moment
	 * because only "isActive" legislators are loaded. Thus, the junit
	 * test will probably fail every year when the currentTerm changes
	 *  
	 * @param stateBulkData
	 * @param currentTerm
	 * @param stateTimeZone
	 * @throws ZipException
	 * @throws IOException
	 */
	public static void LoadCurrentTerm(String stateBulkData, String currentTerm, TimeZone stateTimeZone ) throws ZipException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		sdf.setTimeZone(stateTimeZone);
		mapper.setDateFormat( sdf );

		ZipFile zipFile = new ZipFile( stateBulkData );
		readLegislators(mapper, zipFile);
		readCommittees(mapper, zipFile);
		readBills(mapper, zipFile, currentTerm);
		zipFile.close();
		// map Legislator.Role.Committee to actual Committee objects.
		// sort Committee.members
		PostProcess();
	}

	/**
	 * This will fill out the Legislator references in the committee
	 * class with references to pre-loaded legislators. Without
	 * this call, then only a couple of Legislator objects are filled 
	 * out in the Committee class.
	 */
	private static void PostProcess() {
		// weave Legislator into Committee.Member
		for ( Committee committee: Committees.committees() ) {
			// Sort it, but I'm not sure why
			// Anyway, if you want to get find a committee.member, now
			// you can use Collections.binarySearch(.., Legislator.id)
			Collections.sort(committee.members);
			for ( Committee.Member member: committee.members ) {
				if ( member.legislator != null && member.legislator.id != null ) {
					Legislator tleg = Legislators.get(member.legislator.id);
					if ( tleg != null ) member.legislator = tleg;
				}
			}
		}
		// sort actions ...
		for ( Bill bill: Bills.bills() ) {
			Collections.sort(bill.actions);
		}

	}

	private static void readBills(ObjectMapper mapper, ZipFile zipFile, String currentTerm) throws JsonParseException, JsonMappingException, IOException {
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while ( entries.hasMoreElements() ) {
			ZipEntry entry = entries.nextElement();
			if ( entry.isDirectory() ) continue;
			String eName = entry.getName();
			if ( eName.contains(currentTerm)) {
				Bill bill = mapper.readValue( zipFile.getInputStream(entry), Bill.class );
				Bills.put(bill.id, bill);
			}
		}
	}
	
	private static void readLegislators(ObjectMapper mapper, ZipFile zipFile) throws JsonParseException, JsonMappingException, IOException {
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while ( entries.hasMoreElements() ) {
			ZipEntry entry = entries.nextElement();
			if ( entry.isDirectory() ) continue;
			String eName = entry.getName();
			if ( eName.contains("legislators")) {
				Legislator legislator = 
						mapper.readValue( zipFile.getInputStream(entry), Legislator.class );
				if ( legislator.isActive ) {
					Legislators.put(legislator.id, legislator );
				}
			}
		}
	}
	
	private static void readCommittees(ObjectMapper mapper, ZipFile zipFile) throws JsonParseException, JsonMappingException, IOException {
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while ( entries.hasMoreElements() ) {
			ZipEntry entry = entries.nextElement();
			if ( entry.isDirectory() ) continue;
			String eName = entry.getName();
			if ( eName.contains("committees")) {
				Committee committee = mapper.readValue( zipFile.getInputStream(entry), Committee.class );
				Committees.put(committee.id, committee);
			}
		}
	}

}
