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

import se.avegagroup.clustercontrol.configuration.Constants;
import se.avegagroup.clustercontrol.domain.JkBalancerType;
import se.avegagroup.clustercontrol.domain.JkMemberType;
import se.avegagroup.clustercontrol.domain.WorkerResponse;
import se.avegagroup.clustercontrol.logic.WorkerManager;
import se.avegagroup.clustercontrol.logic.WorkerNotFoundException;
import junit.framework.TestCase;

/**
 * @author admin
 */
public class WorkerManagerTest extends TestCase {

	private static final Logger logger = LoggerFactory.getLogger(WorkerManagerTest.class);

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	/**
	 * Test method for {@link se.avegagroup.clustercontrol.logic.WorkerManager#init(java.net.URL)}.
	 * @throws MalformedURLException 
	 * @throws WorkerNotFoundException 
	 */
	public void testInitDouble() throws MalformedURLException, WorkerNotFoundException {
		WorkerManager.reset();
		logger.debug("Running testInit");
		URL urll = new URL(Constants.TEST_URL);
		
		WorkerResponse response = WorkerManager.init(urll);		
		if(response.getWorkerError()!=null) {
			assertEquals(true, response.getBody()==null);
			assertEquals(true, response.getWorkerError().getMessageKey()!=null);
			assertEquals(true, response.getWorkerError().getMessageKey().equals(""));			
		} else {			
			assertEquals(true, response.getBody()!=null);
			assertEquals(1,WorkerManager.getHosts().size());
		}
		
		response = WorkerManager.init(urll);
		if(response.getWorkerError()!=null) {
			assertEquals(true, response.getBody()==null);
			assertEquals(true, response.getWorkerError().getMessageKey()!=null);
			assertEquals(true, response.getWorkerError().getMessageKey().equals(""));			
		} else {			
			assertEquals(true, response.getBody()!=null);
			assertEquals(1,WorkerManager.getHosts().size());
		}
	}		
	/**
	 * Test method for {@link se.avegagroup.clustercontrol.logic.WorkerManager#init(java.net.URL)}.
	 * @throws MalformedURLException 
	 */
/*	public void testBadInit() throws MalformedURLException {
		WorkerManager.reset();
		logger.debug("Running testInit");
		URL urll = new URL(Constants.TEST_URL);
		
		try {
			WorkerResponse response = WorkerManager.init(urll);
			response.getBody();
		} catch (WorkerNotFoundException e) {
			return;
		}
		fail("Expected WorkerNotFoundException");
	}*/
	/**
	 * Test method for {@link se.avegagroup.clustercontrol.logic.WorkerManager#init(java.net.URL)}.
	 * @throws MalformedURLException 
	 * @throws WorkerNotFoundException 
	 */
	public void testInit() throws MalformedURLException, WorkerNotFoundException{
		WorkerManager.reset();
		logger.debug("Running testInit");
		URL urll = new URL(Constants.TEST_URL);
		
		WorkerResponse response = WorkerManager.init(urll);
		JkBalancerType balancer = WorkerManager.statusComplex(response);
	
		assertNotNull(balancer);
		List<JkMemberType> members = balancer.getMember();
		Iterator<JkMemberType> membersIter = members.iterator();
		while (membersIter.hasNext()) {
			JkMemberType jkMember = (JkMemberType) membersIter.next();
			logger.debug(jkMember.getName()+" "+jkMember.getActivation()+" "+jkMember.getState());
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
