package controller;

import config.PageMap;
import config.RouteMap;
import config.SessionKey;
import framework.WebController;
import framework.annotation.MVCRouteMethod;
import framework.db.Database;
import model.DetailsEmployee;
import model.DetailsEmployer;
import model.DetailsHr;
import model.User;
import model.system.Auth;
import model.DetailsAdmin;

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
        System.out.println("Execute auth/loginProcess");

        String username = req.getParameter("user_name");
        String password = req.getParameter("user_pass");
        Auth.authenticateUser(username, password);
        if (!Auth.isAuthenticated()) {
            req.setAttribute("message", "Invalid username or password");
            this.login(req, resp);
            return;
        }
        User authUser = Auth.getAuthenticatedUser();
        String authUserName = null;
        int userRole = authUser.getRole();
        if (userRole == 1) {
            authUserName = "admin: " + DetailsHr.fetchDetails(authUser.getId()).getFullName();
        }
        if (userRole == 2) {
            authUserName = "HR: " + DetailsHr.fetchDetails(authUser.getId()).getFullName();
        }
        if (userRole == 3) {
            authUserName = DetailsEmployer.fetchDetails(authUser.getId()).getCompanyName();
        }
        if (userRole == 4) {
            authUserName = DetailsEmployee.fetchDetails(authUser.getId()).getFullName();
            setSessionAttribute(req, SessionKey.EMPLOYEE_ID, authUser.getId());
        }
        setSessionAttribute(req,"auth_user", authUserName);
        redirect(resp, RouteMap.ANNOUNCEMENT_LIST);

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

        showRequestParam(req);

        String role = getQueryValue(req, "role");
        if (role == null) {
            redirect(resp, RouteMap.HOME);
            return;
        }
        if (role.equals("3") || role.equals("4") || role.equals("1") || role.equals("2")) {
            setSessionAttribute(req, "role", role);
            display(req, resp, PageMap.REGISTRATION_PAGE);
            return;
        }
        redirect(resp, RouteMap.HOME);
    }

    @MVCRouteMethod(path="/auth/registration", method="POST")
    public void registrationProcess(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        System.out.println("Execute auth/registrationProcess");

        showRequestParam(req);

        String username = req.getParameter("user_name");
        String password = req.getParameter("user_pass");
        String passwordRepeat = req.getParameter("user_pass_repeat");
        String email = req.getParameter("user_email");
        if (username.equals("") || password.equals("") || email.equals("")) {
            req.setAttribute("message", "Invalid credentials");
            this.registration(req, resp);
            return;
        }
        int role = Integer.parseInt(getSessionAttribute(req, "role").toString());
        if (!password.equals(passwordRepeat)) {
            req.setAttribute("message", "Passwords differ");
            this.registration(req, resp);
            return;
        }

        String authUserName = null;

        if (role == 3) {
            String companyName = req.getParameter("company_name");
            String branch = req.getParameter("branch");
            String description = req.getParameter("description");
            if (companyName.equals("") || branch.equals("") || description.equals("")) {
                req.setAttribute("message", "Invalid credentials");
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

            int lastInsertedId = (int) Database.getInstance().getLastInsertedId();
            System.out.println("Last Inserted Id: " + lastInsertedId);
            DetailsEmployer.create(lastInsertedId, companyName, branch, description);
            authUserName = DetailsEmployer.fetchDetails(lastInsertedId).getCompanyName();
        }

        if (role == 4) {
            String fullName = req.getParameter("full_name");
            int age = 0;
            try {
                age = Integer.parseInt(req.getParameter("age"));
            } catch (Exception e) {
                req.setAttribute("message", "Invalid age");
                this.registration(req, resp);
                return;
            }
            String town = req.getParameter("town");
            String education = req.getParameter("education");
            if (fullName.equals("") || town.equals("") || education.equals("")) {
                req.setAttribute("message", "Invalid credentials");
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

            int lastInsertedId = (int) Database.getInstance().getLastInsertedId();
            System.out.println("Last Inserted Id: " + lastInsertedId);

            DetailsEmployee.create(lastInsertedId, fullName, age, town, education);
            authUserName = DetailsEmployee.fetchDetails(lastInsertedId).getFullName();
        }

        if (role == 1) {
            String fullName = req.getParameter("full_name");

            if (fullName.equals("")) {
                req.setAttribute("message", "Invalid credentials");
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

            int lastInsertedId = (int) Database.getInstance().getLastInsertedId();
            System.out.println("Last Inserted Id: " + lastInsertedId);
            DetailsAdmin.create(lastInsertedId, fullName);

            redirect(resp, RouteMap.ADMIN_ADMIN);
            return;
        }

        if (role == 2) {
            String fullName = req.getParameter("full_name");

            if (fullName.equals("")) {
                req.setAttribute("message", "Invalid credentials");
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

            int lastInsertedId = (int) Database.getInstance().getLastInsertedId();
            System.out.println("Last Inserted Id: " + lastInsertedId);
            DetailsHr.create(lastInsertedId, fullName);

            redirect(resp, RouteMap.ADMIN_HR);
            return;
        }

        Auth.authenticateUser(username, password);
        if (!Auth.isAuthenticated()) {
            User.undoCreate(username);
            req.setAttribute("message", "Registration Fail");
            this.registration(req, resp);
            return;
        } else {
            setSessionAttribute(req,"auth_user", authUserName);

        }

        redirect(resp, RouteMap.HOME);

    }

}
