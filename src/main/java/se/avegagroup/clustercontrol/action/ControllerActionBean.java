package se.avegagroup.clustercontrol.action;

import java.net.MalformedURLException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import se.avegagroup.clustercontrol.domain.Host;
import se.avegagroup.clustercontrol.domain.JkStatus;
import se.avegagroup.clustercontrol.logic.WorkerManager;
import se.avegagroup.clustercontrol.logic.WorkerNotFoundException;

@UrlBinding("/Controller.htm")
public class ControllerActionBean extends BaseActionBean {

	private static final Logger logger = LoggerFactory.getLogger(ControllerActionBean.class);

	/**
	 * 
	 * @return
	 */
	@DefaultHandler
	public Resolution view() {
		return new ForwardResolution("/WEB-INF/jsp/controller.jsp");
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
		ArrayList<String> hostList = new ArrayList<String>();
		StringBuilder message = new StringBuilder("Initialized");
		int hostsCount = WorkerManager.getHostsContainer().getHostList().size();
		for (int hostIdx = 0; hostIdx < hostsCount; hostIdx++) {
			Host host = WorkerManager.getHostsContainer().getHostList().get(hostIdx);
			hostList.add(host.getIpAddress());
			message.append(", "+host.getIpAddress());
		}
		if(logger.isDebugEnabled()) {
			logger.debug(message.toString());
		}
		return message.toString();
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
	 * Stops a worker and returns the status of the command
	 * @param loadBalancer
	 * @param worker
	 * @return the status of the command
	 */
	public static ArrayList<JkStatus> stop(String loadBalancer, String worker) {
		return WorkerManager.stop(loadBalancer, worker);
	}
	/**
	 * Disables a worker
	 * @param loadBalancer
	 * @param worker
	 * @return
	 */
	public static ArrayList<JkStatus> disable(String loadBalancer, String worker) {
		return WorkerManager.disable(loadBalancer, worker);
	}
	/**
	 * 
	 * @param loadBalancer
	 * @param worker
	 * @return
	 */
	public static ArrayList<JkStatus> activate(String loadBalancer, String worker) {
		return WorkerManager.activate(loadBalancer, worker);
	}
	/**
	 * returns the balancers for a host
	 * @return
	 */
	public static ArrayList<JkStatus> getStatusComplex(String host) {
		return WorkerManager.statusComplex();
	}
	/**
	 * Initializes the application and returns 
	 * @param url the initialization url
	 * @return 
	 * @throws MalformedURLException 
	 */
	public static ArrayList<JkStatus> initWithUrl(String url) throws MalformedURLException {
		if(false==WorkerManager.isInitialized()) {
			try {
				return WorkerManager.init(url);
			} catch (WorkerNotFoundException e) {
				logger.debug("Failed to locate worker for url: "+url);
			}
		}
		return null;
	}
}
