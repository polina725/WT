<%--
  Created by IntelliJ IDEA.
  User: Polina
  Date: 02.11.2020
  Time: 13:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
    <head>
        <title>Admin</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Авторизация</title>
        <style><%@include file="/WEB-INF/jspFiles/styles/main.css"%></style>
        <style><%@include file="/WEB-INF/jspFiles/styles/header.css"%></style>
    </head>

    <body>

        <div class="_header">
            <div class="_logo">JAVA_WT_2020</div>
            <div class="_nav">
                <nav>
                    <a class="_nav_link" href="login">Выйти</a>
                </nav>
            </div>
        </div>


        <tabel class="courses_l">
            <c:forEach var="crs" items="${courses}">

                <tr class="course">
                    <h1>Курс: ${crs.getName()}</h1>
                    <th>
                        <h1>План приема: ${crs.getFeePlacesAmount()+course.getBudgetPlacesAmount()}</h1>
                        <h1>Подало заявлений: ${crs.getTotalStudentCount()}</h1>
                    </th>
                </tr>
            </c:forEach>
        </tabel>
        <form method="post">
            <input type="submit" id="submit" name="submit" value="Завершить приемную компанию">
        </form>
        <div class="errors">
            <h1>${error_message}</h1>
        </div>
    </body>
</html>
