<%@ page import="model.system.Auth" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>

    <div id="header">
        <h1 id="logo">
            <a href="index.jsp">Jobser</a>
        </h1>

        <div id="message">
            ${auth_user}
        </div>

        <div id="menu">
            <ul>
      <%--          <li><a href="">About Us</a> </li>
                <li><a href="">Contacts</a></li>
                <li><a href="">Other</a></li>   --%>

                <%
                if (Auth.isAuthenticated()) {
                    out.print("<li><a href=\"/Jobser_war2/base/auth/logout\">Logout</a></li>");
                } else {
                    out.print("<li><a href=\"/Jobser_war2/base/auth/login\">Login</a></li>");
                    out.print("<li><a href=\"/Jobser_war2/base/auth/registration?role=3\">Register Employer</a></li>");
                    out.print("<li><a href=\"/Jobser_war2/base/auth/registration?role=4\">Register Employee</a></li>");
                }
                %>

            </ul>
        </div>
    </div>