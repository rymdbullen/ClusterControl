package se.avegagroup.clustercontrol.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import se.avegagroup.clustercontrol.data.JkBalancersType;
import se.avegagroup.clustercontrol.data.JkMemberType;
import se.avegagroup.clustercontrol.data.JkStatusType;
import se.avegagroup.clustercontrol.util.WorkerStatus;

public class ControllerActionBean extends BaseActionBean {

	private static final Log logger = LogFactory.getLog(ControllerActionBean.class);

	/**
	 * 
	 * @return
	 */
	public Resolution view() {
		return new ForwardResolution("/WEB-INF/jsp/dwr/controller.jsp");
	}
	/**
	 * 
	 * @param loadBalancer
	 * @param worker
	 * @return
	 */
	public static String stop(String loadBalancer, String worker) {
		ArrayList<String[]> workerLists = ControllerClient.stop(loadBalancer, worker);
//		WorkerStatus workerStatus = new WorkerStatus();
		String status = "";
		for (int hostIdx = 0; hostIdx < workerLists.size(); hostIdx++) {
			String[] workerList = workerLists.get(hostIdx);
//			JAXBElement<JkStatusType> jkStatus = workerStatus.unmarshall(body);
//			List<JkMemberType> members =  jkStatus.getValue().getBalancers().getBalancer().getMember();
//			Iterator<JkMemberType> membersIter = members.iterator();
//			while (membersIter.hasNext()) {
			int workerIdx = 0;
			for (int i = 0; i < workerList.length; i++) {
				String hostStatus = workerList[i];
				status = status+"["+hostIdx+"]["+workerIdx+"]: "+hostStatus;
				workerIdx++;
			}
//				JkMemberType jkMember = (JkMemberType) membersIter.next();
//				System.out.println(jkMember.getName()+": "+jkMember.getActivation()+" "+jkMember.getBusy());
//				status = status+"["+hostIdx+"]["+workerIdx+"]: "+jkMember.getActivation()+" "+jkMember.getBusy();
//				workerIdx++;
//			}
		}
		return status;
	}
	/**
	 * 
	 * @param loadBalancer
	 * @param worker
	 * @return
	 */
	public static String disable(String loadBalancer, String worker) {
		ArrayList<String[]> workerLists = ControllerClient.disable(loadBalancer, worker);
		//String[] body = bodys.get(0);
		//WorkerStatus workerStatus = new WorkerStatus();
		String status = "";
		for (int hostIdx = 0; hostIdx < workerLists.size(); hostIdx++) {
			String[] workerList = workerLists.get(hostIdx);
//			JAXBElement<JkStatusType> jkStatus = workerStatus.unmarshall(body);
//			List<JkMemberType> members =  jkStatus.getValue().getBalancers().getBalancer().getMember();
//			Iterator<JkMemberType> membersIter = members.iterator();
//			while (membersIter.hasNext()) {
			int workerIdx = 0;
			for (int i = 0; i < workerList.length; i++) {
				String hostStatus = workerList[i];
				status = status+"["+hostIdx+"]["+workerIdx+"]: "+hostStatus;
				workerIdx++;
			}
//				JkMemberType jkMember = (JkMemberType) membersIter.next();
//				System.out.println(jkMember.getName()+": "+jkMember.getActivation()+" "+jkMember.getBusy());
//				status = status+"["+hostIdx+"]["+workerIdx+"]: "+jkMember.getActivation()+" "+jkMember.getBusy();
//				workerIdx++;
//			}
		}
		return status;
	}
	/**
	 * 
	 * @param loadBalancer
	 * @param worker
	 * @return
	 */
	public static String activate(String loadBalancer, String worker) {
		ArrayList<String[]> hostsStatus = ControllerClient.activate(loadBalancer, worker);
//		WorkerStatus workerStatus = new WorkerStatus();
		String status = "";
		int workerIdx = 0;
		for (int hostIdx = 0; hostIdx < hostsStatus.size(); hostIdx++) {
			String[] hostStatus = hostsStatus.get(hostIdx);
			status = status+"["+hostIdx+"]["+workerIdx+"]: "+hostStatus;
			workerIdx++;
		}
//			JAXBElement<JkStatusType> jkStatus = workerStatus.unmarshall(body);
//			List<JkMemberType> members =  jkStatus.getValue().getBalancers().getBalancer().getMember();
//			Iterator<JkMemberType> membersIter = members.iterator();
//			while (membersIter.hasNext()) {
//				JkMemberType jkMember = (JkMemberType) membersIter.next();
//				System.out.println(jkMember.getName()+": "+jkMember.getActivation()+" "+jkMember.getBusy());
//				status = status+"["+hostIdx+"]["+workerIdx+"]: "+jkMember.getActivation()+" "+jkMember.getBusy();
//				workerIdx++;
//			}
//		}
		return status;
	}
	/**
	 * returns the status of the workers for all nodes
	 * @return
	 */
	public static String status() {
		String[] bodys = ControllerClient.status("xml");
		
		String status = ""; 
		WorkerStatus workerStatus = new WorkerStatus();
		for (int hostIdx = 0; hostIdx < bodys.length; hostIdx++) {
			String body = bodys[hostIdx];
			JAXBElement<JkStatusType> jkStatus = workerStatus.unmarshall(body);
			JkBalancersType balancers =  jkStatus.getValue().getBalancers();
			List<JkMemberType> members = balancers.getBalancer().getMember();
			Iterator<JkMemberType> membersIter = members.iterator();
			int workerIdx = 0;
			while (membersIter.hasNext()) {
				JkMemberType jkMember = (JkMemberType) membersIter.next();
				System.out.println(jkMember.getName()+": "+jkMember.getActivation()+" "+jkMember.getBusy());
				status = status+"["+hostIdx+"]["+workerIdx+"]: "+jkMember.getActivation()+" "+jkMember.getBusy();
				workerIdx++;
			}
		}
		return status;
	}
}
