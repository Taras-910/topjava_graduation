<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<head>
    <title>Dishes</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<section>
    <h3><a href="index.jsp">Home</a></h3>
    <hr/>
    <h2>Menus</h2>
    <%--<form method="get" action="meals">
        <input type="hidden" name="action" value="filter">
        <dl>
            <dt>From Date (inclusive):</dt>
            <dd><input type="date" name="startDate" value="${param.startDate}"></dd>
        </dl>
        <dl>
            <dt>To Date (inclusive):</dt>
            <dd><input type="date" name="endDate" value="${param.endDate}"></dd>
        </dl>
        <dl>
            <dt>From Time (inclusive):</dt>
            <dd><input type="time" name="startTime" value="${param.startTime}"></dd>
        </dl>
        <dl>
            <dt>To Time (exclusive):</dt>
            <dd><input type="time" name="endTime" value="${param.endTime}"></dd>
        </dl>
        <button type="submit">Filter</button>
    </form>--%>
    <hr/>
    <a href="dishes?action=create">Add Menu</a>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Restaurant name</th>
            <th>Id</th>
            <th>DishName</th>
            <th>Price</th>
            <th></th>
            <th></th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${menus}" var="menu">
            <tr>
            <jsp:useBean id="menu" type="ru.javawebinar.topjava.to.Menu"/>
            <td>${menu.restaurant.name}</td>
            <c:forEach items="${dishes}" var="dish">
                <tr>
                        <jsp:useBean id="dish" type="ru.javawebinar.topjava.model.Dish"/>
                    <td>${dish.name}</td>
                    <td>${dish.price}</td>
                    <td><a href="menus?action=update&id=${dish.id}">Update</a></td>
                    <td><a href="menus?action=delete&id=${dish.id}">Delete</a></td>
                </tr>
            </c:forEach>
            <td>${menu.vote}</td>
            <td><a href="menus?action=update&id=${menu.restaurant.id}">Update</a></td>
            <td><a href="menus?action=delete&id=${menu.restaurant.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>
