package se.avegagroup.clustercontrol.action;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import se.avegagroup.clustercontrol.domain.HostType;
import se.avegagroup.clustercontrol.domain.JkBalancerType;
import se.avegagroup.clustercontrol.domain.JkMemberType;
import se.avegagroup.clustercontrol.domain.JkStatusType;
import se.avegagroup.clustercontrol.domain.WorkerResponse;
import se.avegagroup.clustercontrol.domain.WorkerResponses;
import se.avegagroup.clustercontrol.logic.WorkerManager;
import se.avegagroup.clustercontrol.logic.WorkerNotFoundException;
import se.avegagroup.clustercontrol.util.WorkerStatus;

@UrlBinding("/Controller.htm")
public class ControllerActionBean extends BaseActionBean {

	private static final Logger logger = LoggerFactory.getLogger(ControllerActionBean.class);

	/**
	 * 
	 * @return
	 */
	@DefaultHandler
	public Resolution view() {
		return new ForwardResolution("/WEB-INF/jsp/dwr/controller.jsp");
	}
	/**
	 * Returns a text if initialized, null if not 
	 * @param worker N/A
	 * @return a text if initialized, null if not
	 */
	public static String isInitialized(String worker) {
		if(false==WorkerManager.isInitialized()) {
			return null;
		}
		String initDescriptionKey = "initialized, ";
		int hostsCount = WorkerManager.getHostsContainer().getHost().size();
		for (int hostIdx = 0; hostIdx < hostsCount; hostIdx++) {
			HostType host = WorkerManager.getHostsContainer().getHost().get(hostIdx);
			initDescriptionKey += host.getIpAddress();
		}
		if(logger.isDebugEnabled()) {
			logger.debug(initDescriptionKey);
		}
		return initDescriptionKey;
	}
	/**
	 * Returns true if client is initalized, false if not
	 * @param worker
	 * @return true if client is initalized, false if not
	 */
	public static Boolean isInit(String worker) {
		return WorkerManager.isInitialized();
	}
	/**
	 * 
	 * @param loadBalancer
	 * @param worker
	 * @return
	 */
	public static List<JkBalancerType> stop(String loadBalancer, String worker) {
		return WorkerManager.stop(loadBalancer, worker);
	}
	/**
	 * Disables a worker
	 * @param loadBalancer
	 * @param worker
	 * @return
	 */
	public static List<JkBalancerType> disable(String loadBalancer, String worker) {
		return WorkerManager.disable(loadBalancer, worker);
	}
	/**
	 * 
	 * @param loadBalancer
	 * @param worker
	 * @return
	 */
	public static List<JkBalancerType> activate(String loadBalancer, String worker) {
		return WorkerManager.activate(loadBalancer, worker);
	}
	/**
	 * returns the balancers for a host
	 * @return
	 */
	public static ArrayList<JkBalancerType> getStatusComplex(String host) {
		WorkerResponses workerResponses = WorkerManager.status("xml");
		
		WorkerStatus workerStatus = new WorkerStatus();
		int hostsCount = workerResponses.getWorkerStatus().size();
		ArrayList<JkBalancerType> balancers = new ArrayList<JkBalancerType>(hostsCount);
		for (int hostIdx = 0; hostIdx < hostsCount; hostIdx++) {
			WorkerResponse workerResponse = workerResponses.getWorkerStatus().get(hostIdx);
			JAXBElement<JkStatusType> jkStatus = workerStatus.unmarshall(workerResponse.getBody());
			balancers.add(jkStatus.getValue().getBalancers().getBalancer());
		}
		return balancers;
	}
	/**
	 * Initializes the application and returns 
	 * @param url the initialization url
	 * @return 
	 */
	public static ArrayList<JkBalancerType> setUrl(String url) {
		try {
			URL workerUrl = new URL(url);
			WorkerResponse workerResponse = WorkerManager.init(workerUrl);
			//
			// try to initialize with the supplied url
			ArrayList<JkBalancerType> balancers = new ArrayList<JkBalancerType>(1);
			WorkerStatus workerStatus = new WorkerStatus();
			JAXBElement<JkStatusType> jkStatus = workerStatus.unmarshall(workerResponse.getBody());
			JkBalancerType balancer = jkStatus.getValue().getBalancers().getBalancer();
			balancers.add(balancer);
			//
			// get more hosts?
			List<JkMemberType> members = balancer.getMember();
			String contextPath = workerUrl.getPath();
			HashSet<String> addressSet = getUniqueHosts(members);
			if (addressSet!=null) {
				if(logger.isDebugEnabled()) {
					logger.debug("Try to get more hosts");
				}
				Iterator<String> addressIterator = addressSet.iterator();
				while (addressIterator.hasNext()) {
					String protocolAndHost = (String) addressIterator.next();
					String address = protocolAndHost+":"+workerUrl.getPort()+contextPath;
					URL newWorkerUrl = new URL(address);
					WorkerManager.init(newWorkerUrl);
				}
			}
			return balancers;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WorkerNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Returns a list of unique host addresses, or null if no more than one host is found
	 * @param members the members to get hosts from
	 * @return a list of unique host addresses, or null if no more than one host is found
	 */
	private static HashSet<String> getUniqueHosts(List<JkMemberType> members) {
		HashSet<String> addressSet = new HashSet<String>(); 
		int memberCount = members.size();
		for (int memberIdx = 0; memberIdx < memberCount; memberIdx++) {
			JkMemberType member = members.get(memberIdx);
			String host = "http://"+member.getHost();
			if(logger.isDebugEnabled()) {
				logger.debug("found: "+host);
			}
			addressSet.add(host);
		}
		if(addressSet.size()>1) {
			// TODO check these hosts...
			return addressSet;
		}
		if(logger.isDebugEnabled()) {
			logger.debug("found no more hosts");
		}
		return null;
	}
}
