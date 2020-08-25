<%@ page import="model.Ad" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.system.Auth" %>
<%@ page import="model.DetailsEmployer" %>
<%@ page import="model.DetailsEmployee" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="../content/header.jsp"></jsp:include>

<div id="body" style="background: khaki">

    <h1>HR's List</h1>

    <div>
        <a href="/Jobser_war2/base/announcement/list?list_by=ad">List by ad</a>
    </div>

    <div>
        <a href="/Jobser_war2/base/announcement/list?list_by=employee">List by employee</a>
    </div>



    <%
//     ArrayList<?> collection = (ArrayList<?>) request.getSession().getAttribute("collection");

        if (request.getSession().getAttribute("list_by") != null) {
            out.print("<form>\n" +
                    "        <input type=\"text\" placeholder=\"search\" name=\"search_key\">\n" +
                    "        <input type=\"submit\">\n" +
                    "    </form>");

            if (request.getSession().getAttribute("list_by").equals("ad")) {
            out.print("<h2>Ads</h2>");
            ArrayList<Ad> collection = (ArrayList<Ad>) request.getSession().getAttribute("collection");
            for (Ad ad : collection) {
                int adId = ad.getId();
                out.print("<div>ID: " + adId + "</div>");
                out.print("<div>Title: " + ad.getTitle() + "</div>");
                out.print("<div>Company Name: " + ad.getEmployerName() + "</div>");
                out.print("<div><a style=\"color: crimson\" href=\"/Jobser_war2/base/announcement/details?ad_id=" + ad.getId() + "\">Details</a></div>");
                out.print("<hr>");
            }

        }
        if (request.getSession().getAttribute("list_by").equals("employee")) {
            out.print("<h2>Employers</h2>");
            ArrayList<DetailsEmployee> collection = (ArrayList<DetailsEmployee>) request.getSession().getAttribute("collection");
            for (DetailsEmployee element : collection) {
                int employerId = element.getUserId();
                out.print("<div>ID: " + employerId + "</div>");
                out.print("<div>Employee's Name: " + element.getFullName() + "</div>");
                out.print("<div><a style=\"color: crimson\" href=\"/Jobser_war2/base/announcement/details?employer_id=" + employerId + "\">Details</a></div>");
                out.print("<hr>");
            }


//            for (DetailsEmployee element : collection) {
//                int employerId = element.getUserId();
//                out.print("<div>ID: " + employerId + "</div>");
//                out.print("<div>Employee's Name: " + element.getFullName() + "</div>");
//                out.print("<div><a style=\"color: crimson\" href=\"/Jobser_war2/base/announcement/details?employer_id=" + employerId + "\">Details</a></div>");
//                out.print("<hr>");
//            }
        }

        int pageLimit = Integer.parseInt ((request.getSession().getAttribute("page_limit")).toString());
            int adCount = Integer.parseInt ((request.getSession().getAttribute("object_count")).toString());
            int pageIndex = Integer.parseInt ((request.getSession().getAttribute("page_index")).toString());
            int previousIndex = (pageIndex < 2) ? 1 : (pageIndex - 1);
            int nextIndex = pageIndex + 1;

            if (pageIndex > 1) {
                String previousPage = "<div style=\"display: inline-block; width: 150px\"><a href=\"list?page_index=" + previousIndex + /*"&search_string=" + searchString + */"\">Previous page</a></div>";
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
                String forPageNumber = "<span style=\"padding-left: 10px; padding-right: 10px\"><a style=\"" + color + "\" href=\"list?page_index=" + coefficient + /*"&search_string=" + searchString + */"\">" + coefficient + "</a></span>";
                out.print(forPageNumber);
                coefficient ++;
            }
            if (coefficient > 1) {
                String color = "color: indigo";
                if (pageIndex == coefficient) {
                    color = "color: darkorange";
                }
                String forPageNumber = "<span style=\"padding-left: 10px; padding-right: 10px\"><a style=\"" + color + "\" href=\"list?page_index=" + coefficient + /*"&search_string=" + searchString +*/ "\">" + coefficient + "</a></span>";
                out.print(forPageNumber);
            }

            if ((pageIndex * pageLimit) < adCount) {
                String nextPage = "<div style=\"display: inline-block; width: 150px\"><a href=\"list?page_index=" + nextIndex + /*"&search_string=" + searchString +*/ "\">Next page</a></div>";
                out.print(nextPage);
            }

            out.print("<div></div>");
            out.print("<div style=\"display: inline-block; width: 200px\">Shown products on page:</div>");


            String color3 = "color: indigo";
            String color5 = "color: indigo";
            String color10 = "color: indigo";

            int limit;
            try {
                limit = Integer.parseInt ((request.getSession().getAttribute("page_limit")).toString());
            }catch (Exception e) {
                limit = 3;
            }

            switch (limit) {
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

            out.print("</div>");

        }

    %>

    <div></div>


</div>

<jsp:include page="../content/footer.jsp"></jsp:include>
