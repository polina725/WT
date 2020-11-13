package service;

import beans.*;
import dao.DAO;
import exception.RegisterException;

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

    private static DAO dao = new DAO();

    final static Logger log;
    static{
        log = Logger.getLogger(DAO.class.getName());
        try {
            log.addHandler(new FileHandler("D:/log.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CommonUserInstance login(String login, String password) throws RegisterException {
        CommonUserInstance user = null;
        try {
            user = (CommonUserInstance)dao.getRegisteredUser(login,hash(password));
        } catch (SQLException e) {
            log.info(DAO.getExceptionStackTrace(e));
            throw new RegisterException("Data base problems");
        }
        return user;
    }

    public static String createUserHashForSession(CommonUserInstance user) throws RegisterException {
        String hashForSession=hash(user.getLogin()+user.getName());
        try {
            log.info(hashForSession);
            dao.updateHash(hashForSession,user.getLogin(),false);
        } catch (SQLException e) {
            log.info(DAO.getExceptionStackTrace(e));
            throw new RegisterException("Some troubles");
        }
        return hashForSession;
    }

    public static void logOut(String hash){
        try {
            dao.updateHash(hash,null,true);
        } catch (SQLException e) {}
    }

    public static String register(CommonUserInstance unregUser, String password) throws RegisterException {
        CommonUserInstance user = null;
        try {
            user = (CommonUserInstance)dao.registerUser(unregUser,hash(password));
        } catch (SQLException e) {
            throw new RegisterException("Login already exists");
        }
        if(user!=null) {
            log.info("user not null in registration");//TODO user==null => not registered => exception => won't execute code after <throw new...> ??????????
            return createUserHashForSession(user);
        }
        return null;
    }

    public static CommonUserInstance getUserByHash(String hash, boolean student) {
        CommonUserInstance user;
        try {
            if (student)
                user = dao.getEntrantByHash(hash);
            else
                user = dao.getUserByHash(hash);
        } catch (SQLException e) {
            log.info(DAO.getExceptionStackTrace(e));
            return null;
        }
        return user;
    }

    public static ArrayList<Department> getDepartmentsAndCourses() throws RegisterException {
        try {
            return dao.getDepartmentsAndItsCoursesNames();
        } catch (SQLException e) {
            throw new RegisterException("double troubles");
        }
    }

    public static void addEntrantEnrollmentInfo(Entrant entrant,String stateLanguage,int score){
        int stateLanId = dao.getStateLanguageId(stateLanguage);
        if(stateLanId!=-1)
            entrant.getSubjects()[2]=new Subject("",stateLanId,score);
        int courseId = dao.getCourseIdByName(entrant.getCourse());
        entrant.setCourseId(courseId);
        dao.pushEntrantEnrollmentData(entrant);
    }

    public static Course getCourseInfo(String courseName){
        return dao.getCourseFullInfo(courseName);
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
