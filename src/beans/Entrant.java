package beans;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Entrant extends CommonUserInstance {
    private int subjectAmount = 3;
    private int certificate;
    private Subject[] subjects = new Subject[subjectAmount];
    private String status;
    private int departmentId;
    private String department;
    private int courseId;
    private String course;

    public Entrant(ResultSet set) {
        super(set);
        try {
            String[] subjectsNames = set.getString("subjects").split(",");
            String[] subjectsIds= set.getString("subject_id").split(",");
            String[] subjectsScores= set.getString("subject_score").split(",");
            for(int i=0;i<subjectAmount;i++)
                subjects[i]=new Subject(subjectsNames[i], Integer.parseInt(subjectsIds[i]),Integer.parseInt(subjectsScores[i]));
            certificate = set.getInt("certificate_score");
            status=set.getString("status");
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
        String tmp =new String(((String)session.getAttribute(key)).getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        getSubjects()[subjInd] = new Subject((String)session.getAttribute(key),
                (int)session.getAttribute(key+"_id"),
                Integer.parseInt(request.getParameter(tmp))
        );
    }
}
