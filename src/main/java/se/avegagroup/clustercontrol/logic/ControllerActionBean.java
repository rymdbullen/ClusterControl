package se.avegagroup.clustercontrol.logic;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.avegagroup.clustercontrol.data.JkBalancerType;
import se.avegagroup.clustercontrol.data.JkMemberType;
import se.avegagroup.clustercontrol.data.JkStatusType;
import se.avegagroup.clustercontrol.util.StringUtil;
import se.avegagroup.clustercontrol.util.WorkerStatus;

@UrlBinding("/Controller.htm")
public class ControllerActionBean extends BaseActionBean {

	private static final Logger logger = LoggerFactory.getLogger(ControllerActionBean.class);

	/**
	 * 
	 * @return
	 */
	@DefaultHandler
	public Resolution view() {
		return new ForwardResolution("/WEB-INF/jsp/dwr/controller.jsp");
	}
	/**
	 * 
	 * @param loadBalancer
	 * @param worker
	 * @return
	 */
	public static List<JkBalancerType> stop(String loadBalancer, String worker) {
		return ControllerClient.stop(loadBalancer, worker);
	}
	/**
	 * 
	 * @param loadBalancer
	 * @param worker
	 * @return
	 */
	public static List<JkBalancerType> disable(String loadBalancer, String worker) {
		return ControllerClient.disable(loadBalancer, worker);
	}
	/**
	 * 
	 * @param loadBalancer
	 * @param worker
	 * @return
	 */
	public static List<JkBalancerType> activate(String loadBalancer, String worker) {
		return ControllerClient.activate(loadBalancer, worker);
	}
	/**
	 * returns the balancers for a host
	 * @return
	 */
	public static ArrayList<JkBalancerType> getStatusComplex(String host) {
		String[] bodys = ControllerClient.status("xml");
		
		ArrayList<JkBalancerType> balancers = new ArrayList<JkBalancerType>(bodys.length);
		WorkerStatus workerStatus = new WorkerStatus();
		for (int hostIdx = 0; hostIdx < bodys.length; hostIdx++) {
			String body = bodys[hostIdx];
			JAXBElement<JkStatusType> jkStatus = workerStatus.unmarshall(body);
			balancers.add(jkStatus.getValue().getBalancers().getBalancer());
		}
		return balancers;
	}
	/**
	 * Initializes the application and returns 
	 * @param url the initialization url
	 * @return 
	 */
	public static ArrayList<JkBalancerType> setUrl(String url) {
		URL urll;
		try {
			urll = new URL(url);
			ControllerClient.init(urll);
			return getStatusComplex("");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 
	 * @param hostname
	 * @return
	 */
	public String setHostname(String hostname) {
		//
		// try to get rest of hosts...
		List<JkBalancerType> balancers = getStatusComplex("");
		ArrayList<String> hosts = new ArrayList<String>(1);
		for (int balancerIdx = 0; balancerIdx < balancers.size(); balancerIdx++) {
			JkBalancerType balancer = balancers.get(balancerIdx);
			List<JkMemberType> members = balancer.getMember();
			for (int memberIdx = 0; memberIdx < members.size(); memberIdx++) {
				JkMemberType member = members.get(memberIdx);
				String host = member.getHost();
				host = StringUtil.getAddress(host);
				if(false==hosts.contains(host)) {
					hosts.add(host);
				}
			}
		}
		if(hosts.size()>1) {
			System.out.println("WEE HAVEE MOREE THAN ONEE");
		} else {
			System.out.println("ONLY ONEE");
		}
		//String status = ControllerClient.init(hosts);
		
		return "status";
	}
}
