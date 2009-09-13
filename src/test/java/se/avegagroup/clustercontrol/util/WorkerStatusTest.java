/**
 * 
 */
package se.avegagroup.clustercontrol.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import se.avegagroup.clustercontrol.data.JkBalancerType;
import se.avegagroup.clustercontrol.data.JkBalancersType;
import se.avegagroup.clustercontrol.data.JkMemberType;
import se.avegagroup.clustercontrol.data.JkStatusType;
import se.avegagroup.clustercontrol.data.Hosts;
import se.avegagroup.clustercontrol.data.HostType;
import se.avegagroup.clustercontrol.logic.ControllerClient;
import junit.framework.TestCase;

/**
 * @author admin
 *
 */
public class WorkerStatusTest extends TestCase {

	private static Log logger = LogFactory.getLog(WorkerStatusTest.class);
//	private static final Logger logger = LoggerFactory.getLogger(WorkerStatusTest.class);

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		String loadBalancer = "lbfootprint";
		String hostname = "localhost";
		
		Hosts hosts = new Hosts();
		HostType host = new HostType();
		hosts.setLoadBalancer(loadBalancer);
		host.setIpAddress(hostname);
		host.setContext("jkmanager");
		host.setPort(8888);
		hosts.getHost().add(host);
		
		ControllerClient.init(hosts);
	}
	/**
	 * Test method for {@link se.avegagroup.clustercontrol.util.WorkerStatus#unmarshall(java.lang.String)}.
	 */
	public void testGetStatusUnmarshall() {
		
		logger.debug("Running testGetStatusUnmarshall");
		
		String[] bodys = ControllerClient.status("xml");
		
		WorkerStatus workerStatus = new WorkerStatus();
		for (int index = 0; index < bodys.length; index++) {
			String body = bodys[index];
			JAXBElement<JkStatusType> jkStatus = workerStatus.unmarshall(body);
			assertNotNull(jkStatus);
			assertEquals(new Integer(1), jkStatus.getValue().getBalancers().getCount());
			JkBalancersType balancers =  jkStatus.getValue().getBalancers();
			assertEquals(new Integer(2), balancers.getBalancer().getMemberCount());
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
		ArrayList<JkBalancerType> workerLists = ControllerClient.activate(worker);
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
		ArrayList<JkBalancerType> workerLists = ControllerClient.disable(worker);
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
