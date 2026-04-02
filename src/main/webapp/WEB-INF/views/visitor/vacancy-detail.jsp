<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Vacancy Detail</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<div class="page">
    <div class="topbar">
        <div class="brand">
            <h1>Vacancy Detail</h1>
            <p>Review the role information before choosing whether to log in and apply.</p>
        </div>
        <div class="nav-actions">
            <a class="btn" href="${pageContext.request.contextPath}/vacancies">Back</a>
            <c:choose>
                <c:when test="${loggedIn}">
                    <c:if test="${isApplicant}">
                        <a class="btn" href="${pageContext.request.contextPath}/applicant/profile">My Profile</a>
                        <a class="btn" href="${pageContext.request.contextPath}/applicant/status">My Status</a>
                    </c:if>
                    <c:if test="${isMO}">
                        <a class="btn" href="${pageContext.request.contextPath}/mo/applicants">MO Review</a>
                    </c:if>
                    <c:if test="${isAdmin}">
                        <a class="btn" href="${pageContext.request.contextPath}/admin/config">Admin Config</a>
                        <a class="btn" href="${pageContext.request.contextPath}/admin/workload">Workload</a>
                    </c:if>
                    <a class="btn" href="${pageContext.request.contextPath}/logout">Log Out</a>
                </c:when>
                <c:otherwise>
                    <a class="btn" href="${pageContext.request.contextPath}/login">Log In</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <c:if test="${not empty flashMessage}"><div class="alert success">${flashMessage}</div></c:if>
    <c:if test="${not empty flashError}"><div class="alert error">${flashError}</div></c:if>

    <c:choose>
        <c:when test="${vacancy == null}">
            <div class="card"><h2>Vacancy not found</h2><p class="hint">The vacancy ID may be invalid or the posting may have been removed.</p></div>
        </c:when>
        <c:otherwise>
            <div class="card detail-list">
                <div>
                    <h2>${vacancy.title}</h2>
                    <p class="hint">${vacancy.moduleCode} - ${vacancy.moduleName}</p>
                </div>
                <div><strong>Description:</strong> ${vacancy.description}</div>
                <div><strong>Preferred background:</strong> ${vacancy.preferredBackground}</div>
                <div><strong>Workload value:</strong> ${vacancy.workloadValue}</div>
                <div><strong>Deadline:</strong> ${vacancy.deadline}</div>
                <div><strong>Current applicants:</strong> ${vacancy.applicantCount}</div>
                <div><strong>Required skills:</strong><div class="meta"><c:forEach items="${vacancy.requiredSkills}" var="skill"><span class="tag">${skill}</span></c:forEach></div></div>

                <c:choose>
                    <c:when test="${loggedIn and isApplicant}">
                        <form method="post" action="${pageContext.request.contextPath}/applicant/apply" class="inline-form">
                            <input type="hidden" name="vacancyId" value="${vacancy.vacancyId}">
                            <button class="btn primary" type="submit">Apply for this vacancy</button>
                        </form>
                        <p class="hint">Current workload rule: each applicant may hold up to ${adminConfig.maxWorkload} active roles.</p>
                    </c:when>
                    <c:when test="${loggedIn and isMO}">
                        <a class="btn primary" href="${pageContext.request.contextPath}/mo/applicants?vacancyId=${vacancy.vacancyId}">Review applicants for this vacancy</a>
                    </c:when>
                    <c:when test="${loggedIn and isAdmin}">
                        <div class="warning">Admin accounts can manage workload rules and view the workload dashboard from the admin area.</div>
                    </c:when>
                    <c:when test="${loggedIn}">
                        <div class="warning">This logged-in account does not have applicant permissions for vacancy applications.</div>
                    </c:when>
                    <c:otherwise>
                        <div class="warning">Please log in before applying, viewing your status, or updating your applicant profile.<div class="spacing-top"><a class="btn primary" href="${pageContext.request.contextPath}/login">Log In to continue</a></div></div>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
