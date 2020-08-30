<%@ page import="model.Ad" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.system.Auth" %>
<%@ page import="model.DetailsEmployer" %>
<%@ page import="model.DetailsEmployee" %>
<%@ page import="config.SessionKey" %>
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

        int employeeId;
        try {
            employeeId = Integer.parseInt(request.getSession().getAttribute(SessionKey.EMPLOYEE_ID).toString());

        } catch (Exception e) {
            employeeId = 0;
        }

        if (employeeId > 0) {

            String detailsCollectionSql = "SELECT details.id, details.user_id, details.full_name, details.age, details.town, details.education " +
                    "FROM details " +
                    "WHERE details.user_id=" + employeeId;
            DetailsEmployee employeeDetails = (DetailsEmployee) request.getSession().getAttribute(SessionKey.EMPLOYEE_DETAILS);
            out.print("<h2>");
            out.print("<div>ID: " + employeeDetails.getUserId() + "</div>");
            out.print("<div>Full name: " + employeeDetails.getFullName() + "</div>");
            out.print("<div>Age: " + employeeDetails.getAge() + "</div>");
            out.print("<div>Town: " + employeeDetails.getTown() + "</div>");
            out.print("<div>Education: " + employeeDetails.getEducation() + "</div>");
            out.print("</h2>");
            out.print("<hr>");


            out.print("<h2>Applied Ads by " + employeeDetails.getFullName() + "</h2>");
            ArrayList<Ad> collection = (ArrayList<Ad>) request.getSession().getAttribute("collection");
            for (Ad ad : collection) {
                int adId = ad.getId();
                out.print("<div>ID: " + adId + "</div>");
                out.print("<div>Title: " + ad.getTitle() + "</div>");
                out.print("<div>Company Name: " + ad.getEmployerName() + "</div>");
                out.print("<div><a style=\"color: crimson\" href=\"/Jobser_war2/base/announcement/details_hr?ad_id=" + ad.getId() + "\">Details</a></div>");
                out.print("<hr>");
            }

        }


//        --------------------------------------------------

            int pageLimit = Integer.parseInt ((request.getSession().getAttribute("page_limit")).toString());
            int objectCount = Integer.parseInt ((request.getSession().getAttribute("object_count")).toString());
            int pageIndex = Integer.parseInt ((request.getSession().getAttribute("page_index")).toString());
            int previousIndex = (pageIndex < 2) ? 1 : (pageIndex - 1);
            int nextIndex = pageIndex + 1;

            if (pageIndex > 1) {
                String previousPage = "<div style=\"display: inline-block; width: 150px\"><a href=\"?page_index=" + previousIndex + "\">Previous page</a></div>";
                out.print(previousPage);
            } else {
                out.print("<div style=\"display: inline-block; width: 150px\"></div>");
            }

            int coefficient = 1;
            while ((objectCount - pageLimit * coefficient) > 0) {
                String color = "color: indigo";
                if (pageIndex == coefficient) {
                    color = "color: darkorange";
                }
                String forPageNumber = "<span style=\"padding-left: 10px; padding-right: 10px\"><a style=\"" + color + "\" href=\"?page_index=" + coefficient + /*"&search_string=" + searchString + */"\">" + coefficient + "</a></span>";
                out.print(forPageNumber);
                coefficient ++;
            }
            if (coefficient > 1) {
                String color = "color: indigo";
                if (pageIndex == coefficient) {
                    color = "color: darkorange";
                }
                String forPageNumber = "<span style=\"padding-left: 10px; padding-right: 10px\"><a style=\"" + color + "\" href=\"?page_index=" + coefficient + /*"&search_string=" + searchString +*/ "\">" + coefficient + "</a></span>";
                out.print(forPageNumber);
            }

            if ((pageIndex * pageLimit) < objectCount) {
                String nextPage = "<div style=\"display: inline-block; width: 150px\"><a href=\"?page_index=" + nextIndex + /*"&search_string=" + searchString +*/ "\">Next page</a></div>";
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

                out.print("<span><a style=\" padding-left: 5px " + color3 + "\"  href=\"?page_limit=3\">3</a></span>");
                out.print("<span> </span>");
                out.print("<span><a style=\" padding-left: 5px " + color5 + "\"  href=\"?page_limit=5\">5</a></span>");
                out.print("<span> </span>");
                out.print("<span><a style=\" padding-left: 5px " + color10 + "\"  href=\"?page_limit=10\">10</a></span>");

                out.print("</div>");




    %>

    <div></div>


</div>

<jsp:include page="../content/footer.jsp"></jsp:include>
