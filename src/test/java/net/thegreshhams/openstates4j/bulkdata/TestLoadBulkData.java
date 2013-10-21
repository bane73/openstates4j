package net.thegreshhams.openstates4j.bulkdata;


import static org.junit.Assert.assertTrue;

import java.util.TimeZone;
import java.util.TreeMap;

import net.thegreshams.openstates4j.bulkdata.Bills;
import net.thegreshams.openstates4j.bulkdata.Legislators;
import net.thegreshams.openstates4j.bulkdata.LoadBulkData;
import net.thegreshams.openstates4j.model.Bill;
import net.thegreshams.openstates4j.model.Legislator;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestLoadBulkData {
	protected static final Logger LOGGER = Logger.getRootLogger();

	@BeforeClass
	public static void setup() {
		try {
			LoadBulkData.LoadCurrentTerm(LoadBulkData.class.getResource("/2013-10-07-ca-json.zip").getFile(), "20132014", TimeZone.getTimeZone("GMT-08:00") );
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
/*
	@Test
	public void testCommitteeConsistencyCheck() {
		TreeMap<String, ArrayList<String>> committeeMembersByLegislators = new TreeMap<String, ArrayList<String>>();
		// first, create a map of committees and their members list according to the legislator records
		for ( Legislator legislator: Legislators.legislators() ) {
			// only for current session
//			if ( !legislator.isActive ) continue;
			for (Legislator.Role role: legislator.roles ) {
				// not all roles are committee roles.
				if ( role.committee != null ) {
					ArrayList<String> committeeMembers = committeeMembersByLegislators.get(role.committee.id);
					if ( committeeMembers == null ) {
						committeeMembers = new ArrayList<String>();
						committeeMembersByLegislators.put(role.committee.id, committeeMembers);
					}
					committeeMembers.add(legislator.id);
				}
			}
		}
		// sort the member lists to save code and time
		for ( ArrayList<String> committeeMembers: committeeMembersByLegislators.values() ) {
			Collections.sort( committeeMembers );
		}
		// second, check the map and member list we made against the committee member list.
		for ( String key: committeeMembersByLegislators.keySet() ) {
			ArrayList<String> committeeMembers = committeeMembersByLegislators.get(key);
			Committee committee = Committees.get(key);
			for ( Committee.Member member: committee.members ) {
				if ( member.legislator.id != null ) {
					if ( Collections.binarySearch(committeeMembers, member.legislator.id) == -1 ) {
						LOGGER.info("Discripency between committee membership as defined in Legislator.roles and Committee.members:\n" 
							+ "***: Committee says that Legislator " + member.legislator.id + " is a member, but the Legislator does not have this role"
						);
					}
				} else {
					LOGGER.info("***: Committee.members has a null Committee.members.legislator.id:\n" 
						+ "     Committee is " + committee.id + ":" + committee.committee + "\n"
						+ "     Committee.Member is " + member
					);					
				}
			}
		}
		// third, check the Committee.members lists against member lists as defined by the Legislator.roles
		for ( Committee committee: Committees.committees() ) {
			ArrayList<String> committeeMembers = committeeMembersByLegislators.get(committee.id);
			if ( committeeMembers == null ) {
				LOGGER.info("***: No (active) legislator has a role defined for comittee.id:" + committee.id );
				continue;
			}
			for ( String legislatorId: committeeMembers ) {
				if ( Collections.binarySearch( committee.members, new Committee.Member(legislatorId) ) == -1) {
					LOGGER.info("***: Discripency between Committee.members and committee membership as defined in Legislator.roles:\n" 
							+ "     Legislator.roles says that " + legislatorId + " is a member but that member.legislator.id is not found in committee " + committee.id
						);
				}
			}
		}
	}
*/

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
