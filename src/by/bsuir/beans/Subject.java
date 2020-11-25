package by.bsuir.beans;

public class Subject {
    private String name;
    private int id;
    private int score;

    public Subject(String name,int id,int score){
        this.name=name;
        this.id=id;
        this.score=score;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
