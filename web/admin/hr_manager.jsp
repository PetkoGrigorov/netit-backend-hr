<%@ page import="model.DetailsHr" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.system.DetailsAdmin" %>
<%@ page import="config.SessionKey" %>
<%@ page import="model.system.Auth" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="../content/header.jsp"></jsp:include>

<div id="body" style="background: khaki">
    <h1>
        <div>HR manager</div>
        <hr>

        <div><a href="/Jobser_war2/base/auth/registration?role=2">Create HR</a></div>
        <hr>
    </h1>

        <%
            ArrayList<DetailsHr> adminCollection = (ArrayList<DetailsHr>) request.getSession().getAttribute(SessionKey.HR);
            if (adminCollection != null) {
                for (DetailsHr hrDetails : adminCollection) {
                    out.print("<h1>");
                    out.print("<div>ID: " + hrDetails.getUserId() + "</div>");
                    out.print("<div>hr: " + hrDetails.getFullName() + "</div>");
                    out.print("</h1>");
                    out.print("<div><a href=\"/Jobser_war2/base/admin/hr_update?hr_id=" + hrDetails.getUserId() + "\">Update</a></div>");
                    if (hrDetails.getUserId() != Auth.getAuthenticatedUser().getId()) {
                        out.print("<div><a href=\"/Jobser_war2/base/admin/hr_delete?hr_id=" + hrDetails.getUserId() + "\">Delete</a></div>");
                    }
                    out.print("<hr>");

                }

            }
        %>


    <div><a href="/Jobser_war2/base/announcement/list">List</a></div>


</div>

<jsp:include page="../content/footer.jsp"></jsp:include>
