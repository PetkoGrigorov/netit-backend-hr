package model;

import framework.db.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class User {

    private int id;
    private String username;
    private String password;
    private String email;
    private int role;



    public User(int id, String username, String password, String email, int role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public static ArrayList<User> fetchAll() throws SQLException {
        ArrayList<User> userCollection = new ArrayList<>();
        ResultSet resultSet = Database.getInstance().selectAll("users").where("is_active", Database.Condition.EQUAL, 1)
                            .printQueryBuilder().fetch();
        while (resultSet.next()) {
            userCollection.add(new User(resultSet.getInt("id"),
                                        resultSet.getString("username"),
                                        resultSet.getString("password"),
                                        resultSet.getString("email"),
                                        resultSet.getInt("role")));
        }
        return userCollection;
    }

    public static  User fetchUser(String username, String password) throws SQLException {
        User user = null;
        ResultSet resultSet = Database.getInstance().selectAll("users")
                            .where("username", Database.Condition.EQUAL, username)
                            .andWhere("password", Database.Condition.EQUAL, password)
                            .andWhere("is_active", Database.Condition.EQUAL, 1)
                            .printQueryBuilder().fetch();
        while (resultSet.next()){
            user = new User(resultSet.getInt("id"),
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("email"),
                            resultSet.getInt("role"));
        }
        return user;
    }

    public static boolean isUnique(final String column, Object value) throws SQLException {
        ResultSet resultSet = Database.getInstance().selectSome(new ArrayList<String>(){{add(column);}}, "users")
                            .printQueryBuilder().fetch();
        while (resultSet.next()) {
            if (resultSet.getString(column).equals(value.toString())) {
                return false;
            }
        }
        return true;
    }

    public static void create(final String username, final String password, final String email, final int role) {
        Database.getInstance().insert("users", new HashMap<String, Object>(){{
            put("username", username);
            put("password", password);
            put("email", email);
            put("role", role);
            put("is_active", 1);
        }}).printQueryBuilder().execute();
    }

    public static void undoCreate(String username) {
        Database.getInstance().delete("users").where("username", Database.Condition.EQUAL, username).printQueryBuilder().execute();
    }


}
