<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Browse Jobs</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<div class="${isAdmin ? 'page page-admin' : 'page'}">
    <div class="topbar-wide">
        <div class="brand">
            <h1>Browse Jobs</h1>
            <p>Every course publishes one TA team. Compare the course cards below and apply directly from the list when the basics of your profile are ready.</p>
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
                <div class="nav-actions panel-nav">
                    <a class="btn btn-nav btn-nav-active" href="${pageContext.request.contextPath}/vacancies">Browse Jobs</a>
                    <c:choose>
                        <c:when test="${loggedIn}">
                            <c:if test="${isApplicant}"><a class="btn btn-nav" href="${pageContext.request.contextPath}/applicant/profile">My Profile</a></c:if>
                            <c:if test="${isApplicant}"><a class="btn btn-nav" href="${pageContext.request.contextPath}/applicant/status">Application History</a></c:if>
                            <c:if test="${isMO}"><a class="btn btn-nav" href="${pageContext.request.contextPath}/mo/applicants">MO Review</a></c:if>
                            <a class="btn btn-nav btn-nav-logout" href="${pageContext.request.contextPath}/logout">Log Out</a>
                        </c:when>
                        <c:otherwise><a class="btn btn-nav" href="${pageContext.request.contextPath}/login">Log In</a></c:otherwise>
                    </c:choose>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <c:if test="${not empty flashMessage}"><div class="alert success">${flashMessage}</div></c:if>
    <c:if test="${not empty flashError}"><div class="alert error">${flashError}</div></c:if>

    <c:choose>
        <c:when test="${empty vacancies}">
            <div class="card empty-state">
                <h2>No course jobs available</h2>
                <p class="hint">There are currently no open TA opportunities available to browse.</p>
                <c:if test="${not loggedIn and not isAdmin}"><div><a class="btn primary" href="${pageContext.request.contextPath}/login">Log In to Apply</a></div></c:if>
            </div>
        </c:when>
        <c:otherwise>
            <div class="vacancy-grid">
                <c:forEach items="${vacancies}" var="vacancy">
                    <div class="vacancy-card">
                        <div class="card-header">
                            <div>
                                <h2>${vacancy.moduleCode} - ${vacancy.moduleName}</h2>
                                <p class="hint">One course-based TA team posted by the organiser for this module.</p>
                                <p class="hint"><strong>Campus:</strong> ${empty vacancy.campus ? 'To be confirmed' : vacancy.campus}</p>
                            </div>
                            <span class="status-badge status-open">${vacancy.status}</span>
                        </div>
                        <p>${vacancy.description}</p>
                        <div class="meta spacing-top">
                            <span class="tag">TA places: ${vacancy.positionCount > 0 ? vacancy.positionCount : 1}</span>
                            <c:if test="${vacancy.leaderRoleAvailable}"><span class="tag">Lead TA appointed later</span></c:if>
                            <span class="tag">${vacancy.applicantCount} applicants</span>
                        </div>
                        <div class="meta spacing-top">
                            <c:forEach items="${vacancy.requiredSkills}" var="skill"><span class="tag">${skill}</span></c:forEach>
                        </div>
                        <div class="detail-actions spacing-top">
                            <c:choose>
                                <c:when test="${isApplicant and appliedVacancyIds[vacancy.vacancyId]}">
                                    <span class="status-chip status-chip-pending">Applied</span>
                                    <a class="btn btn-nav" href="${pageContext.request.contextPath}/vacancy?id=${vacancy.vacancyId}">View details</a>
                                </c:when>
                                <c:when test="${isApplicant}">
                                    <form method="post" action="${pageContext.request.contextPath}/applicant/apply" class="inline-form">
                                        <input type="hidden" name="vacancyId" value="${vacancy.vacancyId}">
                                        <button class="btn primary" type="submit">Apply now</button>
                                    </form>
                                    <a class="btn btn-nav" href="${pageContext.request.contextPath}/vacancy?id=${vacancy.vacancyId}">View details</a>
                                </c:when>
                                <c:when test="${loggedIn}">
                                    <a class="btn primary" href="${pageContext.request.contextPath}/vacancy?id=${vacancy.vacancyId}">View details</a>
                                </c:when>
                                <c:otherwise>
                                    <a class="btn primary" href="${pageContext.request.contextPath}/login">Log In to Apply</a>
                                    <a class="btn btn-nav" href="${pageContext.request.contextPath}/vacancy?id=${vacancy.vacancyId}">View details</a>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>

