package by.bsuir.dao;

import by.bsuir.beans.Course;
import by.bsuir.beans.Department;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class CourseDAO {
    final static Logger log;
    static{
        log = Logger.getLogger(ConnectionPool.class.getName());
        try {
            log.addHandler(new FileHandler("D:/log.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String ALL_COURSES_SELECT_QUERY =
            "SELECT course.*, " +
                    "       department.name as department " +
                    "FROM course\n" +
                    "         JOIN department ON department.iddepartment = course.department_iddepartment\n" +
                    "GROUP BY course.idcourse;";
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

    public Course getCourseFullInfo(String courseName) {
        Connection connection = ConnectionPool.getInstance().connect();
        ResultSet set;
        try {
            PreparedStatement st = connection.prepareStatement(COURSE_SELECT_QUERY);
            st.setString(1,courseName);
            set= st.executeQuery();
        } catch (SQLException e) {
           // log.info(getExceptionStackTrace(e));
            try {
                connection.close();
            } catch (SQLException ignored) {}
            //throw e;
            return null;
        }
        Course course = null;
        try {
            if(set.next())
                course = new Course(set);
        } catch (SQLException e) {
           // log.info(e.getMessage());
        }
        try {
            connection.close();
        } catch (SQLException e) {
          //  log.info(e.getMessage());
        }
        return course;
    }

    public ArrayList<Department> getDepartmentsAndItsCoursesNames() throws SQLException {
        Connection connection = ConnectionPool.getInstance().connect();
        ResultSet set;
        try {
            PreparedStatement st = connection.prepareStatement(ALL_COURSES_SELECT_QUERY);
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
        HashMap<String,Department> map = new HashMap<>();
        try {
            while (set.next()) {
                String key = set.getString("department");
                if(!map.containsKey(key)){
                    map.put(key,new Department(key,set.getInt("department_iddepartment")));
                }
                Department d = map.get(key);
                d.addCourse(new Course(
                        set.getInt("idcourse"),
                        set.getString("name"),
                        set.getInt("enrollment_plan_budget"),
                        set.getInt("enrollment_plan_fee")
                ));
                map.replace(key,d);
            }
        }catch(SQLException e){
          //  log.info(e.getMessage());
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
           // log.info(e.getMessage());
        }
        ArrayList<Department> departments = new ArrayList<>();
        for(Map.Entry<String,Department> el : map.entrySet()){
            departments.add(el.getValue());
        }
        return departments;
    }
}
