<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Comparison page</title>
        <link rel="stylesheet" href="/style/bootstrap.css"></link>
    </head>
    <body>
        <h1>GRS Provider information on Bioproject ${it[1]["id"]}</h1>
        <div class="container">
            <div class="row">
            <%-- TODO: make this a search where you can also enter straininfo culture IDs or a project name! --%>
                <form class="form-search" action="search" method="get">
                    <input name="id" class="form-search" type="search" placeholder="Bioproject ID">
                    <button type="submit" class="btn">Go</button>
                </fieldset>
                </form>
            </div>
            <div class="row">
                <table class="table">
                <tr><th>Label</th><c:forEach var="name" items="${it[0].providerNames}"><th>${name}</th></c:forEach></tr>

                <c:forEach var="key" items="${it[0].labelsMatching}">
                <tr class="success"><td>${key}</td>
                <c:forEach var="value" items="${it[0].data[key]}"><td>${value}</td></c:forEach>
                </tr>
                </c:forEach>

                <c:forEach var="key" items="${it[0].labelsWithConflicts}">
                <tr class="error"><td>${key}</td>
                <c:forEach var="value" items="${it[0].data[key]}"><td>${value}</td></c:forEach>
                </tr>
                </c:forEach>

                <c:forEach var="key" items="${it[0].labelsUniques}">
                <tr class="info"><td>${key}</td>
                <c:forEach var="value" items="${it[0].data[key]}"><td>${value}</td></c:forEach>
                </tr>
                </c:forEach>
            </div>
        </div>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
        <script src="/js/bootstrap.js"></script>
        <script src="/js/comparison.js"></script>
    </body>
</html>
