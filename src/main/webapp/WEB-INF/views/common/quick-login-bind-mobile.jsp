<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String stateTitle = request.getAttribute("stateTitle") != null ? request.getAttribute("stateTitle").toString() : "Phone quick login binding";
    String stateMessage = request.getAttribute("stateMessage") != null ? request.getAttribute("stateMessage").toString() : "";
    String boundDeviceName = request.getAttribute("boundDeviceName") != null ? request.getAttribute("boundDeviceName").toString() : null;
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bind Phone Quick Login</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="login-body applicant-login-body">
<div class="page login-page quick-login-mobile-page">
    <div class="card quick-login-mobile-card">
        <h1><%= stateTitle %></h1>
        <p class="hint"><%= stateMessage %></p>
        <% if (boundDeviceName != null && !boundDeviceName.isBlank()) { %>
            <div class="quick-login-mobile-user">
                <strong>Bound device:</strong>
                <span><%= boundDeviceName %></span>
            </div>
        <% } %>
        <div class="quick-login-mobile-actions">
            <a class="btn" href="${pageContext.request.contextPath}/login">Back to login</a>
        </div>
    </div>
</div>
</body>
</html>
