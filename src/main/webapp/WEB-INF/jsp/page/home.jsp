<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<stripes:layout-render name="/WEB-INF/jsp/layout/layout.jsp" title="ClusterControl Uninitialized">
  <stripes:layout-component name="body">
    <p>Congratulations, you've set up a Stripes project!</p>
	<p>
		Status: <span id="demoReply"></span>&#160;&#160;&#160;<input id="btnStatusComplex" value="Refresh" type="button" onclick="getStatusComplex()" disabled="disabled" title="Update Status table"/>
	</p>
  </stripes:layout-component>
</stripes:layout-render>
