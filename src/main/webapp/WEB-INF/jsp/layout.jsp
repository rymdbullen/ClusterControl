<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<s:layout-definition>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title>${title}</title>
    <link rel="stylesheet" type="text/css" href="${contextPath}/css/style.css">
    <s:layout-component name="head">
    </s:layout-component>
    <script type='text/javascript' src='/clustercontrol/dwr/interface/JkController.js'></script>
    <script type='text/javascript' src='/clustercontrol/dwr/engine.js'></script>
    <script type='text/javascript' src='/clustercontrol/dwr/util.js'></script>
  </head>
  <body onload="javascript:init()">
    <div id="main">
      <s:layout-component name="body">
      </s:layout-component>
    </div>
  </body>
</html>

</s:layout-definition>