<%--
  Created by IntelliJ IDEA.
  User: Polina
  Date: 01.11.2020
  Time: 21:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
    <head>
        <title>Enrollment form</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <style><%@include file="/WEB-INF/jspFiles/styles/main.css"%></style>
        <style><%@include file="/WEB-INF/jspFiles/styles/header.css"%></style>
        <script><%@include file="/WEB-INF/jspFiles/scripts/scriptsCheckingEnrollmentForm.jsp"%></script>
    </head>
    <fmt:setLocale value='<%=request.getSession().getAttribute("lang")%>'/>
    <fmt:setBundle basename="lang" var="loc"/>
    <fmt:message bundle="${loc}" key="lang.label.en_lang" var="en_lang"/>
    <fmt:message bundle="${loc}" key="lang.label.ph" var="phys"/>
    <fmt:message bundle="${loc}" key="lang.label.ru_lang" var="ru_lang"/>
    <fmt:message bundle="${loc}" key="lang.label.by_lang" var="by_lang"/>
    <fmt:message bundle="${loc}" key="lang.label.math" var="math"/>
    <fmt:message bundle="${loc}" key="lang.label.certificate_score" var="cert"/>
    <fmt:message bundle="${loc}" key="lang.label.st_lang" var="st_lang"/>
    <fmt:message bundle="${loc}" key="lang.label.singout" var="out"/>
    <fmt:message bundle="${loc}" key="lang.label.department" var="dep"/>
    <fmt:message bundle="${loc}" key="lang.label.instruction" var="instr"/>
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

    <div class="_forms">
        <form action="" method="post" oninput="checkForm()">

            <div class="ru_by_language_choose">
                <input type="radio" id="ru" name="language" value="ru">
                <label for="ru">${ru_lang}</label><br>
                <input type="radio" id="by" name="language" value="by">
                <label for="by">${by_lang}</label><br>
                <label for="state_lan">${st_lang}</label><br>
                <input type="number" id="state_lan" name="state_lan" min="0" max="100" value="0"/><br>
            </div>


            <div class="scores">
                <p>${instr}</p>
                <c:forEach  var="subj" items="${course.getSubjects()}">
                    <label for="${subj.getId()}">
                        <c:choose>
                            <c:when test="${subj.getId()==1}">${en_lang}</c:when>
                            <c:when test="${subj.getId()==3}">${phys}</c:when>
                            <c:otherwise>${math}</c:otherwise>
                        </c:choose>
                    </label><br>
                    <input type="number" id="${subj.getId()}" name="${subj.getName()}" min="0" max="100" value="0"/><br>
                </c:forEach>
                <label for="certificate">${cert}</label><br>
                <input type="number" id="certificate" name="certificate_score" min="0" max="100" value="0"/><br>
            </div>

            <input class="_submit" type = 'submit' id="submit" name = 'submit_btn' value = 'OK' disabled="true">
        </form>

        <h1>${error_message}</h1>


    </div>

    </body>
</html>
