package net.thegreshams.openstates4j.bulkdata;

import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import net.thegreshams.openstates4j.model.Legislator;

public final class Legislature {
	
	protected static final Logger LOGGER = Logger.getRootLogger();

	private static TreeMap<String, Legislator> legislators = new TreeMap<>();
	
	public static Legislator get(String id) {
		return legislators.get(id);
	}
	
	public static void put(String id, Legislator legislator ) {
		legislators.put(id, legislator);
	}

	public static Set<String> keySet() {
		return legislators.keySet();
	}
	
	public static Collection<Legislator> legislators() {
		return legislators.values();
	}

}
