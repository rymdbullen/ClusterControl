package se.avegagroup.clustercontrol.util;

import se.avegagroup.clustercontrol.logic.ConfigManager;

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
	 * 
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

	public static String createTargetUrlReadOnly(String host) {
		String parametersReadOnly = "cmd=list&opt=36";
		return createTargetUrl(host, parametersReadOnly);
	}

	public static String createTargetUrl(String host, String parameters) {
		String targetPort = "";
		if ("8888" != null) {
			targetPort = ":8888";
		}
		String targetHost = "http://"+ host + targetPort;
		String targetContext = "/"+ConfigManager.getJkContext() + "?" + parameters;
		return targetHost + targetContext;
	}
}
