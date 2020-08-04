package framework;

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
        return req.getQueryString().contains(key);
    }

    protected String getQueryValue(HttpServletRequest req, String key) {
        return getQueryHashMap(req).get(key);
    }


}
