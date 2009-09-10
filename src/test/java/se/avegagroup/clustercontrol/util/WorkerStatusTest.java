/**
 * 
 */
package se.avegagroup.clustercontrol.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

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
		host.setPort("8888");
		hosts.getHost().add(host);
		
		ControllerClient cc = new ControllerClient();
		cc.setHosts(hosts);
	}
	/**
	 * Test method for {@link se.avegagroup.clustercontrol.util.WorkerStatus#unmarshall(java.lang.String)}.
	 */
	public void testGetStatusUnmarshall() {
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
				System.out.println(jkMember.getName()+" "+jkMember.getActivation()+" "+jkMember.getState());
			}
		}
	}
	/**
	 * Test method for {@link se.avegagroup.clustercontrol.util.WorkerStatus#unmarshall(java.lang.String)}.
	 */
	public void testActivateUnmarshall() {
		String worker = "footprint1";
		ArrayList<JkBalancerType> workerLists = ControllerClient.activate(worker);
		for (int i = 0; i < workerLists.size(); i++) {
			JkBalancerType workerList = workerLists.get(i);
			for (int index = 0; index < workerList.getMemberCount(); index++) {
				JkMemberType workerStatus = workerList.getMember().get(index);
				System.out.println("["+index+"]: "+workerStatus.getActivation());
				assertEquals("ACT", workerStatus.getActivation());
			}
		}
	}
	/**
	 * Test method for {@link se.avegagroup.clustercontrol.util.WorkerStatus#unmarshall(java.lang.String)}.
	 */
	public void testDisableUnmarshall() {
		String worker = "footprint1";
		ArrayList<JkBalancerType> workerLists = ControllerClient.disable(worker);
		for (int i = 0; i < workerLists.size(); i++) {
			JkBalancerType workerList = workerLists.get(i);
			for (int index = 0; index < workerList.getMemberCount(); index++) {
				JkMemberType workerStatus = workerList.getMember().get(index);
				System.out.println("["+index+"]: "+workerStatus.getActivation());
				assertEquals("DIS", workerStatus.getActivation());
			}
		}
	}
}
