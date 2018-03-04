<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meals</title>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>
<table border="1">
    <tr>
        <th>DateTime</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <c:forEach var="num" items="${meals}">
        <tr bgcolor="${num.isExceed() ? 'red' : 'green'}">
            <td><c:out value="${num.getDateTime()}"/></td>
            <td><c:out value="${num.getDescription()}"/></td>
            <td><c:out value="${num.getCalories()}"/></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>