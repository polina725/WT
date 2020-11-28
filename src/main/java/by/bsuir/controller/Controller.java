package by.bsuir.controller;

import by.bsuir.beans.Department;
import by.bsuir.dao.CourseDAO;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;


public class Controller extends HttpServlet {

    private Logger logger;
    private HttpSession session;
    private CourseDAO courseDAO = new CourseDAO();

    public Controller() {
        logger = Logger.getLogger(this.getClass());
        PropertyConfigurator.configure(this.getClass().getClassLoader().getResource("log4j.properties"));
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        session = request.getSession(true);
        if(session.getAttribute("hash")==null) {
            session.setAttribute("chosen_course", request.getParameter("link_button"));
            response.sendRedirect("login");
        }
        else{
            session.setAttribute("chosen_course",request.getParameter("link_button"));
            response.sendRedirect("enroll");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        session = request.getSession(true);
        ArrayList<Department> l = courseDAO.getDepartmentsAndItsCoursesNames();
        request.setAttribute("departments",l);
        request.getRequestDispatcher("/WEB-INF/jspFiles/departments.jsp").forward(request, response);
    }
}

