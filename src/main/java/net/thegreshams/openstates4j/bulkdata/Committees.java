package net.thegreshams.openstates4j.bulkdata;

import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import net.thegreshams.openstates4j.model.Committee;

public final class Committees {

	protected static final Logger LOGGER = Logger.getRootLogger();

	private static TreeMap<String, Committee> committees = new TreeMap<>();
	
	public static Committee get(String key) {
		return committees.get(key);
	}
	
	public static void put(String id, Committee committee ) {
		committees.put(id, committee);
	}
	
	public static Set<String> keySet() {
		return committees.keySet();
	}
	
	public static Collection<Committee> committees() {
		return committees.values();
	}
	
	public static String findCommitteeKey(String committeeName, String chamber) {
		for ( Committee committee: committees.values() ) {
			if ( committeeName.contains(committee.committee) && !committeeName.equals(committee.committee) ) LOGGER.info("Legislator.Role.Committee.committee name issue: " + committeeName );
			if ( (committee.committee.contains(committeeName) || committeeName.contains(committee.committee)) && (committee.chamber.equals(chamber) || committee.chamber.equals("joint")) ) return committee.id;
		}
		LOGGER.info("Committe Name Match Not Found:" + committeeName);
		return null;
	}
}
