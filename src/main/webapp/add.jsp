<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h2> Create meal</h2>

<form action="meals" method="post">

    <input type="hidden" name="id" value="${meal.getId()}">

    Description: <input type="text" name="description" value="${meal.getDescription()}">
    <br>
    <br>

    Calories: <input type="text" name="calories" value="${meal.getCalories()}">
    <br>
    <br>
    <input type="submit">

</form>
</body>
</html>
