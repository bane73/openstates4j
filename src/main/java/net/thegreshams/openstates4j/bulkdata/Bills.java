package net.thegreshams.openstates4j.bulkdata;

import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import net.thegreshams.openstates4j.model.Bill;

public final class Bills {
	protected static final Logger LOGGER = Logger.getRootLogger();

	private static TreeMap<String, Bill> bills = new TreeMap<>();
	
	public static Bill get(String id) {
		return bills.get(id);
	}
	
	public static void put(String id, Bill bill ) {
		bills.put(id,  bill);
	}
	
	public static Set<String> keySet() {
		return bills.keySet();
	}
	
	public static Collection<Bill> bills() {
		return bills.values();
	}

}
