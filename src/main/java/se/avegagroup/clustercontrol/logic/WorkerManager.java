package se.avegagroup.clustercontrol.logic;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.avegagroup.clustercontrol.domain.Hosts;
import se.avegagroup.clustercontrol.domain.HostType;
import se.avegagroup.clustercontrol.domain.JkBalancerType;
import se.avegagroup.clustercontrol.domain.JkMemberType;
import se.avegagroup.clustercontrol.domain.JkResultType;
import se.avegagroup.clustercontrol.domain.JkStatusType;
import se.avegagroup.clustercontrol.domain.WorkerResponse;
import se.avegagroup.clustercontrol.domain.WorkerResponses;
import se.avegagroup.clustercontrol.http.HttpClient;
import se.avegagroup.clustercontrol.util.StringUtil;
import se.avegagroup.clustercontrol.util.WorkerStatus;

public class WorkerManager {

	private static final Logger logger = LoggerFactory.getLogger(WorkerManager.class);

	private static final String RESPONSE_FORMAT_XML = "xml";
	private static final String RESPONSE_FORMAT_PROPERTIES = "prop";
	private static final String RESPONSE_FORMAT_TEXT = "txt";

	private static Hosts _hosts = new Hosts();
//	private static WorkerResponses _workerResponses = new WorkerResponses();

	/**
	 * Resets the ControllerClient hosts container. 
	 */
	public static void reset() {
		_hosts = new Hosts();
	}
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
	 * @throws MalformedURLException
	 * @throws WorkerNotFoundException
	 */
	public static WorkerResponse init(String url) throws MalformedURLException, WorkerNotFoundException {
		URL workerUrl;
		workerUrl = new URL(url);
		return init(workerUrl);
	}
	/**
	 * Initializes the ControllerClient from supplied url. Sets up a hosts container and returns a list of balancers.
	 * @param url the initializing url
	 * @return list of balancers
	 * @throws WorkerNotFoundException if init or getStatus fails
	 */
	public static WorkerResponse init(URL url) throws WorkerNotFoundException {
		if(logger.isDebugEnabled()) {
			logger.debug("Initializing url: "+url.toString());
		}
		HostType host = new HostType();
		host.setIpAddress(url.getHost());
		host.setPort(url.getPort());
		String jkContext = StringUtil.checkPath(url.getPath());
		host.setContext(jkContext);
		
		//
		// try to get workers for this url
		String parameters = StringUtil.getMimeXmlParameters();
		WorkerResponse workerResponse = HttpClient.executeUrl(host, parameters);
		if(workerResponse.getBody()==null) {
			return null;
			//throw new WorkerNotFoundException();
		}
		//
		// this could not work
		JkBalancerType balancer = statusComplex(workerResponse);
		if(balancer!=null && balancer.getMemberCount() > 0) {			
			addHost(host);
		}
		return workerResponse;
	}
	/**
	 * Checks if the provided host is exists, if not it adds it to the hosts container
	 * @param newHost the host to add
	 * @return true if host added, false if not
	 */
	private static boolean addHost(HostType newHost) {
		if(_hosts==null) {
			if(logger.isDebugEnabled()) {
				logger.debug("_hosts==null: resetting _hosts");
			}
			reset();
		}
		//
		// previous hosts found, check if already setup
		int hostsCount = _hosts.getHost().size();
		for (int i = 0; i < hostsCount; i++) {
			HostType host = _hosts.getHost().get(i);
			if(host.getIpAddress().equals(newHost.getIpAddress())) {
				if(host.getPort().equals(newHost.getPort())) {
					if(host.getContext().equals(newHost.getContext())) {
						logger.info("Already exist; trying to add host, "+host.getIpAddress()+", "+host.getPort()+", "+host.getContext());
						return false;
					}
				}
			}
		}
		logger.info("Added host, "+newHost.getIpAddress()+", "+newHost.getPort()+", "+newHost.getContext());
		_hosts.getHost().add(newHost);
		return true;
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
		logger.info("Activating worker: "+worker);
		String activateParameters = StringUtil.getActivateParameters(loadBalancer, worker);
		String xmlMimeParameters = StringUtil.getMimeXmlParameters();
		WorkerResponses workerResponses = HttpClient.executeUrls(_hosts, activateParameters + "&" + xmlMimeParameters);

		parseStatusAndPause(workerResponses, worker);

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
		logger.info("Disabling worker: "+worker);
		String disableParameters = StringUtil.getDisableParameters(loadBalancer, worker);
		String xmlMimeParameters = StringUtil.getMimeXmlParameters();
		WorkerResponses workerResponses = HttpClient.executeUrls(_hosts, disableParameters + "&" + xmlMimeParameters);

		parseStatusAndPause(workerResponses, worker);

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
	 * @param workerResponses the workerlist, ie html bodys
	 * @param worker the worker to get status for
	 */
	private static void parseStatusAndPause(WorkerResponses workerResponses, String worker) {
		//
		// process the disable action responses
		int hostsCount = workerResponses.getWorkerStatus().size();
		for (int hostIdx = 0; hostIdx < hostsCount; hostIdx++) {
			WorkerResponse workerList = workerResponses.getWorkerStatus().get(hostIdx);
			WorkerStatus workerStatus = new WorkerStatus();
			JAXBElement<JkStatusType> jkStatus = workerStatus.unmarshall(workerList.getBody());
			JkResultType result = jkStatus.getValue().getResult();
			if(logger.isDebugEnabled()) {
				if (result.getType().equals("OK")) {
					logger.debug("Worker: '" + worker + "' action OK!");
				} else {
					logger.debug("Worker: '" + worker + "' action NOK!");
				}
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
			if(logger.isDebugEnabled()) {
				logger.debug("Error when suspending thread: "+e.getMessage());
			}
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
		WorkerResponses workerResponses = HttpClient.executeUrls(_hosts, stopParameters + "&" + xmlMimeParameters);

		parseStatusAndPause(workerResponses, worker);

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
		//
		// retrieve the statuses
		WorkerResponses workerResponses = HttpClient.executeUrls(_hosts, StringUtil.getMimeXmlParameters());
		return statusComplex(workerResponses);
	}
	/**
	 * Returns a list of balancer for supplied worker responses
	 * @param workerResponse the responses to convert to balancers
	 * @return a list of balancers
	 */
	public static ArrayList<JkBalancerType> statusComplex(WorkerResponses workerResponses) {
		int workerListCount = workerResponses.getWorkerStatus().size();
		ArrayList<JkBalancerType> resultList = new ArrayList<JkBalancerType>();
		for (int workerListIdx = 0; workerListIdx < workerListCount; workerListIdx++) {
			WorkerResponse workerResponse = workerResponses.getWorkerStatus().get(workerListIdx);
			JkBalancerType statusComplex = null;
			try {
				statusComplex = statusComplex(workerResponse);
			} catch (WorkerNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//
			// store the status
			resultList.add(statusComplex);
		}
		return resultList;
	}
	/**
	 * Returns a balancer for supplied worker response
	 * @param workerResponse the response to convert to a balancer
	 * @return a balancer
	 * @throws WorkerNotFoundException 
	 */
	public static JkBalancerType statusComplex(WorkerResponse workerResponse) throws WorkerNotFoundException {
		WorkerStatus workerStatus = new WorkerStatus();
		JAXBElement<JkStatusType> jkStatus = workerStatus.unmarshall(workerResponse.getBody());
		JkResultType result = jkStatus.getValue().getResult();
		if(result.getType().equals("NOK")) {
			throw new WorkerNotFoundException();
		}
		return jkStatus.getValue().getBalancers().getBalancer();
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
		WorkerResponses workerResponses = HttpClient.executeUrls(_hosts, StringUtil.getMimeXmlParameters());
		int workerListCount = workerResponses.getWorkerStatus().size();
		for (int workerListIdx = 0; workerListIdx < workerListCount; workerListIdx++) {
			//ArrayList<String> memberList = new ArrayList<String>();
			WorkerResponse workerList = workerResponses.getWorkerStatus().get(workerListIdx);
			WorkerStatus workerStatus = new WorkerStatus();
			JAXBElement<JkStatusType> jkStatus = workerStatus.unmarshall(workerList.getBody());
			JkResultType result = jkStatus.getValue().getResult();
			int memberCount = jkStatus.getValue().getBalancers().getBalancer().getMemberCount();
			String[] memberList = new String[memberCount]; 
			for (int memberIdx = 0; memberIdx < memberCount; memberIdx++) {
				JkMemberType member = jkStatus.getValue().getBalancers().getBalancer().getMember().get(memberIdx);
				if(logger.isDebugEnabled()) {
					logger.debug("Worker: '" + member.getName() + "' activation: "+member.getActivation()+" state: "+member.getState()+" busy: "+member.getBusy());
					logger.debug("Result: "+result.getType());
				}
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
	public static WorkerResponses status(String format) {
		String targetUrl = "";
		if (format.equals(RESPONSE_FORMAT_PROPERTIES)) {
			targetUrl = StringUtil.getMimePropertiesParameters();
		} else if (format.equals(RESPONSE_FORMAT_XML)) {
			targetUrl = StringUtil.getMimeXmlParameters();
		} else if (format.equals(RESPONSE_FORMAT_TEXT)) {
			targetUrl = StringUtil.getMimeTextParameters();
		}
		return HttpClient.executeUrls(_hosts, targetUrl);
	}
	/**
	 * Returns the status per host, in the properties format
	 * @return the status per host, in the properties format
	 */
	public WorkerResponses getStatusAsProperties() {
		return status(StringUtil.getMimePropertiesParameters());
	}
	/**
	 * Returns the status per host, in the text format
	 * @return the status per host, in the text format
	 */
	public WorkerResponses getStatusAsText() {
		return status(StringUtil.getMimeTextParameters());
	}

	/**
	 * Returns the status per host, in the xml format
	 * @return the status per host, in the xml format
	 */
	public WorkerResponses getStatusAsXml() {
		return status(StringUtil.getMimeXmlParameters());
	}
	/**
	 * Returns the hosts container
	 * @return the hosts container
	 */
	public static Hosts getHostsContainer() {
		return _hosts;
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
