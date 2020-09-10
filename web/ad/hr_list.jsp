<%@ page import="model.Ad" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.system.Auth" %>
<%@ page import="model.DetailsEmployer" %>
<%@ page import="model.DetailsEmployee" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="../content/header.jsp"></jsp:include>

<div id="body" style="background: khaki">



    <%
        if (request.getSession().getAttribute("list_by") == null) {
            out.print("<h1>HR's Dashboard</h1>");
        } else {
            if (request.getSession().getAttribute("list_by").equals("employee")) {
                out.print("<h1>HR's List by Employee</h1>");
            } else if (request.getSession().getAttribute("list_by").equals("ad")) {
                out.print("<h1>HR's List by Ad</h1>");
            } else {
                out.print("<h1>HR's Dashboard</h1>");
            }
        }

    %>

    <div>
        <a href="/Jobser_war2/base/announcement/list?list_by=ad">List by ad</a>
    </div>

    <div>
        <a href="/Jobser_war2/base/announcement/list?list_by=employee">List by employee</a>
    </div>


    <%
        Object listBy = request.getSession().getAttribute("list_by");

        if (listBy != null && (listBy.equals("ad") || listBy.equals("employee"))) {
            out.print("<form>\n" +
                    "        <input type=\"text\" placeholder=\"search\" name=\"search_key\">\n" +
                    "        <input type=\"submit\">\n" +
                    "    </form>");

            int objectCount = Integer.parseInt((request.getSession().getAttribute("object_count")).toString());
            out.print("<div>Count of selection: " + objectCount + "</div> <hr>");

            if (listBy.equals("ad")) {
                out.print("<h2>Ads</h2>");
                ArrayList<Ad> collection = (ArrayList<Ad>) request.getSession().getAttribute("collection");
                for (Ad ad : collection) {
                    int adId = ad.getId();
                    out.print("<div>ID: " + adId + "</div>");
                    out.print("<div>Title: " + ad.getTitle() + "</div>");
                    out.print("<div>Company Name: " + ad.getEmployerName() + "</div>");
                    out.print("<div><a style=\"color: crimson\" href=\"/Jobser_war2/base/announcement/list_hr?ad_id=" + ad.getId() + "\">Employees</a></div>");
                    out.print("<hr>");
                }
            }


            if (listBy.equals("employee")) {
                out.print("<h2>Employees</h2>");
                ArrayList<DetailsEmployee> collection = (ArrayList<DetailsEmployee>) request.getSession().getAttribute("collection");
                for (DetailsEmployee element : collection) {
                    int employerId = element.getUserId();
                    out.print("<div>ID: " + employerId + "</div>");
                    out.print("<div>Employee's Name: " + element.getFullName() + "</div>");
                    out.print("<div><a style=\"color: crimson\" href=\"/Jobser_war2/base/announcement/list_hr?employee_id=" + employerId + "\">Ads</a></div>");
                    out.print("<hr>");
                }
            }

            int pageLimit = Integer.parseInt((request.getSession().getAttribute("page_limit")).toString());
//            int objectCount = Integer.parseInt ((request.getSession().getAttribute("object_count")).toString());
            int pageIndex = Integer.parseInt((request.getSession().getAttribute("page_index")).toString());
            int previousIndex = (pageIndex < 2) ? 1 : (pageIndex - 1);
            int nextIndex = pageIndex + 1;

            if (pageIndex > 1) {
                String previousPage = "<div style=\"display: inline-block; width: 150px\"><a href=\"list?page_index=" + previousIndex + "\">Previous page</a></div>";
                out.print(previousPage);
            } else {
                out.print("<div style=\"display: inline-block; width: 150px\"></div>");
            }

            int number = 1;
            while ((objectCount - pageLimit * number) > 0) {
                String color = "color: indigo";
                if (pageIndex == number) {
                    color = "color: darkorange";
                }
                String forPageNumber = "<span style=\"padding-left: 10px; padding-right: 10px\"><a style=\"" + color + "\" href=\"list?page_index=" + number + /*"&search_string=" + searchString + */"\">" + number + "</a></span>";
                out.print(forPageNumber);
                number++;
            }
            if (number > 1) {
                String color = "color: indigo";
                if (pageIndex == number) {
                    color = "color: darkorange";
                }
                String forPageNumber = "<span style=\"padding-left: 10px; padding-right: 10px\"><a style=\"" + color + "\" href=\"list?page_index=" + number + /*"&search_string=" + searchString +*/ "\">" + number + "</a></span>";
                out.print(forPageNumber);
            }

            if ((pageIndex * pageLimit) < objectCount) {
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
                    limit = Integer.parseInt((request.getSession().getAttribute("page_limit")).toString());
                } catch (Exception e) {
                    limit = 3;
                }

                switch (limit) {
                    case 3:
                        color3 = "; color: darkturquoise";
                        break;
                    case 5:
                        color5 = "; color: darkturquoise";
                        break;
                    case 10:
                        color10 = "; color: darkturquoise";
                        break;
                    default:
                        String color = "; color: indigo";
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
