<%@ page import="objets.Objet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h1>Page JSP</h1>
    <% if(request.getAttribute("saved")!=null) { %>
        <p>saved: <%=Objet.class.cast(request.getAttribute("saved")).getAttribut()%></p>
    <% } %>
    <form action="save" method="post">
        <p>attribut: <input type="text" name="attribut"></p>
        <p>date: <input type="text" name="date" pattern="\d{4}-\d{2}-\d{2}" placeholder="yyyy-mm-dd" required> </p>
        <p>nombre <input type="number" name="nombre"></p>
        <input type="submit" value="valider">
    </form>
</body>
</html>
