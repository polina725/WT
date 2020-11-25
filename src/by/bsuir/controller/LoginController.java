package by.bsuir.controller;

import by.bsuir.beans.User;
import by.bsuir.beans.CommonUser;
import by.bsuir.dao.ConnectionPool;
import by.bsuir.exception.RegisterException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import static by.bsuir.service.SomeAction.*;

public class LoginController extends HttpServlet {

    final static Logger log;
    static{
        log = Logger.getLogger(ConnectionPool.class.getName());
        try {
            log.addHandler(new FileHandler("D:/log.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        User user = null;
        try {
            user = login(login,password);
        } catch (RegisterException e) {
            log.info(ConnectionPool.getExceptionStackTrace(e));
            request.setAttribute("error_message",e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jspFiles/main.jsp").forward(request, response);
        }
        if(user!=null){
            log.info(user.getName());
            String hash = null;
            try {
                hash = createUserHashForSession(user);
            } catch (RegisterException e) {
                log.info(ConnectionPool.getExceptionStackTrace(e));
                request.setAttribute("error_message",e.getMessage());
                request.getRequestDispatcher("/WEB-INF/jspFiles/main.jsp").forward(request, response);
            }
            session.setAttribute("hash", hash);
            session.setAttribute("role",user.getRole());
            if(user.getRole().equals("ADMIN"))
                response.sendRedirect("admin");
            else{
                String s = getStatus(user.getId());
                session.setAttribute("entrantStatus",s);
            }
            if(session.getAttribute("chosen_course")!=null)
                response.sendRedirect("enroll");
            else
                request.getRequestDispatcher("/WEB-INF/jspFiles/main.jsp").forward(request, response);
        }
        else {
            request.setAttribute("error_message", "Invalid login or password");
            request.getRequestDispatcher("/WEB-INF/jspFiles/main.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        String hash = (String) session.getAttribute("hash");
        if(hash!=null && request.getParameter("submit_btn")!=null){
            logOut(hash);
            session.removeAttribute("hash");
            session.removeAttribute("role");
            session.removeAttribute("chosen_course");
            session.removeAttribute("first_s");
            session.removeAttribute("first_s_id");
            session.removeAttribute("second_s_id");
            session.removeAttribute("second_s");
            session.removeAttribute("entrantStatus");
        }
        else if(hash!=null) {
            CommonUser user=null;
            if(session.getAttribute("role").equals("STUDENT"))
                user = getUserByHash((String)session.getAttribute("hash"),true,true);
            else
                user = getUserByHash((String)session.getAttribute("hash"),false,false);
            request.setAttribute("user",user);
        }
        request.getRequestDispatcher("/WEB-INF/jspFiles/main.jsp").forward(request, response);
    }
}
