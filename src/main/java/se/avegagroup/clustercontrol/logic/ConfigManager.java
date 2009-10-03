package se.avegagroup.clustercontrol.logic;

import se.avegagroup.clustercontrol.domain.Hosts;

public class ConfigManager {

	private static Hosts _hosts = null;
	private static String _context = "jkmanager";

	public static Hosts getdHosts() {
		return _hosts;
	}
	/**
	 * 
	 * @return
	 */
	public static String getJdkContext() {
		return _context;
	}

}
