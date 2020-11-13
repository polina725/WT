package controller;

import beans.CommonUserInstance;
import dao.DAO;
import exception.RegisterException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import static service.SomeAction.*;

public class LoginController extends HttpServlet {

    final static Logger log;
    static{
        log = Logger.getLogger(DAO.class.getName());
        try {
            log.addHandler(new FileHandler("D:/log.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        /// TODO understand why this shit doesn't work...   request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        CommonUserInstance user = null;
        try {
            user = login(login,password);
        } catch (RegisterException e) {
            log.info(DAO.getExceptionStackTrace(e));
            request.setAttribute("error_message",e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jspFiles/main.jsp").forward(request, response);
        }
        if(user!=null){
            log.info(user.getName());
            String hash = null;
            try {
                hash = createUserHashForSession(user);
            } catch (RegisterException e) {
                log.info(DAO.getExceptionStackTrace(e));
                request.setAttribute("error_message",e.getMessage());
                request.getRequestDispatcher("/WEB-INF/jspFiles/main.jsp").forward(request, response);
            }
            session.setAttribute("hash", hash);
            if(session.getAttribute("chosen_course")==null)
                response.sendRedirect("departments");
            else
                response.sendRedirect("enroll");
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
            //if(session.getAttribute("chosen_course")!=null)
                session.removeAttribute("chosen_course");
        }
        else
            request.setAttribute("loggedIn",false);
        request.getRequestDispatcher("/WEB-INF/jspFiles/main.jsp").forward(request, response);
    }
}
