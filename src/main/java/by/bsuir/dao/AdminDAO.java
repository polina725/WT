package by.bsuir.dao;

import by.bsuir.beans.Course;
import by.bsuir.beans.Entrant;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static by.bsuir.service.Utilities.getExceptionStackTrace;

public class AdminDAO {
    private Logger logger;
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

    public AdminDAO(){
        logger = Logger.getLogger(this.getClass());
        PropertyConfigurator.configure(this.getClass().getClassLoader().getResource("log4j.properties"));
    }

    public HashMap<Integer,Course> getCourses(){
        if(courses==null){
            courses = getCoursesIds();
            setStudentCount();
        }
        return this.courses;
    }

    public void insertRecord(){
        try(Connection connection = ConnectionPool.getInstance().connect();
            PreparedStatement st = connection.prepareStatement(CREATE_RECORD_FOR_COURSE))     {
            for(Map.Entry<Integer,Course> course : courses.entrySet()){
                st.setInt(1,course.getKey());
                st.setInt(2,course.getValue().getTotalStudentCount());
                st.executeUpdate();
            }
        }catch(SQLException e){
            logger.error(getExceptionStackTrace(e));
        }
    }

    private void setStudentCount(){
        try(Connection connection = ConnectionPool.getInstance().connect();
            PreparedStatement st = connection.prepareStatement(ENTRANT_COUNT)) {
            ResultSet set= st.executeQuery();
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
        } catch (SQLException e) {
           logger.error(getExceptionStackTrace(e));
        }
    }

    private HashMap<Integer,Course> getCoursesIds(){
        HashMap<Integer, Course> l = null;
        try(Connection connection = ConnectionPool.getInstance().connect();
            PreparedStatement st = connection.prepareStatement(COURSE_ID_SELECT)) {
            ResultSet set= st.executeQuery();
            l= new HashMap<>();
            while(set.next()){
                Course c = new Course(set.getInt("idcourse"),
                        set.getString("name"),
                        set.getInt("enrollment_plan_budget"),
                        set.getInt("enrollment_plan_fee"));
                l.put(c.getIdCourse(), c);
            }
        } catch (SQLException e) {
            logger.error(getExceptionStackTrace(e));
        }
        return l;
    }

    public void changeStudentStatus(){
        for(Map.Entry<Integer, Course> entry : courses.entrySet()){
            int idCourse = entry.getValue().getIdCourse();
            ArrayList<Entrant> entrants = getSortedEntrantsIds(idCourse);
            updateEntrantStatus(entrants,idCourse);
        }
    }

    private void updateEntrantStatus(ArrayList<Entrant> entrants,int courseId){
        Connection connection = ConnectionPool.getInstance().connect();
        if(connection==null){
            logger.error("Connection wasn't opened");
            return;
        }
        int i=0;
        int budget_places_amount = courses.get(courseId).getBudgetPlacesAmount();
        int fee_places_amount = courses.get(courseId).getFeePlacesAmount();
        for(Entrant entrant : entrants){
            try(PreparedStatement st = connection.prepareStatement(CHANGE_ENTRANT_STATUS)) {
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
               logger.error(getExceptionStackTrace(e));
            }
        }
        try {
            connection.close();
        } catch (SQLException ignored) { }
    }

    private ArrayList<Entrant> getSortedEntrantsIds(int idCourse){
        ArrayList<Entrant> l=null;
        try(Connection connection = ConnectionPool.getInstance().connect();
            PreparedStatement st = connection.prepareStatement(ENTRANT_IDS)) {
             st.setInt(1, idCourse);
            ResultSet set = st.executeQuery();
            l = new ArrayList<>();
            while(set.next()){
                Entrant e = new Entrant(set.getInt("identrant"));
                l.add(e);
            }
        } catch (SQLException e) {
            logger.error(getExceptionStackTrace(e));
        }
        return l;
    }
}
