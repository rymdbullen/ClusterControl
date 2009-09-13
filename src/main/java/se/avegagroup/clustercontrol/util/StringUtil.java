package se.avegagroup.clustercontrol.util;

public class StringUtil extends net.sourceforge.stripes.util.StringUtil {

	private static final String WORKER_STOP = "2";
	private static final String WORKER_DISABLE = "1";
	private static final String WORKER_ACTIVATE = "0";

	public static String getDisableParameters(String loadBalancer, String worker) {
		String parameters = addUpdateParameters(loadBalancer, worker, WORKER_DISABLE);
		return parameters;
	}
	public static String getStopParameters(String loadBalancer, String worker) {
		String parameters = addUpdateParameters(loadBalancer, worker, WORKER_STOP);
		return parameters;
	}
	public static String getActivateParameters(String loadBalancer, String worker) {
		String parameters = addUpdateParameters(loadBalancer, worker, WORKER_ACTIVATE);
		return parameters;
	}
	/**
	 * Adds update parameters to 
	 * @param loadBalancer
	 * @param worker
	 * @param command
	 * @return
	 */
	private static String addUpdateParameters(String loadBalancer, String worker, String command) {
		String parameters = "cmd=update&from=list&w="+ loadBalancer +"&sw="+ worker +"&wa="+ command +"&wf=1&wn="+ worker +"&wr=&wc=&wd=0";
		return (parameters);
	}
	/**
	 * 
	 * @return
	 */
	public static String getMimePropertiesParameters() {
		return "mime=prop&opt=4";
	}
	/**
	 * 
	 * @return
	 */
	public static String getMimeXmlParameters() {
		return "mime=xml&opt=4";
	}
	/**
	 * 
	 * @return
	 */
	public static String getMimeTextParameters() {
		return "mime=txt&opt=4";
	}

	/**
	 * Returns the ipaddress from host including possible port
	 * @param host the complete host including possible port
	 * @return the ipaddress
	 */
	public static String getAddress(String host) {
		String strippedHost = host.replaceAll("(:\\d{1,6})", "");
System.out.println(strippedHost);
		return strippedHost;
	}
	/**
	 * Returns the port from host including possible port
	 * @param host the complete host including possible port
	 * @return the port
	 */
	public static int getPort(String host) {
		String strippedPort = host.replaceAll("([\\.\\d]*:)", "");
System.out.println(strippedPort);
		return Integer.parseInt(strippedPort);
	}
	/**
	 * Checks and assures that the context not ends with a slash 
	 * @param path the path to check
	 * @return a correct context
	 */
	public static String checkPath(String path) {
		int lastIndex = path.lastIndexOf("/");
		if(lastIndex==0) {
			return path;
		}
		String context = path.substring(0, lastIndex);
		return context;
	}
}
