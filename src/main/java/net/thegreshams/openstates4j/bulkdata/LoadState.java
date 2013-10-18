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

public final class LoadState {
	protected static final Logger LOGGER = Logger.getRootLogger();
	
	public static void Load(String stateBulkData, TimeZone stateTimeZone ) throws ZipException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		sdf.setTimeZone(stateTimeZone);
		mapper.setDateFormat( sdf );

		ZipFile zipFile = new ZipFile( stateBulkData );
		readLegislators(mapper, zipFile);
		readCommittees(mapper, zipFile);
		readBills(mapper, zipFile);
		zipFile.close();
		// map Legislator.Role.Committee to actual Committee objects.
		// sort Committee.members
		PostProcess();
		
	}
	
	private static void PostProcess() {
		// put full Committee references into Legislator.Roles
		for ( Legislator legislator: Legislature.legislators() ) {
			for ( Legislator.Role role: legislator.roles ) {
				if ( role.committee != null ) {
					// note that not all roles describe membership in a committee
					if ( role.committee.committee != null ) {
						String key = Committees.findCommitteeKey( role.committee.committee, legislator.chamber);
						Committee committee = Committees.get( key );
						role.committee = committee;
					}
				}
			}
		}
		for ( Committee committee: Committees.committees() ) {
			Collections.sort(committee.members);
		}
	}

	private static void readBills(ObjectMapper mapper, ZipFile zipFile) throws JsonParseException, JsonMappingException, IOException {
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while ( entries.hasMoreElements() ) {
			ZipEntry entry = entries.nextElement();
			if ( entry.isDirectory() ) continue;
			String eName = entry.getName();
			if ( eName.contains("ca/20132014")) {
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
					Legislature.put(legislator.id, legislator );
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
