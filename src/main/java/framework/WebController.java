package framework;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebController {

    protected String absolutePath = "http://localhost:8080/Jobser_war2";

    public void redirect(HttpServletResponse resp, String path) {
        try {
            resp.sendRedirect(absolutePath + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void display(HttpServletRequest req, HttpServletResponse resp, String path) {
        try {
            req.getRequestDispatcher(path).forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    public void setSession(HttpServletRequest req, String key, Object value) {
        req.getSession().setAttribute(key, value);
    }

    public Object getSession(HttpServletRequest req, String key) {
        return req.getSession().getAttribute(key);
    }

}
