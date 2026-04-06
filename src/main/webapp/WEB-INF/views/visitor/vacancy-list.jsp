<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Vacancy List</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<div class="${isAdmin ? 'page page-admin' : 'page'}">
    <div class="${isAdmin ? 'topbar topbar-admin' : 'topbar'}">
        <div class="brand">
            <h1>Visitor Interface</h1>
            <p>Browse TA opportunities before logging in. Protected actions will ask you to sign in.</p>
        </div>
        <c:choose>
            <c:when test="${isAdmin}">
                <div class="nav-actions admin-nav">
                    <a class="btn btn-nav" href="${pageContext.request.contextPath}/admin/config">Config</a>
                    <a class="btn btn-nav" href="${pageContext.request.contextPath}/admin/workload">Workload</a>
                    <a class="btn btn-nav" href="${pageContext.request.contextPath}/admin/blacklist">Blacklist</a>
                    <a class="btn btn-nav btn-nav-active" href="${pageContext.request.contextPath}/vacancies">Browse Vacancies</a>
                    <a class="btn btn-nav btn-nav-logout" href="${pageContext.request.contextPath}/logout">Log Out</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="nav-actions">
                    <c:choose>
                        <c:when test="${loggedIn}">
                            <c:if test="${isApplicant}">
                                <a class="btn" href="${pageContext.request.contextPath}/applicant/profile">My Profile</a>
                                <a class="btn" href="${pageContext.request.contextPath}/applicant/status">My Status</a>
                            </c:if>
                            <c:if test="${isMO}">
                                <a class="btn" href="${pageContext.request.contextPath}/mo/applicants">MO Review</a>
                            </c:if>
                            <a class="btn" href="${pageContext.request.contextPath}/logout">Log Out</a>
                        </c:when>
                        <c:otherwise>
                            <a class="btn" href="${pageContext.request.contextPath}/login">Log In</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <c:if test="${not empty flashMessage}"><div class="alert success">${flashMessage}</div></c:if>
    <c:if test="${not empty flashError}"><div class="alert error">${flashError}</div></c:if>

    <div class="grid">
        <c:forEach items="${vacancies}" var="vacancy">
            <div class="card">
                <div class="card-header">
                    <div>
                        <h2>${vacancy.title}</h2>
                        <p class="hint">${vacancy.description}</p>
                    </div>
                    <span class="status-badge status-open">${vacancy.status}</span>
                </div>
                <p><strong>Module:</strong> ${vacancy.moduleCode} - ${vacancy.moduleName}</p>
                <p><strong>Deadline:</strong> ${vacancy.deadline}</p>
                <div class="meta">
                    <c:forEach items="${vacancy.requiredSkills}" var="skill"><span class="tag">${skill}</span></c:forEach>
                </div>
                <p class="hint spacing-top">${vacancy.applicantCount} applicants</p>
                <a class="btn primary" href="${pageContext.request.contextPath}/vacancy?id=${vacancy.vacancyId}">View details</a>
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>
