package by.bsuir.dao;

import by.bsuir.beans.User;
import by.bsuir.beans.Entrant;
import by.bsuir.beans.CommonUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
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
    private String ENTRANTS_SUBJECT_AND_COURSE_QUERY = "WITH EntrantInfo AS (\n" +
                                                        "    WITH SubjectInfo AS(\n" +
                                                        "        SELECT (entrant_identrant) as id,\n" +
                                                        "               GROUP_CONCAT(score) as scores,\n" +
                                                        "               GROUP_CONCAT(subject_idsubject) as subject_id,\n" +
                                                        "               GROUP_CONCAT(subject.name) as subjects\n" +
                                                        "        FROM entrant_has_subject\n" +
                                                        "                 JOIN subject ON subject.idsubject=entrant_has_subject.subject_idsubject\n" +
                                                        "              WHERE entrant_identrant=?\n" +
                                                        "        GROUP BY entrant_identrant\n" +
                                                        "    )\n" +
                                                        "    SELECT identrant,\n" +
                                                        "           certificate_score,\n" +
                                                        "           total_score,\n" +
                                                        "           course_idcourse,\n" +
                                                        "           SubjectInfo.scores,\n" +
                                                        "           SubjectInfo.subject_id,\n" +
                                                        "           SubjectInfo.subjects\n" +
                                                        "    FROM entrant\n" +
                                                        "             JOIN SubjectInfo ON SubjectInfo.id = entrant.identrant\n" +
                                                        "    -- WHERE identrant = 1\n" +
                                                        "    GROUP BY entrant.identrant\n" +
                                                        ")\n" +
                                                        "SELECT EntrantInfo.*,\n" +
                                                        "       GROUP_CONCAT(course.name) as course,\n" +
                                                        "       GROUP_CONCAT(department.name) as department\n" +
                                                        "FROM EntrantInfo\n" +
                                                        "         JOIN course ON course.idcourse=EntrantInfo.course_idcourse\n" +
                                                        "         JOIN department ON department.iddepartment =course.department_iddepartment\n" +
                                                        "GROUP BY EntrantInfo.identrant;";

    private String HASH_SET_QUERY = "UPDATE user SET hash=? WHERE login=?;";
    private String HASH_DELETE_QUERY = "UPDATE user SET hash=null WHERE hash=?;";
    private String ENTRANT_STATUS = "SELECT enrollment_status FROM entrant WHERE identrant = ?;";

    public CommonUser getRegisteredUser(String login, String hashPassword) throws SQLException {
        Connection connection = ConnectionPool.getInstance().connect();
        if(connection == null)
            throw new SQLException("Couldn't connect data base");
        ResultSet result;
        try {
            PreparedStatement st ;
            st = connection.prepareStatement(USER_SELECT_QUERY);
            st.setString(2,hashPassword);
            st.setString(1,login);
            result = st.executeQuery();
        } catch (SQLException e) {
          //  log.info(getExceptionStackTrace(e));
            try {
                connection.close();
            } catch (SQLException ignored) { }
            throw e;
        }

        if(!result.next()) {
          //  log.info("false");
            return null;
        }
        CommonUser user = new User(result);
        try {
            connection.close();
        } catch (SQLException e) {
        //    log.info(getExceptionStackTrace(e));
            return user;
        }
        return user;
    }

    public void updateHash(String hash,String login,boolean delete) throws SQLException {
        Connection connection = ConnectionPool.getInstance().connect();
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
          //  log.info(getExceptionStackTrace(e));
        }
    }

    public CommonUser getUserByHash(String hash, boolean ENTRANT) throws SQLException {
        Connection connection = ConnectionPool.getInstance().connect();
        if(connection == null)
            throw new SQLException("Couldn't connect data base");
        ResultSet result;
        try {
            PreparedStatement st ;
            if(ENTRANT)
                st = connection.prepareStatement(ENTRANT_BY_HASH_QUERY);
            else
                st = connection.prepareStatement(USER_BY_HASH_QUERY);
            st.setString(1,hash);
            result = st.executeQuery();
        } catch (SQLException e) {
            //  log.info(getExceptionStackTrace(e));
            try {
                connection.close();
            } catch (SQLException ignored) { }
            throw e;
        }
        if(!result.next()) {
            return null;
        }
        CommonUser user;
        if(!ENTRANT) {
            user = new User(result);
        }
        else
            user = new Entrant(result);
        try {
            connection.close();
        } catch (SQLException e) {
            return user;
        }
        return user;
    }

    public Entrant getEntrantsSubjectsAndCourse(Entrant entrant){
        Connection conn = ConnectionPool.getInstance().connect();
        try {
            PreparedStatement st = conn.prepareStatement(ENTRANTS_SUBJECT_AND_COURSE_QUERY);
            st.setInt(1,entrant.getId());
            ResultSet result = st.executeQuery();
            if(result.next())
                entrant.addSubjectsAndCourseInfo(result);
/*            else
                ;*/
        } catch (SQLException e) {
        }
        return entrant;
    }

    public CommonUser registerUser(User unregUser, String password) throws SQLException {
        Connection conn = ConnectionPool.getInstance().connect();
        if(conn==null)
            throw new SQLException();
        try {
            PreparedStatement st = conn.prepareStatement(USER_INSERT_QUERY);
            st.setString(1,unregUser.getLogin());
            st.setString(2,password);
            st.setString(3,unregUser.getName());
            st.setString(4,unregUser.getSurname());
           // log.info(st.toString());
            st.executeUpdate();
        } catch (SQLException e) {
          //  log.info(getExceptionStackTrace(e));
            throw e;
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getRegisteredUser(unregUser.getLogin(),password);
    }

    public String getEntrantStatus(int idEntrant) throws SQLException {
        Connection conn = ConnectionPool.getInstance().connect();
        String status="";
        try {
            PreparedStatement st = conn.prepareStatement(ENTRANT_STATUS);
            st.setInt(1,idEntrant);
            // log.info(st.toString());
            ResultSet set = st.executeQuery();
            if(set.next())
                status = set.getString("enrollment_status");
        } catch (SQLException e) {
            //  log.info(getExceptionStackTrace(e));
            throw e;
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }
}
