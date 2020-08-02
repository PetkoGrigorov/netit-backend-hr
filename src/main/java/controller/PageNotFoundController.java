package controller;

import config.PageMap;
import config.RouteMap;
import framework.WebController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PageNotFoundController extends WebController {

    public void index(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("Execute PageNotFoundController/index");
//        redirect(resp, PageMap.PAGE_NOT_FOUND);
        display(req, resp, PageMap.PAGE_NOT_FOUND);
    }


}
