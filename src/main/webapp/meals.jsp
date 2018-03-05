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
    <c:forEach var="meal" items="${meals}">
        <tr bgcolor="${meal.isExceed() ? 'red' : 'green'}">
            <td><c:out value="${meal.getDateTime().toLocalDate().toString()}"/>
                <c:out value="${meal.getDateTime().toLocalTime().toString()}"/></td>
            <td><c:out value="${meal.getDescription()}"/></td>
            <td><c:out value="${meal.getCalories()}"/></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>