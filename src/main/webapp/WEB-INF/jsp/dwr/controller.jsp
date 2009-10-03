<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/layout.jsp" title="ClusterControl">
	<s:layout-component name="body">
		<script><!--
    	function init() {
    	  dwr.util.useLoadingMessage();
    	  JkController.isInitialized('workerName', functionRenderInit);
    	}
        function activate(workerName) {
      	  	//var loadBalancer = dwr.util.getValue("loadBalancer");
      	  	//var workerName = dwr.util.getValue("workerName");
      	  	var loadBalancer = "lbfootprint";
      	  	JkController.activate(loadBalancer, workerName, functionRenderStatus);
      	}
        function disable(workerName) {
      	  	//var loadBalancer = dwr.util.getValue("loadBalancer");
      	  	//var workerName = dwr.util.getValue("workerName");
      	  	var loadBalancer = "lbfootprint";
      		JkController.disable(loadBalancer, workerName, functionRenderStatus);
      	}
        function functionRenderInit(hosts) {
        	if(undefined == hosts) {
	        	dwr.util.setValue("demoReply", "not initialized");
	        	disableControls();
	        	return;
        	}
        	enableControls();
	        dwr.util.setValue("demoReply", ""+hosts);
        }
        function enableControls() {
        	toggleControls(false);
        }
        function disableControls() {
        	toggleControls(true);
        }
        function toggleControls(enableDisable) {
        	$("btnStatusComplex").disabled = enableDisable;
        	$("btnAct").disabled = enableDisable;
        	$("btnDis").disabled = enableDisable;
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
	              		$("btnAct" + id).disabled = true;
	              		$("btnDis" + id).disabled = false;
	              	} else if(member.activation != "ACT" ) {
	              		$("columnActivation" + id).style.backgroundColor = "red";
	              		$("btnAct" + id).disabled = false;
	              		$("btnDis" + id).disabled = true;
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
        function setUrl() {
      	  	var url = dwr.util.getValue("hostname");
      	  	JkController.setUrl(url, {
      	  		callback:function(balancers) { functionRenderStatus(balancers); },
      	  		timeout:5000,
      	  		errorHandler:function(message) { alert("Oops: " + message); }		
      	  	});
      	}
        function disableClicked(eleid) {
        	  // we were an id of the form "edit{id}", eg "edit42". We lookup the "42"
        	  var person = eleid.substring(6);
        	  disable(person);
        }
        function activateClicked(eleid) {
        	  // we were an id of the form "edit{id}", eg "edit42". We lookup the "42"
        	  var person = eleid.substring(6);
        	  activate(person);
        }
    --></script>
		<h1>JK Status</h1>
		<p>
			Hostname: <input type="text" id="hostname" size="50" /><input id="btnSetUrl" value="Initialize" type="button" onclick="setUrl()" /> <br />
			<br />
			<br />
			Status: <span id="demoReply"></span>&#160;&#160;&#160;<input id="btnStatusComplex" value="Get Status" type="button" onclick="getStatusComplex()" disabled="disabled" /> <br />
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
					<td><input id="btnDis" type="button" value="Disable" onclick="disableClicked(this.id)" disabled="disabled" /> <input id="btnAct" type="button" value="Activate" onclick="activateClicked(this.id)" disabled="disabled" />
					</td>
				</tr>
			</tbody>
		</table>
		<p>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
		<h3>TODO</h3>
		<li>
			<ul>1. Quartz or javascript timer, how to push...</ul>
			<ul>2. Handle more than one host: backend and frontend, ie tables</ul>
			<ul>3. Ask tomcat manager for contexts</ul>
			<ul>4. Visual Enhancement</ul>
			<ul>Show current sessions, etc etc</ul>
			<ul>Handle different jk versions</ul>
		</li>
		</p>
	</s:layout-component>
</s:layout-render>
