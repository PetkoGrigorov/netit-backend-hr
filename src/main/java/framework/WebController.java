package framework;

import config.SessionKey;

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

    protected void setSession(HttpServletRequest req, String key, Object value) {
        req.getSession().setAttribute(key, value);
    }

    protected Object getSession(HttpServletRequest req, String key) {
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


}
