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
<div class="page page-admin">
    <div class="topbar-wide">
        <div class="brand">
            <h1>MO Review Area</h1>
            <p>Select one course job to review applicants, record decisions, and appoint a lead TA where that option exists.</p>
        </div>
        <div class="nav-actions panel-nav">
            <a class="btn btn-nav btn-nav-active" href="${pageContext.request.contextPath}/mo/applicants">MO Review</a>
            <a class="btn btn-nav" href="${pageContext.request.contextPath}/mo/create-vacancy">Publish Course Job</a>
            <a class="btn btn-nav btn-nav-subtle" href="${pageContext.request.contextPath}/vacancies">Browse Jobs</a>
            <a class="btn btn-nav btn-nav-logout" href="${pageContext.request.contextPath}/logout">Log Out</a>
        </div>
    </div>

    <c:if test="${not empty flashMessage}"><div class="alert success">${flashMessage}</div></c:if>
    <c:if test="${not empty flashError}"><div class="alert error">${flashError}</div></c:if>

    <div class="card section-stack">
        <div class="subcard">
            <strong>Organiser review queue</strong>
            <div class="hint">Each card represents one course-based TA team. Open the course to review applicants and use the TA place count as your capacity guide.</div>
        </div>

        <c:choose>
            <c:when test="${empty managedVacancies}">
                <div class="subcard empty-state">
                    <h3>No organiser-owned course jobs available</h3>
                    <p class="hint">Publish your first course job, then return here once applications start arriving.</p>
                    <div>
                        <a class="btn primary" href="${pageContext.request.contextPath}/mo/create-vacancy">Publish your first course job</a>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="vacancy-grid">
                    <c:forEach items="${managedVacancies}" var="vacancy">
                        <div class="vacancy-card">
                            <div class="card-header">
                                <div>
                                    <h2>${vacancy.moduleCode} - ${vacancy.moduleName}</h2>
                                    <p class="hint">Course-based TA team</p>
                                    <p class="hint"><strong>Campus:</strong> ${empty vacancy.campus ? 'To be confirmed' : vacancy.campus}</p>
                                </div>
                                <span class="status-badge ${vacancy.status == 'OPEN' ? 'status-open' : 'status-closed'}">${vacancy.status}</span>
                            </div>
                            <p>${vacancy.description}</p>
                            <div class="meta spacing-top">
                                <span class="tag">TA places: ${vacancy.positionCount > 0 ? vacancy.positionCount : 1}</span>
                                <span class="tag">${vacancy.applicantCount} applicants</span>
                                <c:if test="${vacancy.leaderRoleAvailable}">
                                    <span class="tag">Lead TA appointment available</span>
                                </c:if>
                            </div>
                            <div class="detail-actions spacing-top">
                                <a class="btn primary" href="${pageContext.request.contextPath}/mo/applicants?vacancyId=${vacancy.vacancyId}">Review applicants</a>
                                <form method="post"
                                      action="${pageContext.request.contextPath}/mo/vacancy/archive"
                                      class="inline-form"
                                      onsubmit="return confirm('Archive this course job? It will be hidden from Browse Jobs.');">
                                    <input type="hidden" name="vacancyId" value="${vacancy.vacancyId}">
                                    <button type="submit" class="btn btn-nav btn-nav-logout">Archive</button>
                                </form>
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

