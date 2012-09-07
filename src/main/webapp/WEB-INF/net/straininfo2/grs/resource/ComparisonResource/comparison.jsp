<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head><title>Comparison page</title></head>
<body>
	<h1>Megx data</h1>
	<p><c:forEach var="item" items="${it[0]}">${item.key}: ${item.value}<br /></c:forEach></p>
	<h1>NCBI data</h1>
	<p><c:forEach var="item" items="${it[1]}">${item.key}: ${item.value}<br /></c:forEach></p>
</body>
</html>