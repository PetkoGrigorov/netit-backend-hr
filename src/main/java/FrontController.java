import framework.annotation.MVCRouteMethod;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@WebServlet(value = "/base/*")
public class FrontController extends HttpServlet {

    @Override
    public void init() throws ServletException {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        requestProcessorAnnotation(req, resp, "GET");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        requestProcessorAnnotation(req, resp, "POST");
    }

    private void requestProcessorAnnotation(HttpServletRequest req, HttpServletResponse resp, String methodType) {

        try {
            Class<?> controllerReference = null;
            try {
                controllerReference = Class.forName(getControllerClassName(req));
            } catch (ClassNotFoundException e) {
                controllerReference = Class.forName("controller.PageNotFoundController");
            }
            Object controllerInstance = controllerReference.newInstance();
            Method[] controllerMethods = controllerReference.getMethods();
            Method controllerMethod = controllerReference.getMethod("index", HttpServletRequest.class, HttpServletResponse.class);

            for (Method method: controllerMethods) {
                if (method.isAnnotationPresent(MVCRouteMethod.class)) {
                    MVCRouteMethod methodAnnotation = method.getAnnotation(MVCRouteMethod.class);
                    if (methodAnnotation.path().equals(req.getPathInfo()) && methodAnnotation.method().equals(methodType)) {
                        controllerMethod = method;
                        break;
                    }
                }
            }

            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("Controller Reference: " + controllerReference);
            System.out.println("Controller Instance: " + controllerInstance);
            System.out.println("Controller Method: " + controllerMethod);
            System.out.println("------------------------------------------------------------");

            controllerMethod.invoke(controllerInstance, req, resp);


        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getControllerClassName(HttpServletRequest req) {
        String[] path = req.getPathInfo().substring(1).split("/");
        return "controller." + path[0].substring(0, 1).toUpperCase() + path[0].substring(1) + "Controller";
    }




}
