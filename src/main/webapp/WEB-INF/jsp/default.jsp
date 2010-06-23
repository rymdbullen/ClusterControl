<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="context" scope="page" value="${pageContext.request.contextPath}"/>
<stripes:layout-definition>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<meta http-equiv="Content-Language" content="en"/>
		<script type='text/javascript' src='/dwr/interface/JkController.js'></script>
		<script type='text/javascript' src='/dwr/engine.js'></script>
		<script type='text/javascript' src='/dwr/util.js'></script>
	</head>
	</body>
	<div id="main">
	<stripes:layout-component name="body"/>
	</div>
	<div id="footer">
	<stripes:layout-render name="/WEB-INF/jsp/fragment/footer.jsp" />
	</div>
	</body>
</html>
</stripes:layout-definition>
