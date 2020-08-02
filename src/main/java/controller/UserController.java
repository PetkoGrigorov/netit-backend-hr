package controller;

import framework.WebController;
import framework.annotation.MVCRouteMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserController extends WebController {

    public void index(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute user/index");
    }

    @MVCRouteMethod(path = "/user/create", method = "GET")
    public void create(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute user/create");
    }

    @MVCRouteMethod(path = "/user/remove", method = "GET")
    public void remove(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute user/remove");
    }
}
