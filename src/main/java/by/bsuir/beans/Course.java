package by.bsuir.beans;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Course {
    private int subjectAmount = 2;
    private String department;
    private int idCourse;
    private String name;
    private int budgetPlacesAmount;
    private int feePlacesAmount;
    private Subject[] subjects = new Subject[subjectAmount];
    private boolean acceptApplications=true;
    private int totalStudentCount = 0;

    public Course(ResultSet set){
        try {
            department=set.getString("department");
            idCourse=set.getInt("idcourse");
            name=set.getString("name");
            budgetPlacesAmount=set.getInt("enrollment_plan_budget");
            feePlacesAmount=set.getInt("enrollment_plan_fee");
            String[] subjectsNames = set.getString("subjects").split(",");
            String[] subjectsIds= set.getString("subject_id").split(",");
            for(int i=0;i<subjectAmount;i++)
                subjects[i]=new Subject(subjectsNames[i], Integer.parseInt(subjectsIds[i]),0);
/*            if(set.getInt("idrecord")!=0)
                acceptApplications=false;*/
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Course(int idCourse,String name,int amountBudget,int amountFee){
        this.idCourse=idCourse;
        this.name=name;
        this.feePlacesAmount=amountFee;
        this.budgetPlacesAmount=amountBudget;
    }

    public String getDepartment() {
        return department;
    }

    public int getIdCourse() {
        return idCourse;
    }

    public String getName() {
        return name;
    }

    public int getBudgetPlacesAmount() {
        return budgetPlacesAmount;
    }

    public int getFeePlacesAmount() {
        return feePlacesAmount;
    }

    public Subject[] getSubjects() {
        return subjects;
    }

    public boolean isAcceptApplications() {
        return acceptApplications;
    }

    public void setTotalStudentCount(int count){
        this.totalStudentCount = count;
    }

    public int getTotalStudentCount(){
        return this.totalStudentCount;
    }
}
