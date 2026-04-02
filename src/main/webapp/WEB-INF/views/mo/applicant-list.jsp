<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>MO Review</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<div class="page">
    <div class="topbar">
        <div class="brand">
            <h1>MO Review Area</h1>
            <p>Select one of your vacancies to review the current applicants.</p>
        </div>
        <div class="nav-actions">
    <a class="btn primary" href="${pageContext.request.contextPath}/mo/create-vacancy">Create Vacancy</a>
    <a class="btn" href="${pageContext.request.contextPath}/vacancies">Browse Vacancies</a>
    <a class="btn" href="${pageContext.request.contextPath}/logout">Log Out</a>
</div>
    </div>

    <c:if test="${not empty flashMessage}"><div class="alert success">${flashMessage}</div></c:if>
    <c:if test="${not empty flashError}"><div class="alert error">${flashError}</div></c:if>

    <div class="grid">
        <c:choose>
            <c:when test="${empty managedVacancies}">
                <div class="card"><p class="hint">No organiser-owned vacancies are currently available for review.</p></div>
            </c:when>
            <c:otherwise>
                <c:forEach items="${managedVacancies}" var="vacancy">
                    <div class="card">
                        <div class="card-header">
                            <div>
                                <h2>${vacancy.title}</h2>
                                <p class="hint">${vacancy.moduleCode} - ${vacancy.moduleName}</p>
                            </div>
                            <span class="status-badge status-open">${vacancy.status}</span>
                        </div>
                        <p><strong>Applicants:</strong> ${vacancy.applicantCount}</p>
                        <p><strong>Deadline:</strong> ${vacancy.deadline}</p>
                        <a class="btn primary" href="${pageContext.request.contextPath}/mo/applicants?vacancyId=${vacancy.vacancyId}">Review applicants</a>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
