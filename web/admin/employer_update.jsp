<%@ page import="model.DetailsEmployer" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="config.SessionKey" %>
<jsp:include page="../content/header.jsp"></jsp:include>

<div id="body" style="background: khaki">
    <h1>
        <div>
            <%
                ArrayList<DetailsEmployer> employerCollection = (ArrayList<DetailsEmployer>) request.getSession().getAttribute(SessionKey.EMPLOYER_DETAILS);
                if (employerCollection != null) {
                    for (DetailsEmployer employerDetails : employerCollection) {
                        out.print("<div>Update</div>");
                        out.print("<div>employer: " + employerDetails.getCompanyName() + "</div>");
                        out.print("<div>branch: " + employerDetails.getBranch() + "</div>");
                        out.print("<div>description: " + employerDetails.getDescription() + "</div>");
                        out.print("<div>\n" +
                                "            <form method=\"POST\">  \n" +
                                "                <input name=\"update_name\" type=\"text\" placeholder=\"new name\">\n" +
                                "                <input name=\"update_branch\" type=\"text\" placeholder=\"new branch\">\n" +
                                "                <input name=\"update_description\" type=\"text\" placeholder=\"new description\">\n" +
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

    <div><a href="/Jobser_war2/base/admin/employer">List Employees</a></div>

</div>

<jsp:include page="../content/footer.jsp"></jsp:include>