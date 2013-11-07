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

	public static TreeMap<String, Committee> findAllCommittees(String legislatorId) {
		TreeMap<String, Committee> byLegislator = new TreeMap<>();
		for ( Committee committee: committees.values() ) {
			for ( Committee.Member member: committee.members ) {
				if ( member.legislator.id != null && member.legislator.id.equals(legislatorId) ) {
					byLegislator.put(committee.id, committee);
				}
			}
		}
		return byLegislator;
	}
	
}
