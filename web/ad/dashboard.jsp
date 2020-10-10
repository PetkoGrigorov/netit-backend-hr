<%@ page import="model.Ad" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.system.Auth" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<jsp:include page="../content/header.jsp"></jsp:include>

<div id="body" style="background: khaki">

    <h1>Employee's Dashboard</h1>
    <form>
        <input type="text" placeholder="search by company" name="search_key">
        <input type="submit">
    </form>

    <%
        int objectCount = Integer.parseInt((request.getSession().getAttribute("object_count")).toString());
        out.print("<div>Count of selection: " + objectCount + "</div> <hr>");
        ArrayList<Ad> adCollection = (ArrayList<Ad>) request.getSession().getAttribute("collection");
        if (adCollection != null) {
            int userId = Auth.getAuthenticatedUser().getId();
            for (Ad ad : adCollection) {
                int adId = ad.getId();
                String status = Ad.fetchStatus(adId, userId);
                if (status != null) {
                    out.print("<div style=\"color: green\">Status: " + status + "</div>");
                }
                out.print("<div>ID: " + adId + "</div>");
                out.print("<div>Title: " + ad.getTitle() + "</div>");
                out.print("<div>Company Name: " + ad.getEmployerName() + "</div>");
                out.print("<div><a style=\"color: crimson\" href=\"/Jobser_war2/base/announcement/details?ad_id=" + ad.getId() + "\">Details</a></div>");
                out.print("<hr>");
            }
        }

//        ---------------------------------------------------------------------

       /* int pageLimit = Integer.parseInt ((request.getSession().getAttribute("page_limit")).toString());
        int adCount = Integer.parseInt ((request.getSession().getAttribute("object_count")).toString());
        int pageIndex = Integer.parseInt ((request.getSession().getAttribute("page_index")).toString());
        int previousIndex = (pageIndex < 2) ? 1 : (pageIndex - 1);
        int nextIndex = pageIndex + 1;

        if (pageIndex > 1) {
            String previousPage = "<div style=\"display: inline-block; width: 150px\"><a href=\"list?page_index=" + previousIndex + "\">Previous page</a></div>";
            out.print(previousPage);
        } else {
            out.print("<div style=\"display: inline-block; width: 150px\"></div>");
        }

        int coefficient = 1;
        while ((adCount - pageLimit * coefficient) > 0) {
            String color = "color: indigo";
            if (pageIndex == coefficient) {
                color = "color: darkorange";
            }
            String forPageNumber = "<span style=\"padding-left: 10px; padding-right: 10px\"><a style=\"" + color + "\" href=\"list?page_index=" + coefficient + "\">" + coefficient + "</a></span>";
            out.print(forPageNumber);
            coefficient ++;
        }
        if (coefficient > 1) {
            String color = "color: indigo";
            if (pageIndex == coefficient) {
                color = "color: darkorange";
            }
            String forPageNumber = "<span style=\"padding-left: 10px; padding-right: 10px\"><a style=\"" + color + "\" href=\"list?page_index=" + coefficient + "\">" + coefficient + "</a></span>";
            out.print(forPageNumber);
        }

        if ((pageIndex * pageLimit) < adCount) {
            String nextPage = "<div style=\"display: inline-block; width: 150px\"><a href=\"list?page_index=" + nextIndex + "\">Next page</a></div>";
            out.print(nextPage);
        }*/

//        ---------------------------------------------------------------------

    %>

    <jsp:include page="../content/paging.jsp"></jsp:include>

<%--    <div></div>
    <div >
        <div style="display: inline-block; width: 200px">Shown products on page:</div>
        <%
            String color3 = "color: indigo";
            String color5 = "color: indigo";
            String color10 = "color: indigo";

            switch (pageLimit) {
                case 3 : color3 = "; color: darkturquoise";
                break;
                case 5 : color5 = "; color: darkturquoise";
                break;
                case 10 : color10 = "; color: darkturquoise";
                break;
                default: String color = "; color: indigo";
            }
            out.print("<span><a style=\" padding-left: 5px " + color3 + "\"  href=\"list?page_limit=3\">3</a></span>");
            out.print("<span> </span>");
            out.print("<span><a style=\" padding-left: 5px " + color5 + "\"  href=\"list?page_limit=5\">5</a></span>");
            out.print("<span> </span>");
            out.print("<span><a style=\" padding-left: 5px " + color10 + "\"  href=\"list?page_limit=10\">10</a></span>");

        %>

    </div> --%>

</div>

<jsp:include page="../content/footer.jsp"></jsp:include>
