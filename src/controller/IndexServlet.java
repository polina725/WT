package controller;

import dao.Connector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.HashMap;
import java.util.Scanner;

//@WebServlet(name="login")
public class IndexServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String password = request.getParameter("password");
        String repeatedPassword = request.getParameter("repeat_password");

        HashMap<String,String> map = new HashMap<>();

        map.put("login",request.getParameter("login"));
        map.put("password",password);
        map.put("repeat_password",repeatedPassword);
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(
                    password.getBytes(StandardCharsets.UTF_8));

           // Connector.pushData();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HashMap<String,String> map = new HashMap<>();
        map.put("error_message","dEbIL");
        request.setAttribute("m",map);
        request.getRequestDispatcher("/WEB-INF/jspFiles/register.jsp").forward(request, response);
        request.getRequestDispatcher("/WEB-INF/jspFiles/register.jsp").forward(request, response);
    }
}

