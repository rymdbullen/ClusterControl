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

@UrlBinding("/controller.html")
public class ControllerActionBean extends BaseActionBean {

	private static final Logger logger = LoggerFactory.getLogger(ControllerActionBean.class);

	private static String initializedUrl;
	
	/**
	 * 
	 * @return
	 */
	//@DefaultHandler
	public Resolution view() {
		return new ForwardResolution("/WEB-INF/jsp/page/controller.jsp");
	}

	public String getInitializedUrl() {
		return initializedUrl;
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
	 * @param worker the worker to disable
	 * @return
	 */
	public static ArrayList<JkStatus> disable(String worker) {
		return WorkerManager.disable(worker);
	}
	/**
	 * Activates a worker
	 * @param loadBalancer
	 * @param worker the worker to activate
	 * @return
	 */
	public static ArrayList<JkStatus> activate(String worker) {
		return WorkerManager.activate(worker);
	}
	/**
	 * Disables all workers
	 * @param rate the rate to disable workers
	 * @return
	 */
	public static ArrayList<JkStatus> disableAll(String rate) {
		return WorkerManager.disableAll(rate);
	}
	/**
	 * Activates all workers
	 * @param rate the rate of the workers activation
	 * @return
	 */
	public static ArrayList<JkStatus> activateAll(String rate) {
		return WorkerManager.activateAll(rate);
	}
	/**
	 * Returns the jk statuses
	 * @param host N/A
	 * @return the jk statuses
	 */
	public static ArrayList<JkStatus> getStatusComplex(String host) {
		return WorkerManager.statusComplex();
	}
	/**
	 * Initializes the application and returns an array of jk status 
	 * @param initUrl the initialization url
	 * @return an array of jk status
	 * @throws MalformedURLException 
	 */
	public static ArrayList<JkStatus> initWithUrl(String initUrl) throws MalformedURLException {
		if(false==WorkerManager.isInitialized()) {
			try {
				ArrayList<JkStatus> statuses = WorkerManager.init(initUrl);
				if(statuses!=null && statuses.size()>0) { 
					initializedUrl = initUrl;
				}
				return statuses;
			} catch (WorkerNotFoundException e) {
				logger.debug("Failed to locate worker for url: "+initUrl);
			}
		}
		return null;
	}
}
