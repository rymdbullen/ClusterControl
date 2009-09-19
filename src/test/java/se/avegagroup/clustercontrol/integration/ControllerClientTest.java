/**
 * 
 */
package se.avegagroup.clustercontrol.integration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.client.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.avegagroup.clustercontrol.domain.JkBalancerType;
import se.avegagroup.clustercontrol.domain.JkMemberType;
import se.avegagroup.clustercontrol.logic.WorkerManager;
import junit.framework.TestCase;

/**
 * @author admin
 */
public class ControllerClientTest extends TestCase {

	private static final Logger logger = LoggerFactory.getLogger(ControllerClientTest.class);

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	/**
	 * Test method for {@link se.avegagroup.clustercontrol.logic.WorkerManager#init(java.net.URL)}.
	 * @throws MalformedURLException 
	 */
	public void testInitDouble() throws MalformedURLException, HttpResponseException {
		WorkerManager.reset();
		logger.debug("Running testInit");
		String url = "http://localhost:8888/jkmanager";
		URL urll = new URL(url);
		
		ArrayList<JkBalancerType> balancers = WorkerManager.init(urll);
		assertEquals(1,balancers.size());
		
		balancers = WorkerManager.init(urll);
		assertEquals(1,balancers.size());
	}		
	/**
	 * Test method for {@link se.avegagroup.clustercontrol.logic.WorkerManager#init(java.net.URL)}.
	 * @throws MalformedURLException 
	 */
	public void testInit() throws MalformedURLException, HttpResponseException{
		WorkerManager.reset();
		logger.debug("Running testInit");
		String url = "http://localhost:8888/jkmanager";
		URL urll = new URL(url);
		
		ArrayList<JkBalancerType> balancers = WorkerManager.init(urll);		
		
		for (int index = 0; index < balancers.size(); index++) {
			JkBalancerType balancer = balancers.get(index);
			assertNotNull(balancer);
			List<JkMemberType> members = balancer.getMember();
			Iterator<JkMemberType> membersIter = members.iterator();
			while (membersIter.hasNext()) {
				JkMemberType jkMember = (JkMemberType) membersIter.next();
				logger.debug(jkMember.getName()+" "+jkMember.getActivation()+" "+jkMember.getState());
			}
		}
	}
	/**
	 * Test method for {@link se.avegagroup.clustercontrol.logic.WorkerManager#activate(String)}.
	 */
	public void testActivateUnmarshall() throws HttpResponseException {
		logger.debug("Running testActivateUnmarshall");
		String worker = "footprint1";
		ArrayList<JkBalancerType> workerLists = WorkerManager.activate(worker);
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
	/**
	 * Test method for {@link se.avegagroup.clustercontrol.logic.WorkerManager#disable(String)}.
	 */
	public void testDisableUnmarshall() throws HttpResponseException {
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
