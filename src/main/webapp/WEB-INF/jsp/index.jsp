<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Java Enterprise (Topjava_graduation)</title>
</head>
<body>
<h3>Выпускной проект <a href="https://github.com/JavaWebinar/topjava" target="_blank">Java Enterprise (Topjava)</a></h3>
<hr>
<ul>
    <li><a href="menus">Menus</a></li>
</ul>
<hr>
<form method="post" action="users">
    <b>Log-in </b>
    <select name="userId">
        <option value="100000">User</option>
        <option value="100001">Admin</option>
    </select>
    <button type="submit">Select</button>
</form>
</body>
</html>
