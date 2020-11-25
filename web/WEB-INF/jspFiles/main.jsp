<%--
  Created by IntelliJ IDEA.
  User: Polina
  Date: 30.09.2020
  Time: 16:47
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Авторизация</title>
        <style><%@include file="/WEB-INF/jspFiles/styles/main.css"%></style>
        <style><%@include file="/WEB-INF/jspFiles/styles/header.css"%></style>
        <script><%@include file="/WEB-INF/jspFiles/scripts/scriptsCheckingEnrollmentForm.jsp"%></script>
    </head>
    <fmt:setLocale value='<%=request.getSession().getAttribute("lang")%>'/>
    <fmt:setBundle basename="resourses.lang" var="loc"/>
    <fmt:message bundle="${loc}" key="lang.label.password" var="password"/>
    <fmt:message bundle="${loc}" key="lang.label.login" var="login"/>
    <fmt:message bundle="${loc}" key="lang.label.name" var="name"/>
    <fmt:message bundle="${loc}" key="lang.label.surname" var="surname"/>
    <fmt:message bundle="${loc}" key="lang.label.en_lang" var="en_lang"/>
    <fmt:message bundle="${loc}" key="lang.label.ph" var="phys"/>
    <fmt:message bundle="${loc}" key="lang.label.ru_lang" var="ru_lang"/>
    <fmt:message bundle="${loc}" key="lang.label.by_lang" var="by_lang"/>
    <fmt:message bundle="${loc}" key="lang.label.certificate" var="cert"/>
    <fmt:message bundle="${loc}" key="lang.label.department" var="dep"/>
    <fmt:message bundle="${loc}" key="lang.label.course" var="crs"/>
    <fmt:message bundle="${loc}" key="lang.label.singout" var="out"/>
    <fmt:message bundle="${loc}" key="lang.link.reg" var="reg_l"/>
    <body>

    <%@include file="/WEB-INF/jspFiles/templates/header.jsp"%>
        <div class="_forms" >
        <c:choose>
            <c:when test="${hash == null}">
                <form action="" target="_self" method="post">
                    <label for="login" >E-mail</label>
                    <input type="text" id="login" name="login">	<br/><br/>
                    <label for="password" ><c:out value="${password}"/></label>
                    <input type="password" id="password" name="password">	<br/><br/>
                    <input class="_submit" type = 'submit' name = 'submit_btn' value = 'Sing in' id="submit" >
                </form>
            </c:when>
            <c:otherwise>
                <div class="user_info">
                    <label>Имя: ${user.getName()}</label><br>
                    <label>Фамилия: ${user.getSurname()}</label><br>
                    <label>Логин: ${user.getLogin()}</label><br>
                    <c:if test="${user.getRole().equals(\"STUDENT\") && !user.getStatus().equals(\"UNDEFINED\")}">
                        <c:forEach var="subj" items="${user.getSubjects()}">
                            <label>
                                <c:choose>
                                    <c:when test="${subj.getId()==1}">Английский язык</c:when>
                                    <c:when test="${subj.getId()==3}">Физика</c:when>
                                    <c:when test="${subj.getId()==4}">Русский язык</c:when>
                                    <c:when test="${subj.getId()==5}">Белорусский язык</c:when>
                                    <c:otherwise>Математика</c:otherwise>
                                </c:choose>
                                : ${subj.getScore()}</label><br><br>
                        </c:forEach>
                        <label>Аттестат: ${user.getCertificate()}</label><br><br>
                        <label>Факультет: ${user.getDepartment()}</label><br><br>
                        <label>Специальность: ${user.getCourse()}</label><br><br>
                    </c:if>
                </div>
                <form action="" target="_self" method="get">
                    <input class="_submit" type = 'submit' name = 'submit_btn' value = 'Выход'>
                </form>
            </c:otherwise>
        </c:choose>
        </div>
        <div class="_reg_link">
            <a class="_link" href="reg">Еще не зарегистрировались?</a>
        </div>
        <div class="_forms">
            <form method="post" action="change_lang">
                <input type="hidden" name="lang" value="ru" />
                <input type="submit" value="RU" /></input>
            </form>
            <form method="post" action="change_lang">
                <input type="hidden" name="lang" value="en" />
                <input type="submit" value="EN" /></input>
            </form>
        </div>

        <div class="errors">
            <h1>${error_message}</h1>
        </div>

    </body>
</html>
