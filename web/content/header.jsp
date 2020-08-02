
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
                <li><a href="">About Us</a> </li>
                <li><a href="">Contacts</a></li>
                <li><a href="">Other</a></li>
                <li><a href="/Jobser_war2/base/auth/login">Login</a></li>
                <li><a href="/Jobser_war2/base/auth/registration">Registration</a></li>
            </ul>
        </div>
    </div>