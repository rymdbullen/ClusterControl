package se.avegagroup.clustercontrol.integration;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.avegagroup.clustercontrol.action.ControllerActionBean;
import se.avegagroup.clustercontrol.configuration.Constants;
import se.avegagroup.clustercontrol.domain.JkBalancer;
import se.avegagroup.clustercontrol.domain.JkMember;
import se.avegagroup.clustercontrol.domain.JkStatus;
import se.avegagroup.clustercontrol.logic.WorkerManager;
import junit.framework.TestCase;

public class ControllerActionBeanTest extends TestCase {
	private static final Logger logger = LoggerFactory.getLogger(ControllerActionBeanTest.class);
	public void testInitWithUrl() throws MalformedURLException {
		WorkerManager.reset();
		logger.debug("Running tests against: "+Constants.TEST_URL);
		ArrayList<JkStatus> balancerList = ControllerActionBean.initWithUrl(Constants.TEST_URL);
		Iterator<JkStatus> listIter = balancerList.iterator();
		System.out.println("Found these hosts:");
		while (listIter.hasNext()) {
			JkStatus jkStatus = (JkStatus) listIter.next();
			System.out.println(jkStatus.getServer().getName());
		}
		assertEquals(2, balancerList.size());
		assertEquals(2, WorkerManager.getHosts().size());
	}
	public void testDisable() {
		logger.debug("Running testDisable");
		String worker = "footprint1";
		String lb = "lbfootprint";
		ArrayList<JkStatus> workerLists = ControllerActionBean.disable(lb, worker);
		for (int i = 0; i < workerLists.size(); i++) {
			JkStatus workerList = workerLists.get(i);
			JkBalancer balancer = workerList.getBalancers().getBalancer();
			for (int index = 0; index < balancer.getMemberCount(); index++) {
				JkMember workerStatus = balancer.getMember().get(index);
				if(worker.equals(workerStatus.getName())) {
					assertEquals("DIS", workerStatus.getActivation());
				}
				logger.debug("["+index+"]: "+workerStatus.getName()+" "+workerStatus.getActivation());
			}
		}
	}
	public void testActivate() {
		logger.debug("Running testActivate");
		String worker = "footprint1";
		String lb = "lbfootprint";
		ArrayList<JkStatus> workerLists = ControllerActionBean.activate(lb, worker);
		for (int i = 0; i < workerLists.size(); i++) {
			JkStatus workerList = workerLists.get(i);
			JkBalancer balancer = workerList.getBalancers().getBalancer();
			for (int index = 0; index < balancer.getMemberCount(); index++) {
				JkMember workerStatus = balancer.getMember().get(index);
				if(worker.equals(workerStatus.getName())) {
					assertEquals("ACT", workerStatus.getActivation());
				}
				logger.debug("["+index+"]: "+workerStatus.getName()+" "+workerStatus.getActivation());
			}
		}
	}
}
