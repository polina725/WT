package by.bsuir.service;

import by.bsuir.beans.CommonUser;
import by.bsuir.beans.Entrant;
import by.bsuir.dao.UserDAO;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Utilities {

    private static UserDAO userDAO = new UserDAO();

    public static CommonUser getUserByHash(String hash, boolean student, boolean additionalInfo) {
        CommonUser user;
        if (student) {
            user = userDAO.getUserByHash(hash, true);
            if(!((Entrant)user).getStatus().equals("UNDEFINED") && additionalInfo)
                user = userDAO.getEntrantsSubjectsAndCourse((Entrant)user);
        }
        else
            user = userDAO.getUserByHash(hash,false);
        return user;
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

    public static String getExceptionStackTrace(Exception e){
        String ex=e.getMessage()+"\n";
        StackTraceElement[] elements = e.getStackTrace();
        for(StackTraceElement el : elements){
            ex+=el.toString()+"\n";
        }
        return ex;
    }
}
