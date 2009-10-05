/**
 * 
 */
package se.avegagroup.clustercontrol.integration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.avegagroup.clustercontrol.domain.JkBalancerType;
import se.avegagroup.clustercontrol.domain.JkBalancersType;
import se.avegagroup.clustercontrol.domain.JkMemberType;
import se.avegagroup.clustercontrol.domain.JkStatusType;
import se.avegagroup.clustercontrol.domain.Hosts;
import se.avegagroup.clustercontrol.domain.HostType;
import se.avegagroup.clustercontrol.domain.WorkerResponse;
import se.avegagroup.clustercontrol.domain.WorkerResponses;
import se.avegagroup.clustercontrol.logic.WorkerManager;
import se.avegagroup.clustercontrol.util.WorkerStatus;
import junit.framework.TestCase;

/**
 * @author admin
 *
 */
public class WorkerStatusTest extends TestCase {

	private static final Logger logger = LoggerFactory.getLogger(WorkerStatusTest.class);

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		String loadBalancer = "lbfootprint";
		String hostname = "192.168.10.115";
		
		Hosts hosts = new Hosts();
		HostType host = new HostType();
		hosts.setLoadBalancer(loadBalancer);
		host.setIpAddress(hostname);
		host.setContext("jkmanager");
		hosts.getHost().add(host);
		
		WorkerManager.init(hosts);
	}
	/**
	 * Test method for {@link se.avegagroup.clustercontrol.util.WorkerStatus#unmarshall(java.lang.String)}.
	 */
	public void testGetStatusUnmarshall() {
		
		logger.debug("Running testGetStatusUnmarshall");
		
		WorkerResponses workerResponses = WorkerManager.status("xml");
		
		WorkerStatus workerStatus = new WorkerStatus();
		int hostsCount = workerResponses.getWorkerStatus().size();
		for (int hostIdx = 0; hostIdx < hostsCount; hostIdx++) {
			WorkerResponse workerResponse = workerResponses.getWorkerStatus().get(hostIdx);
			JAXBElement<JkStatusType> jkStatus = workerStatus.unmarshall(workerResponse.getBody());
			assertNotNull(jkStatus);
			assertEquals(new Integer(1), jkStatus.getValue().getBalancers().getCount());
			JkBalancersType balancers =  jkStatus.getValue().getBalancers();
			assertEquals(new Integer(4), balancers.getBalancer().getMemberCount());
			List<JkMemberType> members = balancers.getBalancer().getMember();
			Iterator<JkMemberType> membersIter = members.iterator();
			while (membersIter.hasNext()) {
				JkMemberType jkMember = (JkMemberType) membersIter.next();
				logger.debug(jkMember.getName()+" "+jkMember.getActivation()+" "+jkMember.getState());
			}
		}
	}
	/**
	 * Test method for {@link se.avegagroup.clustercontrol.util.WorkerStatus#unmarshall(java.lang.String)}.
	 */
	public void testActivateUnmarshall() {
		logger.debug("Running testActivateUnmarshall");
		String worker = "footprint1";
		ArrayList<JkBalancerType> workerLists = WorkerManager.activate(worker);
		for (int i = 0; i < workerLists.size(); i++) {
			JkBalancerType workerList = workerLists.get(i);
			for (int index = 0; index < workerList.getMemberCount(); index++) {
				JkMemberType workerStatus = workerList.getMember().get(index);
				if(worker.equals(workerStatus.getName())) {
					assertEquals("ACT", workerStatus.getActivation());
				}
				logger.debug("["+index+"]: "+workerStatus.getName()+" "+workerStatus.getActivation());
			}
		}
	}
	/**
	 * Test method for {@link se.avegagroup.clustercontrol.util.WorkerStatus#unmarshall(java.lang.String)}.
	 */
	public void testDisableUnmarshall() {
		logger.debug("Running testDisableUnmarshall");
		String worker = "footprint1";
		ArrayList<JkBalancerType> workerLists = WorkerManager.disable(worker);
		for (int i = 0; i < workerLists.size(); i++) {
			JkBalancerType workerList = workerLists.get(i);
			for (int index = 0; index < workerList.getMemberCount(); index++) {
				JkMemberType workerStatus = workerList.getMember().get(index);
				if(worker.equals(workerStatus.getName())) {
					assertEquals("DIS", workerStatus.getActivation());
				}
				logger.debug("["+index+"]: "+workerStatus.getName()+" "+workerStatus.getActivation());
			}
		}
	}
}
