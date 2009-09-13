<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/layout.jsp" title="ClusterControl">
	<s:layout-component name="body">
		<script>
    	function init() {
    	  dwr.util.useLoadingMessage();
    	  JkController.isInitialized('workerName', functionRenderInit);
    	}
        function activate() {
      	  	var loadBalancer = dwr.util.getValue("loadBalancer");
      	  	var workerName = dwr.util.getValue("workerName");
      	  	JkController.activate(loadBalancer, workerName, functionRenderStatus);
      	}
        function disable() {
        	var loadBalancer = dwr.util.getValue("loadBalancer");
        	var workerName = dwr.util.getValue("workerName");
      		JkController.disable(loadBalancer, workerName, functionRenderStatus);
      	}
        function functionRenderInit(hosts) {
        	if(undefined == hosts) {
	        	dwr.util.setValue("demoReply", "not initialized");
	        	disableControls();
	        	return;
        	}
        	enableControls();
        	var host;
        	var output = "";
        	for (var hostsIdx = 0; hostsIdx < hosts.length; hostsIdx++) {
  	      		host = hosts[hostsIdx];
  	      		output = output + ", " + host.ipAddress;
        	}
	        dwr.util.setValue("demoReply", "Init OK"+output);
        }
        function enableControls() {
        	toggleControls(false);
        }
        function disableControls() {
        	toggleControls(true);
        }
        function toggleControls(enableDisable) {
        	$("btnStatusComplex").disabled = enableDisable;
        	$("btnActivate").disabled = enableDisable;
        	$("btnDisable").disabled = enableDisable;
        }
        function functionRenderStatus(balancers) {
        	// Delete all the rows except for the "pattern" row
            dwr.util.removeAllRows("statusbody", { filter:function(tr) {
              return (tr.id != "pattern");
            }});
           	
            var balancer, members, member, id;
                  
        	for (var balancerIdx = 0; balancerIdx < balancers.length; balancerIdx++) {
        		balancer = balancers[balancerIdx];
        		members = balancer.member;
	            //dwr.util.cloneNode("pattern", { idSuffix:id });
        		
	        	for (var memberIdx = 0; memberIdx < members.length; memberIdx++) {
	            	member = members[memberIdx];
		            id = member.name;
		            dwr.util.cloneNode("pattern", { idSuffix:id });
	              	dwr.util.setValue("columnHost" + id, member.host);
	              	dwr.util.setValue("columnWorker" + id, member.name);
	              	dwr.util.setValue("columnState" + id, member.state);
	              	dwr.util.setValue("columnActivation" + id, member.activation);
	              	if(member.activation == "ACT" ) {
		              	$("columnActivation" + id).style.backgroundColor = "green";
	              		//$("btnActivation" + id).disabled = true;
	              		//$("btnDisabled" + id).disabled = false;
	              	} else if(member.activation != "ACT" ) {
	              		//$("btnActivation" + id).disabled = false;
	              		//$("btnDisabled" + id).disabled = true;
	              		$("columnActivation" + id).style.backgroundColor = "red";
	              	}
	              	$("pattern" + id).style.display = "table-row";
	        	}
	        }
        	dwr.util.setValue("demoReply", "Init OK");
        	enableControls();
        }
        function getStatusComplex() {
      	  	var name = dwr.util.getValue("demoName");
      	  	JkController.getStatusComplex(name, functionRenderStatus);
      	}
        function setHostname() {
      	  	var url = dwr.util.getValue("hostname");
      	  	JkController.setUrl(url, functionRenderStatus);
      	}
    </script>
		<h1>JK Status</h1>
		<p>Hostname: <input type="text" id="hostname" size="50" /><input id="btnSetHostname" value="Initialize" type="button" onclick="setHostname()" /> <br />
		<br />
		<br />
		<input id="btnStatusComplex" value="StatusComplex" type="button" onclick="getStatusComplex()" disabled="disabled" /> <br />
		Reply: <span id="demoReply"></span> <br />
		Status: <span id="demoStatus"></span>
		</p>
		<table border="1" class="rowed grey">
			<thead>
				<tr>
					<th>Host</th>
					<th>Worker</th>
					<th>State</th>
					<th>Activation</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody id="statusbody">
				<tr id="pattern" style="display: none;">
					<td><span id="columnHost">Host</span></td>
					<td><span id="columnWorker">Worker</span></td>
					<td><span id="columnState">State</span></td>
					<td><span id="columnActivation">Activation</span></td>
					<td><input id="btnDisable" type="button" value="Disable" onclick="disableClicked(this.id)" disabled="disabled" /> <input id="btnActivate" type="button" value="Activate" onclick="activateClicked(this.id)" disabled="disabled" />
					</td>
				</tr>
			</tbody>
		</table>
		<p>
		<h3>TODO</h3>
		<li>
			<ul>1. Quartz or javascript timer, how to push...</ul>
			<ul>2. Visual Enhancement</ul>
		</li>
		<h3>BUGS</h3>
		<li>
			<ul>Handle different jk versions</ul>
		</li>
		</p>
	</s:layout-component>
</s:layout-render>
