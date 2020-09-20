<%@ page import="model.DetailsHr" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="config.SessionKey" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="../content/header.jsp"></jsp:include>

<div id="body" style="background: khaki">
    <h1>
        <div>
            <%
                ArrayList<DetailsHr> hrCollection = (ArrayList<DetailsHr>) request.getSession().getAttribute(SessionKey.HR);
                if (hrCollection != null) {
                    for (DetailsHr hrDetails : hrCollection) {
                        out.print("<div>Update</div>");
                        out.print("<div>hr: " + hrDetails.getFullName() + "</a> </div>");
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

    <div><a href="/Jobser_war2/base/admin/hr">List HRs</a></div>

</div>

<jsp:include page="../content/footer.jsp"></jsp:include>
