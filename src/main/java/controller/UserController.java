package controller;

import framework.WebController;
import framework.annotation.MVCRouteMethod;
import framework.annotation.RoleAccess;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserController extends WebController {

    public void index(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute user/index");
    }

    @MVCRouteMethod(path = "/user/create", method = "GET")
    @RoleAccess(role = 1)
    public void create(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute user/create");
    }

    @MVCRouteMethod(path = "/user/create", method = "GET")
    public void update(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute user/create");
    }

    @MVCRouteMethod(path = "/user/remove", method = "GET")
    public void remove(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute user/remove");
    }

    @MVCRouteMethod(path = "/user/details", method = "GET")
    @RoleAccess(role = 2)
    public void details(HttpServletRequest req, HttpServletResponse resp) {

    }

}
