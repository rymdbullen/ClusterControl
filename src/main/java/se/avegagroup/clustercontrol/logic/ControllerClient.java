package se.avegagroup.clustercontrol.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.bind.JAXBElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import se.avegagroup.clustercontrol.data.Hosts;
import se.avegagroup.clustercontrol.data.HostType;
import se.avegagroup.clustercontrol.data.JkMemberType;
import se.avegagroup.clustercontrol.data.JkResultType;
import se.avegagroup.clustercontrol.data.JkStatusType;
import se.avegagroup.clustercontrol.util.StringUtil;
import se.avegagroup.clustercontrol.util.WorkerStatus;

public class ControllerClient {

	private static final Log logger = LogFactory.getLog(ControllerClient.class);

	private static final String RESPONSE_FORMAT_XML = "xml";
	private static final String RESPONSE_FORMAT_PROPERTIES = "prop";
	private static final String RESPONSE_FORMAT_TEXT = "txt";

	private static Hosts _hosts = null;

	/**
	 * initializes the 
	 */
	public static void init(String hostname) {
		Hosts hosts = new Hosts();
		HostType host = new HostType();
		host.setHostname("name");
		host.setIpAddress(hostname);
		host.setPort("8888");
		hosts.getHost().add(host);
		
		_hosts = hosts;  
	}
	/**
	 * 
	 * @param worker
	 * @return
	 */
	public static String[] executeUrls(String parameters) {
		Iterator<HostType> hosts = _hosts.getHost().iterator();
		String[] workerLists = new String[_hosts.getHost().size()];
		int index = 0;
		while (hosts.hasNext()) {
			HostType host = (HostType) hosts.next();
			workerLists[index] = executeUrl(host, parameters);
			index++;
		}
		return workerLists;
	}

	/**
	 * Creates the request (GET) and receives a encapsulated response object, ie
	 * the html body;
	 * 
	 * @param parameters
	 *            the parameters to execute
	 * @return the response, ie html body
	 */
	public static String executeUrl(HostType host, String parameters) {
		String targetUrl = StringUtil.createTargetUrl(host.getIpAddress(), parameters);
		logger.debug("executing request " + targetUrl);
		System.out.println("executing request " + targetUrl);

		// creates the response handler
		HttpClient httpclient = new DefaultHttpClient();
		String responseBody = null;
		try {
			HttpGet httpget = new HttpGet(targetUrl);
			org.apache.http.client.ResponseHandler<String> responseHandler = new BasicResponseHandler();
			responseBody = (String) httpclient
					.execute(httpget, responseHandler);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		httpclient.getConnectionManager().shutdown();
		return responseBody;
	}

	/**
	 * 
	 * @param loadBalancer
	 * @param worker
	 * @return
	 */
	public static ArrayList<String[]> activate(String loadBalancer, String worker) {
		//
		// Perform the activate action
		String activateParameters = StringUtil.getActivateParameters(loadBalancer, worker);
		String xmlMimeParameters = StringUtil.getMimeXmlParameters();
		String[] workerLists = executeUrls(activateParameters + "&" + xmlMimeParameters);

		//
		// process the activate action responses
		for (int index = 0; index < workerLists.length; index++) {
			String workerList = workerLists[index];
			WorkerStatus workerStatus = new WorkerStatus();
			JAXBElement<JkStatusType> jkStatus = workerStatus.unmarshall(workerList);
			JkResultType result = jkStatus.getValue().getResult();
			if (result.getType().equals("OK")) {
				System.out.println("Worker: '" + worker + "' activated OK!");
			} else {
				System.out.println("Worker: '" + worker + "' activated NOK!");
			}
		}

		//
		// wait for x seconds
		try {
			System.out.println("Hello Mac! ACTIVATE");
			// Sleep for 3 seconds
			// Thread.sleep() must be within a try - catch block
			Thread.sleep(3000);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return status();
	}

	/**
	 * 
	 * @param worker
	 * @return
	 */
	public static ArrayList<String[]> activate(String worker) {
		return activate(_hosts.getLoadBalancer(), worker);
	}

	/**
	 * 
	 * @param loadBalancer
	 * @param worker
	 * @return
	 */
	public static ArrayList<String[]> disable(String loadBalancer, String worker) {
		String disableParameters = StringUtil.getDisableParameters(loadBalancer, worker);
		String xmlMimeParameters = StringUtil.getMimeXmlParameters();
		String[] workerLists = executeUrls(disableParameters + "&" + xmlMimeParameters);

		//
		// process the activate action responses
		for (int index = 0; index < workerLists.length; index++) {
			String workerList = workerLists[index];
			WorkerStatus workerStatus = new WorkerStatus();
			try {
				JAXBElement<JkStatusType> jkStatus = workerStatus.unmarshall(workerList);
				JkResultType result = jkStatus.getValue().getResult();
				if (result.getType().equals("OK")) {
					System.out.println("Worker: '" + worker + "' disabled OK!");
				} else {
					System.out.println("Worker: '" + worker + "' disabled NOK!");
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error in:\r\n"+workerList);
			}
		}

		//
		// wait for x seconds
		try {
			System.out.println("Hello Mac! DISABLE");
			// Sleep for 3 seconds
			// Thread.sleep() must be within a try - catch block
			Thread.sleep(3000);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return status();
	}

	/**
	 * 
	 * @param worker
	 * @return
	 */
	public static ArrayList<String[]> disable(String worker) {
		return disable(_hosts.getLoadBalancer(), worker);
	}

	/**
	 * 
	 * @param loadBalancer
	 * @param worker
	 * @return
	 */
	public static ArrayList<String[]> stop(String loadBalancer, String worker) {
		String stopParameters = StringUtil.getStopParameters(loadBalancer, worker);
		String xmlMimeParameters = StringUtil.getMimeXmlParameters();
		String[] workerLists = executeUrls(stopParameters + "&" + xmlMimeParameters);

		//
		// process the activate action responses
		for (int index = 0; index < workerLists.length; index++) {
			String workerList = workerLists[index];
			WorkerStatus workerStatus = new WorkerStatus();
			try {
				JAXBElement<JkStatusType> jkStatus = workerStatus.unmarshall(workerList);
				JkResultType result = jkStatus.getValue().getResult();
				if (result.getType().equals("OK")) {
					System.out.println("Worker: '" + worker + "' stopped OK!");
				} else {
					System.out.println("Worker: '" + worker + "' stopped NOK!");
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error in:\r\n"+workerList);
			}
		}

		//
		// wait for x seconds
		try {
			System.out.println("Hello Mac! STOP");
			// Sleep for 3 seconds
			// Thread.sleep() must be within a try - catch block
			Thread.sleep(3000);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return status();
	}

	/**
	 * 
	 * @param worker
	 * @return
	 */
	public static ArrayList<String[]> stop(String worker) {
		return stop(_hosts.getLoadBalancer(), worker);
	}

	/**
	 * 
	 * @param worker
	 * @return
	 */
	public static ArrayList<String[]> status() {
		/** List of statuses like 0,0 - 1,0 */
		ArrayList<String[]> resultList = new ArrayList<String[]>();
		
		//
		// retrieve the statuses
		String[] workerLists = executeUrls(StringUtil.getMimeXmlParameters());
		int workerListCount = workerLists.length;
		for (int workerListIdx = 0; workerListIdx < workerListCount; workerListIdx++) {
			//ArrayList<String> memberList = new ArrayList<String>();
			String workerList = workerLists[workerListIdx];
			WorkerStatus workerStatus = new WorkerStatus();
			JAXBElement<JkStatusType> jkStatus = workerStatus.unmarshall(workerList);
			JkResultType result = jkStatus.getValue().getResult();
			int memberCount = jkStatus.getValue().getBalancers().getBalancer().getMemberCount();
			String[] memberList = new String[memberCount]; 
			for (int memberIdx = 0; memberIdx < memberCount; memberIdx++) {
				JkMemberType member = jkStatus.getValue().getBalancers().getBalancer().getMember().get(memberIdx);
				System.out.println("Worker: '" + member.getName() + "' activation: "+member.getActivation()+" state: "+member.getState()+" busy: "+member.getBusy());
				System.out.println("Result: "+result.getType());
				//memberList.add(result.getType());
				memberList[memberIdx] = result.getType();
			}
			resultList.add(memberList);
		}
		return resultList;
	}

	/**
	 * 
	 * @param host
	 * @param format
	 * @return
	 */
	public static String[] status(String format) {
		String targetUrl = "";
		if (format.equals(RESPONSE_FORMAT_PROPERTIES)) {
			targetUrl = StringUtil.getMimePropertiesParameters();
		} else if (format.equals(RESPONSE_FORMAT_XML)) {
			targetUrl = StringUtil.getMimeXmlParameters();
		} else if (format.equals(RESPONSE_FORMAT_TEXT)) {
			targetUrl = StringUtil.getMimeTextParameters();
		}
		return executeUrls(targetUrl);
	}

	public String[] getStatusAsProperties() {
		return executeUrls(StringUtil.getMimePropertiesParameters());
	}

	public String[] getStatusAsProperties(String host) {
		return executeUrls(StringUtil.getMimePropertiesParameters());
	}

	public String[] getStatusAsText() {
		return executeUrls(StringUtil.getMimeTextParameters());
	}

	/**
	 * 
	 * @param host
	 * @return
	 */
	public String[] getStatusAsXml() {
		return executeUrls(StringUtil.getMimeXmlParameters());
	}

	/**
	 * 
	 * @param hosts
	 */
	public void setHosts(Hosts hosts) {
		_hosts = hosts;
	}

	/**
	 * 
	 * @return
	 */
	public Hosts getHosts() {
		return _hosts;
	}
}