<%@ page import="model.DetailsEmployee" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="config.SessionKey" %>
<%@ page import="model.system.Auth" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="../content/header.jsp"></jsp:include>

<div id="body" style="background: khaki">
    <h1>
        <div>Employee manager</div>
        <hr>
    </h1>
    <%
        ArrayList<DetailsEmployee> employeeCollection = (ArrayList<DetailsEmployee>) request.getSession().getAttribute(SessionKey.EMPLOYEE_DETAILS);
        if (employeeCollection != null) {
            for (DetailsEmployee employeeDetails : employeeCollection) {
                out.print("<h1>");
                out.print("<div>ID: " + employeeDetails.getUserId() + "</div>");
                out.print("<div>employee: " + employeeDetails.getFullName() + "</div>");
                out.print("</h1>");
                out.print("<div><a href=\"/Jobser_war2/base/admin/employee_update?employee_id=" + employeeDetails.getUserId() + "\">Update</a></div>");
                if (employeeDetails.getUserId() != Auth.getAuthenticatedUser().getId()) {
                    out.print("<div><a href=\"/Jobser_war2/base/admin/employee_delete?employee_id=" + employeeDetails.getUserId() + "\">Delete</a></div>");
                }
                out.print("<hr>");

            }

        }
    %>

    <div><a href="/Jobser_war2/base/announcement/list">List</a></div>

</div>

<jsp:include page="../content/footer.jsp"></jsp:include>
