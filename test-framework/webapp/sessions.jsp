<%@ page import="java.util.HashMap" %><%--
  Created by IntelliJ IDEA.
  User: andie
  Date: 2023-06-30
  Time: 13:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h1>LISTE SESSIONS</h1>
    <% HashMap<String,Object> liste=(HashMap<String,Object>)request.getAttribute("sessions"); %>
    <ul>
    <% for (String key : liste.keySet()) { %>
        <li><%=key%> :  <%=liste.get(key)%></li>
    <% } %>
    </ul>
</body>
</html>
