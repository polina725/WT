package by.bsuir.controller;

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
import java.nio.charset.StandardCharsets;

import static by.bsuir.service.Utilities.hash;

public class RegisterController extends HttpServlet {
    private static UserDAO userDAO = new UserDAO();
    private Logger logger;

    public RegisterController(){
        logger = Logger.getLogger(this.getClass());
        PropertyConfigurator.configure(this.getClass().getClassLoader().getResource("log4j.properties"));
    }
    //new String(request.getParameter("name").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user= new User(request.getParameter("name"),
                                                             request.getParameter("surname"),
                                                             request.getParameter("login")
                                                            );

        user = (User)userDAO.registerUser(user,hash(request.getParameter("password")));
        if(user!=null){
            String hash=hash(user.getLogin()+user.getName());
            userDAO.updateHash(hash,user.getLogin(),false);
            HttpSession session = request.getSession(true);
            session.setAttribute("hash",hash);
            session.setAttribute("role","STUDENT");
            session.setAttribute("entrantStatus","UNDEFINED");
            if(session.getAttribute("chosen_course")!=null)
                response.sendRedirect("enroll");
            else
                response.sendRedirect("login");
        }
        else {
            request.setAttribute("error_message","Login already exists");
            request.getRequestDispatcher("/WEB-INF/jspFiles/register.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        HttpSession session = request.getSession(true);
        String hash = (String)session.getAttribute("hash");
        if(hash!=null){
            userDAO.updateHash(hash,null,true);
            session.removeAttribute("hash");
            session.removeAttribute("role");
        }
        request.getRequestDispatcher("/WEB-INF/jspFiles/register.jsp").forward(request, response);
    }
}
