package by.bsuir.service;

import by.bsuir.beans.Course;
import by.bsuir.dao.AdminDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EnrollmentProcess {
    private AdminDAO dao = new AdminDAO();

    public void stopEnrollmentCampaign(){
        dao.insertRecord();
    }

    public void changeStudentStatus(){
        dao.changeStudentStatus();
    }

    public ArrayList<Course> getCourses(){
        HashMap<Integer, Course> m = dao.getCourses();
        ArrayList<Course> l = new ArrayList<>();
        for(Map.Entry<Integer,Course> entry : m.entrySet()){
            l.add(entry.getValue());
        }
        return l;
    }
}
