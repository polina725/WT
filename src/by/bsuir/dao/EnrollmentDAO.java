package by.bsuir.dao;

import by.bsuir.beans.Entrant;
import by.bsuir.beans.Subject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnrollmentDAO {
    private String SUBJECT_ID_BY_NAME_QUERY = "SELECT idsubject FROM subject WHERE name=?;";
    private String COURSE_ID_BY_NAME_QUERY = "SELECT idcourse FROM course WHERE name=?;";
    private String ENTRANTS_SUBJECT_SET_INSERT_QUERY  = "INSERT INTO entrant_has_subject  VALUES (?,?,?);";
    private String ENTRANTS_COURSE_AND_CERTIFICATE_SET_QUERY ="UPDATE entrant SET enrollment_status='ENROLLING',course_idcourse=?, certificate_score=?, total_score=? WHERE identrant=?;";

    public int getStateLanguageId(String stateLanCode){
        Connection connection = ConnectionPool.getInstance().connect();
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(SUBJECT_ID_BY_NAME_QUERY);
            if(stateLanCode.equals("ru"))
                st.setString(1,"русский язык");
            else
                st.setString(1,"белорусский язык");
            ResultSet result = st.executeQuery();
            if(result.next())
                return result.getInt("idsubject");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getCourseIdByName(String name){
        Connection connection = ConnectionPool.getInstance().connect();
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(COURSE_ID_BY_NAME_QUERY);
            st.setString(1,name);
            ResultSet result = st.executeQuery();
            if(result.next())
                return result.getInt("idcourse");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void deleteEntrantsSubjects(Entrant entrant) {
        String query = " DELETE FROM entrant_has_subject WHERE entrant_identrant=?;";
        Connection connection = ConnectionPool.getInstance().connect();
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            st.setInt(1,entrant.getId());
            st.executeUpdate();
        } catch (Exception e) { }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void pushEntrantEnrollmentData(Entrant entrant){
        Connection connection = ConnectionPool.getInstance().connect();
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(ENTRANTS_SUBJECT_SET_INSERT_QUERY);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(Subject s : entrant.getSubjects()){
            try {
                st.setInt(1, entrant.getId());
                st.setInt(2, s.getId());
                st.setInt(3,s.getScore());
                st.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
/*        try {
            st.close();
        } catch (SQLException e) {
            log.info(getExceptionStackTrace(e));
        }*/
        PreparedStatement st1 = null;
        try {
            st1=connection.prepareStatement(ENTRANTS_COURSE_AND_CERTIFICATE_SET_QUERY);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            st1.setInt(1,entrant.getCourseId());
            st1.setInt(2,entrant.getCertificate());
            st1.setInt(3,entrant.getTotalScore());
            st1.setInt(4,entrant.getId());
            st1.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

/*        try {
            st1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
