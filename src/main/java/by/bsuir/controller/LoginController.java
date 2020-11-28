package by.bsuir.controller;

import by.bsuir.beans.CommonUser;
import by.bsuir.beans.User;
import by.bsuir.dao.UserDAO;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static by.bsuir.service.Utilities.*;

public class LoginController extends HttpServlet {

    private Logger logger;
    private UserDAO userDAO = new UserDAO();
    private HttpSession session;

    public LoginController(){
        logger = Logger.getLogger(this.getClass());
        PropertyConfigurator.configure(this.getClass().getClassLoader().getResource("log4j.properties"));
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        session = request.getSession(true);
        String action = request.getServletPath();
        switch(action){
            case "/change_lang":
                changeLang(request,response);
                break;
            case "/login":
                login(request,response);
                break;
            case "/logout":
                logout(request, response);
                break;
          //  default:
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        String hash = (String) session.getAttribute("hash");
         if(hash!=null) {
            CommonUser user=null;
            if(session.getAttribute("role").equals("STUDENT"))
                user = getUserByHash((String)session.getAttribute("hash"),true,true);
            else
                user = getUserByHash((String)session.getAttribute("hash"),false,false);
            request.setAttribute("user",user);
        }
        request.getRequestDispatcher("/WEB-INF/jspFiles/main.jsp").forward(request, response);
    }

    private void changeLang(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        session.setAttribute("lang",request.getParameter("lang"));
        request.getRequestDispatcher("/WEB-INF/jspFiles/main.jsp").forward(request, response);
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _login = request.getParameter("login");
        String password = request.getParameter("password");
        User user = (User)userDAO.getRegisteredUser(_login,hash(password));
        if (user != null) {
            String hash=hash(user.getLogin()+user.getName());
            userDAO.updateHash(hash,user.getLogin(),false);
            session.setAttribute("hash", hash);
            session.setAttribute("role", user.getRole());
            if (user.getRole().equals("ADMIN"))
                response.sendRedirect("admin");
            else {
                String s = userDAO.getEntrantStatus(user.getId());
                session.setAttribute("entrantStatus", s);
                if (session.getAttribute("chosen_course") != null)
                    response.sendRedirect("enroll");
                else
                    response.sendRedirect("departments");
            }
        } else {
            request.setAttribute("error_message", "Invalid login or password");
            request.getRequestDispatcher("/WEB-INF/jspFiles/main.jsp").forward(request, response);
        }
    }

    private void logout(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        String hash = (String) session.getAttribute("hash");
        userDAO.updateHash(hash,null,true);
        String[] sessionAttrNames ={"hash","role","first_s","chosen_course","first_s_id","second_s_id","second_s","entrantStatus"};
        for(String attrName : sessionAttrNames){
            session.removeAttribute(attrName);
        }
        request.getRequestDispatcher("/WEB-INF/jspFiles/main.jsp").forward(request, response);
    }
}
