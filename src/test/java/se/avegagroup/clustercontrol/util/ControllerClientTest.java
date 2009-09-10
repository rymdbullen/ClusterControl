/**
 * 
 */
package se.avegagroup.clustercontrol.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import se.avegagroup.clustercontrol.data.JkBalancerType;
import se.avegagroup.clustercontrol.data.JkMemberType;
import se.avegagroup.clustercontrol.logic.ControllerClient;
import junit.framework.TestCase;

/**
 * @author admin
 *
 */
public class ControllerClientTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	/**
	 * Test method for {@link se.avegagroup.clustercontrol.util.WorkerStatus#unmarshall(java.lang.String)}.
	 * @throws MalformedURLException 
	 */
	public void testInit() throws MalformedURLException {
		String url = "http://localhost:8888/jkmanager";
		URL urll = new URL(url);
		
		ArrayList<JkBalancerType> balancers = ControllerClient.init(urll);		
		
		for (int index = 0; index < balancers.size(); index++) {
			JkBalancerType balancer = balancers.get(index);
			assertNotNull(balancer);
			List<JkMemberType> members = balancer.getMember();
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
				if(worker.equals(workerStatus.getName())) {
					assertEquals("ACT", workerStatus.getActivation());
				}
				System.out.println("["+index+"]: "+workerStatus.getName()+" "+workerStatus.getActivation());
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
				if(worker.equals(workerStatus.getName())) {
					assertEquals("DIS", workerStatus.getActivation());
				}
				System.out.println("["+index+"]: "+workerStatus.getName()+" "+workerStatus.getActivation());
			}
		}
	}
}
