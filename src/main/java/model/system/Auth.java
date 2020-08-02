package model.system;

import model.User;

import java.sql.SQLException;

public class Auth {

    private static User authUser = null;

    public static void authenticateUser(String username, String pass) {
        try {
            Auth.authUser = User.fetchUser(username, pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void destroyUser() {
        Auth.authUser = null;
    }

    public static boolean isAuthenticated() {
        return Auth.authUser != null;
    }

    public static User getAuthenticatedUser() {
        return Auth.authUser ;
    }

}
