<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/layout.jsp" title="ClusterControl">
	<s:layout-component name="body">
		<script type="text/javascript"> <!--
			function convertToGetAndRelocate(actionValue) {
				document.jkaggregator.autorefresh.value = actionValue;
				var interval = document.jkaggregator.refreshinterval.value;
				var autorefresh = actionValue;
				var url = window.location+'?';
				if(url.indexOf('autorefresh')!=-1) {
					url = window.location.protocol + '//' +window.location.host + window.location.pathname +'?';
				}
				url = url + 'autorefresh=' + autorefresh + '&';
				url = url + 'refreshinterval=' + interval;
				window.location.replace(url);
			}
		--></script>
		<script type="text/javascript"><!--
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
			<span class="header">Refresh</span>
			On&nbsp;<input type="radio" name="autorefresh" value="on"  onclick="javascript:convertToGetAndRelocate(this.value);" />
			Off&nbsp;<input type="radio" name="autorefresh" value="off"  onclick="javascript:convertToGetAndRelocate(this.value);" />
			Interval&nbsp;<input type="text" name="refreshinterval" value="30" size="3" maxlength="3" />
		</p>
		<p>
					<div class="workers">
						<span class="jkcontrollerheader">2. Intervals</span><br/>
						<br/><div class="jkcontrollersubheader">Enable Intervals</div>

								
						<input type="text" name="enableRateSlow0" value="10" size="2" maxlength="2" class="enableRate" disabled="true" />
						<input type="text" name="enableRateSlow1" value="90" size="2" maxlength="2" class="enableRate" disabled="true" />
						<input type="text" name="enableRateSlow2" value="60" size="2" maxlength="2" class="enableRate" disabled="true" />
						<input type="text" name="enableRateSlow3" value="60" size="2" maxlength="2" class="enableRate" disabled="true" />
								 : <input type="radio" name="enablerate" value="slow" onclick="javascript:setEnableRate('enable', this.value);" />
								<br/>
						<input type="text" name="enableRateFast0" value="10" size="2" maxlength="2" class="enableRate" disabled="true" />
						<input type="text" name="enableRateFast1" value="25" size="2" maxlength="2" class="enableRate" disabled="true" />

						<input type="text" name="enableRateFast2" value="20" size="2" maxlength="2" class="enableRate" disabled="true" />
						<input type="text" name="enableRateFast3" value="20" size="2" maxlength="2" class="enableRate" disabled="true" />
								 : <input type="radio" name="enablerate" value="fast" onclick="javascript:setEnableRate('enable', this.value);" />
								<br/>
						<input type="text" name="enableRateCustom0" value="10" size="2" maxlength="2" class="enableRate" />
						<input type="text" name="enableRateCustom1" value="25" size="2" maxlength="2" class="enableRate" />
						<input type="text" name="enableRateCustom2" value="20" size="2" maxlength="2" class="enableRate" />
						<input type="text" name="enableRateCustom3" value="20" size="2" maxlength="2" class="enableRate" />

								 : <input type="radio" name="enablerate" value="custom" onclick="javascript:setEnableRate('enable', this.value);" />
						<div class="jkcontrollersubheader">Disable interval</div>
						<input type="text" name="disableInterval0" value="3" size="2" maxlength="2" disabled="true" />
						<input type="text" name="disableInterval1" value="3" size="2" maxlength="2" disabled="true" />
						<input type="text" name="disableInterval2" value="3" size="2" maxlength="2" disabled="true" />
						<input type="text" name="disableInterval3" value="3" size="2" maxlength="2" disabled="true" />
								 : <input type="radio" name="enablerate" value="disable" onclick="javascript:setEnableRate('disable', '');" />

					</div> <!-- /workers -->
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
