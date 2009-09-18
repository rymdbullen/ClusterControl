package se.avegagroup.clustercontrol.action;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import se.avegagroup.clustercontrol.domain.HostType;
import se.avegagroup.clustercontrol.domain.JkBalancerType;
import se.avegagroup.clustercontrol.domain.JkMemberType;
import se.avegagroup.clustercontrol.domain.JkStatusType;
import se.avegagroup.clustercontrol.domain.WorkerResponse;
import se.avegagroup.clustercontrol.domain.WorkerResponses;
import se.avegagroup.clustercontrol.logic.WorkerManager;
import se.avegagroup.clustercontrol.util.StringUtil;
import se.avegagroup.clustercontrol.util.WorkerStatus;

@UrlBinding("/Controller.htm")
public class ControllerActionBean extends BaseActionBean {

	private static Log logger = LogFactory.getLog(ControllerActionBean.class);
//	private static final Logger logger = LoggerFactory.getLogger(ControllerActionBean.class);

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
		logger.debug(initDescriptionKey);
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
		URL urll;
		try {
			urll = new URL(url);
			WorkerManager.init(urll);
			return getStatusComplex("");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 
	 * @param hostname
	 * @return
	 */
	public String setHostname(String hostname) {
		//
		// try to get rest of hosts...
		List<JkBalancerType> balancers = getStatusComplex("");
		ArrayList<String> hosts = new ArrayList<String>(1);
		for (int balancerIdx = 0; balancerIdx < balancers.size(); balancerIdx++) {
			JkBalancerType balancer = balancers.get(balancerIdx);
			List<JkMemberType> members = balancer.getMember();
			for (int memberIdx = 0; memberIdx < members.size(); memberIdx++) {
				JkMemberType member = members.get(memberIdx);
				String host = member.getHost();
				host = StringUtil.getAddress(host);
				if(false==hosts.contains(host)) {
					hosts.add(host);
				}
			}
		}
		if(hosts.size()>1) {
			System.out.println("WEE HAVEE MOREE THAN ONEE");
		} else {
			System.out.println("ONLY ONEE");
		}
		//String status = ControllerClient.init(hosts);
		
		return "status";
	}
}
