<%@ page import="java.util.ArrayList" %>
<%@ page import="model.DetailsAdmin" %>
<%@ page import="config.SessionKey" %>
<%@ page import="model.system.Auth" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="../content/header.jsp"></jsp:include>

<div id="body" style="background: khaki">
    <h1>
        <div>Admin Manager</div>
        <hr>

        <div><a href="/Jobser_war2/base/auth/registration?role=1">Create Admin</a></div>
        <hr>
    </h1>
<%--        <div><a href="/Jobser_war2/base/admin/hr">Update Admin</a></div>
        <div><a href="/Jobser_war2/base/admin/employer">Delete Admin</a></div> --%>

        <%
            ArrayList <DetailsAdmin> adminCollection = (ArrayList<DetailsAdmin>) request.getSession().getAttribute(SessionKey.ADMIN);
            if (adminCollection != null) {

                out.print("<div>count: " + request.getSession().getAttribute(SessionKey.OBJECT_COUNT) + "</div>");

                for (DetailsAdmin adminDetails : adminCollection) {
                    out.print("<h1>");
                    out.print("<div>ID: " + adminDetails.getUserId() + "</div>");
                    out.print("<div>admin: " + adminDetails.getFullName() + "</div>");
                    out.print("</h1>");
                    out.print("<div><a href=\"/Jobser_war2/base/admin/admin_update?admin_id=" + adminDetails.getUserId() + "\">Update</a></div>");
                    if (adminDetails.getUserId() != Auth.getAuthenticatedUser().getId()) {
                        out.print("<div><a href=\"/Jobser_war2/base/admin/admin_delete?admin_id=" + adminDetails.getUserId() + "\">Delete</a></div>");
                    }
                    out.print("<hr>");

                }

            }

        %>

    <jsp:include page="../content/paging.jsp"></jsp:include>


    <div><a href="/Jobser_war2/base/announcement/list">List</a></div>


</div>

<jsp:include page="../content/footer.jsp"></jsp:include>
