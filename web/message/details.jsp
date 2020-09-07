<%@ page import="model.system.Auth" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Message" %>
<%@ page import="config.SessionKey" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="../content/header.jsp"></jsp:include>

<div id="body" style="background: khaki">
    <%
        ArrayList<Message> messageCollection = (ArrayList<Message>) request.getSession().getAttribute(SessionKey.MESSAGE);
        if (!messageCollection.isEmpty()) {
            Message message = messageCollection.get(0);
            out.print("<div>Message:</div>");
            out.print("<div>ID: " + message.getId() + "</div>");
            out.print("<div>Value: " + message.getValue() + "</div>");
            out.print("<hr>");
        }

        int adId = Integer.parseInt(request.getSession().getAttribute(SessionKey.AD_ID) + "");
        int employeeId = (int) request.getSession().getAttribute(SessionKey.EMPLOYEE_ID);
        String sessionController = request.getSession().getAttribute(SessionKey.CURRENT_CONTROLLER) + "";

        String paramValue = "";
        if (Auth.getAuthenticatedUser().getRole() == 2) {
            if (request.getSession().getAttribute("list_by").equals("ad")) {
                paramValue = "?employee_id=" + employeeId;
            }

            if (request.getSession().getAttribute("list_by").equals("employee")) {
                paramValue = "?ad_id=" + adId;
            }
        }

        if (Auth.getAuthenticatedUser().getRole() == 4) {
            paramValue = "?ad_id=" + adId;
        }

        out.print("<a href=\"/Jobser_war2/base/announcement" + sessionController + paramValue + "\">All messages</a>");


    %>


</div>

<jsp:include page="../content/footer.jsp"></jsp:include>
