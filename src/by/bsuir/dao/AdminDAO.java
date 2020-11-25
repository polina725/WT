package by.bsuir.dao;

import by.bsuir.beans.Course;
import by.bsuir.beans.Entrant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminDAO {
    private String CREATE_RECORD_FOR_COURSE = "INSERT INTO record (course_idcourse, total_student_count) VALUES (?,?);";
    private String COURSE_ID_SELECT = "SELECT idcourse,\n" +
            "       name,\n" +
            "       enrollment_plan_budget,\n" +
            "       enrollment_plan_fee\n" +
            "FROM course\n" +
            "GROUP BY idcourse";

    private String ENTRANT_COUNT = "SELECT idcourse,\n" +
            "       COUNT(entrant.course_idcourse) as num\n" +
            "FROM course\n" +
            "    JOIN entrant ON entrant.course_idcourse = course.idcourse\n" +
            "GROUP BY idcourse;";

    private String ENTRANT_IDS = "SELECT identrant FROM entrant WHERE course_idcourse = ? ORDER BY total_score;";

    private String CHANGE_ENTRANT_STATUS = "UPDATE entrant SET enrollment_status=? WHERE identrant=? and course_idcourse = ?;";

    private HashMap<Integer,Course> courses;

    public HashMap<Integer,Course> getCourses() throws SQLException {
        if(courses==null){
            courses = getCoursesIds();
            setStudentCount();
        }
        return this.courses;
    }

    public void insertRecord() throws SQLException {
        Connection connection = ConnectionPool.getInstance().connect();
        for(Map.Entry<Integer,Course> course : courses.entrySet()){
            try{
                PreparedStatement st = connection.prepareStatement(CREATE_RECORD_FOR_COURSE);
                st.setInt(1,course.getKey());
                st.setInt(2,course.getValue().getTotalStudentCount());
                st.executeUpdate();
            } catch (SQLException e) {
                //  log.info(getExceptionStackTrace(e));
                try {
                    connection.close();
                    //  log.info(e.getMessage());
                } catch (SQLException ex) {
                    //log.info(ex.getMessage());
                }
                throw e;
            }
        }
        connection.close();
    }

    private void setStudentCount() throws SQLException {
        Connection connection = ConnectionPool.getInstance().connect();
        ResultSet set;
        try {
            PreparedStatement st = connection.prepareStatement(ENTRANT_COUNT);
            set= st.executeQuery();
        } catch (SQLException e) {
            //  log.info(getExceptionStackTrace(e));
            try {
                connection.close();
                //  log.info(e.getMessage());
            } catch (SQLException ex) {
                //log.info(ex.getMessage());
            }
            throw e;
        }
        while(set.next()){
            int count = set.getInt("num");
            int id = set.getInt("idcourse");
            Course c = courses.get(id);
            if(count<c.getBudgetPlacesAmount()+c.getFeePlacesAmount())
                c.setTotalStudentCount(count);
            else
                c.setTotalStudentCount(c.getBudgetPlacesAmount()+c.getFeePlacesAmount());
            courses.replace(id,c);
        }
        connection.close();
    }

    private HashMap<Integer,Course> getCoursesIds() throws SQLException {
        Connection connection = ConnectionPool.getInstance().connect();
        ResultSet set;
        try {
            PreparedStatement st = connection.prepareStatement(COURSE_ID_SELECT);
            set= st.executeQuery();
        } catch (SQLException e) {
            //  log.info(getExceptionStackTrace(e));
            try {
                connection.close();
                //  log.info(e.getMessage());
            } catch (SQLException ex) {
                //log.info(ex.getMessage());
            }
            throw e;
        }
        HashMap<Integer, Course> l= new HashMap<>();
        while(set.next()){
            Course c = new Course(set.getInt("idcourse"),
                    set.getString("name"),
                    set.getInt("enrollment_plan_budget"),
                    set.getInt("enrollment_plan_fee"));
           // c.setTotalStudentCount(set.getInt("num"));
            l.put(c.getIdCourse(), c);
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // log.info(e.getMessage());
        }
        return l;
    }

    public void changeStudentStatus() throws SQLException {
        for(Map.Entry<Integer, Course> entry : courses.entrySet()){
            int idCourse = entry.getValue().getIdCourse();
            ArrayList<Entrant> entrants = getSortedEntrantsIds(idCourse);
            updateEntrantStatus(entrants,idCourse);
        }
    }

    private void updateEntrantStatus(ArrayList<Entrant> entrants,int courseId) throws SQLException {
        Connection connection = ConnectionPool.getInstance().connect();
        int i=0;
        int budget_places_amount = courses.get(courseId).getBudgetPlacesAmount();
        int fee_places_amount = courses.get(courseId).getFeePlacesAmount();
        for(Entrant entrant : entrants){
            try {
                PreparedStatement st = connection.prepareStatement(CHANGE_ENTRANT_STATUS);
                if(i<budget_places_amount)
                    st.setString(1,"ENROLLED_BUDGET");
                else if(i<fee_places_amount)
                    st.setString(1,"ENROLLED_FEE");
                else
                    st.setString(1,"UNENROLLED");
                st.setInt(3, courseId);
                st.setInt(2, entrant.getId());
                st.executeUpdate();
                i++;
            } catch (SQLException e) {
                //  log.info(getExceptionStackTrace(e));
                try {
                    connection.close();
                    //  log.info(e.getMessage());
                } catch (SQLException ex) {
                    //log.info(ex.getMessage());
                }
                throw e;
            }
        }
        connection.close();
    }

    private ArrayList<Entrant> getSortedEntrantsIds(int idCourse) throws SQLException {
        Connection connection = ConnectionPool.getInstance().connect();
        ResultSet set;
        try {
             PreparedStatement st = connection.prepareStatement(ENTRANT_IDS);
             st.setInt(1, idCourse);
             set = st.executeQuery();
        } catch (SQLException e) {
            //  log.info(getExceptionStackTrace(e));
            try {
                connection.close();
                //  log.info(e.getMessage());
            } catch (SQLException ex) {
                //log.info(ex.getMessage());
            }
            throw e;
        }
        ArrayList<Entrant> l = new ArrayList<>();
        while(set.next()){
            Entrant e = new Entrant(set.getInt("identrant"));
            l.add(e);
        }
        connection.close();
        return l;
    }
}
