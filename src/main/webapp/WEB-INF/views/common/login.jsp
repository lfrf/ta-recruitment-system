<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Log In</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<div class="page">
    <div class="topbar">
        <div class="brand">
            <h1>Log In</h1>
            <p>Sign in to apply for vacancies and manage role-specific actions.</p>
        </div>
        <a class="btn" href="${pageContext.request.contextPath}/home">Back to vacancies</a>
    </div>

    <% if (request.getAttribute("flashMessage") != null) { %>
        <div class="alert success"><%= request.getAttribute("flashMessage") %></div>
    <% } %>
    <% if (request.getAttribute("flashError") != null) { %>
        <div class="alert error"><%= request.getAttribute("flashError") %></div>
    <% } %>

    <div class="card" style="max-width:520px;">
        <% if (request.getAttribute("errorMessage") != null) { %>
            <div class="warning"><%= request.getAttribute("errorMessage") %></div>
            <br>
        <% } %>
        <form method="post" action="${pageContext.request.contextPath}/login">
            <div class="field">
                <label>Username</label>
                <input type="text" name="username" placeholder="e.g. applicant01">
            </div>
            <div class="field">
                <label>Password</label>
                <input type="password" name="password" placeholder="Enter password">
            </div>
            <button class="btn primary" type="submit">Log In</button>
        </form>
    </div>
</div>
</body>
</html>