<%@ page import="config.SessionKey" %>
<%@ page import="model.system.DetailsAdmin" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="../content/header.jsp"></jsp:include>

<div id="body" style="background: khaki">
    <h1>
        <div>
            <%
                ArrayList<DetailsAdmin> adminCollection = (ArrayList<DetailsAdmin>) request.getSession().getAttribute(SessionKey.ADMIN);
                if (adminCollection != null) {
                    for (DetailsAdmin adminDetails : adminCollection) {
                        out.print("<div>Update</div>");
                        out.print("<div>admin: " + adminDetails.getFullName() + "</a> </div>");
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




        <%

        %>

    </h1>

    <div><a href="/Jobser_war2/base/admin/admin">List Admins</a></div>

</div>

<jsp:include page="../content/footer.jsp"></jsp:include>
