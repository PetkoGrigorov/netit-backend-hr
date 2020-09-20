
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="../content/header.jsp"></jsp:include>
<div id="body" style="background: khaki">

    <%
        String role = (String) request.getSession().getAttribute("role");
        if (role.equals("3")) {
            out.print("<h1 style=\"color: brown\">Register Employer</h1>");
        }

        if (role.equals("4")) {
            out.print("<h1>Register Employee</h1>");
        }

        if (role.equals("1")) {
            out.print("<h1>Register Admin</h1>");
        }
    %>



${message}
    <form method="post">
        <input class="input" type="text" style="display: block" placeholder="username" name="user_name">
        <input class="input" type="password" style="display: block" placeholder="password" name="user_pass">
        <input class="input" type="password" style="display: block" placeholder="repeat password" name="user_pass_repeat">
        <input class="input" type="email" style="display: block" placeholder="email" name="user_email">

 <%--       <input class="input" type="radio" style="display: inline-block" name="role" value="3">Employer
        <div></div>
        <input class="input" type="radio" style="display: inline-block" name="role" value="4">Employee   --%>

        <%

        if (role.equals("3")) {
            out.print("<input class=\"input\" type=\"text\" style=\"display: block\" placeholder=\"company name\" name=\"company_name\">");
            out.print("<input class=\"input\" type=\"text\" style=\"display: block\" placeholder=\"branch\" name=\"branch\">");
            out.print("<input class=\"input\" type=\"text\" style=\"display: block\" placeholder=\"description\" name=\"description\">");
        }

        if (role.equals("4")) {
            out.print("<input class=\"input\" type=\"text\" style=\"display: block\" placeholder=\"full_name\" name=\"full_name\">");
            out.print("<input class=\"input\" type=\"text\" style=\"display: block\" placeholder=\"age\" name=\"age\">");
            out.print("<input class=\"input\" type=\"text\" style=\"display: block\" placeholder=\"town\" name=\"town\">");
            out.print("<input class=\"input\" type=\"text\" style=\"display: block\" placeholder=\"education\" name=\"education\">");
        }

            if (role.equals("1")) {
                out.print("<input class=\"input\" type=\"text\" style=\"display: block\" placeholder=\"full_name\" name=\"full_name\">");

            }

            if (role.equals("2")) {
                out.print("<input class=\"input\" type=\"text\" style=\"display: block\" placeholder=\"full_name\" name=\"full_name\">");

            }

        %>

        <input class="input" style="display: block" type="submit">
    </form>
</div>
<jsp:include page="../content/footer.jsp"></jsp:include>
