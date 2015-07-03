<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>
<html>
<head>
	<title>Person Page</title>
	<style type="text/css">
		.tg  {border-collapse:collapse;border-spacing:0;border-color:#ccc;}
		.tg td{font-family:Arial, sans-serif;font-size:14px;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#fff;}
		.tg th{font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#f0f0f0;}
		.tg .tg-4eph{background-color:#f9f9f9}
	</style>
</head>
<body>
<h1>
	Cache Utility
</h1>

<c:url var="getCacheCount" value="/cache-service" ></c:url>

<form:form action="${getCacheCount}" commandName="cache">
<table>
	<c:if test="${!empty cacheCount}">
	<tr>
		<td>
			<td>${cacheCount}</td>
		</td>
		
	</tr>
	</c:if>
	
	<tr>
		<td colspan="2">
			 <td><a href="<c:url value='/clearAll' />" >Clear Cache</a></td>
		</td>
	</tr>
</table>	
</form:form>
<br>

</body>
</html>
