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
<div class="page page-admin">
    <div class="topbar topbar-admin">
        <div class="brand">
            <h1>Admin Workload Overview</h1>
            <p>Current limit: ${config.maxWorkload} active roles per applicant. This table shows both total application history and current active load.</p>
        </div>
        <div class="nav-actions admin-nav">
            <a class="btn btn-nav" href="${pageContext.request.contextPath}/admin/config">Config</a>
            <a class="btn btn-nav btn-nav-active" href="${pageContext.request.contextPath}/admin/workload">Workload</a>
            <a class="btn btn-nav" href="${pageContext.request.contextPath}/admin/blacklist">Blacklist</a>
            <a class="btn btn-nav btn-nav-subtle" href="${pageContext.request.contextPath}/vacancies">Browse Vacancies</a>
            <a class="btn btn-nav" href="${pageContext.request.contextPath}/account/password">Change Password</a>
            <a class="btn btn-nav btn-nav-logout" href="${pageContext.request.contextPath}/logout">Log Out</a>
        </div>
    </div>

    <c:if test="${not empty flashMessage}"><div class="alert success">${flashMessage}</div></c:if>
    <c:if test="${not empty flashError}"><div class="alert error">${flashError}</div></c:if>

    <div class="card">
        <form method="get" action="${pageContext.request.contextPath}/admin/workload" class="form-grid">
            <div class="field">
                <label for="applicantKeyword">Applicant keyword</label>
                <input id="applicantKeyword" name="applicantKeyword" value="${applicantKeyword}" placeholder="Name, student ID, email">
            </div>
            <div class="field">
                <label for="moduleKeyword">Module keyword</label>
                <input id="moduleKeyword" name="moduleKeyword" value="${moduleKeyword}" placeholder="Module code or name">
            </div>
            <div class="field field-span-2">
                <label>
                    <input type="checkbox" name="flaggedOnly" ${flaggedOnly ? 'checked' : ''}>
                    Show only overloaded or blacklisted applicants
                </label>
            </div>
            <div class="field field-span-2 form-actions">
                <button class="btn primary" type="submit">Apply filters</button>
                <a class="btn" href="${pageContext.request.contextPath}/admin/workload">Clear filters</a>
            </div>
        </form>
    </div>

    <c:if test="${not empty applicantKeyword or not empty moduleKeyword or flaggedOnly}">
        <div class="card spacing-top">
            <p class="hint">
                Active filters:
                applicant=<strong><c:out value="${empty applicantKeyword ? 'Any' : applicantKeyword}" /></strong>,
                module=<strong><c:out value="${empty moduleKeyword ? 'Any' : moduleKeyword}" /></strong>,
                flaggedOnly=<strong><c:out value="${flaggedOnly ? 'Yes' : 'No'}" /></strong>
            </p>
        </div>
    </c:if>

    <div class="card spacing-top">
        <c:choose>
            <c:when test="${empty summaries}">
                <p class="hint">No workload records matched the current filters.</p>
            </c:when>
            <c:otherwise>
                <div class="summary-card-grid">
                    <c:forEach items="${summaries}" var="summary">
                        <div class="summary-card ${summary.overloaded ? 'summary-card-overloaded' : ''} ${summary.blacklisted ? 'summary-card-blacklisted' : ''}">
                            <div class="summary-card-header">
                                <div>
                                    <h3>${summary.displayName}</h3>
                                    <div class="summary-card-meta">
                                        <span>Student ID: <strong><c:out value="${empty summary.studentId ? '-' : summary.studentId}" /></strong></span>
                                        <span>Email: <strong><c:out value="${empty summary.email ? '-' : summary.email}" /></strong></span>
                                    </div>
                                </div>
                                <div class="summary-card-flags">
                                    <c:if test="${summary.blacklisted}"><span class="tag tag-danger">Blacklisted</span></c:if>
                                    <c:if test="${summary.overloaded}"><span class="tag tag-warning">Overloaded</span></c:if>
                                    <c:if test="${not summary.blacklisted and not summary.overloaded}"><span class="tag">OK</span></c:if>
                                </div>
                            </div>

                            <div class="summary-metrics">
                                <div class="summary-metric">
                                    <span class="summary-metric-label">Total Applications</span>
                                    <strong>${summary.totalApplicationsCount}</strong>
                                </div>
                                <div class="summary-metric">
                                    <span class="summary-metric-label">Pending</span>
                                    <strong>${summary.submittedCount}</strong>
                                </div>
                                <div class="summary-metric">
                                    <span class="summary-metric-label">Unsuccessful</span>
                                    <strong>${summary.unsuccessfulCount}</strong>
                                </div>
                                <div class="summary-metric">
                                    <span class="summary-metric-label">Offered</span>
                                    <strong>${summary.offeredCount}</strong>
                                </div>
                                <div class="summary-metric">
                                    <span class="summary-metric-label">Active</span>
                                    <strong>${summary.activeCount} / ${summary.maxWorkload}</strong>
                                </div>
                            </div>

                            <div class="summary-card-footer">
                                <span class="summary-card-footer-label">Current active modules</span>
                                <div class="summary-card-modules">
                                    <c:choose>
                                        <c:when test="${empty summary.activeModules}">
                                            <span class="tag">-</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="tag"><c:out value="${summary.activeModules}" /></span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
