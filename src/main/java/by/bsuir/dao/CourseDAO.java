package by.bsuir.dao;

import by.bsuir.beans.Course;
import by.bsuir.beans.Department;
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

public class CourseDAO {

    private Logger logger;

    public CourseDAO() {
        logger = Logger.getLogger(this.getClass());
        PropertyConfigurator.configure(this.getClass().getClassLoader().getResource("log4j.properties"));
    }

    private String ALL_COURSES_SELECT_QUERY =
            "SELECT course.*, " +
                    "       department.name as department " +
                    "FROM course\n" +
                    "         JOIN department ON department.iddepartment = course.department_iddepartment\n" +
                    "GROUP BY course.idcourse;";

    public ArrayList<Department> getDepartmentsAndItsCoursesNames(){
        ArrayList<Department> departments = null;
        try(Connection connection = ConnectionPool.getInstance().connect();
            PreparedStatement st = connection.prepareStatement(ALL_COURSES_SELECT_QUERY)) {
            ResultSet set= st.executeQuery();
            HashMap<String,Department> map = new HashMap<>();
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
            departments=new ArrayList<>();
            for(Map.Entry<String,Department> el : map.entrySet()){
                departments.add(el.getValue());
            }
        } catch (SQLException e) {
            logger.error(getExceptionStackTrace(e));
        }
        return departments;
    }
}
