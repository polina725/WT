package by.bsuir.controller;

import by.bsuir.beans.Course;
import by.bsuir.beans.Entrant;
import by.bsuir.beans.Subject;
import by.bsuir.dao.EnrollmentDAO;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static by.bsuir.service.Utilities.*;

public class EnrollmentController extends HttpServlet {
    private Logger logger;
    private EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

    public EnrollmentController(){
        logger = Logger.getLogger(this.getClass());
        PropertyConfigurator.configure(this.getClass().getClassLoader().getResource("log4j.properties"));
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(true);
        Entrant entrant = (Entrant) getUserByHash((String)session.getAttribute("hash"),true,false);
        entrant.setCertificate(Integer.parseInt(request.getParameter("certificate_score")));
        entrant.setSubject(0,"first_s",session,request);
        entrant.setSubject(1,"second_s",session,request);
        entrant.setCourse((String)session.getAttribute("chosen_course"));
        entrant.setCertificate(Integer.parseInt(request.getParameter("certificate_score")));
        addEntrantEnrollmentInfo(entrant,request.getParameter("language"),Integer.parseInt(request.getParameter("state_lan")));
        session.removeAttribute("chosen_course");
        session.removeAttribute("first_s");
        session.removeAttribute("second_s");
        session.removeAttribute("first_s_id");
        session.removeAttribute("second_s_id");
        response.sendRedirect("departments");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        String courseName= (String)session.getAttribute("chosen_course");
        Course c = enrollmentDAO.getCourseFullInfo(courseName);
        session.setAttribute("first_s_id",c.getSubjects()[0].getId());
        session.setAttribute("second_s_id",c.getSubjects()[1].getId());
        session.setAttribute("first_s",c.getSubjects()[0].getName());
        session.setAttribute("second_s",c.getSubjects()[1].getName());
        request.setAttribute("course",c);
        request.getRequestDispatcher("/WEB-INF/jspFiles/enroll.jsp").forward(request, response);
    }

    private void addEntrantEnrollmentInfo(Entrant entrant,String stateLanguage,int score){
        int stateLanId = enrollmentDAO.getStateLanguageId(stateLanguage);
        if(stateLanId!=-1) {
            entrant.getSubjects()[2] = new Subject("", stateLanId, score);
            entrant.calcTotalScore();
        }
        int courseId = enrollmentDAO.getCourseIdByName(entrant.getCourse());
        entrant.setCourseId(courseId);
        if(entrant.getStatus().equals("ENROLLING"))
            enrollmentDAO.deleteEntrantsSubjects(entrant);
        enrollmentDAO.pushEntrantEnrollmentData(entrant);
    }
}
