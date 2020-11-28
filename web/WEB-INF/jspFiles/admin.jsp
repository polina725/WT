<%--
  Created by IntelliJ IDEA.
  User: Polina
  Date: 02.11.2020
  Time: 13:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<html>
    <head>
        <title>Admin</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Авторизация</title>
        <style><%@include file="/WEB-INF/jspFiles/styles/main.css"%></style>
        <style><%@include file="/WEB-INF/jspFiles/styles/header.css"%></style>
    </head>
    <fmt:setLocale value='<%=request.getSession().getAttribute("lang")%>'/>
    <fmt:setBundle basename="lang" var="loc"/>
    <fmt:message bundle="${loc}" key="lang.label.course" var="course"/>
    <fmt:message bundle="${loc}" key="lang.label.singout" var="out"/>
    <fmt:message bundle="${loc}" key="lang.label.enr_plan" var="enr_plan"/>
    <fmt:message bundle="${loc}" key="lang.label.applied" var="applied"/>
    <fmt:message bundle="${loc}" key="lang.btn.end" var="stop"/>
    <fmt:message bundle="${loc}" key="lang.label.list" var="list"/>
    <body>

        <div class="_header">
            <div class="_logo">JAVA_WT_2020</div>
            <div class="_nav">
                <nav>
                    <a class="_nav_link" href="admin">${list}</a>
                    <a class="_nav_link" href="login">${out}/></a>
                </nav>
            </div>
        </div>


        <tabel class="courses_l">
            <c:forEach var="crs" items="${courses}">

                <tr class="course">
                    <h1>${course}: ${crs.getName()}</h1>
                    <th>
                        <h1>${enr_plan}: ${crs.getFeePlacesAmount()+crs.getBudgetPlacesAmount()}</h1>
                        <h1>${applied}: ${crs.getTotalStudentCount()}</h1>
                    </th>
                </tr>
            </c:forEach>
        </tabel>
        <form method="post">
            <input type="submit" id="submit" name="submit" value="${stop}">
        </form>
        <div class="errors">
            <h1>${error_message}</h1>
        </div>
    </body>
</html>
