package by.bsuir.dao;

import by.bsuir.beans.Course;
import by.bsuir.beans.Entrant;
import by.bsuir.beans.Subject;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static by.bsuir.service.Utilities.getExceptionStackTrace;

public class EnrollmentDAO {
    private String SUBJECT_ID_BY_NAME_QUERY = "SELECT idsubject FROM subject WHERE name=?;";
    private String COURSE_ID_BY_NAME_QUERY = "SELECT idcourse FROM course WHERE name=?;";
    private String ENTRANTS_SUBJECT_SET_INSERT_QUERY  = "INSERT INTO entrant_has_subject  VALUES (?,?,?);";
    private String ENTRANTS_COURSE_AND_CERTIFICATE_SET_QUERY ="UPDATE entrant SET enrollment_status='ENROLLING',course_idcourse=?, certificate_score=?, total_score=? WHERE identrant=?;";
    private String COURSE_SELECT_QUERY=
            "WITH CourseInfo AS (\n" +
                    "    SELECT course.*,\n" +
                    "           GROUP_CONCAT(subject.name)      as subjects,\n" +
                    "           GROUP_CONCAT(subject.idsubject) as subject_id\n" +
                    "    FROM course\n" +
                    "             JOIN course_has_subject ON course_has_subject.course_idcourse = course.idcourse\n" +
                    "             LEFT JOIN subject ON subject.idsubject = course_has_subject.subject_idsubject\n" +
                    "    WHERE course.name = ?\n" +
                    "    GROUP BY course.idcourse\n" +
                    ")\n" +
                    "SELECT CourseInfo.name,\n" +
                    "       idcourse,\n" +
                    "       enrollment_plan_budget,\n" +
                    "       enrollment_plan_fee,\n" +
                    "       subjects,\n" +
                    "       subject_id,\n" +
                    "       GROUP_CONCAT(department.name) as department\n" +
                    "FROM CourseInfo JOIN department\n" +
                    "                   ON department.iddepartment = CourseInfo.department_iddepartment\n" +
                    "GROUP BY CourseInfo.idcourse;";
    private Logger logger;

    public EnrollmentDAO(){
        logger = Logger.getLogger(this.getClass());
        PropertyConfigurator.configure(this.getClass().getClassLoader().getResource("log4j.properties"));
    }

    public int getStateLanguageId(String stateLanCode){
        try(Connection connection = ConnectionPool.getInstance().connect();
            PreparedStatement st= connection.prepareStatement(SUBJECT_ID_BY_NAME_QUERY)) {
            if(stateLanCode.equals("ru"))
                st.setString(1,"русский язык");
            else
                st.setString(1,"белорусский язык");
            ResultSet result = st.executeQuery();
            if(result.next())
                return result.getInt("idsubject");
        } catch (SQLException e) {
            logger.error(getExceptionStackTrace(e));
        }
        return -1;
    }

    public int getCourseIdByName(String name){
        try( Connection connection = ConnectionPool.getInstance().connect();
             PreparedStatement st = connection.prepareStatement(COURSE_ID_BY_NAME_QUERY)) {
            st.setString(1,name);
            ResultSet result = st.executeQuery();
            if(result.next())
                return result.getInt("idcourse");
        } catch (SQLException e) {
            logger.error(getExceptionStackTrace(e));
        }
        return -1;
    }

    public void deleteEntrantsSubjects(Entrant entrant) {
        String query = " DELETE FROM entrant_has_subject WHERE entrant_identrant=?;";
        try(Connection connection = ConnectionPool.getInstance().connect();
            PreparedStatement st =connection.prepareStatement(query)) {
            st.setInt(1,entrant.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void pushEntrantEnrollmentData(Entrant entrant){
        try( Connection connection = ConnectionPool.getInstance().connect();
             PreparedStatement st = connection.prepareStatement(ENTRANTS_SUBJECT_SET_INSERT_QUERY)){
            for(Subject s : entrant.getSubjects()) {
                st.setInt(1, entrant.getId());
                st.setInt(2, s.getId());
                st.setInt(3, s.getScore());
                st.executeUpdate();
            }
        } catch (SQLException e) {
            logger.error(getExceptionStackTrace(e));
        }

        try(Connection connection = ConnectionPool.getInstance().connect();
            PreparedStatement st1 = connection.prepareStatement(ENTRANTS_COURSE_AND_CERTIFICATE_SET_QUERY)) {
            st1.setInt(1,entrant.getCourseId());
            st1.setInt(2,entrant.getCertificate());
            st1.setInt(3,entrant.getTotalScore());
            st1.setInt(4,entrant.getId());
            st1.executeUpdate();
        } catch (SQLException e) {
            logger.error(getExceptionStackTrace(e));
        }
    }

    public Course getCourseFullInfo(String courseName) {
        Course course = null;
        try(Connection connection = ConnectionPool.getInstance().connect();
            PreparedStatement st = connection.prepareStatement(COURSE_SELECT_QUERY)) {
            st.setString(1,courseName);
            ResultSet set = st.executeQuery();
            if(set.next())
                course = new Course(set);
        } catch (SQLException e) {
            logger.error(getExceptionStackTrace(e));
        }
        return course;
    }
}
