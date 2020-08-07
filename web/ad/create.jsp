<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<jsp:include page="../content/header.jsp"></jsp:include>

<div id="body" style="background: khaki">
    <div>
        <h2>Create Ad</h2>
    </div>
    <form method="post">
        <input type="text" placeholder="title" name="ad_title">
        <input type="text" placeholder="description" name="ad_description">
        <input type="submit">
    </form>


</div>

<jsp:include page="../content/footer.jsp"></jsp:include>
