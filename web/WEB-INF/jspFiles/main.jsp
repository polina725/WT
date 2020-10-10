<%--
  Created by IntelliJ IDEA.
  User: Polina
  Date: 30.09.2020
  Time: 16:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Авторизация</title>
        <style><%@include file="/WEB-INF/jspFiles/styles/login.css"%></style>
        <style><%@include file="/WEB-INF/jspFiles/styles/header.css"%></style>
    </head>

    <body>

        <div class="_header">
            <div class="_logo">Авторизация</div>
            <div class="_nav">
                <nav>
                    <a class="_nav_link" href="notmain">Факультеты</a>
                    <a class="_nav_link" href="reg">Регистрация</a>
                    <a class="_nav_link" href="notmain">Войти</a>
                </nav>
            </div>
        </div>

        <div class="_forms">
            <form action="" target="_self" method="post">
                <label for="login" >Логин</label>
                <input type="text" id="login" name="login">	<br/><br/>
                <label for="password" >Пароль</label>
                <input type="password" id="password" name="password">	<br/><br/>
                <input class="_submit" type = 'submit' name = 'login_btn' value = 'Войти'>
            </form>
        </div>

    </body>
</html>
