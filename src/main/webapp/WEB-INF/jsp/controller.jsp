<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/layout.jsp" title="ClusterControl">
	<s:layout-component name="body">
		<!-- Combo-handled YUI CSS files: ->
		<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/combo?2.8.0r4/build/container/assets/skins/sam/container.css">
		<!- Combo-handled YUI JS files: ->
		<script type="text/javascript" src="http://yui.yahooapis.com/combo?2.8.0r4/build/yahoo-dom-event/yahoo-dom-event.js&2.8.0r4/build/animation/animation-min.js&2.8.0r4/build/container/container-min.js"></script>
		-->
		<script type="text/javascript">
		<!--
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
		-->
		</script>
		<script type="text/javascript"><!--
    	function init() {
    	  dwr.util.useLoadingMessage();
    	  JkController.isInitialized('workerName', renderInit);
    	}
        function activate(workerName) {
      	  	var loadBalancer = "lbfootprint";
      	  	JkController.activate(loadBalancer, workerName, renderStatus);
      	}
        function disable(workerName) {
      	  	var loadBalancer = "lbfootprint";
      		JkController.disable(loadBalancer, workerName, renderStatus);
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
        function renderInit(initStatus) {
        	if(undefined == initStatus) {
	        	dwr.util.setValue("demoReply", "not initialized");
	        	disableControls();
	        	return;
        	}
        	enableControls();
	        dwr.util.setValue("demoReply", initStatus);
        }
        function getStatusComplex() {
      	  	var name = dwr.util.getValue("demoReply");
      	  	JkController.getStatusComplex(name, renderStatus);
      	}
        function initWithUrlNew() {
      	  	var url = dwr.util.getValue("hostname");
      	  	JkController.initWithUrl(url, {
      	  		callback:function(balancers) { renderStatus(balancers); },
      	  		timeout:5000,
      	  		errorHandler:function(message) { alert("Oops: Could not initialize: "+ url + " : " + message); }		
      	  	});
      	}
        function initWithUrl() {
      	  	var url = dwr.util.getValue("hostname");
      	  	JkController.initWithUrl(url, {
      	  		callback:function(balancers) { renderStatus(balancers); },
      	  		timeout:5000,
      	  		errorHandler:function(message) { alert("Oops: Could not initialize: "+ url + " : " + message); }		
      	  	});
      	}
        function disableClicked(eleid) {
        	  // we were an id of the form "edit{id}", eg "edit42". We lookup the "42"
        	  var person = eleid.substring(21);
        	  disable(person);
        }
        function activateClicked(eleid) {
        	  // we were an id of the form "edit{id}", eg "edit42". We lookup the "42"
        	  var person = eleid.substring(21);
        	  activate(person);
        }
        function renderStatus(jkStatuses) {
        	// Delete all the rows except for the "pattern" row
            dwr.util.removeAllRows("statusbody", { filter:function(tr) {
              return (tr.id != "pattern");
            }});

            var members, member, id;
        	for (var jkStatusIdx = 0; jkStatusIdx < jkStatuses.length; jkStatusIdx++) {
        		jkStatus = jkStatuses[jkStatusIdx];
        		members = jkStatus.balancers.balancer.member;
//alert('jkStatusIdx='+jkStatusIdx+' '+jkStatus.balancers.balancer.name);
	            id = jkStatus.server.name;
	            dwr.util.cloneNode("pattern", { idSuffix:id });
              	dwr.util.setValue("columnHost" + id, jkStatus.server.name);
              	$("pattern" + id).style.display = "table-row";
	        	for (var memberIdx = 0; memberIdx < members.length; memberIdx++) {
	            	member = members[memberIdx];
		            var thisId = member.name;
	              	dwr.util.cloneNode("columnWorker" + id, { idSuffix:thisId });
	              	dwr.util.setValue("columnWorker" + id + thisId, member.activation);
	              	//
	              	// header
	              	if(jkStatusIdx == 1) {
	              		dwr.util.cloneNode("headerWorker", { idSuffix:thisId });
	              		dwr.util.setValue("headerWorker" + thisId, member.name);
	              		$("headerWorker" + thisId).style.display = "block";
	              	}
	              	if(member.activation == "ACT" ) {
		              	$("columnWorker" + id + thisId).style.backgroundColor = "green";
	              		//$("btnAct" + id + thisId).disabled = true;
	              		//$("btnDis" + id + thisId).disabled = false;
	              	} else if(member.activation != "ACT" ) {
	              		$("columnWorker" + id + thisId).style.backgroundColor = "red";
	              		//$("btnAct" + id + thisId).disabled = false;
	              		//$("btnDis" + id + thisId).disabled = true;
	              	}
	              	$("columnWorker" + id + thisId).style.display = "block";
//alert('memberIdx='+memberIdx);
	        	}
	        }
        	dwr.util.setValue("demoReply", "Init OK");
        	enableControls();
        }
    --></script>
		<h1>JK Status</h1>
		<p>
			Hostname: <input type="text" id="hostname" size="50" /><input id="btnInitWithUrl" value="Initialize" type="button" onclick="initWithUrl()" /> <br />
			<br />
			Status: <span id="demoReply"></span>&#160;&#160;&#160;<input id="btnStatusComplex" value="Get Status" type="button" onclick="getStatusComplex()" disabled="disabled" />
			<span class="header">AutoRefresh</span>
			On&nbsp;<input type="radio" name="autorefresh" value="on"  onclick="javascript:convertToGetAndRelocate(this.value);" />
			Off&nbsp;<input type="radio" name="autorefresh" value="off"  onclick="javascript:convertToGetAndRelocate(this.value);" />
			Interval&nbsp;<input type="text" name="refreshinterval" value="30" size="3" maxlength="3" />
		</p>
		<p>
		</p>
		Status: <span id="newStatus"></span>
		<table border="1" class="rowed grey">
			<thead>
				<tr>
					<th>Host</th>
					<th id="headerWorker" style="display: none;">Workers</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody id="statusbody">
				<tr id="pattern" style="display: none;">
					<!-- td><span id="columnHost">Host</span></td -->
					<!-- td><span class="worker" id="columnWorker"></span></td -->
					<td id="columnHost">Host</td>
					<td id="columnWorker" style="display: none;">Worker</td>
					<td>
						<input id="btnDis" type="button" value="Disable" onclick="disableClicked(this.id)" disabled="disabled" /> <input id="btnAct" type="button" value="Activate" onclick="activateClicked(this.id)" disabled="disabled" />
					</td>
				</tr>
			</tbody>
		</table>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
		<!-- 		
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

					</div>  --><!-- /workers -->

		<br/>
		<br/>
		<h3>TODO</h3>
		<ul>
			<li>1. Quartz or javascript timer, how to push...</li>
			<li>2. Handle more than one host: backend (Done) and frontend (ongoing), ie tables</li>
			<li>3. Ask tomcat manager for contexts</li>
			<li>4. Visual Enhancement</li>
			<li>Show current sessions, etc etc</li>
			<li>Handle different jk versions</li>
		</ul>
	</s:layout-component>
</s:layout-render>
