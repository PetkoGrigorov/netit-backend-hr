package framework;

import config.PageMap;
import config.SessionKey;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class WebController {

    protected String absolutePath = "http://localhost:8080/Jobser_war2";

    protected void redirect(HttpServletResponse resp, String path) {
        try {
            resp.sendRedirect(absolutePath + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void display(HttpServletRequest req, HttpServletResponse resp, String path) {
        try {
            req.getRequestDispatcher(path).forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    protected void showRequestParam(HttpServletRequest req) {
        System.out.println("path info: " + req.getPathInfo());
        System.out.println("query string: " + req.getQueryString());
        System.out.println(SessionKey.SEARCH_KEY + "(session attribute): " + req.getSession().getAttribute(SessionKey.SEARCH_KEY));
        System.out.println(SessionKey.SEARCH_KEY + "(query param): " + req.getParameter(SessionKey.SEARCH_KEY));

        System.out.println(SessionKey.LIST_BY + "(session attribute): " + req.getSession().getAttribute(SessionKey.LIST_BY));
        System.out.println(SessionKey.LIST_BY + "(query param): " + req.getParameter(SessionKey.LIST_BY));

        System.out.println(SessionKey.AD_ID + "(session attribute): " + req.getSession().getAttribute(SessionKey.AD_ID));
        System.out.println(SessionKey.AD_ID + "(query param): " + req.getParameter(SessionKey.AD_ID));

        System.out.println(SessionKey.EMPLOYEE_ID + "(session attribute): " + req.getSession().getAttribute(SessionKey.EMPLOYEE_ID));
        System.out.println(SessionKey.EMPLOYEE_ID + "(query param): " + req.getParameter(SessionKey.EMPLOYEE_ID));

        System.out.println(SessionKey.MESSAGE_ID + "(session attribute): " + req.getSession().getAttribute(SessionKey.MESSAGE_ID));
        System.out.println(SessionKey.MESSAGE_ID + "(query param): " + req.getParameter(SessionKey.MESSAGE_ID));

    }

    protected void setSessionAttribute(HttpServletRequest req, String key, Object value) {
        req.getSession().setAttribute(key, value);
    }

    protected Object getSessionAttribute(HttpServletRequest req, String key) {
        return req.getSession().getAttribute(key);
    }

    private HashMap<String, String> getQueryHashMap(HttpServletRequest req) {
        String[] splitQueryStringCollection = req.getQueryString().split("&");
        HashMap<String, String> queryKeyValueCollection = new HashMap<>();
        for (String singleQuery : splitQueryStringCollection) {
            String[] splitSingleQuery = singleQuery.split("=");
            String value;
            try {
                value = splitSingleQuery[1];
            } catch (Exception e) {
                value = null;
            }
            queryKeyValueCollection.put(splitSingleQuery[0], value);
        }
        return queryKeyValueCollection;
    }

    protected boolean hasQuery(HttpServletRequest req, String key) {
        try {
            return req.getQueryString().contains(key);
        } catch (Exception e) {
            return false;
        }
    }

    protected String getQueryValue(HttpServletRequest req, String key) {
        return getQueryHashMap(req).get(key);
    }

    protected String getSearchKeyFromQueryOrSession(HttpServletRequest req) {
        return  (hasQuery(req, SessionKey.SEARCH_KEY)) ? getQueryValue(req, SessionKey.SEARCH_KEY) :
                                                        (String) getSessionAttribute(req, SessionKey.SEARCH_KEY);
    }

    protected String getListByFromQueryOrSession(HttpServletRequest req) {
        return  (hasQuery(req, SessionKey.LIST_BY)) ? getQueryValue(req, SessionKey.LIST_BY) :
                                                    (String) getSessionAttribute(req, SessionKey.LIST_BY);
    }

    protected int getAdIdFromQueryOrSession(HttpServletRequest req) {
        int adId;
        try {
            adId = Integer.parseInt(getQueryValue(req, SessionKey.AD_ID));
        } catch (Exception e) {
            try {
                adId = Integer.parseInt(getSessionAttribute(req, SessionKey.AD_ID) + "");
            } catch (Exception e1) {
                adId = 0;
            }
        }
        return adId;
    }

    protected int getAdminIdFromQueryOrSession(HttpServletRequest req) {
//        int adminId;
//        try {
//            adminId = Integer.parseInt(getQueryValue(req, SessionKey.ADMIN_ID));
//        } catch (Exception e) {
//            try {
//                adminId = Integer.parseInt(getSessionAttribute(req, SessionKey.ADMIN_ID) + "");
//            } catch (Exception e1) {
//                adminId = 0;
//            }
//        }
//        return adminId;

        return getAnyFromQueryOrSession(req, SessionKey.ADMIN_ID);
    }

    protected int getHRIdFromQueryOrSession(HttpServletRequest req) {
//        int hrId;
//        try {
//            hrId = Integer.parseInt(getQueryValue(req, SessionKey.HR_ID));
//        } catch (Exception e) {
//            try {
//                hrId = Integer.parseInt(getSessionAttribute(req, SessionKey.HR_ID) + "");
//            } catch (Exception e1) {
//                hrId = 0;
//            }
//        }
//        return hrId;

        return getAnyFromQueryOrSession(req, SessionKey.HR_ID);
    }

    private int getEmployeeIdFromQuery(HttpServletRequest req) {
        int employeeId;
        try {
            employeeId = Integer.parseInt(getQueryValue(req, SessionKey.EMPLOYEE_ID));
        } catch (Exception e) {
            employeeId = 0;
        }
        return employeeId;
    }

    protected int getEmployeeIdFromQueryOrSession(HttpServletRequest req) {
//        int employeeId;
//
//        try {
//            employeeId = Integer.parseInt(getQueryValue(req, SessionKey.EMPLOYEE_ID));
//        } catch (Exception e) {
//            try {
//                employeeId = Integer.parseInt(getSessionAttribute(req, SessionKey.EMPLOYEE_ID) + "");
//            } catch (Exception e1) {
//                employeeId = 0;
//            }
//        }
//        return employeeId;
        return getAnyFromQueryOrSession(req, SessionKey.EMPLOYEE_ID);
    }

    protected int getEmployerIdFromQueryOrSession(HttpServletRequest req) {
        return getAnyFromQueryOrSession(req, SessionKey.EMPLOYER_ID);
    }

    protected boolean isIdBelongsToEmployee(HttpServletRequest req) {
            int id;
            try {
                id = Integer.parseInt(getQueryValue(req, SessionKey.EMPLOYEE_ID));
            } catch (Exception e) {
                id = 0;
            }

            int role;
            try {
                User user = User.fetchUserById(id);
                role = user.getRole();
            } catch (Exception e) {
                role = 0;
            }

            if (role != 4) {
                return false;
            } else {
                return true;
            }
    }

    private int getAnyFromQueryOrSession(HttpServletRequest req, String sessionKey) {
        int id;
        try {
            id = Integer.parseInt(getQueryValue(req, sessionKey));
        } catch (Exception e) {
            try {
                id = Integer.parseInt(getSessionAttribute(req, sessionKey) + "");
            } catch (Exception e1) {
                id = 0;
            }
        }
        return id;
    }

    protected int getPageIndex(HttpServletRequest req, int objectCount, int pageLimit) {
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

    protected int getPageOffset(HttpServletRequest req, int adCount, int pageLimit) {
        int pageIndex = getPageIndex(req, adCount, pageLimit);
        int offset = (pageIndex - 1) * pageLimit;
        return offset;
    }

    protected int getPageLimit(HttpServletRequest req) {
        int pageLimit = 3;
        if (getSessionAttribute(req, "page_limit") != null) {
            pageLimit = Integer.parseInt(getSessionAttribute(req, "page_limit").toString());
        }
        pageLimit = (hasQuery(req, "page_limit")) ? Integer.parseInt(getQueryValue(req, "page_limit")) : pageLimit;
        return pageLimit;
    }

}
