<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>

<stripes:layout-render name="/WEB-INF/jsp/layout.jsp" title="ClusterControl">
	<stripes:layout-component name="body">
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
				<label>Target URL: <input type="text" id="hostname" size="50" /><input id="btnInitWithUrl" value="Initialize" type="button" onclick="initWithUrl()" /></label>
			</p>
			<p>
				<label>Autorefresh Interval: <input id="autorefresh" type="text" value="15" size="2" />s</label>
			</p>
		</fieldset>
	</stripes:layout-component>
</stripes:layout-render>
