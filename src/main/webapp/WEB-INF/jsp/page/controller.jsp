<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>

<stripes:layout-render name="/WEB-INF/jsp/layout/layout.jsp" title="ClusterControl">
	<stripes:layout-component name="body">
		<script type="text/javascript"><!--
		var interval = gup("autorefresh");
		function refreshPeriodic() 
		{ 
			// Reload the page every x seconds 
			location.reload(); 
			timerID = setTimeout("refreshPeriodic()", interval*1000); 
		}
		function convertToGetAndRelocate( actionValue ) 
		{
			if(actionValue=="off") {
				window.location.replace(window.location.protocol + '//' +window.location.host + window.location.pathname);
				return;
			}
			var url = window.location+'';
			if(url.indexOf('autorefresh')!=-1) {
				url = window.location.protocol + '//' +window.location.host + window.location.pathname +'?';
			}
			interval = dwr.util.getValue("autorefresh");
			url = url + '?autorefresh=' + dwr.util.getValue("autorefresh");
			window.location.replace(url);
		}
		function initiate( actionValue ) 
		{
			if(actionValue=="disable") {
				if(!confirmAction("disable all", "workers")) return;
				JkController.disableAll(actionValue, renderStatus);
			} else if(actionValue=="slow" || actionValue=="medium" || actionValue=="aggressive") {
				if(!confirmAction(actionValue + " activate all", "workers")) return;
				JkController.activateAll(actionValue, renderStatus);
			}
		}
		function gup( name )
		{
			name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
			var regexS = "[\\?&]"+name+"=([^&#]*)";
			var regex = new RegExp( regexS );
			var results = regex.exec( window.location.href );
			if( results == null )
				return "";
			else
				return results[1];
		}
    	function atload() 
    	{
			var url = window.location.search;
        	var ctrlautorefresh = document.getElementById('ctrlautorefresh');
//alert('hej: '+ctrlautorefresh.size+' '+ctrlautorefresh.checked);
			if(url.indexOf('autorefresh')>-1) {
            	timerID = setTimeout("refreshPeriodic()", interval*1000);
            	//ctrlautorefresh[0].checked = true;
				//ctrlautorefresh[1].checked = false;
			} else {
				//ctrlautorefresh[0].checked = false;
				//ctrlautorefresh[1].checked = true;
			}
			dwr.util.useLoadingMessage();
			JkController.isInitialized('workerName', renderInit);
    	}
    	function confirmAction(action, workerName) 
    	{
    		var answer = confirm ("Do you want to " + action + " " + workerName + "?")
    		if (answer) {
    			return true;
    		}
    		return false;
    	}
        function activate(eleid) 
        {
        	// we were an id of the form "edit{id}", eg "edit42". We lookup the "42"
        	var workerName = eleid.substring(6);
        	// do you want to activate this worker?
        	if(!confirmAction("activate", workerName)) return;
            
      	  	JkController.activate(workerName, renderStatus);
      	}
        function disable(eleid)
        {
        	// we were an id of the form "edit{id}", eg "edit42". We lookup the "42"
        	var workerName = eleid.substring(6);
        	// do you want to activate this worker?
        	if(!confirmAction("disable", workerName)) return;
        	
      		JkController.disable(workerName, renderStatus);
      	}
        function renderInit(initStatus) 
        {
        	if(undefined == initStatus) {
	        	dwr.util.setValue("demoReply", "not initialized");
	        	return;
        	}
        	getStatusComplex();
	        dwr.util.setValue("demoReply", initStatus);
        }
        function getStatusComplex() 
        {
      	  	var name = dwr.util.getValue("demoReply");
      	  	JkController.getStatusComplex(name, renderStatus);
      	}
        function initWithUrl() 
        {
      	  	var url = dwr.util.getValue("hostname");
      	  	JkController.initWithUrl(url, {
      	  		callback:function(balancers) { renderStatus(balancers); },
      	  		timeout:5000,
      	  		errorHandler:function(message) { alert("Oops: Could not initialize: "+ url + " : " + message); }		
      	  	});
      	}
        function renderStatus(jkStatuses) 
        {
            // enable table and controls
        	if(jkStatuses != undefined && jkStatuses.length>0) {
        		$("statustable").style.display = "table";
        		$("autorefreshtext").style.display = "block";
            }
            
        	// Delete all the rows except for the "pattern" or "controlpattern" row
            dwr.util.removeAllRows("statusbody", { filter:function(tr) {
                var retval = true;
                if (tr.id != "pattern") {
                    retval = false;
                } else if (tr.id != "controlpattern") {
                    retval = false;
                }
            	return retval;
            }});

            var members, member, id;
        	for (var jkStatusIdx = 0; jkStatusIdx < jkStatuses.length; jkStatusIdx++) {
        		jkStatus = jkStatuses[jkStatusIdx];
        		members = jkStatus.balancers.balancer.member;
	            id = jkStatus.server.name;
	            dwr.util.cloneNode("pattern", { idSuffix:id });
              	dwr.util.setValue("columnHost" + id, jkStatus.server.name);
              	$("pattern" + id).style.display = "table-row";
	        	for (var memberIdx = 0; memberIdx < members.length; memberIdx++) {
	            	member = members[memberIdx];
		            var thisId = member.name;
	              	dwr.util.cloneNode("columnWorker" + id, { idSuffix:thisId });
	              	dwr.util.setValue("columnWorker" + id + thisId, member.activation + " - " + member.state);
	              	dwr.util.cloneNode("cpcolumnWorker", { idSuffix:thisId });
	              	//
	              	// header
	              	if(jkStatusIdx == 1) {
	              		dwr.util.cloneNode("headerWorker", { idSuffix:thisId });
	              		dwr.util.setValue("headerWorker" + thisId, member.name);
	              		$("headerWorker" + thisId).style.display = "table-cell";
	              	}
	              	//
	              	// set color and enable controls
	              	if(member.activation == "ACT" ) {
		              	$("columnWorker" + id + thisId).style.backgroundColor = "green";
		              	$("btnAct" + thisId).disabled = true;
	              		$("btnDis" + thisId).disabled = false;
	              	} else if(member.activation != "ACT" ) {
	              		$("columnWorker" + id + thisId).style.backgroundColor = "red";
	              		$("btnAct" + thisId).disabled = false;
	              		$("btnDis" + thisId).disabled = true;
	              	}
	              	$("columnWorker" + id + thisId).style.display = "table-cell";
	              	$("cpcolumnWorker" + thisId).style.display = "table-cell";
	        	}
	        }
        }
        //
        // runs at body onload
        window.onload=atload;
    --></script>
		<div id="autorefreshtext" style="display: none;">
			<span>AutoRefresh</span>
			On&nbsp;<input type="radio" id="ctrlautorefresh" value="on"  onclick="convertToGetAndRelocate(this.value);" />
			Off&nbsp;<input type="radio" id="ctrlautorefresh" value="off"  onclick="convertToGetAndRelocate(this.value);" />
		</div>
		<table id="statustable" border="1" class="rowed grey" style="display: none;">
			<thead>
				<tr>
					<th>Host</th>
					<th id="headerWorker" style="display: none;">Workers</th>
				</tr>
			</thead>
			<tbody id="statusbody">
				<tr id="pattern" style="display: none;">
					<td id="columnHost">Host</td>
					<td id="columnWorker" style="display: none;">Worker</td>
				</tr>
				<tr id="controlpattern">
					<td id="columnHost">Controls</td>
					<td id="cpcolumnWorker" style="display: none;">
						<input id="btnDis" type="button" value="Disable" onclick="disable(this.id)" disabled="disabled" /> <input id="btnAct" type="button" value="Activate" onclick="activate(this.id)" disabled="disabled" />
					</td>
				</tr>
			</tbody>
		</table>
		<p>
			Status: <span id="demoReply"></span>&#160;&#160;&#160;<input id="btnStatusComplex" value="Refresh" type="button" onclick="getStatusComplex()" disabled="disabled" title="Update Status table"/>
		</p>
		<fieldset>
			<legend>Actions</legend>
			<p>
				<label>Activate: <input type="radio" name="enablerate" value="slow" title="Slow Activation" onclick="initiate(this.value)" />(S)low
				                 <input type="radio" name="enablerate" value="medium" title="Medium Activation" onclick="initiate(this.value)" />(M)edium
				                 <input type="radio" name="enablerate" value="aggressive" title="Fast Activation" onclick="initiate(this.value)" />(A)ggressive
				                 <input type="radio" name="enablerate" value="custom" title="Custom Activation" onclick="initiate(this.value)" />(C)ustom
								&nbsp;&nbsp;&nbsp;Disable: <input type="radio" name="enablerate" value="disable" title="Deactivate" onclick="initiate(this.value)" />
								<!-- input id="btnInitiate" value="Initiate" type="button" onclick="initiate(this.value)" disabled="disabled" title="Initiate Activation/Deactivation"/ --></label>
			</p>
		</fieldset>
		<fieldset>
			<legend>Settings</legend>
			<p>
				<label>Target URL: <input type="text" id="hostname" size="50" value="${actionBean.initializedUrl}" /><input id="btnInitWithUrl" value="Initialize" type="button" onclick="initWithUrl()" /></label>
			</p>
			<p>
				<label>Autorefresh Interval: <input id="autorefresh" type="text" value="15" size="2" />s</label>
			</p>
		</fieldset>
	</stripes:layout-component>
</stripes:layout-render>
