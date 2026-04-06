<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String loginTitle = request.getAttribute("loginTitle") != null ? request.getAttribute("loginTitle").toString() : "Log In";
    String loginSubtitle = request.getAttribute("loginSubtitle") != null ? request.getAttribute("loginSubtitle").toString() : "Sign in to continue.";
    String submitLabel = request.getAttribute("submitLabel") != null ? request.getAttribute("submitLabel").toString() : "Log In";
    String formAction = request.getAttribute("formAction") != null ? request.getAttribute("formAction").toString() : request.getContextPath() + "/login";
    String backHref = request.getAttribute("backHref") != null ? request.getAttribute("backHref").toString() : request.getContextPath() + "/home";
    String backLabel = request.getAttribute("backLabel") != null ? request.getAttribute("backLabel").toString() : "Back";
    String altLoginHref = request.getAttribute("altLoginHref") != null ? request.getAttribute("altLoginHref").toString() : null;
    String altLoginLabel = request.getAttribute("altLoginLabel") != null ? request.getAttribute("altLoginLabel").toString() : null;
    String loginVariant = request.getAttribute("loginVariant") != null ? request.getAttribute("loginVariant").toString() : "applicant";
    String loginAudience = request.getAttribute("loginAudience") != null ? request.getAttribute("loginAudience").toString() : "Access";
    String loginNotice = request.getAttribute("loginNotice") != null ? request.getAttribute("loginNotice").toString() : null;
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= loginTitle %></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="login-body <%= "admin".equals(loginVariant) ? "admin-login-body" : "applicant-login-body" %>">
<div class="page login-page">
    <div class="topbar login-topbar">
        <div class="brand">
            <span class="login-badge <%= "admin".equals(loginVariant) ? "login-badge-admin" : "login-badge-applicant" %>"><%= loginAudience %></span>
            <h1><%= loginTitle %></h1>
            <p><%= loginSubtitle %></p>
        </div>
        <div class="nav-actions">
            <a class="btn" href="<%= backHref %>"><%= backLabel %></a>
            <% if (altLoginHref != null && altLoginLabel != null) { %>
                <a class="btn" href="<%= altLoginHref %>"><%= altLoginLabel %></a>
            <% } %>
        </div>
    </div>

    <% if (request.getAttribute("flashMessage") != null) { %>
        <div class="alert success"><%= request.getAttribute("flashMessage") %></div>
    <% } %>
    <% if (request.getAttribute("flashError") != null) { %>
        <div class="alert error"><%= request.getAttribute("flashError") %></div>
    <% } %>

    <div class="login-shell <%= "admin".equals(loginVariant) ? "login-shell-admin" : "login-shell-applicant" %>">
        <div class="login-copy card">
            <h2><%= "admin".equals(loginVariant) ? "Protected admin access" : "Recruitment account access" %></h2>
            <p class="hint"><%= loginNotice != null ? loginNotice : "Use the correct account type for this page." %></p>
            <div class="login-points">
                <% if ("admin".equals(loginVariant)) { %>
                    <div class="login-point">Use this page for config, workload, and blacklist management.</div>
                    <div class="login-point">Applicant and organiser accounts should switch back to the public login page.</div>
                <% } else { %>
                    <div class="login-point">Use this page to apply for vacancies or manage applicant and organiser actions.</div>
                    <div class="login-point">Admin accounts must switch to the dedicated admin login page.</div>
                <% } %>
            </div>
        </div>

        <div class="card login-form-card">
            <% if (request.getAttribute("errorMessage") != null) { %>
                <div class="warning"><%= request.getAttribute("errorMessage") %></div>
                <br>
            <% } %>
            <form method="post" action="<%= formAction %>">
                <div class="field">
                    <label>Username</label>
                    <input type="text" name="username" placeholder="e.g. applicant01">
                </div>
                <div class="field">
                    <label>Password</label>
                    <input type="password" name="password" placeholder="Enter password">
                </div>
                <button class="btn primary" type="submit"><%= submitLabel %></button>
            </form>
        </div>
    </div>
</div>
</body>
</html>