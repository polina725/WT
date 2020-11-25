package by.bsuir.controller;

import by.bsuir.beans.Course;
import by.bsuir.service.EnrollmentProcess;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminController extends HttpServlet {
    private EnrollmentProcess pr = new EnrollmentProcess();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        if(request.getParameter("submit")!=null) {

            pr.stopEnrollmentCampaign();
            try {
                pr.changeStudentStatus();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        request.getRequestDispatcher("/WEB-INF/jspFiles/admin.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        ArrayList<Course> l = null;
        try {
            l =  pr.getCourses();
        } catch (SQLException e) {
            request.setAttribute("error_message",e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jspFiles/admin.jsp").forward(request, response);
        }
        request.setAttribute("courses",l);
        request.getRequestDispatcher("/WEB-INF/jspFiles/admin.jsp").forward(request, response);
    }
}
