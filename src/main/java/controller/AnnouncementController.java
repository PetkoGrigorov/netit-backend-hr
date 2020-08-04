package controller;

import config.PageMap;
import config.RouteMap;
import framework.WebController;
import framework.annotation.MVCRouteMethod;
import framework.annotation.RoleAccess;
import model.Ad;
import model.system.Auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public class AnnouncementController extends WebController {

    public void index(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute announcement/index");
    }

    @MVCRouteMethod(path = "/announcement/create", method = "GET")
    @RoleAccess(role = 3)
    public void create(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute /announcement/create");
//        if (Auth.getAuthenticatedUser().getRole() == 3) {
//
//        }

    }

    @MVCRouteMethod(path = "/announcement/update", method = "GET")
    @RoleAccess(role = 3)
    public void update(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute announcement/update");
    }

    @MVCRouteMethod(path = "/announcement/delete", method = "GET")
    @RoleAccess(role = 3)
    public void delete(HttpServletRequest req, HttpServletResponse resp) {
        int adId = -1;
        try {
            adId = Integer.parseInt(getQueryValue(req, "ad_id"));
        } catch (Exception e) {
            redirect(resp, PageMap.PAGE_NOT_FOUND);
            return;
        }
        Ad.deleteById(adId);
        this.listRole3(req, resp);
    }

    @MVCRouteMethod(path = "/announcement/list", method = "GET")
    @RoleAccess(role = 3)
    public void listRole3(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute announcement/listRole3");
        int limit;
        try {
            limit = Integer.parseInt(getQueryValue(req,"page_limit"));
        } catch (Exception e) {
            limit = 10;
        }

        int pageIndex;
        try {
            pageIndex = Integer.parseInt(getQueryValue(req,"page_index"));
        } catch (Exception e) {
            pageIndex = 1;
        }
//        int limit = (!hasQuery(req,"page_limit")) ? 10 : Integer.parseInt(getQueryValue(req,"page_limit"));

        int offset = (pageIndex - 1) * limit;
        ArrayList<Ad> adCollection = Ad.fetchAllByAuthEmployer(limit, offset);
        setSession(req,"ad_collection", adCollection);

        display(req, resp, PageMap.OWN_LIST_PAGE);
//        redirect(resp, PageMap.OWN_LIST_PAGE);
    }

    @MVCRouteMethod(path = "/announcement/list", method = "GET")
    @RoleAccess(role = 4)
    public void listRole4(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute announcement/listRole4");
        display(req, resp, PageMap.DASHBOARD_PAGE);
//        redirect(resp, PageMap.DASHBOARD_PAGE);
    }

}
