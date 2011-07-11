<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<stripes:layout-definition>
		<h3>THOUGHTS</h3>
		<ul>
			<li>first view: init url or directly to status page</li>
			<li>second view: status page with controls</li>
			<li>third view: control command sent, updates the rendered view</li>
			<li>fourth view: asynch ajax updates the current already rendered view</li>
			<li>Consider using rmock or mockit for offline builds</li>
		</ul>
		<h3>TODO</h3>
		<ul>
			<li>Error Handling, what if one worker doesn't answer</li>
			<li>Handle activation / deactivation algorithm</li>
			<li>Task: Quartz or javascript timer, how to push...</li>
			<li>Task: Handle different jk versions</li>
			<li>Bug: failure to write heading for one host</li>
			<li>Visual Enhancements, ongoing</li>
		</ul>
		<h3>Done</h3>
		<ul>
			<li>Handle more than one host: backend (Done) and frontend (ongoing), ie tables</li>
			<li>Autorefresh</li>
		</ul>
		<h3>Deferred</h3>
		<ul>
			<li>Ask tomcat manager for contexts</li>
			<li>Show current sessions, etc etc</li>
		</ul>
</stripes:layout-definition>