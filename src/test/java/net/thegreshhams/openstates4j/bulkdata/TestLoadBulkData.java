package net.thegreshhams.openstates4j.bulkdata;


import static org.junit.Assert.assertTrue;

import java.util.TimeZone;
import java.util.TreeMap;

import net.thegreshams.openstates4j.bulkdata.Bills;
import net.thegreshams.openstates4j.bulkdata.Committees;
import net.thegreshams.openstates4j.bulkdata.Legislators;
import net.thegreshams.openstates4j.bulkdata.LoadBulkData;
import net.thegreshams.openstates4j.model.Bill;
import net.thegreshams.openstates4j.model.Committee;
import net.thegreshams.openstates4j.model.Legislator;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestLoadBulkData {
	protected static final Logger LOGGER = Logger.getRootLogger();

	@BeforeClass
	public static void setup() {
		try {
			LoadBulkData.LoadCurrentTerm(TestLoadBulkData.class.getResource("/2013-10-07-ca-json.zip").getFile(), "20132014", TimeZone.getTimeZone("GMT-08:00") );
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testCommitteeConsistencyCheck() {
		// check the Committee.members lists against member lists as defined by the Legislator.roles
		// this will have been woven together in LoadBulkData 
		for( Committee committee: Committees.committees() ) {
			for ( Committee.Member member: committee.members ) {
				if ( member.legislator != null && member.legislator.id != null ) {
					if ( Legislators.get(member.legislator.id) == null ) {
						LOGGER.info("***: Committee.members.legislator.id does not reference a valid legislator:" + member.legislator.id );
					}
				} else {
					LOGGER.info("***: Committee.members.legislator or Committee.members.legislator.id is null:" + committee.id + ":" + member.legislator );
				}
			}
		}
	}

	class PartyStat {
		int memberCount = 0;
		int billsPassed = 0;
	}

	@Test
	public void testPartyStats() {
		TreeMap<String, PartyStat> partyStats = new TreeMap<>();
		// setup parties
		determineParties(partyStats);
		// loop on all bills, including resolutions
		for ( String id: Bills.keySet() ) {
			Bill bill = Bills.get(id);
			if ( determinePassed(bill) ) {
				String legislatorId = determinePrincipalAuthor(bill);
				// a committee may be an author
				if ( legislatorId != null ) {
					Legislator legislator = Legislators.get(legislatorId);
					if ( legislator == null ) LOGGER.info("***: Bill legislatorId " + legislatorId + " references non-existant or inactive legislator for bill:" + bill);
					else if ( legislator.party == null ) LOGGER.info("***: Legislator Party is null:" + legislator);
					else partyStats.get(legislator.party).billsPassed++; 
				}
			}
		}
		// determine the majority party
		PartyStat majorityParty = determineMajorityParty(partyStats);
		boolean majorityPartyPassedMoreBills = true;
		// show all bills passed
		for ( String party: partyStats.keySet() ) {
			PartyStat partyStat = partyStats.get(party);
			LOGGER.info("The " + party + " party has " + partyStat.memberCount + " members and passed " + partyStat.billsPassed + " bills.");
			if ( partyStat.billsPassed > majorityParty.billsPassed ) majorityPartyPassedMoreBills = false;
		}
		assertTrue( majorityPartyPassedMoreBills );
		LOGGER.info("The majority party DID pass the most bills.");
	}
	
	private void determineParties( TreeMap<String, PartyStat> partyStats ) {
		for ( Legislator legislator: Legislators.legislators() ) {
			if ( !legislator.isActive ) continue;
			PartyStat partyStat = partyStats.get(legislator.party); 
			if ( partyStat == null ) {
				partyStat = new PartyStat();
				partyStats.put(legislator.party, partyStat);
			}
			partyStat.memberCount++;
		}
	}
	
	private PartyStat determineMajorityParty(TreeMap<String, PartyStat> partyStats) {
		PartyStat majorityParty = null;
		int max = 0;
		for (String party: partyStats.keySet() ) {
			PartyStat partyStat = partyStats.get(party);
			if ( partyStat.billsPassed > max ) {
				majorityParty = partyStat;
				max = partyStat.billsPassed;
			}
		}
		return majorityParty;
	}

	private String determinePrincipalAuthor(Bill bill) {
		for ( Bill.Sponsor sponsor: bill.sponsors ) {
			if ( sponsor.type.equals("primary") ) {
				return sponsor.legislatorId;
			}
		}
		return null;
	}

	private boolean determinePassed(Bill bill) {
		boolean passed = false;
		for ( Bill.Action action: bill.actions ) {
			if ( action.action.contains("Chaptered") ) return true;
		}
		return passed;
	}
	
}
