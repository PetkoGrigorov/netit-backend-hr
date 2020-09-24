<%@ page import="model.DetailsAdmin" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="config.SessionKey" %>
<%@ page import="model.system.Auth" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="../content/header.jsp"></jsp:include>

<div id="body" style="background: khaki">
    <h1>
        <div>Admin Manager</div>
        <hr>


        <div><a href="/Jobser_war2/base/admin/admin_update">Update Admin</a></div>


        <%


            ArrayList<DetailsAdmin> adminCollection = (ArrayList<DetailsAdmin>) request.getSession().getAttribute(SessionKey.ADMIN);
            if (adminCollection != null) {
                for (DetailsAdmin adminDetails : adminCollection) {
                    out.print("<div>ID: " + adminDetails.getUserId() + "</div>");
                    out.print("<div>Name: " + adminDetails.getFullName() + "</a> </div>");
                    out.print("hr");

                }

            }


            if ((int) request.getSession().getAttribute(SessionKey.ADMIN_ID) != Auth.getAuthenticatedUser().getId()) {
                out.print("<div><a href=\"/Jobser_war2/base/admin/admin_delete\">Delete Admin</a></div>");
            }

        %>

    </h1>


    <div><a href="/Jobser_war2/base/admin/admin">List Admins</a></div>


</div>

<jsp:include page="../content/footer.jsp"></jsp:include>
