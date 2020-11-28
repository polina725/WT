package by.bsuir.controller;

import by.bsuir.beans.Course;
import by.bsuir.service.EnrollmentProcess;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class AdminController extends HttpServlet {
    private EnrollmentProcess pr = new EnrollmentProcess();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        if(request.getParameter("submit")!=null) {
            pr.stopEnrollmentCampaign();
            pr.changeStudentStatus();
        }
        request.getRequestDispatcher("/WEB-INF/jspFiles/admin.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        ArrayList<Course> l = pr.getCourses();
        request.setAttribute("courses",l);
        request.getRequestDispatcher("/WEB-INF/jspFiles/admin.jsp").forward(request, response);
    }
}
