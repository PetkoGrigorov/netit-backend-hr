package controller;

import config.PageMap;
import config.RouteMap;
import config.SessionKey;
import framework.WebController;
import framework.annotation.MVCRouteMethod;
import framework.annotation.RoleAccess;
import model.Ad;
import model.DetailsEmployee;
import model.Message;
import model.User;
import model.system.Auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class AnnouncementController extends WebController {

    public void index(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute announcement/index");
        showRequestParam(req);
    }

    @MVCRouteMethod(path = "/announcement/create", method = "GET")
    @RoleAccess(role = 3)
    public void create(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute /announcement/create");
        showRequestParam(req);
        display(req, resp, PageMap.CREATE_AD_PAGE);
    }

    @MVCRouteMethod(path = "/announcement/create", method = "POST")
    @RoleAccess(role = 3)
    public void createProcess(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute /announcement/createProcess");
        showRequestParam(req);
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
        showRequestParam(req);
        int adId = getAdIdFromQueryOrSession(req);
        setSessionAttribute(req, "update_ad_id", adId);
        Ad ad = Ad.fetchAd(adId);
        setSessionAttribute(req, "ad", ad);
        display(req, resp, PageMap.UPDATE_AD_PAGE);
    }

    @MVCRouteMethod(path = "/announcement/update", method = "POST")
    @RoleAccess(role = 3)
    public void updateProcess(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute announcement/updateProcess");
        showRequestParam(req);

        String title = req.getParameter("ad_title");
        String description = req.getParameter("ad_description");
//        System.out.println("title: " + title);
//        System.out.println("description: " + description);
//        int employerId = Auth.getAuthenticatedUser().getId();
        HashMap<String, Object> columnValueCollection = new HashMap<>();
        if (title != "") {
            columnValueCollection.put("title", title);
        }
        if (description != "") {
            columnValueCollection.put("description", description);
        }
        int adId = getAdIdFromQueryOrSession(req);

        if (!columnValueCollection.isEmpty()) {
            Ad.update(adId, columnValueCollection);
        }
        redirect(resp, RouteMap.ANNOUNCEMENT_LIST);
    }

    @MVCRouteMethod(path = "/announcement/delete", method = "GET")
    @RoleAccess(role = 3)
    public void delete(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute announcement/delete");
        showRequestParam(req);

        int adId = getAdIdFromQueryOrSession(req);
        Ad.deleteById(adId);
        redirect(resp, RouteMap.ANNOUNCEMENT_LIST);
    }

    @MVCRouteMethod(path = "/announcement/list", method = "GET")
    @RoleAccess(role = 1)
    public void listRole1(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute announcement/listRole1 redirect to admin page");
        redirect(resp, PageMap.ADMIN_LIST_PAGE);

    }

    @MVCRouteMethod(path = "/announcement/list", method = "GET")
    @RoleAccess(role = 2)
    public void listRole2(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute announcement/listRole2");
        showRequestParam(req);

        String listBy = getListByFromQueryOrSession(req);
        if (listBy != null) {

            if (!listBy.equals(getSessionAttribute(req, SessionKey.LIST_BY) + "")) {
                setSessionAttribute(req, SessionKey.SEARCH_KEY, null);
                setSessionAttribute(req, SessionKey.LIST_BY, listBy);
            }

            if (listBy.equals("ad")) {
                String countSQL = "SELECT COUNT(DISTINCT ad__employee.ad_id) AS entry_count " +
                        "FROM ad__employee, details, users, ad" +
                        " WHERE ad__employee.is_active=1" +
                        " AND ad__employee.ad_id=ad.id" +
                        " AND ad.employer_id=details.user_id";
                String adCollectionSQL = "SELECT DISTINCT ad__employee.ad_id AS id, ad.employer_id AS employer_id, ad.title, details.company_name AS company_name, ad.description " +
                        "FROM ad__employee, ad, details, users " +
                        "WHERE ad__employee.is_active=1 AND ad__employee.ad_id=ad.id AND details.user_id=ad.employer_id";
                String searchCondition = "details.company_name";

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

            setSessionAttribute(req, SessionKey.CURRENT_CONTROLLER, "/list");
        }

        display(req, resp, PageMap.HR_LIST_PAGE);

    }

    @MVCRouteMethod(path = "/announcement/list_hr", method = "GET")
    @RoleAccess(role = 2)
    public void listAppliedAdsEmployees(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute announcement/list_hr");
        showRequestParam(req);

        if (hasQuery(req, SessionKey.EMPLOYEE_ID)) {
            if (!isIdBelongsToEmployee(req)) {
                redirect(resp, PageMap.PAGE_NOT_FOUND);
                return;
            }
        }

        if (getSessionAttribute(req, "list_by").equals("employee")) {

            int employeeId = getEmployeeIdFromQueryOrSession(req);
            setSessionAttribute(req, SessionKey.EMPLOYEE_ID, employeeId);
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

            System.out.print("SQL Employee details: ");
            DetailsEmployee employeeDetails = DetailsEmployee.fetchEmployeeDetailsSQL(detailsCollectionSql).get(0);
            setSessionAttribute(req, SessionKey.EMPLOYEE_DETAILS, employeeDetails);

            setSessionAttribute(req, SessionKey.CURRENT_CONTROLLER, "/list_hr");
        }

        if (getSessionAttribute(req, "list_by").equals("ad")) {

            int adId = getAdIdFromQueryOrSession(req);
            setSessionAttribute(req, SessionKey.AD_ID, adId);

            String countSQL = "SELECT COUNT(*) AS entry_count " +
                    "FROM ad__employee " +
                    "WHERE ad__employee.is_active=1 AND ad__employee.ad_id=" + adId;
            String employeeCollectionSQL = "SELECT details.id, ad__employee.user_id, details.full_name, details.age, details.town, details.education " +
                    "FROM ad__employee, details " +
                    "WHERE ad__employee.is_active=1 AND ad__employee.ad_id=" + adId + " AND ad__employee.user_id=details.user_id";
            String searchCondition = "details.full_name";
            setCollectionInSession(req, DetailsEmployee.class, countSQL, employeeCollectionSQL, searchCondition);

            String adCollectionSql = "SELECT ad.id, ad.employer_id, ad.title, details.company_name, ad.description " +
                    "FROM ad, details " +
                    "WHERE ad.employer_id=details.user_id AND ad.id=" + adId;

            System.out.print("SQL Ad details: ");
            Ad ad = Ad.fetchAdSQL(adCollectionSql).get(0);
            setSessionAttribute(req, SessionKey.AD, ad);

            setSessionAttribute(req, SessionKey.CURRENT_CONTROLLER, "/list_hr");

        }

        display(req, resp, PageMap.HR_APPLIED_ADS_PAGE);

    }

    @MVCRouteMethod(path = "/announcement/list", method = "GET")
    @RoleAccess(role = 3)
    public void listRole3(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute announcement/listRole3");
        showRequestParam(req);

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
        setSessionAttribute(req, SessionKey.CURRENT_CONTROLLER, "/list");
        display(req, resp, PageMap.OWN_LIST_PAGE);
    }

    @MVCRouteMethod(path = "/announcement/list", method = "GET")
    @RoleAccess(role = 4)
    public void listRole4(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute announcement/listRole4");
        showRequestParam(req);

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
        setSessionAttribute(req, SessionKey.CURRENT_CONTROLLER, "/list");
        display(req, resp, PageMap.DASHBOARD_PAGE);
    }

    @MVCRouteMethod(path = "/announcement/apply", method = "GET")
    @RoleAccess(role = 4)
    public void apply(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute announcement/apply");
        showRequestParam(req);

        int adId = getAdIdFromQueryOrSession(req);
        Ad.apply(adId);
        redirect(resp, RouteMap.ANNOUNCEMENT_LIST);
    }

    @MVCRouteMethod(path = "/announcement/details", method = "GET")
    public void details(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute announcement/details");
        showRequestParam(req);

        int adId = getAdIdFromQueryOrSession(req);
        setSessionAttribute(req, SessionKey.AD_ID, adId);
        int employeeId = getEmployeeIdFromQueryOrSession(req);

        if (Auth.getAuthenticatedUser().getRole() == 3 && !Ad.isAdBelongsToAuthEmployer(adId)) {
            setSessionAttribute(req, SessionKey.AD, null);
            display(req, resp, PageMap.DETAILS_AD_PAGE);
            return;
        }

        Ad ad = Ad.fetchAd(adId);
        setSessionAttribute(req, SessionKey.AD, ad);

        if (Auth.getAuthenticatedUser().getRole() == 4) {
            String status = Ad.fetchStatus(adId, Auth.getAuthenticatedUser().getId());
            setSessionAttribute(req, SessionKey.STATUS, status);
            employeeId = Auth.getAuthenticatedUser().getId();
        }

//        System.out.println("status: " + status);

        String messageCollectionSQL = "SELECT id, ad_id, employee_id, value" +
                " FROM messages" +
                " WHERE is_active=1" +
                " AND ad_id=" + adId +
                " AND employee_id=" + employeeId;

        ArrayList<Message> messageCollection = Message.fetchMessageSQL(messageCollectionSQL);
        setSessionAttribute(req, SessionKey.MESSAGE, messageCollection);

        setSessionAttribute(req, SessionKey.CURRENT_CONTROLLER, "/details");
        display(req, resp, PageMap.DETAILS_AD_PAGE);
    }

    @MVCRouteMethod(path = "/announcement/details_hr", method = "GET")
    @RoleAccess(role = 2)
    public void detailsForHr(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute announcement/detailsForHr");
        showRequestParam(req);

        if (hasQuery(req, SessionKey.EMPLOYEE_ID)) {
            if (!isIdBelongsToEmployee(req)) {
                redirect(resp, PageMap.PAGE_NOT_FOUND);
                return;
            }
        }


        if (getSessionAttribute(req, SessionKey.LIST_BY).equals("employee")) {

            int adId = getAdIdFromQueryOrSession(req);
            setSessionAttribute(req, SessionKey.AD_ID, adId);
//            int employeeId = getEmployeeIdFromQueryOrSession(req);

            messageAndStatusAd(req);

//            int adId = getAdIdFromQuery(req);
//            setSession(req, SessionKey.AD_ID, adId);

            Ad ad = Ad.fetchAd(adId);
            setSessionAttribute(req, SessionKey.AD, ad);

//            int employeeId;
//            try {
//                employeeId = Integer.parseInt(getSession(req, SessionKey.EMPLOYEE_ID) + "");
//
//            } catch (Exception e) {
//                employeeId = 0;
//            }
//
//            if (req.getParameter("status") != null) {
//                int paramStatus = Integer.parseInt(req.getParameter("status"));
//                System.out.println("input status: " + paramStatus);
//                Ad.updateStatus(adId, employeeId, paramStatus);
//            }
//
//            if (req.getParameter("ad_message") != null) {
//                String message = req.getParameter("ad_message");
//                System.out.println("new message: " + message);
//                Message.createMessage(adId, employeeId, message);
//            }
//
//            String messageCollectionSQL = "SELECT id, ad_id, employee_id, value" +
//                    " FROM messages" +
//                    " WHERE is_active=1" +
//                    " AND ad_id=" + adId +
//                    " AND employee_id=" + employeeId;
//
//            ArrayList<Message> messageCollection = Message.fetchMessageSQL(messageCollectionSQL);
//            setSession(req, SessionKey.MESSAGE, messageCollection);
//
//            String status = Ad.fetchStatus(adId, employeeId);
//            setSession(req, SessionKey.STATUS, status);
//            System.out.println("status: " + status);
//            System.out.println("ad id: " + adId);
//            System.out.println("employee id :" + employeeId);


        }

        if (getSessionAttribute(req, SessionKey.LIST_BY).equals("ad")) {

            int employeeId = getEmployeeIdFromQueryOrSession(req);
//            int adId = getAdIdFromQueryOrSession(req);

            setSessionAttribute(req, SessionKey.EMPLOYEE_ID, employeeId);

            messageAndStatusAd(req);

            DetailsEmployee employeeDetails = DetailsEmployee.fetchDetails(employeeId);
            setSessionAttribute(req, SessionKey.EMPLOYEE_DETAILS, employeeDetails);


        }

//        --------------------------------------

        setSessionAttribute(req, SessionKey.CURRENT_CONTROLLER, "/details_hr");
        display(req, resp, PageMap.DETAILS_AD_PAGE);
    }

    private int getPageLimit(HttpServletRequest req) {
        int pageLimit = 3;
        if (getSessionAttribute(req, "page_limit") != null) {
            pageLimit = Integer.parseInt(getSessionAttribute(req, "page_limit").toString());
        }
        pageLimit = (hasQuery(req, "page_limit")) ? Integer.parseInt(getQueryValue(req, "page_limit")) : pageLimit;
        return pageLimit;
    }

    private int getPageIndex(HttpServletRequest req, int objectCount, int pageLimit) {
        int pageIndex = 1;
        if (getSessionAttribute(req, "page_index") != null) {
            pageIndex = Integer.parseInt(getSessionAttribute(req, "page_index").toString());
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

//    private String getSearchKey(HttpServletRequest req) {
//        String searchKey;
//        searchKey = (getSessionAttribute(req, SessionKey.SEARCH_KEY) != null) ? getSessionAttribute(req, SessionKey.SEARCH_KEY).toString() : null;
//        searchKey = (hasQuery(req, SessionKey.SEARCH_KEY)) ? getQueryValue(req, SessionKey.SEARCH_KEY) : searchKey;
//        return searchKey;
//    }

    private int getAdIdFromQuery(HttpServletRequest req) {
        int adId;
        try {
            adId = Integer.parseInt(getQueryValue(req, "ad_id"));
        } catch (Exception e) {
            adId = 1;
        }
        return adId;
    }

//    private int getAdIdFromQueryOrSession(HttpServletRequest req) {
//        int adId;
//        adId = (getSessionAttribute(req, SessionKey.AD_ID) != null) ? (int) getSessionAttribute(req, SessionKey.AD_ID) : 0;
//        adId = (hasQuery(req, SessionKey.AD_ID)) ? getAdIdFromQuery(req) : adId;
//        return adId;
//    }

    private int getPageOffset(HttpServletRequest req, int adCount, int pageLimit) {
        int pageIndex = getPageIndex(req, adCount, pageLimit);
        int offset = (pageIndex - 1) * pageLimit;
        return offset;
    }

    private void setCollectionInSession(HttpServletRequest req, Class classReference, String countSQL, String collectionSQL, String searchCondition) {

        String searchKey = getSearchKeyFromQueryOrSession(req);
        setSessionAttribute(req, SessionKey.SEARCH_KEY, searchKey);

        ArrayList<?> collection = null;
        int objectCount = 0;
        int pageLimit = getPageLimit(req);
        int pageOffset = 0;

        if (req.getPathInfo().equals("/announcement/list_hr")) {
            searchKey = null;
        }

        System.out.println("searchKey (used) in setCollectionInSession: " + searchKey);

        if (searchKey != null) {

            if (classReference.equals(Ad.class)) {
                objectCount = Ad.getCountSQL(countSQL + " AND " + searchCondition + " LIKE '%" + searchKey + "%'");
                System.out.println("Count(like): " + objectCount);
                pageOffset = getPageOffset(req, objectCount, pageLimit);
                collection = Ad.fetchAdSQL(collectionSQL + " AND " + searchCondition + " LIKE '%" + searchKey + "%' LIMIT " + pageOffset + ", " + pageLimit);
            }
            if (classReference.equals(DetailsEmployee.class)) {
                objectCount = DetailsEmployee.getCountSQL(countSQL + " AND " + searchCondition + " like '%" + searchKey + "%'");
                System.out.println("Count(like): " + objectCount);
                pageOffset = getPageOffset(req, objectCount, pageLimit);
                collection = DetailsEmployee.fetchEmployeeDetailsSQL(collectionSQL + " AND " + searchCondition + " like '%" + searchKey + "%' LIMIT " + pageOffset + ", " + pageLimit);
            }
        } else {
            if (classReference.equals(Ad.class)) {
                objectCount = Ad.getCountSQL(countSQL);
                System.out.println("Count(all): " + objectCount);
                pageOffset = getPageOffset(req, objectCount, pageLimit);
                collection = Ad.fetchAdSQL(collectionSQL + " LIMIT " + pageOffset + ", " + pageLimit);
            }
            if (classReference.equals(DetailsEmployee.class)) {
                objectCount = DetailsEmployee.getCountSQL(countSQL);
                System.out.println("Count(all): " + objectCount);
                pageOffset = getPageOffset(req, objectCount, pageLimit);
                collection = DetailsEmployee.fetchEmployeeDetailsSQL(collectionSQL + " LIMIT " + pageOffset + ", " + pageLimit);
            }
        }

        int pageIndex = getPageIndex(req, objectCount, pageLimit);
        setSessionAttribute(req, "object_count", objectCount);
        setSessionAttribute(req, "page_limit", pageLimit);
        setSessionAttribute(req, "page_index", pageIndex);
        setSessionAttribute(req, "collection", collection);
    }

    private void messageAndStatusAd(HttpServletRequest req) {

        int employeeId = getEmployeeIdFromQueryOrSession(req);
        int adId = getAdIdFromQueryOrSession(req);

        if (req.getParameter("status") != null) {
            int paramStatus = Integer.parseInt(req.getParameter(SessionKey.STATUS));
            System.out.println("input status: " + paramStatus);
            Ad.updateStatus(adId, employeeId, paramStatus);
        }

        if (req.getParameter("ad_message") != null) {
            String message = req.getParameter(SessionKey.AD_MESSAGE);
            System.out.println("new message: " + message);
            Message.createMessage(adId, employeeId, message);
        }

        String messageCollectionSQL = "SELECT id, ad_id, employee_id, value" +
                " FROM messages" +
                " WHERE is_active=1" +
                " AND ad_id=" + adId +
                " AND employee_id=" + employeeId;

        ArrayList<Message> messageCollection = Message.fetchMessageSQL(messageCollectionSQL);
        setSessionAttribute(req, SessionKey.MESSAGE, messageCollection);

        String status = Ad.fetchStatus(adId, employeeId);
        setSessionAttribute(req, SessionKey.STATUS, status);
        System.out.println("status: " + status);
        System.out.println("ad id: " + adId);
        System.out.println("employee id :" + employeeId);
    }

}
