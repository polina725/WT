package by.bsuir.service;

import by.bsuir.beans.*;
import by.bsuir.dao.*;
import by.bsuir.exception.RegisterException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class SomeAction {

    private static UserDAO userDAO = new UserDAO();
    private static EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private static CourseDAO courseDAO = new CourseDAO();
    private static AdminDAO adminDAO = new AdminDAO();

    final static Logger log;
    static{
        log = Logger.getLogger(ConnectionPool.class.getName());
        try {
            log.addHandler(new FileHandler("D:/log.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static User login(String login, String password) throws RegisterException {
        User user = null;
        try {
            user = (User)userDAO.getRegisteredUser(login,hash(password));
        } catch (SQLException e) {
            log.info(ConnectionPool.getExceptionStackTrace(e));
            throw new RegisterException("Data base problems");
        }
        return user;
    }

    public static String createUserHashForSession(User user) throws RegisterException {
        String hashForSession=hash(user.getLogin()+user.getName());
        try {
            log.info(hashForSession);
            userDAO.updateHash(hashForSession,user.getLogin(),false);
        } catch (SQLException e) {
            log.info(ConnectionPool.getExceptionStackTrace(e));
            throw new RegisterException("Some troubles");
        }
        return hashForSession;
    }

    public static void logOut(String hash){
        try {
            userDAO.updateHash(hash,null,true);
        } catch (SQLException e) {}
    }

    public static String register(User unregUser, String password) throws RegisterException {
        User user = null;
        try {
            user = (User)userDAO.registerUser(unregUser,hash(password));
        } catch (SQLException e) {
            throw new RegisterException("Login already exists");
        }
        if(user!=null) {
            log.info("user not null in registration");//TODO user==null => not registered => by.bsuir.exception => won't execute code after <throw new...> ??????????
            return createUserHashForSession(user);
        }
        return null;
    }

    public static CommonUser getUserByHash(String hash, boolean student, boolean additionalInfo) {
        CommonUser user;
        try {
            if (student) {
                user = userDAO.getUserByHash(hash, true);
                if(!((Entrant)user).getStatus().equals("UNDEFINED") && additionalInfo)
                    user = userDAO.getEntrantsSubjectsAndCourse((Entrant)user);
            }
            else
                user = userDAO.getUserByHash(hash,false);
        } catch (SQLException e) {
            log.info(ConnectionPool.getExceptionStackTrace(e));
            return null;
        }
        return user;
    }

    public static ArrayList<Department> getDepartmentsAndCourses() throws RegisterException {
        try {
            return courseDAO.getDepartmentsAndItsCoursesNames();
        } catch (SQLException e) {
            throw new RegisterException("double troubles");
        }
    }

    public static String getStatus(int id){
        try {
            String s = userDAO.getEntrantStatus(id);
            return s;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addEntrantEnrollmentInfo(Entrant entrant,String stateLanguage,int score){
        int stateLanId = enrollmentDAO.getStateLanguageId(stateLanguage);
        if(stateLanId!=-1) {
            entrant.getSubjects()[2] = new Subject("", stateLanId, score);
            entrant.calcTotalScore();
        }
        int courseId = enrollmentDAO.getCourseIdByName(entrant.getCourse());
        entrant.setCourseId(courseId);
        if(entrant.getStatus().equals("ENROLLING"))
            enrollmentDAO.deleteEntrantsSubjects(entrant);
        enrollmentDAO.pushEntrantEnrollmentData(entrant);
    }

    public static Course getCourseInfo(String courseName){
        return courseDAO.getCourseFullInfo(courseName);
    }

    public static void insertRecord(){
        try {
            adminDAO.insertRecord();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String hash(String password){
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] encodedhash = digest.digest(
                password.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encodedhash);
    }

}
