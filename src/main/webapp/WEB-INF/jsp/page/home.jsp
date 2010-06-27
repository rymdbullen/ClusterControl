<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<stripes:layout-render name="/WEB-INF/jsp/layout/layout.jsp" title="ClusterControl - Home - Uninitialized">
  <stripes:layout-component name="body">
    <fieldset>
      <legend>Settings</legend>
        <label>Target URL: <input type="text" id="hostname" size="50" /><input id="btnInitWithUrl" value="Initialize" type="button" onclick="initWithUrl()" /></label>
    </fieldset>
  </stripes:layout-component>
</stripes:layout-render>
