<%@ page import="model.DetailsEmployee" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="config.SessionKey" %>
<jsp:include page="../content/header.jsp"></jsp:include>

<div id="body" style="background: khaki">
    <h1>
        <div>
            <%
                ArrayList<DetailsEmployee> employeeCollection = (ArrayList<DetailsEmployee>) request.getSession().getAttribute(SessionKey.EMPLOYEE_DETAILS);
                if (employeeCollection != null) {
                    for (DetailsEmployee employeeDetails : employeeCollection) {
                        out.print("<div>Update</div>");
                        out.print("<div>employee: " + employeeDetails.getFullName() + "</div>");
                        out.print("<div>\n" +
                                "            <form method=\"POST\">  \n" +
                                "                <input name=\"update_name\" type=\"text\" placeholder=\"new name\">\n" +
                                "                <input type=\"submit\">\n" +
                                "            </form>\n" +
                                "\n" +
                                "        </div>");
                    }
                }

            %>
        </div>
        <hr>
    </h1>

    <div><a href="/Jobser_war2/base/admin/hr">List Employees</a></div>

</div>

<jsp:include page="../content/footer.jsp"></jsp:include>