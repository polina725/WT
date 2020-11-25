package by.bsuir.beans;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User implements CommonUser {
    private String name;
    private String surname;
    private String role;
    private String login;
    private int id;
//    private String hash;

    public User(int id){
        this.id = id;
    }

    public User(ResultSet set){
        try {
            id = set.getInt("iduser");
            name = set.getString("name");
            surname = set.getString("surname");
            role = set.getString("role");
            login = set.getString("login");
//            hash=set.getString("hash");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User(String name, String surname, String login){
        this.name = name;
        this.surname = surname;
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getRole() {
        return role;
    }

    public String getLogin() {
        return login;
    }

    public int getId() {
        return id;
    }

    /*public String getHash() {
        return hash;
    }*/
}
