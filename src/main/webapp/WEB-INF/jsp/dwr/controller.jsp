<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<s:layout-render name="/WEB-INF/jsp/layout.jsp" title="Welcome">
  <s:layout-component name="body">
    <script>
    	function init() {
          alert("running init");
    	  dwr.util.useLoadingMessage();
    	}
        function getStatus() {
      	  	var name = ""; //dwr.util.getValue("demoName");
      	  	JkController.getStatus(name, function(data) {
      	    dwr.util.setValue("demoStatus", data);
      	  });
      	}
        function functionRenderStatus(members) {
            //alert(members);
            var member;
        	for (var i = 0; i < members.length; i++) {
            	member = members[i];           	
              	dwr.util.setValue("demoStatus", member.name);
        	}
        }
        function getStatusComplex() {
      	  	var name = dwr.util.getValue("demoName");
      	  	JkController.getStatusComplex(name, functionRenderStatus);
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
      	    dwr.util.setValue("demoStatus", data);
      	  });
      	}
    </script>
    <p>JK Status</p>
	<p>
	  Hostname:
	  <input type="text" id="hostname"/><input value="Set Hostname" type="button" onclick="setHostname()"/>
	  <br/>
	  <br/>
	  loadbalancer, workername
	  <input type="text" id="loadBalancer"/><input type="text" id="workerName"/>
	  <br/>	  
	  <input value="Activate" type="button" onclick="activate()"/>
	  <br/>
	  <input value="Disable" type="button" onclick="disable()"/>
	  <br/>
	  <br/>
	  <input value="Status" type="button" onclick="getStatus()"/>
	  <br/>
	  <br/>
	  <input value="StatusComplex" type="button" onclick="getStatusComplex()"/>
	  <br/>
	  Reply: <span id="demoReply"></span>
	  <br/>
	  Status: <span id="demoStatus"></span>
	</p>
  </s:layout-component>
</s:layout-render>
