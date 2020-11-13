<%--
  Created by IntelliJ IDEA.
  User: Polina
  Date: 02.11.2020
  Time: 19:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
    <head>
        <title>Departments</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <style><%@include file="/WEB-INF/jspFiles/styles/main.css"%></style>
        <style><%@include file="/WEB-INF/jspFiles/styles/header.css"%></style>
    </head>

    <body>

        <%@include file="/WEB-INF/jspFiles/templates/header.jsp"%>

        <div class="courses_table">
            <tabel class="courses_l">
                    <c:forEach var="dep" items="${departments}">
                        <h1><c:out value="${dep.getName()}"/></h1>
                        <tr class="course">
                            <c:forEach var="course" items="${dep.getCourses()}">
                                <th>
                                    <h>Enrollment plan: ${course.getFeePlacesAmount()+course.getBudgetPlacesAmount()}</h>
                                    <form method="post">
                                        <input type="submit" name="link_button" value="${course.getName()}" />
                                    </form>
                                </th>
                            </c:forEach>
                        </tr>
                    </c:forEach>
            </tabel>
            <a href="" name="mem" value="1">OAOAOA</a>
            <h1>${error_message}</h1>
        </div>

    </body>
</html>
