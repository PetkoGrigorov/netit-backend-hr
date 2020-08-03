<%@ page import="model.system.Auth" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="content/header.jsp"></jsp:include>

  <div id="body" style="background: khaki">
    <h1>

    <%
      if (Auth.isAuthenticated()) {
        out.print("<li><a href=\"/Jobser_war2/base/auth/logout\">Logout</a></li>");
        int role = Auth.getAuthenticatedUser().getRole();
        String rolePage = "";
        if (role == 3) {
          rolePage = "List";
        }
        if (role == 4) {
          rolePage = "Dashboard";
        }
          out.print("<li><a href=\"/Jobser_war2/base/announcement/list\">" + rolePage + "</a></li>");

      } else {
        out.print("<li><a href=\"/Jobser_war2/base/auth/login\">Login</a></li>");
        out.print("<li><a href=\"/Jobser_war2/base/auth/registration?role=3\">Register Employer</a></li>");
        out.print("<li><a href=\"/Jobser_war2/base/auth/registration?role=4\">Register Employee</a></li>");
      }
    %>

    </h1>


  </div>

<jsp:include page="content/footer.jsp"></jsp:include>
