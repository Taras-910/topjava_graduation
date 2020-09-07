<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<head>
    <title>Dishes</title>
    <link rel="stylesheet" href="../../css/style.css">
</head>
<body>
<section>
    <h3><a href="/topjava">Home</a></h3>
    <hr/>
    <h2>Menus</h2>
    <hr/>
    <a href="menus/create">Add Menu</a>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Restaurant name</th>
            <th hidden>Id</th>
            <th>
            <th1>DishName / </th1>
            <th1>Price</th1>
            </th>
            <th>like</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${menus}" var="menu">
            <tr>
                <jsp:useBean id="menu" type="ru.javawebinar.topjava.to.Menu"/>
                <td>${menu.restaurant.name}</td>
                <td hidden>${menu.restaurant.id}</td>
                <td>
                    <c:forEach items="${menu.restaurant.dishes}" var="dish">
                        <tr1>
                            <jsp:useBean id="dish" type="ru.javawebinar.topjava.model.Dish"/>
                            <td1>${dish.name}</td1>
                            /
                            <td1>${dish.price}</td1>
                            <td1><a href="menus?action=update&id=${dish.id}">Update</a></td1>
                            <td1><a href="menus?action=delete&id=${dish.id}">Delete</a></td1>
                        </tr1>
                        <br>
                    </c:forEach>
                    <a class="${menu.restaurant.dishes.size() <= 5? 'display': 'hidden'}" href="/rest/admin/dishes/add">
                        Add Dish
                    </a>
                </td>
                <td>${menu.toVote}</td>
                <td><a href="menus?action=update&id=${menu.restaurant.id}">Update</a></td>
                <td><a href="menus?action=delete&id=${menu.restaurant.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>
