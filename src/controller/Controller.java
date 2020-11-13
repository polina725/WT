package controller;

import beans.Course;
import beans.Department;
import dao.DAO;
import exception.RegisterException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import static service.SomeAction.*;


public class Controller extends HttpServlet {
    final static Logger log;
    static{
        log = Logger.getLogger(Controller.class.getName());
        try {
            log.addHandler(new FileHandler("D:/log.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    HttpSession session;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();
        session = request.getSession(true);
        if(session.getAttribute("hash")==null) {
            session.setAttribute("chosen_course", request.getParameter("link_button"));
            response.sendRedirect("login");
        }
        else{
            doRedirect(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        session = request.getSession(true);
        String action = request.getServletPath();
        switch(action){
            case "/departments":
                showDepartmentsPage(request,response);
                break;
        }
    }

    private void showDepartmentsPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArrayList<Department> l = null;
        try {
            l = getDepartmentsAndCourses();
        } catch (RegisterException e) {
            request.setAttribute("error_message",e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jspFiles/departments.jsp").forward(request, response);
        }
        request.setAttribute("departments",l);

        request.getRequestDispatcher("/WEB-INF/jspFiles/departments.jsp").forward(request, response);
    }

    private void doRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        session.setAttribute("chosen_course",request.getParameter("link_button"));
        response.sendRedirect("enroll");
    }

}

