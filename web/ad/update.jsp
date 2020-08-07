<%@ page import="model.Ad" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<jsp:include page="../content/header.jsp"></jsp:include>

<div id="body" style="background: khaki">
    <div>
        <h2>Update Ad</h2>
    </div>
    <form method="post">
        <%
            Ad ad = (Ad) request.getSession().getAttribute("ad");
            if (ad != null) {
                    out.print("<div>ID: " + ad.getId() + "</div>");
                    out.print("<div>Title: " + ad.getTitle() + "</div>");
                    out.print("<div>Description: " + ad.getDescription() + "</div>");
                    out.print("<hr>");
            }
        %>
        <input type="text" placeholder="title" name="ad_title">
        <input type="text" placeholder="description" name="ad_description">
        <input type="submit">
    </form>



</div>

<jsp:include page="../content/footer.jsp"></jsp:include>
