package se.avegagroup.clustercontrol.data;

import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JkStatus {

            public static final String KEY_BALANCEWORKERS = "worker.lbfootprint.balance_workers";
            public static final String KEY_MEMBERCOUNT = "worker.lbfootprint.member_count";
            public static final String KEY_RESULTTYPE = "worker.result.type";
            public static final String KEY_RESULTMESSAGE = "worker.result.message";
            private HashMap<String, String> status;
            private static final Log logger = LogFactory.getLog(JkStatus.class);
            private List<Server> servers;

            public JkStatus(String body) {
            	status = init(body);
            }

            public boolean isResultOk() {
            	String result = (String)status.get("worker.result.type");
            	return result != null && result.equals("OK");
            }

            private static HashMap<String, String> init(String body) {
            	HashMap<String, String> hm = new HashMap<String, String>();
            	for (StringTokenizer st = new StringTokenizer(body, "\n"); st.hasMoreElements();) {
            		String inputLine = (String)st.nextElement();
            		String keyValue[] = inputLine.split("=");
            		String key = keyValue[0];
            		String value = "";
            		if (keyValue.length == 2) {
            			value = keyValue[1];
                    } else {
                    	logger.debug((new StringBuilder()).append("Value for key '").append(keyValue[0]).append("' not found").toString());
                    }
            		String storedValue = (String)hm.get(key);
					if (storedValue != null) {
						hm.put(key, (new StringBuilder()).append(storedValue).append(",").append(value).toString());
                    } else {
                    	hm.put(key, value);
                    }
                }

            	return hm;
            }

            public String getBalanceWorkers() {
            	String result = (String)status.get("worker.lbfootprint.balance_workers");
				return result;
            }

            public int getMemberCount() {
            	String result = (String)status.get("worker.lbfootprint.member_count");
				return Integer.parseInt(result);
            }

            public String getWorkerStatus(String worker) {
            	String result = (String)status.get((new StringBuilder()).append("worker.").append(worker).append(".state").toString());
				return result;
            }

            public String getWorkerActivation(String worker) {
            	String result = (String)status.get((new StringBuilder()).append("worker.").append(worker).append(".activation").toString());
            	return result;
            }

            public void setServers(List<Server> servers) {
            	this.servers = servers;
            }

            public List<Server> getServers() {
            	return servers;
            }

}
