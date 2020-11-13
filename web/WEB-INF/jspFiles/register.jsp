<!--jsp:useBean id="error_message" scope="response" type=""-->
<%--
  Created by IntelliJ IDEA.
  User: Polina
  Date: 01.10.2020
  Time: 18:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

    <body>

        <%@include file="/WEB-INF/jspFiles/templates/header.jsp"%>

        <div class="_forms">
            <form action="" target="_self" method="post" oninput="checkForm()">
                <label for="login" >Login</label>
                <input type="text" id="login" name="login" maxlength="45">	<br/><br/>

                <label for="password" >Password</label>
                <input type="password" id="password" name="password" maxlength="15">	<br/><br/>

                <label for="repeat_password">Repeat password</label>
                <input type="password" id="repeat_password" name="repeat_password" maxlength="15" >	<br/><br/>

                <label for="name">Name</label>
                <input type="text" id="name" name="name" maxlength="45">	<br/><br/>

                <label for="surname">Surname</label>
                <input type="text" id="surname" name="surname" maxlength="45">	<br/><br/>

                <input class="_submit" type = 'submit' id="submit" name = 'login_btn' value = 'Send' disabled="true">
            </form>
        </div>

        <div class="errors">
            <h1>${error_message}</h1>
        </div>

    </body>

</html>
