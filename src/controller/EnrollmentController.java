package controller;

import beans.Course;
import beans.Entrant;
import beans.Subject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import static service.SomeAction.*;

public class EnrollmentController extends HttpServlet {
    final static Logger log;
    static{
        log = Logger.getLogger(Controller.class.getName());
        try {
            log.addHandler(new FileHandler("D:/log.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        Entrant entrant = (Entrant) getUserByHash((String)session.getAttribute("hash"),true);
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
        //request.getRequestDispatcher("/WEB-INF/jspFiles/enroll.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        String courseName= (String)session.getAttribute("chosen_course");
        Course c = getCourseInfo(courseName);
        session.setAttribute("first_s_id",c.getSubjects()[0].getId());
        session.setAttribute("second_s_id",c.getSubjects()[1].getId());
        session.setAttribute("first_s",c.getSubjects()[0].getName());
        session.setAttribute("second_s",c.getSubjects()[1].getName());
        request.setAttribute("course",c);
  //      request.setAttribute("error_message",session.getAttribute("chosen_course")+"\n"+session.getAttribute("hash"));
        request.getRequestDispatcher("/WEB-INF/jspFiles/enroll.jsp").forward(request, response);
    }

}
