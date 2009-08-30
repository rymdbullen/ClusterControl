<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<s:layout-render name="/WEB-INF/jsp/layout.jsp" title="Welcome">
  <s:layout-component name="body">
    <script>
    	function init() {
    	  dwr.util.useLoadingMessage();
    	}
        function getStatus() {
      	  	var name = ""; //dwr.util.getValue("demoName");
      	  	JkController.getStatus(name, function(data) {
      	    dwr.util.setValue("demoReply", data);
      	  });
      	}
        function activate() {
      	  	var loadBalancer = dwr.util.getValue("loadBalancer");
      	  	var workerName = dwr.util.getValue("workerName");
      	  	JkController.activate(loadBalancer, workerName, function(data) {
      	    dwr.util.setValue("demoReply", data);
      	  });
      	}
        function disable() {
        	var loadBalancer = dwr.util.getValue("loadBalancer");
        	var workerName = dwr.util.getValue("workerName");
      		JkController.disable(loadBalancer, workerName, function(data) {
      	    dwr.util.setValue("demoReply", data);
      	  });
      	}
        function setHostname() {
      	  	var hostname = dwr.util.getValue("hostname");
      	  	JkController.setHostname(hostname, function(data) {
      	    dwr.util.setValue("demoReply", data);
      	  });
      	}
    </script>
    <p>JK Status</p>
	<p>
	  Hostname:
	  <input type="text" id="hostname"/>
	  <br/>
	  loadbalancer, workername
	  <input type="text" id="loadBalancer"/><input type="text" id="workerName"/>
	  <br/>	  
	  <input value="Activate" type="button" onclick="activate()"/>
	  <br/>
	  <input value="Disable" type="button" onclick="disable()"/>
	  <br/>
	  <input value="Set Hostname" type="button" onclick="setHostname()"/>
	  <br/>
	  <input value="Status" type="button" onclick="getStatus()"/>
	  <br/>
	  Reply: <span id="demoReply"></span>
	</p>
  </s:layout-component>
</s:layout-render>
