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
        redirect(resp, PageMap.HOME_PAGE);
        System.out.println("Execute auth/loginProcess");
    }

    @MVCRouteMethod(path="/auth/registration", method="GET")
    public void registration(HttpServletRequest req, HttpServletResponse resp) {
        display(req, resp, PageMap.REGISTRATION_PAGE);
        System.out.println("Execute auth/registration");
    }

    @MVCRouteMethod(path="/auth/registration", method="POST")
    public void registrationProcess(HttpServletRequest req, HttpServletResponse resp) throws SQLException {

        String username = req.getParameter("user_name");
        String password = req.getParameter("user_pass");
        String passwordRepeat = req.getParameter("user_pass_repeat");
        String email = req.getParameter("user_email");
        if (req.getParameter("role") == null) {
            req.setAttribute("message", "Select Employer or Employee");
            this.registration(req, resp);
            return;
        }
        int role = Integer.parseInt(req.getParameter("role"));
        if (!password.equals(passwordRepeat)) {
            req.setAttribute("message", "Passwords differ");
            this.registration(req, resp);
            return;
        }
        if (User.isUnique("username", username) && User.isUnique("email", email)) {
            User.create(username, password, email, role);
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

        redirect(resp, RouteMap.HOME);

        System.out.println("Execute auth/registrationProcess");
    }
}
