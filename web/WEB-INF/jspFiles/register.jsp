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
        <head>
            <meta charset="utf-8">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <title>Registration</title>
            <style><%@include file="/WEB-INF/jspFiles/styles/login.css"%></style>
            <style><%@include file="/WEB-INF/jspFiles/styles/header.css"%></style>
        </head>
    </head>

    <body>

        <div class="_header">
            <div class="_logo">Авторизация</div>
            <div class="_nav">
                <nav>
                    <a class="_nav_link" href="notmain">Факультеты</a>
                    <a class="_nav_link" href="notmain">Регистрация</a>
                    <a class="_nav_link" href="notmain">Войти</a>
                </nav>
            </div>
        </div>

        <div class="_forms">
            <form action="" target="_self" method="post">
                <label for="login" >Login</label>
                <input type="text" id="login" name="login">	<br/><br/>

                <label for="password" >Password</label>
                <input type="password" id="password" name="password">	<br/><br/>

                <label for="repeat_password" >Repeat password</label>
                <input type="password" id="repeat_password" name="repeat_password">	<br/><br/>

                <label for="name">Name</label>
                <input type="text" id="name" name="name">	<br/><br/>

                <label for="surname">Surname</label>
                <input type="text" id="surname" name="surname">	<br/><br/>

                <input class="_submit" type = 'submit' name = 'login_btn' value = 'Send'>
            </form>
        </div>

        <div class="errors">
            <h1>${m.error_message}</h1>
        </div>

    </body>

</html>
