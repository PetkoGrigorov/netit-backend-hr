package controller;

import config.PageMap;
import config.RouteMap;
import config.SessionKey;
import framework.WebController;
import framework.annotation.MVCRouteMethod;
import framework.annotation.RoleAccess;
import model.Ad;
import model.DetailsEmployee;
import model.DetailsEmployer;
import model.Message;
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
    @RoleAccess(role = 2)
    public void listRole2(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute announcement/listRole2");
        if (!hasQuery(req, "list_by") && getSession(req, "list_by") == null) {
            display(req, resp, PageMap.HR_LIST_PAGE);
            return;
        }
        String listBy = (hasQuery(req, "list_by")) ? getQueryValue(req, "list_by") : getSession(req, "list_by").toString();


        if (hasQuery(req, "list_by")) {
            String querySearch = getQueryValue(req, "list_by");
            Object sessionSearch = getSession(req, "list_by");
            if (!querySearch.equals(sessionSearch)) {
                setSession(req, "search_key", null);
            }
        }

        setSession(req, "list_by", listBy);


        if (listBy.equals("ad")) {
            String countSQL = "SELECT COUNT(DISTINCT ad__employee.ad_id) AS entry_count " +
                    "FROM ad__employee, details, users, ad " +
                    "WHERE ad__employee.is_active=1";
            String adCollectionSQL = "SELECT DISTINCT ad__employee.ad_id AS id, ad.employer_id AS employer_id, ad.title, details.company_name AS company_name, ad.description " +
                    "FROM ad__employee, ad, details, users " +
                    "WHERE ad__employee.is_active=1 AND ad__employee.ad_id=ad.id AND details.user_id=ad.employer_id";
            String searchCondition = "ad__employee.ad_id=ad.id AND details.user_id=ad.employer_id AND details.company_name";

            setCollectionInSession(req, Ad.class, countSQL, adCollectionSQL, searchCondition);
        }

        if (listBy.equals("employee")) {
            String countSQL = "SELECT COUNT(DISTINCT ad__employee.user_id) AS entry_count " +
                    "FROM ad__employee, details, users, ad " +
                    "WHERE ad__employee.is_active=1 AND ad__employee.user_id=details.user_id";
            String adCollectionSQL = "SELECT DISTINCT ad__employee.user_id, details.id, details.full_name, details.age, details.town, details.education " +
                    "FROM details, ad__employee " +
                    "WHERE ad__employee.is_active=1 AND ad__employee.user_id=details.user_id";
            String searchCondition = "details.full_name";

            setCollectionInSession(req, DetailsEmployee.class, countSQL, adCollectionSQL, searchCondition);
        }

        setSession(req, "current_controller", "/list");

        display(req, resp, PageMap.HR_LIST_PAGE);
    }

    @MVCRouteMethod(path = "/announcement/list_hr", method = "GET")
    @RoleAccess(role = 2)
    public void listAppliedAds(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute announcement/list_hr");

        int employeeId = (hasQuery(req, "employee_id")) ? getEmployeeIdFromQuery(req) : Integer.parseInt(getSession(req, "employee_id") + "");
        setSession(req, SessionKey.EMPLOYEE_ID, employeeId);
        System.out.println("employee id: " + employeeId);

        String countSQL = "SELECT COUNT(*) AS entry_count " +
                "FROM ad__employee " +
                "WHERE ad__employee.is_active=1 AND ad__employee.user_id=" + employeeId;
        String adCollectionSQL = "SELECT ad__employee.ad_id AS id, ad.employer_id AS employer_id, ad.title, details.company_name AS company_name, ad.description " +
                "FROM ad__employee, ad, details " +
                "WHERE ad__employee.is_active=1 AND ad__employee.user_id=" + employeeId + " AND ad__employee.ad_id=ad.id AND details.user_id=ad.employer_id";
        String searchCondition = "ad.title";

        setCollectionInSession(req, Ad.class, countSQL, adCollectionSQL, searchCondition);
        String detailsCollectionSql = "SELECT details.id, details.user_id, details.full_name, details.age, details.town, details.education " +
                                "FROM details " +
                                "WHERE details.user_id=" + employeeId;
        DetailsEmployee employeeDetails = DetailsEmployee.fetchEmployeeDetailsSQL(detailsCollectionSql).get(0);
        setSession(req, SessionKey.EMPLOYEE_DETAILS, employeeDetails);

        setSession(req, SessionKey.CURRENT_CONTROLLER, "/list_hr");
        display(req, resp, PageMap.HR_APPLIED_ADS_PAGE);
    }

    @MVCRouteMethod(path = "/announcement/list", method = "GET")
    @RoleAccess(role = 3)
    public void listRole3(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute announcement/listRole3");

        String countSQL = "SELECT COUNT(*) AS entry_count " +
                "FROM ad, details " +
                "WHERE ad.is_active=1 AND ad.employer_id=details.user_id AND ad.employer_id=" + Auth.getAuthenticatedUser().getId();
        String adCollectionSQL = "SELECT ad.id, ad.employer_id, ad.title, ad.description, details.company_name " +
                "FROM ad, details " +
                "WHERE ad.is_active = 1 AND ad.employer_id = details.user_id AND ad.employer_id=" + Auth.getAuthenticatedUser().getId();
        String searchCondition = "details.company_name";

        setCollectionInSession(req, Ad.class, countSQL, adCollectionSQL, searchCondition);

        /*int pageLimit = getPageLimit(req);
//        int pageIndex = getPageIndex(req);

//        int offset = (pageIndex - 1) * pageLimit;
        int adCount = Ad.getCountByAuthEmployer();
        int offset = getPageOffset(req, adCount, pageLimit);
        int pageIndex = getPageIndex(req, adCount, pageLimit);
        setSession(req, "object_count", adCount);
        setSession(req, "page_index", pageIndex);
        setSession(req, "page_limit", pageLimit);
        ArrayList<Ad> adCollection = Ad.fetchAllByAuthEmployer(pageLimit, offset);
        setSession(req, "collection", adCollection);*/
        setSession(req, "current_controller", "/list");
        display(req, resp, PageMap.OWN_LIST_PAGE);
    }

    @MVCRouteMethod(path = "/announcement/list", method = "GET")
    @RoleAccess(role = 4)
    public void listRole4(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute announcement/listRole4");

        String countSQL = "SELECT COUNT(*) AS entry_count " +
                "FROM ad, details " +
                "WHERE ad.is_active=1 AND ad.employer_id=details.user_id";
        String adCollectionSQL = "SELECT ad.id, ad.employer_id, ad.title, ad.description, details.company_name " +
                "FROM ad, details " +
                "WHERE ad.is_active = 1 AND ad.employer_id = details.user_id";

        String searchCondition = "company_name";

        setCollectionInSession(req, Ad.class, countSQL, adCollectionSQL, searchCondition);


        /*int pageLimit = getPageLimit(req);
        String searchKey = getSearchKey(req);
        System.out.println("searchKey: " + searchKey);

        ArrayList<Ad> adCollection;
        int adCount = 0;
        if (searchKey != null) {
            setSession(req, "search_key", searchKey);
            adCount = Ad.getCount(searchKey);
            int offset = getPageOffset(req, adCount, pageLimit);
            adCollection = Ad.fetchAllLike("company_name", searchKey, pageLimit, offset);
        } else {
            setSession(req, "search_key", null);
            adCount = Ad.getCount();
            int offset = getPageOffset(req, adCount, pageLimit);
            adCollection = Ad.fetchAll(pageLimit, offset);
        }
        setSession(req, "object_count", adCount);
        System.out.println("adCount: " + adCount);

        int pageIndex = getPageIndex(req, adCount, pageLimit);
        setSession(req, "page_index", pageIndex);
        setSession(req, "page_limit", pageLimit);

        setSession(req, "collection", adCollection);*/
        setSession(req, "current_controller", "/list");
        display(req, resp, PageMap.DASHBOARD_PAGE);
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
                return;
            }
        }

        Ad ad = Ad.fetchAd(adId);
        setSession(req, "ad", ad);

        String status = Ad.fetchStatus(adId, Auth.getAuthenticatedUser().getId());
        setSession(req, "status", status);
        System.out.println("status: " + status);

        String messageCollectionSQL = "SELECT id, ad_id, employee_id, value" +
                " FROM messages" +
                " WHERE is_active=1" +
                " AND ad_id=" + adId +
                " AND employee_id=" + Auth.getAuthenticatedUser().getId();

        ArrayList<Message> messageCollection = Message.fetchMessageSQL(messageCollectionSQL);
        setSession(req, SessionKey.MESSAGE, messageCollection);

        display(req, resp, PageMap.DETAILS_AD_PAGE);
    }

    @MVCRouteMethod(path = "/announcement/details_hr", method = "GET")
    @RoleAccess(role = 2)
    public void detailsForHr(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute announcement/detailsForHr");

        int adId = getAdIdFromQuery(req);
        setSession(req, SessionKey.AD_ID, adId);

        Ad ad = Ad.fetchAd(adId);
        setSession(req, SessionKey.AD, ad);

        int employeeId;
        try {
            employeeId = Integer.parseInt(getSession(req, SessionKey.EMPLOYEE_ID) + "");

        } catch (Exception e) {
            employeeId = 0;
        }

        if (req.getParameter("status") != null) {
            int paramStatus = Integer.parseInt(req.getParameter("status"));
            System.out.println("input status: " + paramStatus);
            Ad.updateStatus(adId, employeeId, paramStatus);
        }

        if (req.getParameter("ad_message") != null) {
            String message = req.getParameter("ad_message");
            System.out.println("new message: " + message);
            Message.createMessage(adId, employeeId, message);
        }

        String messageCollectionSQL = "SELECT id, ad_id, employee_id, value" +
                                    " FROM messages" +
                                    " WHERE is_active=1" +
                                    " AND ad_id=" + adId +
                                    " AND employee_id=" + employeeId;

        ArrayList<Message> messageCollection = Message.fetchMessageSQL(messageCollectionSQL);
        setSession(req, SessionKey.MESSAGE, messageCollection);

        String status = Ad.fetchStatus(adId, employeeId);
        setSession(req, SessionKey.STATUS, status);
        System.out.println("status: " + status);
        System.out.println("ad id: " + adId);
        System.out.println("employee id :" + employeeId);

        display(req, resp, PageMap.DETAILS_AD_PAGE);
    }

    private int getPageLimit(HttpServletRequest req) {
        int pageLimit = 3;
        if (getSession(req, "page_limit") != null) {
            pageLimit = Integer.parseInt(getSession(req, "page_limit").toString());
        }
        pageLimit = (hasQuery(req, "page_limit")) ? Integer.parseInt(getQueryValue(req, "page_limit")) : pageLimit;
        return pageLimit;
    }

    private int getPageIndex(HttpServletRequest req, int objectCount, int pageLimit) {
        int pageIndex = 1;
        if (getSession(req, "page_index") != null) {
            pageIndex = Integer.parseInt(getSession(req, "page_index").toString());
        }
        pageIndex = (hasQuery(req, "page_index")) ? Integer.parseInt(getQueryValue(req, "page_index")) : pageIndex;
        int numberOfPages = (int) Math.ceil(objectCount * 1.0 / pageLimit);
        if (pageIndex > numberOfPages) {
            pageIndex = numberOfPages;
        }
        if (pageIndex < 1) {
            pageIndex = 1;
        }
        return pageIndex;
    }

    private String getSearchKey(HttpServletRequest req) {
        String searchKey = null;
        if (getSession(req, "search_key") != null) {
            searchKey = getSession(req, "search_key").toString();
        }
        searchKey = (hasQuery(req, "search_key")) ? getQueryValue(req, "search_key") : searchKey;
        return searchKey;
    }

    private int getAdIdFromQuery(HttpServletRequest req) {
        int adId;
        try {
            adId = Integer.parseInt(getQueryValue(req, "ad_id"));
        } catch (Exception e) {
            adId = 1;
        }
        return adId;
    }

    private int getEmployeeIdFromQuery(HttpServletRequest req) {
        int employeeId;
        try {
            employeeId = Integer.parseInt(getQueryValue(req, "employee_id"));
        } catch (Exception e) {
            employeeId = 0;
        }
        return employeeId;
    }

    private int getPageOffset(HttpServletRequest req, int adCount, int pageLimit) {
        int pageIndex = getPageIndex(req, adCount, pageLimit);
        int offset = (pageIndex - 1) * pageLimit;
        return offset;
    }

    private void setCollectionInSession(HttpServletRequest req, Class classReference, String countSQL, String collectionSQL, String searchCondition) {

        String searchKey = getSearchKey(req);
        ArrayList<?> collection = null;
        int objectCount = 0;
        int pageLimit = getPageLimit(req);
        int pageOffset = 0;

        System.out.println("search string: " + searchKey);

        if (searchKey != null) {
            setSession(req, "search_key", searchKey);
            if (classReference.equals(Ad.class)){
                objectCount = Ad.getCountSQL(countSQL + " AND " + searchCondition + " LIKE '%" + searchKey + "%'");
                System.out.println("Count(like): " + objectCount);
                pageOffset = getPageOffset(req, objectCount, pageLimit);
                collection = Ad.fetchAdSQL(collectionSQL + " AND " + searchCondition + " like '%" + searchKey + "%' LIMIT " + pageOffset + ", " + pageLimit);
            }
            if (classReference.equals(DetailsEmployee.class)) {
                objectCount = DetailsEmployee.getCountSQL(countSQL + " AND " + searchCondition + " like '%" + searchKey + "%'");
                System.out.println("Count(like): " + objectCount);
                pageOffset = getPageOffset(req, objectCount, pageLimit);
                collection = DetailsEmployee.fetchEmployeeDetailsSQL(collectionSQL + " AND " + searchCondition + " like '%" + searchKey + "%' LIMIT " + pageOffset + ", " + pageLimit);
            }
        } else {
            setSession(req, "search_key", null);
            if (classReference.equals(Ad.class)){
                objectCount = Ad.getCountSQL(countSQL);
                System.out.println("Count(like): " + objectCount);
                pageOffset = getPageOffset(req, objectCount, pageLimit);
                collection = Ad.fetchAdSQL(collectionSQL + " LIMIT " + pageOffset + ", " + pageLimit);
            }
            if (classReference.equals(DetailsEmployee.class)) {
                objectCount = DetailsEmployee.getCountSQL(countSQL);
                System.out.println("Count(like): " + objectCount);
                pageOffset = getPageOffset(req, objectCount, pageLimit);
                collection = DetailsEmployee.fetchEmployeeDetailsSQL(collectionSQL + " LIMIT " + pageOffset + ", " + pageLimit);
            }
        }

        int pageIndex = getPageIndex(req, objectCount, pageLimit);
        setSession(req, "object_count", objectCount);
        setSession(req, "page_limit", pageLimit);
        setSession(req, "page_index", pageIndex);
        setSession(req, "collection", collection);
    }


}
