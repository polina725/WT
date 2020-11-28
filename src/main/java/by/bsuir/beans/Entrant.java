package by.bsuir.beans;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Entrant extends User {
    private int subjectAmount = 3;
    private int certificate;
    private Subject[] subjects = new Subject[subjectAmount];
    private String status;
    private String department;
    private int courseId;
    private String course;
    private int totalScore=0;

    public Entrant(int id){
        super(id);
    }

    public Entrant(ResultSet set) {
        super(set);
        try {
            status=set.getString("status");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addSubjectsAndCourseInfo(ResultSet set){
        try {
            String[] subjectsNames = set.getString("subjects").split(",");
            String[] subjectsIds= set.getString("subject_id").split(",");
            String[] subjectsScores= set.getString("scores").split(",");
            if(subjectsIds.length!=0)
                for(int i=0;i<subjectAmount;i++) {
                    subjects[i] = new Subject(subjectsNames[i], Integer.parseInt(subjectsIds[i]), Integer.parseInt(subjectsScores[i]));
                }
            certificate = set.getInt("certificate_score");
            totalScore=set.getInt("total_score");
            department=set.getString("department");
            course=set.getString("course");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getCertificate() {
        return certificate;
    }

    public void setCertificate(int certificate) {
        this.certificate = certificate;
    }

    public Subject[] getSubjects() {
        return subjects;
    }

    /*public void setSubjects(Subject[] subjects) {
        this.subjects = subjects;
    }*/

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setSubject(int subjInd, String key, HttpSession session, HttpServletRequest request){
        String tmp =(String)session.getAttribute(key);
        getSubjects()[subjInd] = new Subject(tmp,
                (int)session.getAttribute(key+"_id"),
                Integer.parseInt(request.getParameter(tmp))
        );
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void calcTotalScore() {
        for(Subject s : subjects)
            this.totalScore+=s.getScore();
        this.totalScore+=certificate;
    }

    public String getDepartment() {
        return department;
    }
}
