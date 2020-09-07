<%@ page import="model.Ad" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.system.Auth" %>
<%@ page import="config.SessionKey" %>
<%@ page import="model.Message" %>
<%@ page import="model.DetailsEmployee" %>
<%@ page import="framework.db.Database" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<jsp:include page="../content/header.jsp"></jsp:include>

<div id="body" style="background: khaki">
    <div>
        <h2>Ad Details</h2>
    </div>

    <%
    Ad ad = (Ad) request.getSession().getAttribute(SessionKey.AD);
        int authUserRole = Auth.getAuthenticatedUser().getRole();
        if (ad != null) {
            int adId = ad.getId();
            out.print("<div>ID: " + adId + "</div>");
            out.print("<div>Title: " + ad.getTitle() + "</div>");
            out.print("<div>Company name: " + ad.getEmployerName() + "</div>");
            out.print("<div>Description: " + ad.getDescription() + "</div>");
            out.print("<hr>");

            if (authUserRole == 2) {

                out.print("<div><h2>Employee Details</h2></div>");

                DetailsEmployee employeeDetails = (DetailsEmployee) request.getSession().getAttribute(SessionKey.EMPLOYEE_DETAILS);
                out.print("<div>ID: " + employeeDetails.getUserId() + "</div>");
                out.print("<div>Full name: " + employeeDetails.getFullName() + "</div>");
                out.print("<div>Age: " + employeeDetails.getAge() + "</div>");
                out.print("<div>Town: " + employeeDetails.getTown() + "</div>");
                out.print("<div>Education: " + employeeDetails.getEducation() + "</div>");
                out.print("<hr>");


                String query = "SELECT * FROM ad__employee WHERE is_active=1 AND ad_id=" + request.getSession().getAttribute(SessionKey.AD_ID) +
                        " AND user_id=" + request.getSession().getAttribute(SessionKey.EMPLOYEE_ID);
                if (Database.getInstance().sqlQuery(query).printQueryBuilder().fetch() == null) {

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

            }

            if (authUserRole == 4) {
                if (request.getSession().getAttribute(SessionKey.STATUS) == null) {
                    out.print("<div><a style=\"color: crimson\" href=\"/Jobser_war2/base/announcement/apply?ad_id=" + adId + "\">Apply</a></div>");
                } else {
                    out.print("<div style=\"color: green\">Status: " + request.getSession().getAttribute(SessionKey.STATUS).toString() + "</div>");
                }
            }

            if (authUserRole == 3) {
                out.print("<div><a style=\"color: crimson\" href=\"/Jobser_war2/base/announcement/update?ad_id=" + ad.getId() + "\">Update</a></div>");
                out.print("<div><a style=\"color: crimson\" href=\"/Jobser_war2/base/announcement/delete?ad_id=" + ad.getId() + "\">Delete</a></div>");


            }

            if (authUserRole == 2 || authUserRole == 4) {
                ArrayList<Message> messageArrayList = (ArrayList<Message>) request.getSession().getAttribute(SessionKey.MESSAGE);
                if (!messageArrayList.isEmpty()) {
                    out.print("<div>Messages:</div>");
//                    int objectCount = Integer.parseInt((request.getSession().getAttribute("object_count")).toString());
//                    out.print("<div>Count of selection: " + objectCount + "</div> <hr>");
                    for (Message message : messageArrayList) {
                        out.print("<div>ID: " + message.getId() + "</div>");
                        out.print("<div>Value: <a href=\"/Jobser_war2/base/message/details?message_id=" + message.getId() + "\">" + message.getValue() + "</a></div>");
                        out.print("<hr>");
                    }
                }
            }




        }

        if (authUserRole == 2) {
            if (request.getSession().getAttribute("list_by").equals("ad")) {
                out.print("<a href=\"/Jobser_war2/base/announcement/list_hr?ad_id=" + request.getSession().getAttribute(SessionKey.AD_ID) + "\">List</a>");
            }
            if (request.getSession().getAttribute("list_by").equals("employee")) {
                out.print("<a href=\"/Jobser_war2/base/announcement/list_hr?employee_id=" + request.getSession().getAttribute(SessionKey.EMPLOYEE_ID) + "\">List</a>");
            }
        }

        if (authUserRole == 4 || authUserRole == 3) {
            out.print("<a href=\"/Jobser_war2/base/announcement/list\">List</a>");
        }


//           out.print("<a href=\"/Jobser_war2/base/announcement/list\">List</a>");


    %>

<%-- <a href="/Jobser_war2/base/announcement/list">List</a> --%>

</div>

<jsp:include page="../content/footer.jsp"></jsp:include>
