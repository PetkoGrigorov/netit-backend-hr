package controller;

import config.PageMap;
import config.RouteMap;
import config.SessionKey;
import framework.WebController;
import framework.annotation.MVCRouteMethod;
import framework.annotation.RoleAccess;
import model.DetailsEmployee;
import model.DetailsEmployer;
import model.DetailsHr;
import model.DetailsAdmin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public class AdminController extends WebController {

    public void index(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute admin /index");
        showRequestParam(req);
    }

    @MVCRouteMethod(path = "/admin/admin", method = "GET")
    @RoleAccess(role = 1)
    public void admin(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute admin/admin manage admin users");
//        String queryAllAdminSQL = "SELECT details.id, details.user_id, details.full_name FROM details, users " +
//                "WHERE users.is_active=1 AND details.is_active=1 AND details.user_id=users.id AND users.role=1 " +
//                "AND users.id<>24 "
////                + "AND users.id<>" + Auth.getAuthenticatedUser().getId()
//                ;
//
//        ArrayList<DetailsAdmin> adminCollection = DetailsAdmin.fetchAdminCollection(queryAllAdminSQL);
//        setSessionAttribute(req, SessionKey.ADMIN, adminCollection);
//
//        display(req, resp, PageMap.ADMIN_ADMIN_MANAGER_PAGE);

        listUsersByRole(req, resp, 1);
    }

    @MVCRouteMethod(path = "/admin/admin_create", method = "GET")
    @RoleAccess(role = 1)
    public void adminCreate(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute admin/admin manage admin users");
        display(req, resp, PageMap.ADMIN_ADMIN_MANAGER_PAGE);
    }

    @MVCRouteMethod(path = "/admin/admin_action", method = "GET")
    @RoleAccess(role = 1)
    public void adminAction(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute admin/admin manage admin users");
        int adminId = getAdminIdFromQueryOrSession(req);
        setSessionAttribute(req, SessionKey.ADMIN_ID, adminId);
        String queryAllAdminSQL = "SELECT details.id, details.user_id, details.full_name FROM details, users " +
                                "WHERE users.is_active=1 AND details.is_active=1 AND details.user_id=users.id AND users.role=1 " +
                                "AND users.id=" + adminId +
                                " AND users.id<>24 "
//                                + " AND users.id<>" + Auth.getAuthenticatedUser().getId()
                                ;

        ArrayList<DetailsAdmin> adminCollection = DetailsAdmin.fetchAdminCollection(queryAllAdminSQL);
        setSessionAttribute(req, SessionKey.ADMIN, adminCollection);

        display(req, resp, PageMap.ADMIN_ADMIN_ACTION_PAGE);
    }

    @MVCRouteMethod(path = "/admin/admin_update", method = "GET")
    @RoleAccess(role = 1)
    public void adminUpdate(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute admin/adminUpdate manage admin users");
        int adminId = getAdminIdFromQueryOrSession(req);
        setSessionAttribute(req, SessionKey.ADMIN_ID, adminId);
        String queryAllAdminSQL = "SELECT details.id, details.user_id, details.full_name FROM details, users " +
                "WHERE users.is_active=1 AND details.is_active=1 AND details.user_id=users.id AND users.role=1 " +
                "AND users.id=" + adminId +
                " AND users.id<>24 "
//                                + " AND users.id<>" + Auth.getAuthenticatedUser().getId()
                ;

        ArrayList<DetailsAdmin> adminCollection = DetailsAdmin.fetchAdminCollection(queryAllAdminSQL);
        setSessionAttribute(req, SessionKey.ADMIN, adminCollection);
        display(req, resp, PageMap.ADMIN_ADMIN_UPDATE_PAGE);
    }

    @MVCRouteMethod(path = "/admin/admin_update", method = "POST")
    @RoleAccess(role = 1)
    public void adminUpdateProcess(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute admin/adminUpdateProcess manage admin users");
        String newName = req.getParameter("update_name");
        if (newName != null && !newName.equals("")) {
            DetailsAdmin.update((int)getSessionAttribute(req, SessionKey.ADMIN_ID), newName);
        }
        redirect(resp, RouteMap.ADMIN_ADMIN);
    }

    @MVCRouteMethod(path = "/admin/admin_delete", method = "GET")
    @RoleAccess(role = 1)
    public void adminDelete(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute admin/adminDelete manage admin users");
        int adminId = getAdminIdFromQueryOrSession(req);
        setSessionAttribute(req, SessionKey.ADMIN_ID, adminId);
        DetailsAdmin.deleteSoft(adminId);
//        display(req, resp, PageMap.ADMIN_ADMIN_MANAGER_PAGE);
        admin(req, resp);
    }

    @MVCRouteMethod(path = "/admin/hr", method = "GET")
    @RoleAccess(role = 1)
    public void hr(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute admin/hr manage HR users");
//        String queryAllHRSQL = "SELECT details.id, details.user_id, details.full_name FROM details, users " +
//                "WHERE users.is_active=1 AND details.is_active=1 AND details.user_id=users.id AND users.role=2";
//
//        ArrayList<DetailsHr> hrCollection = DetailsHr.fetchHRCollection(queryAllHRSQL);
//        setSessionAttribute(req, SessionKey.HR, hrCollection);
//        display(req, resp, PageMap.ADMIN_HR_MANAGER_PAGE);
        listUsersByRole(req, resp, 2);
    }

    @MVCRouteMethod(path = "/admin/hr_update", method = "GET")
    @RoleAccess(role = 1)
    public void hrUpdate(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute admin/hrUpdate manage admin users");
        int hrId = getHRIdFromQueryOrSession(req);
        setSessionAttribute(req, SessionKey.HR_ID, hrId);
        String queryHRSQL = "SELECT details.id, details.user_id, details.full_name FROM details, users " +
                "WHERE users.is_active=1 AND details.is_active=1 AND details.user_id=users.id AND users.role=2 " +
                "AND users.id=" + hrId;

        ArrayList<DetailsHr> hrCollection = DetailsHr.fetchHRCollection(queryHRSQL);
        setSessionAttribute(req, SessionKey.HR, hrCollection);
        display(req, resp, PageMap.ADMIN_HR_UPDATE_PAGE);
    }

    @MVCRouteMethod(path = "/admin/hr_update", method = "POST")
    @RoleAccess(role = 1)
    public void hrUpdateProcess(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute admin/hrUpdateProcess manage admin users");
        String newName = req.getParameter("update_name");
        if (newName != null && !newName.equals("")) {
            DetailsHr.update((int)getSessionAttribute(req, SessionKey.HR_ID), newName);
        }
        redirect(resp, RouteMap.ADMIN_HR);
    }

    @MVCRouteMethod(path = "/admin/hr_delete", method = "GET")
    @RoleAccess(role = 1)
    public void hrDelete(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute admin/hrDelete manage admin users");
        int hrId = getHRIdFromQueryOrSession(req);
        setSessionAttribute(req, SessionKey.HR_ID, hrId);
        DetailsHr.deleteSoft(hrId);
//        display(req, resp, PageMap.ADMIN_ADMIN_MANAGER_PAGE);
        redirect(resp, RouteMap.ADMIN_HR);
    }

    @MVCRouteMethod(path = "/admin/employer", method = "GET")
    @RoleAccess(role = 1)
    public void employer(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute admin/employer manage employer users");
//        String queryAllEmployerSQL = "SELECT details.id, details.user_id, details.company_name, details.branch, details.description FROM details, users " +
//                " WHERE users.is_active=1 AND details.is_active=1 AND details.user_id=users.id AND users.role=3";
//        ArrayList<DetailsEmployer> employerCollection = DetailsEmployer.fetchEmployerDetailsSQL(queryAllEmployerSQL);
//        setSessionAttribute(req, SessionKey.EMPLOYER_DETAILS, employerCollection);
//
//        display(req, resp, PageMap.ADMIN_EMPLOYER_MANAGER_PAGE);

        listUsersByRole(req, resp, 3);
    }

    @MVCRouteMethod(path = "/admin/employer", method = "POST")
    @RoleAccess(role = 1)
    public void employerPost(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute admin/employerPost manage employer users");
        employer(req, resp);
    }

    @MVCRouteMethod(path = "/admin/employer_update", method = "GET")
    @RoleAccess(role = 1)
    public void employerUpdate(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute admin/employerUpdate manage admin users");
        int employerId = getEmployerIdFromQueryOrSession(req);
        setSessionAttribute(req, SessionKey.EMPLOYER_ID, employerId);
        String queryEmployerSQL = "SELECT details.id, details.user_id, details.company_name, details.branch, details.description FROM details, users " +
                " WHERE users.is_active=1 AND details.is_active=1 AND details.user_id=users.id AND users.role=3 " +
                " AND users.id=" + employerId;

        ArrayList<DetailsEmployer> employerCollection = DetailsEmployer.fetchEmployerDetailsSQL(queryEmployerSQL);
        setSessionAttribute(req, SessionKey.EMPLOYER_DETAILS, employerCollection);
        display(req, resp, PageMap.ADMIN_EMPLOYER_UPDATE_PAGE);
    }

    @MVCRouteMethod(path = "/admin/employer_update", method = "POST")
    @RoleAccess(role = 1)
    public void employerUpdateProcess(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute admin/employerUpdateProcess manage admin users");
        String newName = req.getParameter("update_name");
        String newBranch = req.getParameter("update_branch");
        String newDescription = req.getParameter("update_description");

        String setColumnsWithValues = "SET";

        if (newName != null && !newName.equals("")) {
            setColumnsWithValues += " details.company_name='" + newName + "',";
        }
        if (newBranch != null && !newBranch.equals("")) {
            setColumnsWithValues += " details.branch='" + newBranch + "',";
        }
        if (newDescription != null && !newDescription.equals("")) {
            setColumnsWithValues += " details.description='" + newDescription + "',";
        }
        if (!setColumnsWithValues.equals("SET")) {
            setColumnsWithValues = setColumnsWithValues.substring(0, setColumnsWithValues.length()-1);
            DetailsEmployer.updateComplex((int)getSessionAttribute(req, SessionKey.EMPLOYER_ID), setColumnsWithValues);
        }

//        DetailsEmployee.update((int)getSessionAttribute(req, SessionKey.EMPLOYEE_ID), newName);

        display(req, resp, RouteMap.ADMIN_EMPLOYER);
    }

    @MVCRouteMethod(path = "/admin/employer_delete", method = "GET")
    @RoleAccess(role = 1)
    public void employerDelete(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute admin/employerDelete manage admin users");
        int employerId = getEmployerIdFromQueryOrSession(req);
        setSessionAttribute(req, SessionKey.EMPLOYER_ID, employerId);
        DetailsEmployer.deleteSoft(employerId);
//        display(req, resp, PageMap.ADMIN_ADMIN_MANAGER_PAGE);
        redirect(resp, RouteMap.ADMIN_EMPLOYER);
    }



    @MVCRouteMethod(path = "/admin/employee", method = "GET")
    @RoleAccess(role = 1)
    public void employee(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute admin/employee manage employee users");
//        String queryAllEmployeeSQL = "SELECT details.id, details.user_id, details.full_name, details.age, details.town, details.education FROM details, users " +
//                "WHERE users.is_active=1 AND details.is_active=1 AND details.user_id=users.id AND users.role=4";
//
//        ArrayList<DetailsEmployee> employeeCollection = DetailsEmployee.fetchEmployeeDetailsSQL(queryAllEmployeeSQL);
//        setSessionAttribute(req, SessionKey.EMPLOYEE_DETAILS, employeeCollection);
//
//        display(req, resp, PageMap.ADMIN_EMPLOYEE_MANAGER_PAGE);

        listUsersByRole(req, resp, 4);
    }

    @MVCRouteMethod(path = "/admin/employee", method = "POST")
    @RoleAccess(role = 1)
    public void employeePost(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute admin/employeePost manage employee users");
        employee(req, resp);
    }

    @MVCRouteMethod(path = "/admin/employee_update", method = "GET")
    @RoleAccess(role = 1)
    public void employeeUpdate(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute admin/employeeUpdate manage admin users");
        int employeeId = getEmployeeIdFromQueryOrSession(req);
        setSessionAttribute(req, SessionKey.EMPLOYEE_ID, employeeId);
        String queryEmployeeSQL = "SELECT details.id, details.user_id, details.full_name, details.age, details.town, details.education FROM details, users " +
                "WHERE users.is_active=1 AND details.is_active=1 AND details.user_id=users.id AND users.role=4 " +
                "AND users.id=" + employeeId;

        ArrayList<DetailsEmployee> employeeCollection = DetailsEmployee.fetchEmployeeDetailsSQL(queryEmployeeSQL);
        setSessionAttribute(req, SessionKey.EMPLOYEE_DETAILS, employeeCollection);
        display(req, resp, PageMap.ADMIN_EMPLOYEE_UPDATE_PAGE);
    }

    @MVCRouteMethod(path = "/admin/employee_update", method = "POST")
    @RoleAccess(role = 1)
    public void employeeUpdateProcess(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute admin/employeeUpdateProcess manage admin users");
        String newName = req.getParameter("update_name");
        String newAge = req.getParameter("update_age");
        String newTown = req.getParameter("update_town");
        String newEducation = req.getParameter("update_education");

        String setColumnsWithValues = "SET";

        if (newName != null && !newName.equals("")) {
            setColumnsWithValues += " details.full_name='" + newName + "',";
        }
        if (newAge != null && !newAge.equals("")) {
            setColumnsWithValues += " details.age=" + newAge + ",";
        }
        if (newTown != null && !newTown.equals("")) {
            setColumnsWithValues += " details.town='" + newTown + "',";
        }
        if (newEducation != null && !newEducation.equals("")) {
            setColumnsWithValues += " details.education='" + newEducation + "',";
        }
        if (!setColumnsWithValues.equals("SET")) {
            setColumnsWithValues = setColumnsWithValues.substring(0, setColumnsWithValues.length()-1);
            DetailsEmployee.updateComplex((int)getSessionAttribute(req, SessionKey.EMPLOYEE_ID), setColumnsWithValues);
        }

//        DetailsEmployee.update((int)getSessionAttribute(req, SessionKey.EMPLOYEE_ID), newName);

        display(req, resp, RouteMap.ADMIN_EMPLOYEE);
    }

    @MVCRouteMethod(path = "/admin/employee_delete", method = "GET")
    @RoleAccess(role = 1)
    public void employeeDelete(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Execute admin/employeeDelete manage admin users");
        int employeeId = getEmployeeIdFromQueryOrSession(req);
        setSessionAttribute(req, SessionKey.EMPLOYEE_ID, employeeId);
        DetailsEmployee.deleteSoft(employeeId);
//        display(req, resp, PageMap.ADMIN_ADMIN_MANAGER_PAGE);
        redirect(resp, RouteMap.ADMIN_EMPLOYEE);
    }

    //    ------------------------------

    public void listUsersByRole(HttpServletRequest req, HttpServletResponse resp, int role) {
        System.out.println("Execute admin/listUsersByRole");
        String queryUsersByRoleSQL = "SELECT details.id, details.user_id, details.company_name, details.branch, details.description, " +
                " details.full_name, details.age, details.town, details.education FROM details, users " +
                " WHERE users.is_active=1 AND details.is_active=1 AND details.user_id=users.id AND users.role=" + role;
        String className = "";
        switch (role) {
            case 1 : ArrayList<DetailsAdmin> adminCollection = DetailsAdmin.fetchAdminCollection(queryUsersByRoleSQL + " AND users.id<>24 ");
                setSessionAttribute(req, SessionKey.ADMIN, adminCollection);
                display(req, resp, PageMap.ADMIN_ADMIN_MANAGER_PAGE);
            break;
            case 2 : ArrayList<DetailsHr> hrCollection = DetailsHr.fetchHRCollection(queryUsersByRoleSQL);
                setSessionAttribute(req, SessionKey.HR, hrCollection);
                display(req, resp, PageMap.ADMIN_HR_MANAGER_PAGE);
            break;
            case 3 : ArrayList<DetailsEmployer> employerCollection = DetailsEmployer.fetchDetailsSQL(queryUsersByRoleSQL);
                setSessionAttribute(req, SessionKey.EMPLOYER_DETAILS, employerCollection);

                display(req, resp, PageMap.ADMIN_EMPLOYER_MANAGER_PAGE);;
                break;
            case 4 : ArrayList<DetailsEmployee> employeeCollection = DetailsEmployee.fetchEmployeeDetailsSQL(queryUsersByRoleSQL);
                setSessionAttribute(req, SessionKey.EMPLOYEE_DETAILS, employeeCollection);
                display(req, resp, PageMap.ADMIN_EMPLOYEE_MANAGER_PAGE);
                break;
            default: display(req, resp, PageMap.PAGE_NOT_FOUND);
        }


    }

}
