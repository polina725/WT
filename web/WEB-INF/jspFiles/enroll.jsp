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
    </head>

    <body>
    <%@include file="/WEB-INF/jspFiles/templates/header.jsp"%>

    <h1>0/${course.getBudgetPlacesAmount()}</h1>
    <h1>0/${course.getFeePlacesAmount()}</h1><br>
    <div class="_forms">
        <form action="" method="post">

            <div class="ru_by_language_choose">
                <p>Choose language</p>
                <input type="radio" id="ru" name="language" value="ru">
                <label for="ru">Russian</label><br>
                <input type="radio" id="by" name="language" value="by">
                <label for="by">Belorussian</label><br>
            </div>


            <div class="scores">
                <p>Enter each subject's score</p>
                <c:forEach  var="subj" items="${course.getSubjects()}">
                    <label for="${subj.getId()}">${subj.getName()}</label><br>
                    <input type="number" id="${subj.getId()}" name="${subj.getName()}" min="0" max="100"/><br>
                </c:forEach>
                <label for="state_lan">State language</label><br>
                <input type="number" id="state_lan" name="state_lan" min="0" max="100"><br>
                <label for="certificate">Certificate score</label><br>
                <input type="number" id="certificate" name="certificate_score" min="0" max="100"><br>
            </div>

            <input class="_submit" type = 'submit' name = 'submit_btn' value = 'Submit'>
        </form>

        <h1>${error_message}</h1>


    </div>

    </body>
</html>
