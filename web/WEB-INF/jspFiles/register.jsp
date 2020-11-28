<!--jsp:useBean id="error_message" scope="response" type=""-->
<%--
  Created by IntelliJ IDEA.
  User: Polina
  Date: 01.10.2020
  Time: 18:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Registration</title>
        <style><%@include file="/WEB-INF/jspFiles/styles/main.css"%></style>
        <style><%@include file="/WEB-INF/jspFiles/styles/header.css"%></style>

        <script><%@include file="/WEB-INF/jspFiles/scripts/scriptsCheckingRegestrationForm.jsp"%></script>
        <script>
            function nameAndSurnameCorrect(){
                return !(/[^A-Za-zА-Яа-яЁё]/.test(document.getElementById("name").value) || /[^A-Za-zА-Яа-яЁё]/.test(document.getElementById("surname").value));
            }
        </script>
    </head>
    <fmt:setLocale value='<%=request.getSession().getAttribute("lang")%>'/>
    <fmt:setBundle basename="lang" var="loc"/>
    <fmt:message bundle="${loc}" key="lang.label.password" var="password"/>
    <fmt:message bundle="${loc}" key="lang.label.repeat_password" var="r_password"/>
    <fmt:message bundle="${loc}" key="lang.label.login" var="login"/>
    <fmt:message bundle="${loc}" key="lang.label.name" var="name"/>
    <fmt:message bundle="${loc}" key="lang.label.surname" var="surname"/>
    <fmt:message bundle="${loc}" key="lang.label.singout" var="out"/>
    <body>

        <div class="_header">
            <div class="_logo">JAVA_WT_2020</div>
            <div class="_nav">
                <nav>
                    <a class="_nav_link" href="login">${out}</a>
                </nav>
            </div>
        </div>

        <div class="_forms">
            <form action="" target="_self" method="post" oninput="checkForm()">
                <label for="login" >${login}</label>
                <input type="text" id="login" name="login" maxlength="45">	<br/><br/>

                <label for="password" >${password}</label>
                <input type="password" id="password" name="password" maxlength="15">	<br/><br/>

                <label for="repeat_password">${r_password}</label>
                <input type="password" id="repeat_password" name="repeat_password" maxlength="15" >	<br/><br/>

                <label for="name">${name}</label>
                <input type="text" id="name" name="name" maxlength="45">	<br/><br/>

                <label for="surname">${surname}</label>
                <input type="text" id="surname" name="surname" maxlength="45">	<br/><br/>

                <input class="_submit" type = 'submit' id="submit" name = 'login_btn' value = 'OK' disabled="true">
            </form>
        </div>

        <div class="errors">
            <h1>${error_message}</h1>
        </div>

    </body>

</html>
