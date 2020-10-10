<%@ page import="config.SessionKey" %>


<%

    if ((request.getSession().getAttribute(SessionKey.PAGE_LIMIT)) != null) {

    int pageLimit = Integer.parseInt ((request.getSession().getAttribute(SessionKey.PAGE_LIMIT)).toString());
    int objectCount = Integer.parseInt ((request.getSession().getAttribute(SessionKey.OBJECT_COUNT)).toString());
    int pageIndex = Integer.parseInt ((request.getSession().getAttribute(SessionKey.PAGE_INDEX)).toString());
    int previousIndex = (pageIndex < 2) ? 1 : (pageIndex - 1);
    int nextIndex = pageIndex + 1;

    if (pageIndex > 1) {
        String previousPage = "<div style=\"display: inline-block; width: 150px\"><a href=\"list?page_index=" + previousIndex + "\">Previous page</a></div>";
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

    if ((pageIndex * pageLimit) < objectCount) {
        String nextPage = "<div style=\"display: inline-block; width: 150px\"><a href=\"list?page_index=" + nextIndex + "\">Next page</a></div>";
        out.print(nextPage);
    }



        out.print("<div></div>\n" +
                "<div >\n" +
                "    <div style=\"display: inline-block; width: 200px\">Shown products on page:</div>");

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

    }
    %>

</div>