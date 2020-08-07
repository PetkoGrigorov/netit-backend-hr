<%@ page import="model.Ad" %>
<%@ page import="java.util.ArrayList" %>
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

            if (request.getSession().getAttribute("status") == null) {
                out.print("<div><a style=\"color: crimson\" href=\"/Jobser_war2/base/announcement/apply?ad_id=" + adId + "\">Apply</a></div>");
            } else {
                out.print("<div style=\"color: green\">Status: " + request.getSession().getAttribute("status").toString() + "</div>");
            }
        }
    %>





</div>

<jsp:include page="../content/footer.jsp"></jsp:include>
