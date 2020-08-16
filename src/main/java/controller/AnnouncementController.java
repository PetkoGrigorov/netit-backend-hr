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
import java.util.HashMap;

public class AnnouncementController extends WebController {

    public void index(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute announcement/index");
    }

    @MVCRouteMethod(path = "/announcement/create", method = "GET")
    @RoleAccess(role = 3)
    public void create(HttpServletRequest req, HttpServletResponse resp) {
        display(req, resp, PageMap.CREATE_AD_PAGE);
    }

    @MVCRouteMethod(path = "/announcement/create", method = "POST")
    @RoleAccess(role = 3)
    public void createProcess(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute /announcement/create");
        String title = req.getParameter("ad_title");
        String description = req.getParameter("ad_description");
        int employerId = Auth.getAuthenticatedUser().getId();
        Ad.create(employerId, title, description);
        redirect(resp, RouteMap.ANNOUNCEMENT_LIST);
    }

    @MVCRouteMethod(path = "/announcement/update", method = "GET")
    @RoleAccess(role = 3)
    public void update(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute announcement/update");
        int adId = 0;
        try {
            adId = Integer.parseInt(getQueryValue(req, "ad_id"));
        } catch (Exception e) {
            redirect(resp, PageMap.PAGE_NOT_FOUND);
        }
        setSession(req, "update_ad_id", adId);
        Ad ad = Ad.fetchAd(adId);
        setSession(req, "ad", ad);
        display(req, resp, PageMap.UPDATE_AD_PAGE);
    }

    @MVCRouteMethod(path = "/announcement/update", method = "POST")
    @RoleAccess(role = 3)
    public void updateProcess(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute announcement/update");

        String title = req.getParameter("ad_title");
        String description = req.getParameter("ad_description");
        System.out.println("title: " + title);
        System.out.println("description: " + description);
        int employerId = Auth.getAuthenticatedUser().getId();
        HashMap<String, Object> columnValueCollection = new HashMap<>();
        if (title != "") {
            columnValueCollection.put("title", title);
        }
        if (description != "") {
            columnValueCollection.put("description", description);
        }
        int adId = 0;
        try {
            adId = Integer.parseInt(getSession(req, "update_ad_id").toString());
        } catch (Exception e) {
            redirect(resp, PageMap.PAGE_NOT_FOUND);
        }

        if (!columnValueCollection.isEmpty()) {
            Ad.update(adId, columnValueCollection);
        }
        redirect(resp, RouteMap.ANNOUNCEMENT_LIST);
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
        redirect(resp, RouteMap.ANNOUNCEMENT_LIST);
    }

    @MVCRouteMethod(path = "/announcement/list", method = "GET")
    @RoleAccess(role = 3)
    public void listRole3(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute announcement/listRole3");
        int pageLimit = getPageLimit(req);
        int pageIndex = getPageIndex(req);


        int offset = (pageIndex - 1) * pageLimit;
        ArrayList<Ad> adCollection = Ad.fetchAllByAuthEmployer(pageLimit, offset);
        setSession(req, "ad_collection", adCollection);
        display(req, resp, PageMap.OWN_LIST_PAGE);
//        redirect(resp, PageMap.OWN_LIST_PAGE);
    }

    @MVCRouteMethod(path = "/announcement/list", method = "GET")
    @RoleAccess(role = 4)
    public void listRole4(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute announcement/listRole4");
        int pageLimit = getPageLimit(req);
        int pageIndex = getPageIndex(req);


        ArrayList<Ad> adCollection;

        String searchKey = null;
        if (getSession(req, "search_key") != null) {
            searchKey = getSession(req, "search_key").toString();
        }
        if (hasQuery(req, "search_key")) {
            searchKey = getSearchKeyFromQuery(req);
        }
        System.out.println("searchKey: " + searchKey);

        int adCount = 0;
        if (searchKey != null) {
            setSession(req, "search_key", searchKey);
            adCount = Ad.getCount(searchKey);
            int offset = getOffset(adCount, pageLimit, pageIndex);
            adCollection = Ad.fetchAllLike("company_name", searchKey, pageLimit, offset);

        } else {
            setSession(req, "search_key", null);
            adCount = Ad.getCount();
            int offset = getOffset(adCount, pageLimit, pageIndex);
            adCollection = Ad.fetchAll(pageLimit, offset);
        }
        setSession(req, "ad_count", adCount);
        System.out.println("adCount: " + adCount);



        int offsetCorrection = (pageIndex < 1) ? 0 : (pageIndex - 1);
        pageIndex =  getCorrectPageIndex(adCount, pageLimit, pageIndex);
        setSession(req, "page_index", pageIndex);
        setSession(req, "page_limit", pageLimit);


        setSession(req, "ad_collection", adCollection);
        display(req, resp, PageMap.DASHBOARD_PAGE);
//        redirect(resp, PageMap.DASHBOARD_PAGE);
    }

    @MVCRouteMethod(path = "/announcement/apply", method = "GET")
    @RoleAccess(role = 4)
    public void apply(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute announcement/apply");

        int adId = getAdIdFromQuery(req);
        Ad.apply(adId);
        redirect(resp, RouteMap.ANNOUNCEMENT_LIST);
    }

    @MVCRouteMethod(path = "/announcement/details", method = "GET")
    public void details(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute announcement/details");

        int adId = getAdIdFromQuery(req);

        if (Auth.getAuthenticatedUser().getRole() == 3) {
            if (!Ad.isAdBelongsToAuthEmployer(adId)) {
                setSession(req, "ad", null);
                display(req, resp, PageMap.DETAILS_AD_PAGE);
//                redirect(resp, PageMap.PAGE_NOT_FOUND);
                return;
            }
        }

        Ad ad = Ad.fetchAd(adId);
        setSession(req, "ad", ad);

        String status = Ad.fetchStatus(adId, Auth.getAuthenticatedUser().getId());
        setSession(req, "status", status);
        System.out.println("status: " + status);

        display(req, resp, PageMap.DETAILS_AD_PAGE);

//        redirect(resp, PageMap.DASHBOARD_PAGE);
    }

    private int getPageLimit(HttpServletRequest req) {
        int pageLimit = 2;
        if (getSession(req, "page_limit") != null) {
            pageLimit = Integer.parseInt (getSession(req, "page_limit").toString());
        }
        pageLimit = (hasQuery(req, "page_limit")) ? Integer.parseInt(getQueryValue(req,"page_limit")) : pageLimit;
//        setSessionAttrib(req, "page_limit", pageLimit);
        return pageLimit;
    }

    private int getPageIndex(HttpServletRequest req) {
        int pageIndex = 1;
        if (getSession(req, "page_index") != null) {
//            pageIndex = Integer.parseInt((req.getSession().getAttribute("page_index")).toString());
            pageIndex = Integer.parseInt(getSession(req, "page_index").toString());
        }
        pageIndex = (hasQuery(req, "page_index")) ? Integer.parseInt(getQueryValue(req,"page_index")) : pageIndex;
        return pageIndex;
    }


    private int getAdIdFromQuery(HttpServletRequest req) {
        int adId;
        try {
            adId = Integer.parseInt(getQueryValue(req, "ad_id"));
        } catch (Exception e) {
            adId = 1;
        }
//        int adId = (!hasQuery(req, "ad_id")) ? 1 : Integer.parseInt(getQueryValue(req,"ad_id"));
        return adId;
    }

    private String getSearchKeyFromQuery(HttpServletRequest req) {
        String searchKey;
        try {
            searchKey = getQueryValue(req, "search_key");
        } catch (Exception e) {
            searchKey = null;
        }
        return searchKey;
    }

    private int getOffset(int adCount, int pageLimit, int pageIndex) {
        pageIndex = getCorrectPageIndex(adCount, pageLimit, pageIndex);
        int offset = (pageIndex - 1) * pageLimit;
        return offset;
    }

    private int getCorrectPageIndex(int adCount, int pageLimit, int pageIndex) {
        int numberOfPages = (int) Math.ceil(adCount*1.0/pageLimit);
        if (pageIndex > numberOfPages) {
            pageIndex = numberOfPages;
        }
        if (pageIndex < 1) {
            pageIndex = 1;
        }
        return pageIndex;
    }

}
