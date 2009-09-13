package se.avegagroup.clustercontrol.logic;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.avegagroup.clustercontrol.action.ControllerActionBean;
import se.avegagroup.clustercontrol.data.Hosts;
import se.avegagroup.clustercontrol.data.HostType;
import se.avegagroup.clustercontrol.data.JkBalancerType;
import se.avegagroup.clustercontrol.data.JkMemberType;
import se.avegagroup.clustercontrol.data.JkResultType;
import se.avegagroup.clustercontrol.data.JkStatusType;
import se.avegagroup.clustercontrol.util.StringUtil;
import se.avegagroup.clustercontrol.util.WorkerStatus;

public class ControllerClient {

	private static final Logger logger = LoggerFactory.getLogger(ControllerActionBean.class);

	private static final String RESPONSE_FORMAT_XML = "xml";
	private static final String RESPONSE_FORMAT_PROPERTIES = "prop";
	private static final String RESPONSE_FORMAT_TEXT = "txt";

	private static Hosts _hosts = new Hosts();

	/**
	 * Initializes the ControllerClient with a hosts container. 
	 * @param hosts the hosts container
	 */
	public static void init(Hosts hosts) {
		_hosts = hosts;
	}
	/**
	 * Initializes the ControllerClient from supplied url. Sets up a hosts container and returns a list of balancers.
	 * @param url the initializing url
	 * @return list of balancers
	 */
	public static ArrayList<JkBalancerType> init(URL url) {
		if(_hosts!=null || _hosts.getHost()!=null) {
			// previous hosts found, check if already setup
			int hostsCount = _hosts.getHost().size();
			for (int i = 0; i < hostsCount; i++) {
				HostType host = _hosts.getHost().get(i);
				if(host.getIpAddress().equals(url.getHost())) {
					if(host.getPort() == url.getPort()) {
						
					}
				}
			}
		}
		HostType host = new HostType();
		host.setIpAddress(url.getHost());
		host.setPort(url.getPort());
		String jkContext = StringUtil.checkPath(url.getPath());
		host.setContext(""+jkContext);
		_hosts.getHost().add(host);

		return statusComplex();
	}
	/**
	 * 
	 * @param loadBalancer
	 * @param hostnames
	 * @return
	 */
	public static String init(String loadBalancer, ArrayList<HostType> hostnames) {
		for (int i = 0; i < hostnames.size(); i++) {
			_hosts.getHost().add(hostnames.get(i));
		}
		_hosts.setLoadBalancer(loadBalancer);
		
		//ArrayList<String[]> statuses = status();
		
		return "OK";
	}

	/**
	 * initializes the Controller Client host config
	 * @param loadBalancer
	 * @param hostnames
	 * @return
	 */
	public static String init(String loadBalancer, String[] hostnames) {
		for (int i = 0; i < hostnames.length; i++) {
			String hostname = hostnames[i];
			String ipAddress = StringUtil.getAddress(hostname);
			int port = StringUtil.getPort(hostname);
			HostType host = new HostType();
//host.setHostname("name");
			host.setIpAddress(ipAddress);
			host.setPort(port);
			_hosts.getHost().add(host);
		}
		_hosts.setLoadBalancer(loadBalancer);
		
		//ArrayList<String[]> statuses = status();
		
		return "OK";
	}
	/**
	 * Executes urls
	 * @param parameters the parameters to execute
	 * @return the workerlists, ie html bodys
	 */
	public static String[] executeUrls(String parameters) {
		Iterator<HostType> hosts = _hosts.getHost().iterator();
		String[] workerLists = new String[_hosts.getHost().size()];
		int index = 0;
		while (hosts.hasNext()) {
			HostType host = (HostType) hosts.next();
			workerLists[index] = executeUrl(host, parameters);
			index++;
		}
		return workerLists;
	}

	/**
	 * Creates the request (GET) and receives a encapsulated response object, ie
	 * the html body;
	 * 
	 * @param parameters
	 *            the parameters to execute
	 * @return the response, ie html body
	 */
	public static String executeUrl(HostType host, String parameters) {
		String targetUrl = createTargetUrl(host, parameters);
		logger.debug("executing request " + targetUrl);

		// creates the response handler
		HttpClient httpclient = new DefaultHttpClient();
		String responseBody = null;
		try {
			HttpGet httpget = new HttpGet(targetUrl);
			org.apache.http.client.ResponseHandler<String> responseHandler = new BasicResponseHandler();
			responseBody = (String) httpclient.execute(httpget, responseHandler);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		httpclient.getConnectionManager().shutdown();
		return responseBody;
	}

	/**
	 * Activates the worker for the supplied loadbalancer and returns the list of balancers
	 * @param loadBalancer the loadbalancer
	 * @param worker the worker to activate
	 * @return list of balancers
	 */
	public static ArrayList<JkBalancerType> activate(String loadBalancer, String worker) {
		//
		// Perform the activate action
		String activateParameters = StringUtil.getActivateParameters(loadBalancer, worker);
		String xmlMimeParameters = StringUtil.getMimeXmlParameters();
		String[] workerLists = executeUrls(activateParameters + "&" + xmlMimeParameters);

		parseStatusAndPause(workerLists, worker);

		return statusComplex();
	}

	/**
	 * Activates the worker for the initialized loadbalancer and returns the list of balancers
	 * @param worker the worker to activate
	 * @return list of balancers
	 */
	public static ArrayList<JkBalancerType> activate(String worker) {
		return activate(_hosts.getLoadBalancer(), worker);
	}

	/**
	 * Disables the worker for the initialized loadbalancer and returns the list of balancers
	 * @param loadBalancer the loadbalancer
	 * @param worker the worker to disable
	 * @return list of balancers
	 */
	public static ArrayList<JkBalancerType> disable(String loadBalancer, String worker) {
		//
		// Perform the disable action
		String disableParameters = StringUtil.getDisableParameters(loadBalancer, worker);
		String xmlMimeParameters = StringUtil.getMimeXmlParameters();
		String[] workerLists = executeUrls(disableParameters + "&" + xmlMimeParameters);

		parseStatusAndPause(workerLists, worker);

		return statusComplex();
	}
	/**
	 * Disables the worker for the initialized loadbalancer and returns the list of balancers
	 * @param worker the worker to disable
	 * @return list of balancers
	 */
	public static ArrayList<JkBalancerType> disable(String worker) {
		return disable(_hosts.getLoadBalancer(), worker);
	}
	/**
	 * Parses the supplied workerLists and 
	 * @param workerLists the workerlist, ie html bodys
	 * @param worker the worker to get status for
	 */
	private static void parseStatusAndPause(String[] workerLists, String worker) {
		//
		// process the disable action responses
		for (int index = 0; index < workerLists.length; index++) {
			String workerList = workerLists[index];
			WorkerStatus workerStatus = new WorkerStatus();
			JAXBElement<JkStatusType> jkStatus = workerStatus.unmarshall(workerList);
			JkResultType result = jkStatus.getValue().getResult();
			if (result.getType().equals("OK")) {
				logger.debug("Worker: '" + worker + "' action OK!");
			} else {
				logger.debug("Worker: '" + worker + "' action NOK!");
			}
		}

		//
		// wait for x seconds
		try {
			logger.info("Sleeping");
			// Sleep for 3 seconds
			// Thread.sleep() must be within a try - catch block
			Thread.sleep(3000);
		} catch (Exception e) {
			logger.debug("Error when suspending thread: "+e.getMessage());
		}
	}
	/**
	 * Stops the worker for the supplied loadbalancer and returns the list of balancers
	 * @param loadBalancer the loadbalancer
	 * @param worker the worker to stop
	 * @return list of balancers
	 */
	public static List<JkBalancerType> stop(String loadBalancer, String worker) {
		String stopParameters = StringUtil.getStopParameters(loadBalancer, worker);
		String xmlMimeParameters = StringUtil.getMimeXmlParameters();
		String[] workerLists = executeUrls(stopParameters + "&" + xmlMimeParameters);

		parseStatusAndPause(workerLists, worker);

		return statusComplex();
	}

	/**
	 * Stops the worker for the initialized loadbalancer and returns the list of balancers
	 * @param worker the worker to stop
	 * @return list of balancers
	 */
	public static List<JkBalancerType> stop(String worker) {
		return stop(_hosts.getLoadBalancer(), worker);
	}
	/**
	 * Returns the balancers
	 * @return the balancers
	 */
	public static ArrayList<JkBalancerType> statusComplex() {
		/** List of statuses like 0,0 - 1,0 */
		ArrayList<JkBalancerType> resultList = new ArrayList<JkBalancerType>();
		
		//
		// retrieve the statuses
		String[] workerLists = executeUrls(StringUtil.getMimeXmlParameters());
		int workerListCount = workerLists.length;
		for (int workerListIdx = 0; workerListIdx < workerListCount; workerListIdx++) {
			String workerList = workerLists[workerListIdx];
			WorkerStatus workerStatus = new WorkerStatus();
			JAXBElement<JkStatusType> jkStatus = workerStatus.unmarshall(workerList);
			JkResultType result = jkStatus.getValue().getResult();
			if(result.getType().equals("NOK")) {
				//throw new WorkerNotFoundException();
				return null;
			}
			resultList.add(jkStatus.getValue().getBalancers().getBalancer());
		}
		return resultList;
	}
	/**
	 * Returns the status as string values.
	 * @return the status as string values
	 */
	public static ArrayList<String[]> status() {
		/** List of statuses like 0,0 - 1,0 */
		ArrayList<String[]> resultList = new ArrayList<String[]>();
		
		//
		// retrieve the statuses
		String[] workerLists = executeUrls(StringUtil.getMimeXmlParameters());
		int workerListCount = workerLists.length;
		for (int workerListIdx = 0; workerListIdx < workerListCount; workerListIdx++) {
			//ArrayList<String> memberList = new ArrayList<String>();
			String workerList = workerLists[workerListIdx];
			WorkerStatus workerStatus = new WorkerStatus();
			JAXBElement<JkStatusType> jkStatus = workerStatus.unmarshall(workerList);
			JkResultType result = jkStatus.getValue().getResult();
			int memberCount = jkStatus.getValue().getBalancers().getBalancer().getMemberCount();
			String[] memberList = new String[memberCount]; 
			for (int memberIdx = 0; memberIdx < memberCount; memberIdx++) {
				JkMemberType member = jkStatus.getValue().getBalancers().getBalancer().getMember().get(memberIdx);
				System.out.println("Worker: '" + member.getName() + "' activation: "+member.getActivation()+" state: "+member.getState()+" busy: "+member.getBusy());
				System.out.println("Result: "+result.getType());
				//memberList.add(result.getType());
				memberList[memberIdx] = result.getType();
			}
			resultList.add(memberList);
		}
		return resultList;
	}

	/**
	 * Returns the status in the supplied format
	 * @param format the format to get status for
	 * @return the status in the supplied format
	 */
	public static String[] status(String format) {
		String targetUrl = "";
		if (format.equals(RESPONSE_FORMAT_PROPERTIES)) {
			targetUrl = StringUtil.getMimePropertiesParameters();
		} else if (format.equals(RESPONSE_FORMAT_XML)) {
			targetUrl = StringUtil.getMimeXmlParameters();
		} else if (format.equals(RESPONSE_FORMAT_TEXT)) {
			targetUrl = StringUtil.getMimeTextParameters();
		}
		return executeUrls(targetUrl);
	}
	/**
	 * Returns the status per host, in the properties format
	 * @return the status per host, in the properties format
	 */
	public String[] getStatusAsProperties() {
		return executeUrls(StringUtil.getMimePropertiesParameters());
	}
	/**
	 * Returns the status per host, in the text format
	 * @return the status per host, in the text format
	 */
	public String[] getStatusAsText() {
		return executeUrls(StringUtil.getMimeTextParameters());
	}

	/**
	 * Returns the status per host, in the xml format
	 * @return the status per host, in the xml format
	 */
	public String[] getStatusAsXml() {
		return executeUrls(StringUtil.getMimeXmlParameters());
	}
	/**
	 * Returns the hosts container
	 * @return the hosts container
	 */
	public static Hosts getHostsContainer() {
		return _hosts;
	}
	/**
	 * Creates the target url to execute by the http client
	 * @param host the host with all info about the target, ie ipaddress, port, context
	 * @param parameters the control parameters, added as url parameters 
	 * @return the target url to execute by the http client
	 */
	public static String createTargetUrl(HostType host, String parameters) {
		Integer port = host.getPort();
		String portPart = "";
		String targetHost = "";
		if (port!=null && port > 0) {
			portPart = ":"+port;
		}
		targetHost = "http://"+ host.getIpAddress() + portPart;
		String targetContext = "/"+host.getContext() + "?" + parameters;
		return targetHost + targetContext;
	}
	/**
	 * Returns true if one or more hosts added to hosts container, false if not
	 * @return true if one or more hosts added to hosts container, false if not
	 */
	public static Boolean isInitialized() {
		if(_hosts!=null && _hosts.getHost().size()>0){
			logger.info("initialized: "+_hosts.getHost().size()+" hosts");
			return true;
		}
		logger.info("ControllerClient not initialized");
		return false;
	}
	/**
	 * Returns all hosts in hosts container
	 * @return all hosts in hosts container
	 */
	public static List<HostType> getHosts() {
		if(_hosts!=null && _hosts.getHost().size()>0){
			logger.info("returning "+_hosts.getHost().size()+" hosts");
			return _hosts.getHost();
		} 
		return null;
	}
}
