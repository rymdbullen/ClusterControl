<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/layout.jsp" title="ClusterControl">
	<s:layout-component name="body">
		<script>
    	function init() {
    	  dwr.util.useLoadingMessage();
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
	              	} else if(member.activation != "ACT" ) {
	              		$("columnActivation" + id).style.backgroundColor = "red";
	              	}
	              	$("pattern" + id).style.display = "table-row";
	        	}
	        }
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
		<p>Hostname: <input type="text" id="hostname" size="50" /><input
			value="Set Hostname" type="button" onclick="setHostname()" /> <br />
		<br />
		loadbalancer, workername <input type="text" id="loadBalancer" /><input
			type="text" id="workerName" /> <input value="Activate" type="button"
			onclick="activate()" /> <input value="Disable" type="button"
			onclick="disable()" /> <br />
		<br />
		<input value="StatusComplex" type="button"
			onclick="getStatusComplex()" /> <br />
		Reply: <span id="demoReply"></span> <br />
		Status: <span id="demoStatus"></span></p>
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
					<td><input id="disable" type="button" value="Disable"
						onclick="disableClicked(this.id)" /> <input id="activate"
						type="button" value="Activate" onclick="activateClicked(this.id)" />
					</td>
				</tr>
			</tbody>
		</table>
		<p>
		<p>
		<h3>TODO</h3>
		<li>
			<ul>1. Quartz?</ul>
			<ul>2. Visual Enhancement</ul>
		</li>
		</p>
	</s:layout-component>
</s:layout-render>
