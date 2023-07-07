<%@ page import="objets.Objet" %>
<%@ page import="javax.servlet.http.HttpServletRequest" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>Page JSP</h1>
<form action="save" method="post" enctype="multipart/form-data">
    <p>date: <input type="text" name="date" pattern="\d{4}-\d{2}-\d{2}" placeholder="yyyy-mm-dd" required> </p>
    <p>nombre <input type="number" name="nombre"></p>
    <p>haha: <input type="text" name="liste[]"> <input type="text" name="liste[]"></p>
    <input type="submit" value="valider">
    <% if (request.getAttribute("saved")!=null) { %>
        <h1>SAVED: <%=request.getAttribute("saved")%></h1>
    <% } %>
</form>
</body>
</html>
