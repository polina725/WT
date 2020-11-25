<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Polina
  Date: 01.11.2020
  Time: 21:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
    <head>
        <title>Enrollment form</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <style><%@include file="/WEB-INF/jspFiles/styles/main.css"%></style>
        <style><%@include file="/WEB-INF/jspFiles/styles/header.css"%></style>
        <script><%@include file="/WEB-INF/jspFiles/scripts/scriptsCheckingEnrollmentForm.jsp"%></script>
    </head>

    <body>
    <%@include file="/WEB-INF/jspFiles/templates/header.jsp"%>

    <h1>0/${course.getBudgetPlacesAmount()}</h1>
    <h1>0/${course.getFeePlacesAmount()}</h1><br>
    <div class="_forms">
        <form action="" method="post" oninput="checkForm()">

            <div class="ru_by_language_choose">
                <p>Выберите гос язык</p>
                <input type="radio" id="ru" name="language" value="ru">
                <label for="ru">Русский</label><br>
                <input type="radio" id="by" name="language" value="by">
                <label for="by">Белоруский</label><br>
            </div>


            <div class="scores">
                <p>Enter each subject's score</p>
                <c:forEach  var="subj" items="${course.getSubjects()}">
                    <label for="${subj.getId()}">
                        <c:choose>
                            <c:when test="${subj.getId()==1}">Английский язык</c:when>
                            <c:when test="${subj.getId()==3}">Физика</c:when>
                            <c:otherwise>Математика</c:otherwise>
                        </c:choose>
                    </label><br>
                    <input type="number" id="${subj.getId()}" name="${subj.getName()}" min="0" max="100" value="0"/><br>
                </c:forEach>
                <label for="state_lan">Гос язык</label><br>
                <input type="number" id="state_lan" name="state_lan" min="0" max="100" value="0"/><br>
                <label for="certificate">Бал аттестата</label><br>
                <input type="number" id="certificate" name="certificate_score" min="0" max="100" value="0"/><br>
            </div>

            <input class="_submit" type = 'submit' id="submit" name = 'submit_btn' value = 'OK' disabled="true">
        </form>

        <h1>${error_message}</h1>


    </div>

    </body>
</html>
