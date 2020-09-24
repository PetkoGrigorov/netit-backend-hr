<%@ page import="model.DetailsEmployer" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="config.SessionKey" %>
<%@ page import="model.system.Auth" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="../content/header.jsp"></jsp:include>

<div id="body" style="background: khaki">
    <h1>
        <div>Employer manager</div>
        <hr>
    </h1>
    <%
        ArrayList<DetailsEmployer> employerCollection = (ArrayList<DetailsEmployer>) request.getSession().getAttribute(SessionKey.EMPLOYER_DETAILS);
        if (employerCollection != null) {
            for (DetailsEmployer employerDetails : employerCollection) {
                out.print("<h1>");
                out.print("<div>ID: " + employerDetails.getUserId() + "</div>");
                out.print("<div>employer: " + employerDetails.getCompanyName() + "</div>");
                out.print("</h1>");
                out.print("<div><a href=\"/Jobser_war2/base/admin/employer_update?employer_id=" + employerDetails.getUserId() + "\">Update</a></div>");
                if (employerDetails.getUserId() != Auth.getAuthenticatedUser().getId()) {
                    out.print("<div><a href=\"/Jobser_war2/base/admin/employer_delete?employer_id=" + employerDetails.getUserId() + "\">Delete</a></div>");
                }
                out.print("<hr>");

            }

        }
    %>


    <div><a href="/Jobser_war2/base/announcement/list">List</a></div>


</div>

<jsp:include page="../content/footer.jsp"></jsp:include>
