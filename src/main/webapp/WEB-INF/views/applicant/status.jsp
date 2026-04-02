<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Application Status</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<div class="page">
    <div class="topbar">
        <div class="brand">
            <h1>My Application Status</h1>
            <p>Track the progress of every submitted vacancy application.</p>
        </div>
        <div class="nav-actions">
            <a class="btn" href="${pageContext.request.contextPath}/vacancies">Browse Vacancies</a>
            <a class="btn" href="${pageContext.request.contextPath}/applicant/profile">My Profile</a>
            <a class="btn" href="${pageContext.request.contextPath}/logout">Log Out</a>
        </div>
    </div>

    <c:if test="${not empty flashMessage}">
        <div class="alert success">${flashMessage}</div>
    </c:if>
    <c:if test="${not empty flashError}">
        <div class="alert error">${flashError}</div>
    </c:if>

    <div class="card">
        <c:choose>
            <c:when test="${empty applications}">
                <p class="hint">You have not submitted any applications yet.</p>
            </c:when>
            <c:otherwise>
                <table class="table">
                    <thead>
                    <tr>
                        <th>Vacancy</th>
                        <th>Module</th>
                        <th>Submitted At</th>
                        <th>Status</th>
                        <th>Review Note</th>
                        <th>Optional Feedback</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${applications}" var="application">
                        <c:set var="vacancy" value="${vacancyById[application.vacancyId]}" />
                        <tr>
                            <td><c:out value="${vacancy != null ? vacancy.title : application.vacancyId}" /></td>
                            <td><c:out value="${vacancy != null ? vacancy.moduleCode : '-'}" /></td>
                            <td>${application.submittedAt}</td>
                            <td><span class="status-badge status-${application.status}">${application.status}</span></td>
                            <td><c:out value="${empty application.reviewNote ? '-' : application.reviewNote}" /></td>
                            <td><c:out value="${empty application.optionalFeedback ? '-' : application.optionalFeedback}" /></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
