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
    <fmt:setBundle basename="lang" var="loc"/>
    <fmt:message bundle="${loc}" key="lang.label.password" var="password"/>
    <fmt:message bundle="${loc}" key="lang.label.login" var="login"/>
    <fmt:message bundle="${loc}" key="lang.label.name" var="name"/>
    <fmt:message bundle="${loc}" key="lang.label.surname" var="surname"/>
    <fmt:message bundle="${loc}" key="lang.label.en_lang" var="en_lang"/>
    <fmt:message bundle="${loc}" key="lang.label.ph" var="phys"/>
    <fmt:message bundle="${loc}" key="lang.label.ru_lang" var="ru_lang"/>
    <fmt:message bundle="${loc}" key="lang.label.by_lang" var="by_lang"/>
    <fmt:message bundle="${loc}" key="lang.label.math" var="math"/>
    <fmt:message bundle="${loc}" key="lang.label.certificate" var="cert"/>
    <fmt:message bundle="${loc}" key="lang.label.department" var="dep"/>
    <fmt:message bundle="${loc}" key="lang.label.course" var="crs"/>
    <fmt:message bundle="${loc}" key="lang.label.singout" var="out"/>
    <fmt:message bundle="${loc}" key="lang.link.reg" var="reg_l"/>
    <fmt:message bundle="${loc}" key="lang.label.reg" var="reg"/>
    <fmt:message bundle="${loc}" key="lang.label.singin" var="in"/>
    <fmt:message bundle="${loc}" key="lang.label.list" var="stats"/>
    <fmt:message bundle="${loc}" key="lang.label.fee_st" var="fee_st"/>
    <fmt:message bundle="${loc}" key="lang.label.budget_st" var="budget_st"/>
    <fmt:message bundle="${loc}" key="lang.label.unenrolled_st" var="unenrolled_st"/>
    <fmt:message bundle="${loc}" key="lang.label.enrolling_st" var="enrolling_st"/>
    <body>

        <div class="_header">
            <div class="_logo">JAVA_WT_2020</div>
            <div class="_nav">
                <nav>
                    <a class="_nav_link" href="reg">${reg}</a>
                    <a class="_nav_link" href="departments">
                        <c:choose>
                            <c:when test="${\"ADMIN\".equals(status)}"> ${stats}</c:when>
                            <c:otherwise>${dep}</c:otherwise>
                        </c:choose></a>
                    <a class="_nav_link" href="login">
                        <c:choose>
                            <c:when test="${hash==null}"> ${in}</c:when>
                            <c:otherwise>${out}</c:otherwise>
                        </c:choose>
                    </a>
                </nav>
            </div>
        </div>

        <div class="_forms" >
        <c:choose>
            <c:when test="${hash == null}">
                <form target="_self" method="post" action="login">
                    <label for="login" >${login}</label>
                    <input type="text" id="login" name="login">	<br/><br/>
                    <label for="password" >${password}</label>
                    <input type="password" id="password" name="password">	<br/><br/>
                    <input class="_submit" type = 'submit' name = 'submit_btn' value = '${in}' id="submit" >
                </form>
            </c:when>
            <c:otherwise>
                <div class="user_info">
                    <c:if test="${user.getRole().equals(\"STUDENT\") && !user.getStatus().equals(\"UNDEFINED\")}">
                        <label>${name}: ${user.getName()}</label><br>
                        <label>${surname}: ${user.getSurname()}</label><br>
                        <label>${login}: ${user.getLogin()}</label><br>
                        <c:forEach var="subj" items="${user.getSubjects()}">
                            <label>
                                <c:choose>
                                    <c:when test="${subj.getId()==1}">${en_lang}</c:when>
                                    <c:when test="${subj.getId()==3}">${phys}</c:when>
                                    <c:when test="${subj.getId()==4}">${ru_lang}</c:when>
                                    <c:when test="${subj.getId()==5}">${by_lang}</c:when>
                                    <c:otherwise>${math}</c:otherwise>
                                </c:choose>
                                : ${subj.getScore()}</label><br><br>
                        </c:forEach>
                        <label>${cert}: ${user.getCertificate()}</label><br><br>
                        <label>${dep}: ${user.getDepartment()}</label><br><br>
                        <label>${crs}: ${user.getCourse()}</label><br><br>
                        <label>
                            <c:choose>
                                <c:when test="${user.getStatus().equals(\"ENROLLED_BUDGET\")}">${budget_st}</c:when>
                                <c:when test="${user.getStatus().equals(\"ENROLLED_FEE\")}">${fee_st}</c:when>
                                <c:when test="${user.getStatus().equals(\"ENROLLING\")}">${enrolling_st}</c:when>
                                <c:otherwise>${unenrolled_st}</c:otherwise>
                            </c:choose>
                        </label>
                    </c:if>
                </div>
                <div>
                    <form action="logout" method="post">
                        <input class="_submit" type = 'submit' name = 'submit_btn' value = '${out}'>
                    </form>
                </div>
            </c:otherwise>
        </c:choose>
        </div>
        <div class="_reg_link">
            <a class="_link" href="reg">${reg_l}</a>
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
