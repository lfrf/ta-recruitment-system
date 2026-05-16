<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String stateTitle = request.getAttribute("stateTitle") != null ? request.getAttribute("stateTitle").toString() : "Quick login";
    String stateMessage = request.getAttribute("stateMessage") != null ? request.getAttribute("stateMessage").toString() : "";
    boolean canConfirm = Boolean.TRUE.equals(request.getAttribute("canConfirm"));
    String requestId = request.getAttribute("requestId") != null ? request.getAttribute("requestId").toString() : "";
    String boundDeviceName = request.getAttribute("boundDeviceName") != null ? request.getAttribute("boundDeviceName").toString() : null;
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quick Login Confirm</title>
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
                <strong>Bound phone:</strong>
                <span><%= boundDeviceName %></span>
            </div>
        <% } %>
        <% if (canConfirm) { %>
            <form method="post" action="${pageContext.request.contextPath}/quick-login/mobile" class="quick-login-mobile-actions">
                <input type="hidden" name="request" value="<%= requestId %>">
                <button class="btn primary btn-hero-compact" type="submit">Confirm quick login</button>
                <a class="btn" href="${pageContext.request.contextPath}/vacancies">Cancel</a>
            </form>
        <% } else { %>
            <div class="quick-login-mobile-actions">
                <a class="btn" href="${pageContext.request.contextPath}/login">Back to login</a>
            </div>
        <% } %>
    </div>
</div>
</body>
</html>
