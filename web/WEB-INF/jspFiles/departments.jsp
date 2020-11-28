<%--
  Created by IntelliJ IDEA.
  User: Polina
  Date: 02.11.2020
  Time: 19:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
    <head>
        <title>Departments</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <style><%@include file="/WEB-INF/jspFiles/styles/main.css"%></style>
        <style><%@include file="/WEB-INF/jspFiles/styles/header.css"%></style>
    </head>
    <fmt:setLocale value='<%=request.getSession().getAttribute("lang")%>'/>
    <fmt:setBundle basename="lang" var="loc"/>
    <fmt:message bundle="${loc}" key="lang.label.enr_plan" var="enr_plan"/>
    <fmt:message bundle="${loc}" key="lang.label.singout" var="out"/>
    <fmt:message bundle="${loc}" key="lang.label.department" var="dep"/>
    <body>
        <div class="_header">
            <div class="_logo">JAVA_WT_2020</div>
            <div class="_nav">
                <nav>
                    <a class="_nav_link" href="departments">${dep}</a>
                    <a class="_nav_link" href="login">${out}</a>
                </nav>
            </div>
        </div>

        <div class="courses_table">
            <tabel class="courses_l">
                    <c:forEach var="dep" items="${departments}">
                        <h1><c:out value="${dep.getName()}"/></h1>
                        <tr class="course">
                            <c:forEach var="course" items="${dep.getCourses()}">
                                <th>
                                    <h3>${enr_plan}: ${course.getFeePlacesAmount()+course.getBudgetPlacesAmount()}</h3>
                                    <form method="post">
                                        <input type="hidden" name="link_button" value="${course.getName()}" />
                                        <input type="submit" value="${course.getName()}"/>
                                    </form>
                                </th>
                            </c:forEach>
                        </tr>
                    </c:forEach>
            </tabel>
            <h1>${error_message}</h1>
        </div>

    </body>
</html>
