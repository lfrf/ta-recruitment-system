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
<div class="page">
    <div class="topbar">
        <div class="brand">
            <h1>Admin Configuration</h1>
            <p>Configure the application-wide workload limit and visitor browsing policy.</p>
        </div>
        <div class="nav-actions">
            <a class="btn" href="${pageContext.request.contextPath}/admin/workload">Workload</a>
            <a class="btn" href="${pageContext.request.contextPath}/vacancies">Browse Vacancies</a>
            <a class="btn" href="${pageContext.request.contextPath}/logout">Log Out</a>
        </div>
    </div>

    <c:if test="${not empty flashMessage}"><div class="alert success">${flashMessage}</div></c:if>
    <c:if test="${not empty flashError}"><div class="alert error">${flashError}</div></c:if>

    <div class="card">
        <form method="post" action="${pageContext.request.contextPath}/admin/config" class="form-grid">
            <div class="field">
                <label for="maxWorkload">max_workload</label>
                <input id="maxWorkload" name="maxWorkload" type="number" min="1" max="10" value="${config.maxWorkload}" required>
            </div>
            <div class="field field-span-2">
                <label>
                    <input type="checkbox" name="allowVisitorBrowsing" ${config.allowVisitorBrowsing ? 'checked' : ''}>
                    Allow visitor browsing before login
                </label>
            </div>
            <div class="field field-span-2"><button class="btn primary" type="submit">Save configuration</button></div>
        </form>
    </div>
</div>
</body>
</html>
