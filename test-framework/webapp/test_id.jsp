<%@ page import="objets.Objet" %><%--
  Created by IntelliJ IDEA.
  User: andie
  Date: 2023-05-22
  Time: 23:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <% if(request.getAttribute("id")!=null) { %>
        <p>saved: <%=request.getAttribute("id")%></p>
    <% } %>
</body>
</html>
