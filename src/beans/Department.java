package beans;

import java.util.ArrayList;

public class Department {
    private String name;
    private int id;
    private ArrayList<Course> courses = new ArrayList<>();

    public Department(String name,int id){
        this.name=name;
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void addCourse(Course course) {
        this.courses.add(course);
    }
}
