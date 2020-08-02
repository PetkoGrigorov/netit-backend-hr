package controller;

import config.PageMap;
import config.RouteMap;
import framework.WebController;
import framework.annotation.MVCRouteMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeController extends WebController {

    @MVCRouteMethod(path = "/home/index", method = "GET")
    public void index(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute home/index");
        display(req, resp, PageMap.HOME_PAGE);
    }

    @MVCRouteMethod(path = "/home/list", method = "GET")
    public void list(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute home/list");
        redirect(resp, RouteMap.HOME);
    }



}
