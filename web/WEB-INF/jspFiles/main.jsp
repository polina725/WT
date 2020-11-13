<%--
  Created by IntelliJ IDEA.
  User: Polina
  Date: 30.09.2020
  Time: 16:47
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!--%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%-->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Авторизация</title>
        <style><%@include file="/WEB-INF/jspFiles/styles/main.css"%></style>
        <style><%@include file="/WEB-INF/jspFiles/styles/header.css"%></style>
        <script><%@include file="/WEB-INF/jspFiles/scripts/scriptsCheckingLoginForm.jsp"%></script>
    </head>

    <body>

    <%@include file="/WEB-INF/jspFiles/templates/header.jsp"%>

        <div class="_forms" >
        <c:choose>
            <c:when test="${hash == null}">
                <form action="" target="_self" method="post">
                    <label for="login" >E-mail</label>
                    <input type="text" id="login" name="login">	<br/><br/>
                    <label for="password" >Password</label>
                    <input type="password" id="password" name="password">	<br/><br/>
                    <input class="_submit" type = 'submit' name = 'submit_btn' value = 'Sing in' id="submit" >
                </form>
            </c:when>
            <c:otherwise>
                <form action="" target="_self" method="get">
                    <input class="_submit" type = 'submit' name = 'submit_btn' value = 'Sing out'>
                </form>
            </c:otherwise>
        </c:choose>
        </div>

        <div class="_reg_link">
            <a class="_link" href="reg">Do you have an account?</a>
        </div>

        <div class="errors">
            <h1>${error_message}</h1>
        </div>

    </body>
</html>
