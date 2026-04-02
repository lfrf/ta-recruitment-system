<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Configuration</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<div class="page page-admin">
    <div class="topbar topbar-admin">
        <div class="brand">
            <h1>Admin Configuration</h1>
            <p>Configure the application-wide workload limit and visitor browsing policy.</p>
        </div>
        <div class="nav-actions admin-nav">
            <a class="btn btn-nav btn-nav-active" href="${pageContext.request.contextPath}/admin/config">Config</a>
            <a class="btn btn-nav" href="${pageContext.request.contextPath}/admin/workload">Workload</a>
            <a class="btn btn-nav" href="${pageContext.request.contextPath}/admin/blacklist">Blacklist</a>
            <a class="btn btn-nav btn-nav-subtle" href="${pageContext.request.contextPath}/vacancies">Browse Vacancies</a>
            <a class="btn btn-nav btn-nav-logout" href="${pageContext.request.contextPath}/logout">Log Out</a>
        </div>
    </div>

    <c:if test="${not empty flashMessage}"><div class="alert success">${flashMessage}</div></c:if>
    <c:if test="${not empty flashError}"><div class="alert error">${flashError}</div></c:if>

    <div class="card">
        <form method="post" action="${pageContext.request.contextPath}/admin/config" class="form-grid">
            <div class="field">
                <label for="maxWorkload">max_workload</label>
                <input id="maxWorkload" name="maxWorkload" type="number" min="1" max="10" value="${config.maxWorkload}" required>
                <div class="hint">Set the maximum number of active roles one applicant can hold at the same time.</div>
            </div>
            <div class="field field-span-2">
                <div class="config-toggle-card">
                    <div class="config-toggle-copy">
                        <strong>Allow visitor browsing before login</strong>
                        <div class="hint">When enabled, visitors can browse vacancy information before signing in. Disable this to require login first.</div>
                    </div>
                    <label class="toggle-switch">
                        <input type="checkbox" name="allowVisitorBrowsing" ${config.allowVisitorBrowsing ? 'checked' : ''}>
                        <span>Enabled</span>
                    </label>
                </div>
            </div>
            <div class="field field-span-2">
                <div class="config-submit-bar">
                    <div class="config-submit-copy">
                        <strong>Apply configuration changes</strong>
                        <div class="hint">These settings take effect immediately for visitor access and workload checks.</div>
                    </div>
                    <button class="btn primary btn-hero" type="submit">
                        <span class="btn-hero-text">
                            <span class="btn-hero-title">Save configuration</span>
                            <span class="btn-hero-subtitle">Apply workload and access rules</span>
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
