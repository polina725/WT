package by.bsuir.controller;

import by.bsuir.beans.User;
import by.bsuir.dao.ConnectionPool;
import by.bsuir.exception.RegisterException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import static by.bsuir.service.SomeAction.logOut;
import static by.bsuir.service.SomeAction.register;

public class RegisterController extends HttpServlet {
    final static Logger log;
    static{
        log = Logger.getLogger(ConnectionPool.class.getName());
        try {
            log.addHandler(new FileHandler("D:/log.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //new String(request.getParameter("name").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //TODO show that passwords doesn't match + allowed characters(on jsp)
        User unregUser= new User(request.getParameter("name"),
                                                             request.getParameter("surname"),
                                                             request.getParameter("login")
                                                            );
        String hash = null;
        try {
            hash=register(unregUser,request.getParameter("password"));
        } catch (RegisterException e) {
            request.setAttribute("error_message",e.getMessage());
           // request.getRequestDispatcher("/WEB-INF/jspFiles/register.jsp").forward(request, response);
        }
        if(hash!=null){
            HttpSession session = request.getSession(true);
            session.setAttribute("hash",hash);
            session.setAttribute("role","STUDENT");
            if(session.getAttribute("chosen_course")!=null)
                response.sendRedirect("enroll");
            else/////////////////////////////////////////////////////////
                response.sendRedirect("login");
        }
        else
            request.getRequestDispatcher("/WEB-INF/jspFiles/register.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        HttpSession session = request.getSession(true);
        String hash = (String)session.getAttribute("hash");
        if(hash!=null){
            logOut((String)session.getAttribute("hash"));
            session.removeAttribute("hash");
            session.removeAttribute("role");
        }
        request.getRequestDispatcher("/WEB-INF/jspFiles/register.jsp").forward(request, response);
    }
}
