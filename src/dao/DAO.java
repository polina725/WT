package dao;

import beans.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class DAO {

    final static Logger log;
    static{
        log = Logger.getLogger(DAO.class.getName());
        try {
            log.addHandler(new FileHandler("D:/log.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String DATABASE_URL ="jdbc:mysql://localhost:3306/";
    private String DATABASE_NAME = "enrollment";
    private String DATABASE_USER_NAME = "user";
    private String DATABASE_PASSWORD = "12345";
    private String OPTIONS = "?serverTimezone=Europe/Minsk&allowMultiQueries=true"; //useUnicode=true&characterEncoding=UTF8&

    private String USER_INSERT_QUERY = "INSERT INTO user ( login , password , user.name , surname , role ) VALUES ( ? , ? , ? , ? , 'STUDENT');"+
                                  "INSERT INTO entrant ( identrant ) VALUES ( LAST_INSERT_ID() );";
    private String USER_SELECT_QUERY = "SELECT * FROM user WHERE user.login = ? and user.password = ?;";
    private String USER_BY_HASH_QUERY = "SELECT * FROM user WHERE user.hash = ?;";
    private String ENTRANT_BY_HASH_QUERY = "SELECT    iduser," +
                                              "       login,\n" +
                                              "       name,\n" +
                                              "       surname,\n" +
                                              "       role,\n" +
                                              "       (entrant.enrollment_status) as status\n" +
                                              "FROM user\n" +
                                              "    JOIN entrant ON entrant.identrant=user.iduser\n" +
                                              "    WHERE user.hash=?\n" +
                                              "GROUP BY user.iduser;";
    private String HASH_SET_QUERY = "UPDATE user SET hash=? WHERE login=?;";
    private String HASH_DELETE_QUERY = "UPDATE user SET hash=null WHERE hash=?;";
    private static String ALL_COURSES_SELECT_QUERY =
                                                            "SELECT course.*, " +
                                                            "       department.name as department " +
                                                            "FROM course\n" +
                                                            "         JOIN department ON department.iddepartment = course.department_iddepartment\n" +
                                                            "GROUP BY course.idcourse;";
    private static String COURSE_SELECT_QUERY=
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
    //TODO for en name column
    private static String SUBJECT_ID_BY_NAME_QUERY = "SELECT idsubject FROM subject WHERE name=?;";
    private static String COURSE_ID_BY_NAME_QUERY = "SELECT idcourse FROM course WHERE name=?;";
    private static String ENTRANTS_SUBJECT_SET_INSERT_QUERY  = "INSERT INTO entrant_has_subject  VALUES (?,?,?);";
    private static String ENTRANTS_COURSE_AND_CERTIFICATE_SET_QUERY ="UPDATE entrant SET course_idcourse=?, certificate_score=? WHERE identrant=?;";
    
    private Connection connect() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DATABASE_URL+ DATABASE_NAME + OPTIONS,DATABASE_USER_NAME,DATABASE_PASSWORD);
        } catch (SQLException e) {
            log.info(getExceptionStackTrace(e));
        } catch (ClassNotFoundException e) {
            log.info(getExceptionStackTrace(e));
        }
        return connection;
    }

    public User getRegisteredUser(String loginOrSessionHash, String hashPassword) throws SQLException {
        Connection connection = connect();
        if(connection == null)
            throw new SQLException("Couldn't connect data base");
        ResultSet result;
        try {
            PreparedStatement st ;
            if(hashPassword!=null) {
                st = connection.prepareStatement(USER_SELECT_QUERY);
                st.setString(2,hashPassword);
            }
            else{
                if(ENTRANT)
                    st = connection.prepareStatement(ENTRANT_BY_HASH_QUERY);
                else
                    st = connection.prepareStatement(USER_BY_HASH_QUERY);
            }
            st.setString(1,loginOrSessionHash);
            result = st.executeQuery();
        } catch (SQLException e) {
            log.info(getExceptionStackTrace(e));
            try {
                connection.close();
            } catch (SQLException ignored) { }
            throw e;
        }

        if(!result.next()) {
            log.info("false");
            return null;
        }
        User user;
        if(hashPassword!=null || !ENTRANT) {
            user = new CommonUserInstance(result);
            ENTRANT = false;
        }
        else
            user = new Entrant(result);
        try {
            connection.close();
        } catch (SQLException e) {
            log.info(getExceptionStackTrace(e));
            return user;
        }
        return user;
    }

    public void updateHash(String hash,String login,boolean delete) throws SQLException {
        Connection connection = connect();
        if(connection==null)
            throw new SQLException();
        PreparedStatement st;
        if(delete)
            st = connection.prepareStatement(HASH_DELETE_QUERY);
        else {
            st = connection.prepareStatement(HASH_SET_QUERY);
            st.setString(2,login);
        }
        st.setString(1,hash);
        st.executeUpdate();
        try {
            connection.close();
        }catch(SQLException e){
            log.info(getExceptionStackTrace(e));
        }
    }

    public CommonUserInstance getUserByHash(String hash) throws SQLException {
        return (CommonUserInstance)getRegisteredUser(hash,null);
    }
    private static boolean ENTRANT=false;       
    public Entrant getEntrantByHash(String hash) throws SQLException {
        ENTRANT=true;
        return (Entrant) getRegisteredUser(hash,null);
    }

    public User registerUser(CommonUserInstance unregUser, String password) throws SQLException {
        Connection conn = connect();
        if(conn==null)
            throw new SQLException();
        try {
            PreparedStatement st = conn.prepareStatement(USER_INSERT_QUERY);
            st.setString(1,unregUser.getLogin());
            st.setString(2,password);
            st.setString(3,unregUser.getName());
            st.setString(4,unregUser.getSurname());
            log.info(st.toString());
            st.executeUpdate();
        } catch (SQLException e) {
            log.info(getExceptionStackTrace(e));
            throw e;
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getRegisteredUser(unregUser.getLogin(),password);
    }

    public Course getCourseFullInfo(String courseName) {
        Connection connection = connect();
        ResultSet set;
        try {
            PreparedStatement st = connection.prepareStatement(COURSE_SELECT_QUERY);
            st.setString(1,courseName);
            set= st.executeQuery();
        } catch (SQLException e) {
            log.info(getExceptionStackTrace(e));
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
            log.info(e.getMessage());
        }
        try {
            connection.close();
        } catch (SQLException e) {
            log.info(e.getMessage());
        }
        return course;
    }

    public ArrayList<Department> getDepartmentsAndItsCoursesNames() throws SQLException {
        Connection connection = connect();
        ResultSet set;
        try {
            PreparedStatement st = connection.prepareStatement(ALL_COURSES_SELECT_QUERY);
            set= st.executeQuery();
        } catch (SQLException e) {
            log.info(getExceptionStackTrace(e));
            try {
                connection.close();
                log.info(e.getMessage());
            } catch (SQLException ex) { log.info(ex.getMessage());}
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
            log.info(e.getMessage());
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
        ArrayList<Department> departments = new ArrayList<>();
        for(Map.Entry<String,Department> el : map.entrySet()){
            departments.add(el.getValue());
        }
        return departments;
    }

    public int getStateLanguageId(String stateLanCode){
        Connection connection = connect();
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
        Connection connection = connect();
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

    public void pushEntrantEnrollmentData(Entrant entrant){
        Connection connection = connect();
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
            st1.setInt(3,entrant.getId());
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

    public static String getExceptionStackTrace(Exception e){
        String ex=e.getMessage()+"\n";
        StackTraceElement[] elements = e.getStackTrace();
        for(StackTraceElement el : elements){
            ex+=el.toString()+"\n";
        }
        return ex;
    }
}


// TODO connection==null case + exceptions