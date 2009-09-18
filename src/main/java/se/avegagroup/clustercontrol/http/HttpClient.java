package se.avegagroup.clustercontrol.http;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.avegagroup.clustercontrol.domain.Hosts;
import se.avegagroup.clustercontrol.domain.HostType;
import se.avegagroup.clustercontrol.domain.ResponseError;
import se.avegagroup.clustercontrol.domain.WorkerResponse;
import se.avegagroup.clustercontrol.domain.WorkerResponses;
import se.avegagroup.clustercontrol.http.RetryHandler;

public class HttpClient {

	private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);

	/**
	 * Executes urls
	 * @param parameters the parameters to execute
	 * @return the workerlists, ie html bodys
	 */
	public static WorkerResponses executeUrls(Hosts _hosts, String parameters) {
		int hostsCount = _hosts.getHost().size();
		WorkerResponses workerResponses = new WorkerResponses();
		
		for (int hostIdx = 0; hostIdx < hostsCount; hostIdx++) {
			HostType host = _hosts.getHost().get(hostIdx);
			workerResponses.getWorkerStatus().add(executeUrl(host, parameters));
		}
		return workerResponses;
	}

	/**
	 * Creates the request (GET) and receives a encapsulated response object, ie
	 * the html body. Returns a WorkerResponse for this request.
	 * 
	 * @param host
	 * 			  the host to request
	 * @param parameters
	 *            the parameters to execute
	 * @return the response, ie html body
	 */
	public static WorkerResponse executeUrl(HostType host, String parameters) {
		String targetUrl = createTargetUrl(host, parameters);
		logger.debug("executing request " + targetUrl);

		// creates the response handler
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpRequestRetryHandler retryHandler = new RetryHandler();
		httpclient.setHttpRequestRetryHandler(retryHandler);

		WorkerResponse workerResponse = new WorkerResponse();
		String responseBody = null;
		try {
			HttpGet httpget = new HttpGet(targetUrl);
			org.apache.http.client.ResponseHandler<String> responseHandler = new BasicResponseHandler();
			responseBody = (String) httpclient.execute(httpget, responseHandler);
			workerResponse.setBody(responseBody);
			workerResponse.setHost(host.getIpAddress());
		} catch (ClientProtocolException e) {
			logger.error(e.getClass().getCanonicalName() +" "+e.getMessage()+" "+e.getLocalizedMessage());
			ResponseError responseError = new ResponseError();
			responseError.setMessageKey(e.getClass().getCanonicalName());
			responseError.setMessage(e.getMessage());
			workerResponse.setWorkerError(responseError);
		} catch (IOException e) {
			logger.error(e.getClass() +" "+e.getMessage()+" "+e.getLocalizedMessage());
			if(e instanceof HttpResponseException) {
				logger.error("Failed to get response for: "+host.getIpAddress()+", "+host.getPort()+", "+host.getContext());
			}
			if(e instanceof HttpHostConnectException) {
				logger.error("Failed to connect to host: "+host.getIpAddress()+", "+host.getPort());
			}
			ResponseError responseError = new ResponseError();
			responseError.setMessageKey(e.getClass().getCanonicalName());
			responseError.setMessage(e.getMessage());
			workerResponse.setWorkerError(responseError);
		}
		// shutdown the client
		httpclient.getConnectionManager().shutdown();
		return workerResponse;
	}
	/**
	 * Creates the target url to execute by the http client
	 * @param host the host with all info about the target, ie ipaddress, port, context
	 * @param parameters the control parameters, added as url parameters 
	 * @return the target url to execute by the http client
	 */
	public static String createTargetUrl(HostType host, String parameters) {
		Integer port = host.getPort();
		String portPart = "";
		String targetHost = "";
		if (port!=null && port > 0) {
			portPart = ":"+port;
		}
		targetHost = "http://"+ host.getIpAddress() + portPart;
		String targetContext = "/"+host.getContext() + "?" + parameters;
		return targetHost + targetContext;
	}
}
