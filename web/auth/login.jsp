
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="../content/header.jsp"></jsp:include>
<div id="body" style="background: khaki">
    ${message}
    <form method="post">
        <input class="input" type="text" style="display: block" placeholder="username" name="user_name">
        <input class="input" type="password" style="display: block" placeholder="password" name="user_pass">
        <input class="input" style="display: block" type="submit">
    </form>
</div>
<jsp:include page="../content/footer.jsp"></jsp:include>
