package dao;

import java.sql.*;
import java.util.List;

public class Connector {

    private Connection connect() throws SQLException{
        String databaseName = "enrollment";
        String databaseUserName = "";
        String password = "12345";
        String options = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/"+ databaseName + options,databaseUserName,password);
    }

    public static void bringData(String str){

    }

    public static void pushData(List<String> info){

    }

}
