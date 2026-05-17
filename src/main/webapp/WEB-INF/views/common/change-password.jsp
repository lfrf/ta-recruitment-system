<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Change Password</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<div class="page page-admin">
    <div class="topbar-wide">
        <div class="brand">
            <h1>Change Password</h1>
            <p>Update your account password. This action does not change any profile content.</p>
        </div>
        <div class="nav-actions panel-nav">
            <a class="btn btn-nav btn-nav-subtle" href="${backHref}">Back</a>
            <a class="btn btn-nav btn-nav-logout" href="${pageContext.request.contextPath}/logout">Log Out</a>
        </div>
    </div>

    <c:if test="${not empty flashMessage}"><div class="alert success">${flashMessage}</div></c:if>
    <c:if test="${not empty flashError}"><div class="alert error">${flashError}</div></c:if>
    <% if (request.getAttribute("errorMessage") != null) { %>
        <div class="alert error"><%= request.getAttribute("errorMessage") %></div>
    <% } %>

    <div class="card">
        <form method="post" action="${pageContext.request.contextPath}/account/password" class="form-grid">
            <div class="field">
                <label for="currentPassword">Current Password</label>
                <input id="currentPassword" name="currentPassword" type="password" autocomplete="current-password" required>
            </div>
            <div class="field"></div>
            <div class="field">
                <label for="newPassword">New Password</label>
                <input id="newPassword" name="newPassword" type="password" minlength="8" maxlength="64" autocomplete="new-password" required>
                <div class="hint">Use 8-64 characters.</div>
            </div>
            <div class="field">
                <label for="confirmPassword">Confirm New Password</label>
                <input id="confirmPassword" name="confirmPassword" type="password" minlength="8" maxlength="64" autocomplete="new-password" required>
            </div>
            <div class="field field-span-2">
                <div class="config-submit-bar">
                    <div class="config-submit-copy">
                        <strong>Update password</strong>
                        <div class="hint">After saving, keep using the new password for future logins.</div>
                    </div>
                    <button class="btn primary btn-hero" type="submit">
                        <span class="btn-hero-text">
                            <span class="btn-hero-title">Save new password</span>
                            <span class="btn-hero-subtitle">Apply account security update</span>
                        </span>
                        <svg class="btn-hero-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
                            <path d="M6 12.5l4 4L18 8.75" />
                        </svg>
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>
