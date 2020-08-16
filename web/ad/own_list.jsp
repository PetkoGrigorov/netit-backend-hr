<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Ad" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="../content/header.jsp"></jsp:include>

<div id="body" style="background: khaki">

    <h1>Employer's Announcement</h1>
    <div>
        <a href="/Jobser_war2/base/announcement/create">Create new ad</a>
        <hr>
    </div>


    <%
        ArrayList<Ad> adCollection = (ArrayList<Ad>) request.getSession().getAttribute("ad_collection");
        if (adCollection != null) {

            for (Ad ad : adCollection) {
                out.print("<div>ID: " + ad.getId() + "</div>");
                out.print("<div>Title: " + ad.getTitle() + "</div>");
//                out.print("<div>Description: " + ad.getDescription() + "</div>");

//                out.print("<div><a style=\"color: crimson\" href=\"/Jobser_war2/base/announcement/update?ad_id=" + ad.getId() + "\">Update</a></div>");
//                out.print("<div><a style=\"color: crimson\" href=\"/Jobser_war2/base/announcement/delete?ad_id=" + ad.getId() + "\">Delete</a></div>");

                out.print("<div><a style=\"color: crimson\" href=\"/Jobser_war2/base/announcement/details?ad_id=" + ad.getId() + "\">Details</a></div>");

                out.print("<hr>");
            }
        }

    %>

</div>

<jsp:include page="../content/footer.jsp"></jsp:include>
