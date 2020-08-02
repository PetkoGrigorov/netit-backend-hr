package controller;

import config.PageMap;
import config.RouteMap;
import framework.WebController;
import framework.annotation.MVCRouteMethod;
import model.User;
import model.system.Auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AuthController extends WebController {

    public void index(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute auth/index");
    }

    @MVCRouteMethod(path="/auth/login", method="GET")
    public void login(HttpServletRequest req, HttpServletResponse resp) {
        display(req, resp, PageMap.LOGIN_PAGE);

        System.out.println("Execute auth/login");
    }

    @MVCRouteMethod(path = "/auth/login", method = "POST")
    public void loginProcess(HttpServletRequest req, HttpServletResponse resp) {
        String username = req.getParameter("user_name");
        String password = req.getParameter("user_pass");
        Auth.authenticateUser(username, password);
        if (!Auth.isAuthenticated()) {
            req.setAttribute("message", "Invalid username or password");
            this.login(req, resp);
            return;
        }
        setSession(req,"auth_user", Auth.getAuthenticatedUser().getUsername());
        redirect(resp, RouteMap.HOME);
        System.out.println("Execute auth/loginProcess");
    }

    @MVCRouteMethod(path="/auth/logout", method="GET")
    public void logout(HttpServletRequest req, HttpServletResponse resp) {
        Auth.destroyUser();
        req.getSession().invalidate();
        redirect(resp, RouteMap.HOME);
        System.out.println("Execute auth/logout");
    }

    @MVCRouteMethod(path="/auth/registration", method="GET")
    public void registration(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("Execute auth/registration");

        String role = getQueryValue(req, "role");
        if (role == null) {
            redirect(resp, RouteMap.HOME);
            return;
        }
        if (role.equals("3") || role.equals("4")) {
            setSession(req, "role", role);
            display(req, resp, PageMap.REGISTRATION_PAGE);
            return;
        }
        redirect(resp, RouteMap.HOME);
    }

    @MVCRouteMethod(path="/auth/registration", method="POST")
    public void registrationProcess(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        String username = req.getParameter("user_name");
        String password = req.getParameter("user_pass");
        String passwordRepeat = req.getParameter("user_pass_repeat");
        String email = req.getParameter("user_email");
        int role = Integer.parseInt(getSession(req, "role").toString());
        if (!password.equals(passwordRepeat)) {
            req.setAttribute("message", "Passwords differ");
            this.registration(req, resp);
            return;
        }
        if (User.isUnique("username", username) && User.isUnique("email", email)) {
            User.create(username, password, email, role);
        } else {
            req.setAttribute("message", "Username or email exists in system");
            this.registration(req, resp);
            return;
        }

        Auth.authenticateUser(username, password);
        if (!Auth.isAuthenticated()) {
            User.undoCreate(username);
            req.setAttribute("message", "Registration Fail");
            this.registration(req, resp);
            return;
        } else {
            setSession(req,"auth_user", Auth.getAuthenticatedUser().getUsername());
        }



        if (role == 3) {

        }

        redirect(resp, RouteMap.HOME);

        System.out.println("Execute auth/registrationProcess");
    }

}
