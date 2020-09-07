<%@ page contentType="text/html; charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>User list</title>
    <style>
    </style>
</head>
<body>
<ul>
    <section>
        <h3><a href="index.jsp">Home</a></h3>
        <hr/>
        <h2>Users</h2>
        <button><a href="users?action=create">Add User</a></button>
        <br><br>
        <table border="1" cellpadding="8" cellspacing="0">
            <thead>
            <tr>
                <td><input type="hidden" name="id">id</td>
                <th>Имя</th>
                <th>Почта</th>
                <th>Зарегистрирован</th>
                <th>Voted</th>
                <th>Роли</th>
                <th></th>
                <th></th>
            </tr>
            </thead>
            <c:forEach items="${users}" var="user">
                <jsp:useBean id="user" type="ru.javawebinar.topjava.model.User"/>
                <br>
                <td><input type="hidden" name="id" value="${user.id}"></td>
                <td>${user.name}</td>
                <td>${user.email}</td>
                <td>${user.registered}</td>
                <td>${user.voted}</td>
                <td>${user.roles}</td>
                <td><input type="checkbox" checked="" onclick="enable($(this),100000);"></td>
                <td><fmt:formatDate value="${user.registered}" pattern="yyyy-MM-dd hh:mm" /></td>
                <td><a href="users?action=update&id=${user.id}">Update</a></td>
                <td><a href="users?action=delete&id=${user.id}">Delete</a></td>
                </tr>
            </c:forEach>
        </table>
    </section>
</ul>
</body>
</html>

