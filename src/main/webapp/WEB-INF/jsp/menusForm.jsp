<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Dish</title>
    <style>
        dl {
            background: none repeat scroll 0 0 #FAFAFA;
            margin: 8px 0;
            padding: 0;
        }
        dt {
            display: inline-block;
            width: 170px;
        }
        dd {
            display: inline-block;
            margin-left: 8px;
            vertical-align: top;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="/topjava">Home</a></h3>
    <hr>
    <h2>${param.action == 'create' ? 'Create dish' : 'Edit dish'}</h2>
    <jsp:useBean id="dish" type="ru.javawebinar.topjava.model.Dish" scope="request"/>
    <form method="post" action="menus">
        <input type="hidden" name="id" value="${dish.id}">
        <dl>
            <dt>Дата/Время:</dt>
            <dd><input type="datetime-local" value="${dish.localDate}" name="dateTime" required></dd>
        </dl>
        <dl>
            <dt>Описание:</dt>
            <dd><input type="text" value="${dish.description}" size=40 name="description" required></dd>
        </dl>
        <dl>
            <dt>Калории:</dt>
            <dd><input type="number" value="${dish.calories}" name="calories" required></dd>
        </dl>
        <input type="hidden" name="userId" value="${dish.userId}">
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()" type="button">Отменить</button>
    </form>
</section>
</body>
</html>
