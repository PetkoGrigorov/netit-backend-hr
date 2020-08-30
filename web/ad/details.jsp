<%@ page import="model.Ad" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.system.Auth" %>
<%@ page import="config.SessionKey" %>
<%@ page import="model.Message" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<jsp:include page="../content/header.jsp"></jsp:include>

<div id="body" style="background: khaki">
    <div>
        <h2>Ad Details</h2>
    </div>

    <%
    Ad ad = (Ad) request.getSession().getAttribute(SessionKey.AD);
        if (ad != null) {
            int adId = ad.getId();
            out.print("<div>ID: " + adId + "</div>");
            out.print("<div>Description: " + ad.getDescription() + "</div>");
            out.print("<hr>");
            int authUserRole = Auth.getAuthenticatedUser().getRole();

            int employeeId;
            try {
                employeeId = Integer.parseInt(request.getSession().getAttribute(SessionKey.EMPLOYEE_ID) + "");

            } catch (Exception e) {
                employeeId = 0;
            }

            if (authUserRole == 2) {
                out.print("<div style=\"color: green\">Status: " + request.getSession().getAttribute(SessionKey.STATUS) + "</div>");
                out.print("<div style=\"color: crimson\">Update Status: </div>");
                out.print(" <form style=\"color: crimson\">\n" +
                        "    <input type=\"radio\" name=\"status\" value=\"2\"> approved for interview <br>\n" +
                        "    <input type=\"radio\" name=\"status\" value=\"3\"> interviewed <br>\n" +
                        "    <input type=\"radio\" name=\"status\" value=\"4\"> rejected <br>\n" +
                        "    <input style=\"color: crimson\" type=\"submit\">\n" +
                        "    </form>");

                out.print("<form>\n" +
                        "        <input type=\"text\" placeholder=\"message\" name=\"ad_message\">\n" +
                        "        <input type=\"submit\">\n" +
                        " </form>");

            }

            if (authUserRole == 4) {
                if (request.getSession().getAttribute("status") == null) {
                    out.print("<div><a style=\"color: crimson\" href=\"/Jobser_war2/base/announcement/apply?ad_id=" + adId + "\">Apply</a></div>");
                } else {
                    out.print("<div style=\"color: green\">Status: " + request.getSession().getAttribute("status").toString() + "</div>");
                }
            }

            if (authUserRole == 3) {
                out.print("<div><a style=\"color: crimson\" href=\"/Jobser_war2/base/announcement/update?ad_id=" + ad.getId() + "\">Update</a></div>");
                out.print("<div><a style=\"color: crimson\" href=\"/Jobser_war2/base/announcement/delete?ad_id=" + ad.getId() + "\">Delete</a></div>");
            }

            if (authUserRole == 2 || authUserRole == 4) {

                ArrayList<Message> messageArrayList = (ArrayList<Message>) request.getSession().getAttribute(SessionKey.MESSAGE);
                System.out.println(messageArrayList);
                if (!messageArrayList.isEmpty()) {
                    out.print("<div>Messages:</div>");
                    for (Message message : messageArrayList) {
                        out.print("<div>ID: " + message.getId() + "</div>");
                        out.print("<div>Value: " + message.getValue() + "</div>");
                        out.print("<hr>");
                    }
                }
            }

        }

        String sessionController = request.getSession().getAttribute("current_controller") + "";
        out.print("<a href=\"/Jobser_war2/base/announcement" + sessionController + "\">List</a>");
    %>

<%-- <a href="/Jobser_war2/base/announcement/list">List</a> --%>


</div>

<jsp:include page="../content/footer.jsp"></jsp:include>
