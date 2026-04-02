<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Workload Overview</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<div class="page">
    <div class="topbar">
        <div class="brand">
            <h1>Admin Workload Overview</h1>
            <p>Current limit: ${config.maxWorkload} active roles per applicant.</p>
        </div>
        <div class="nav-actions">
            <a class="btn" href="${pageContext.request.contextPath}/admin/config">Config</a>
            <a class="btn" href="${pageContext.request.contextPath}/vacancies">Browse Vacancies</a>
            <a class="btn" href="${pageContext.request.contextPath}/logout">Log Out</a>
        </div>
    </div>

    <c:if test="${not empty flashMessage}"><div class="alert success">${flashMessage}</div></c:if>
    <c:if test="${not empty flashError}"><div class="alert error">${flashError}</div></c:if>

    <div class="card">
        <table class="table">
            <thead>
            <tr><th>Applicant</th><th>Student ID</th><th>Email</th><th>Submitted</th><th>Offered</th><th>Active</th><th>Modules</th><th>Flags</th></tr>
            </thead>
            <tbody>
            <c:forEach items="${summaries}" var="summary">
                <tr>
                    <td>${summary.displayName}</td>
                    <td><c:out value="${empty summary.studentId ? '-' : summary.studentId}" /></td>
                    <td><c:out value="${empty summary.email ? '-' : summary.email}" /></td>
                    <td>${summary.submittedCount}</td>
                    <td>${summary.offeredCount}</td>
                    <td>${summary.activeCount} / ${summary.maxWorkload}</td>
                    <td><c:out value="${empty summary.activeModules ? '-' : summary.activeModules}" /></td>
                    <td>
                        <c:if test="${summary.blacklisted}"><span class="tag">Blacklisted</span></c:if>
                        <c:if test="${summary.overloaded}"><span class="tag">Overloaded</span></c:if>
                        <c:if test="${not summary.blacklisted and not summary.overloaded}"><span class="tag">OK</span></c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
