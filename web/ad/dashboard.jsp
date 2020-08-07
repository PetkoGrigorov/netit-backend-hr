<%@ page import="model.Ad" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<jsp:include page="../content/header.jsp"></jsp:include>

<div id="body" style="background: khaki">

    <h1>Employee's Dashboard</h1>

    <%
        ArrayList<Ad> adCollection = (ArrayList<Ad>) request.getSession().getAttribute("ad_collection");
        if (adCollection != null) {

            for (Ad ad : adCollection) {
                out.print("<div>ID: " + ad.getId() + "</div>");
                out.print("<div>Title: " + ad.getTitle() + "</div>");
//                out.print("<div>Description: " + ad.getDescription() + "</div>");
                out.print("<div>Company Name: " + ad.getEmployerName() + "</div>");
                out.print("<div><a style=\"color: crimson\" href=\"/Jobser_war2/base/announcement/details?ad_id=" + ad.getId() + "\">Details</a></div>");
                out.print("<hr>");
            }
        }

    %>

</div>

<jsp:include page="../content/footer.jsp"></jsp:include>
