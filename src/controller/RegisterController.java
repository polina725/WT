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

import static service.SomeAction.register;

public class RegisterController extends HttpServlet {
    final static Logger log;
    static{
        log = Logger.getLogger(DAO.class.getName());
        try {
            log.addHandler(new FileHandler("D:/log.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //new String(request.getParameter("name").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //TODO show that passwords doesn't match + allowed characters(on jsp)
        CommonUserInstance unregUser= new CommonUserInstance(request.getParameter("name"),
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
            request.setAttribute("error_message","log out before registration");
            response.sendRedirect("login");
        }
        request.getRequestDispatcher("/WEB-INF/jspFiles/register.jsp").forward(request, response);
    }
}
