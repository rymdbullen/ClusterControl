package se.avegagroup.clustercontrol.logic;

import se.avegagroup.clustercontrol.data.HostType;
import se.avegagroup.clustercontrol.data.Hosts;

public class ConfigManager {

	private static Hosts _hosts = null;
	private static String _context = "jkmanager";

	/**
	 * initializes the 
	 */
	public static void indit(String hostname) {
		Hosts hosts = new Hosts();
		HostType host = new HostType();
		//host.setHostname("name");
		host.setIpAddress(hostname);
		host.setPort(8888);
		
		_hosts = hosts;  
	}
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
