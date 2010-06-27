<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<stripes:layout-definition>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title>${title}</title>
    <link rel="stylesheet" type="text/css" href="${contextPath}/css/style.css">
    <script type='text/javascript' src='${contextPath}/dwr/interface/JkController.js'></script>
    <script type='text/javascript' src='${contextPath}/dwr/engine.js'></script>
    <script type='text/javascript' src='${contextPath}/dwr/util.js'></script>
  </head>
  <body>
    <div id="header">
      <stripes:layout-render name="/WEB-INF/jsp/fragment/header.jsp" />
    </div>
    <div id="content">
      <stripes:layout-component name="body" />
    </div>
    <div id="footer">
      <stripes:layout-render name="/WEB-INF/jsp/fragment/footer.jsp" />
    </div>
  </body>
</html>
</stripes:layout-definition>