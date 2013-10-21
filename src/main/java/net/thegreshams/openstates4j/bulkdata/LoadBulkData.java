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
	
	private static void PostProcess() {
		for ( Committee committee: Committees.committees() ) {
			Collections.sort(committee.members);
			for ( Committee.Member member: committee.members ) {
				if ( member.legislator != null && member.legislator.id != null ) {
					member.legislator = Legislators.get(member.legislator.id);
				}
			}
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
