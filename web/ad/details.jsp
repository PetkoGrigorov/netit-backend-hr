<%@ page import="model.Ad" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.system.Auth" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<jsp:include page="../content/header.jsp"></jsp:include>

<div id="body" style="background: khaki">
    <div>
        <h2>Ad Details</h2>
    </div>

    <%
    Ad ad = (Ad) request.getSession().getAttribute("ad");
        if (ad != null) {
            int adId = ad.getId();
            out.print("<div>ID: " + adId + "</div>");
            out.print("<div>Description: " + ad.getDescription() + "</div>");
            out.print("<hr>");
            int authUserRole = Auth.getAuthenticatedUser().getRole();

            if (authUserRole == 4) {
                if (request.getSession().getAttribute("status") == null) {
                    out.print("<div><a style=\"color: crimson\" href=\"/Jobser_war2/base/announcement/apply?ad_id=" + adId + "\">Apply</a></div>");
                } else {
                    out.print("<div style=\"color: green\">Status: " + request.getSession().getAttribute("status").toString() + "</div>");
                }
            }

            if (authUserRole == 3) {
                out.print("<div><a style=\"color: crimson\" href=\"/Jobser_war2/base/announcement/update?ad_id=" + ad.getId() + "\">Update</a></div>");
                out.print("<div><a style=\"color: crimson\" href=\"/Jobser_war2/base/announcement/delete?ad_id=" + ad.getId() + "\">Delete</a></div>");
            }

        }
    %>

<a href="/Jobser_war2/base/announcement/list">List</a>



</div>

<jsp:include page="../content/footer.jsp"></jsp:include>
