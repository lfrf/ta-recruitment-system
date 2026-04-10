<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Application History</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<div class="page page-admin">
    <div class="topbar-wide">
        <div class="brand">
            <h1>Application History</h1>
            <p>Use this page when you want to look back at previous course applications, notes, or outcomes.</p>
        </div>
        <div class="nav-actions panel-nav">
            <a class="btn btn-nav btn-nav-subtle" href="${pageContext.request.contextPath}/vacancies">Browse Jobs</a>
            <a class="btn btn-nav" href="${pageContext.request.contextPath}/applicant/profile">My Profile</a>
            <a class="btn btn-nav btn-nav-active" href="${pageContext.request.contextPath}/applicant/status">Application History</a>
            <a class="btn btn-nav btn-nav-logout" href="${pageContext.request.contextPath}/logout">Log Out</a>
        </div>
    </div>

    <c:if test="${not empty flashMessage}"><div class="alert success">${flashMessage}</div></c:if>
    <c:if test="${not empty flashError}"><div class="alert error">${flashError}</div></c:if>

    <div class="card section-stack">
        <div class="subcard">
            <strong>Previous applications</strong>
            <div class="hint">Browse Jobs remains the main start page. Come here when you want to check an earlier note or decision, or cancel an application that is still under review.</div>
            <div class="detail-actions spacing-top">
                <a class="btn btn-nav" href="${pageContext.request.contextPath}/applicant/profile">Edit my profile</a>
                <a class="btn btn-nav" href="${pageContext.request.contextPath}/vacancies">Back to Browse Jobs</a>
            </div>
        </div>

        <c:choose>
            <c:when test="${empty applications}">
                <div class="subcard empty-state">
                    <h3>No applications yet</h3>
                    <p>Go to Browse Jobs when you are ready to apply for a course-based TA opening.</p>
                    <div class="detail-actions spacing-top"><a class="btn primary" href="${pageContext.request.contextPath}/vacancies">Browse Jobs</a></div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="summary-card-grid application-status-grid">
                    <c:forEach items="${applications}" var="application">
                        <c:set var="vacancy" value="${vacancyById[application.vacancyId]}" />
                        <div class="summary-card status-card">
                            <div class="summary-card-header">
                                <div>
                                    <h3><c:out value="${vacancy != null ? vacancy.moduleCode : application.vacancyId}" /> - <c:out value="${vacancy != null ? vacancy.moduleName : 'Course'}" /></h3>
                                    <div class="summary-card-meta">
                                        <span>Applied <c:out value="${application.submittedAt}" /></span>
                                        <c:if test="${application.leadTa}"><span>Lead TA appointment</span></c:if>
                                    </div>
                                </div>
                                <div class="summary-card-flags">
                                    <c:choose>
                                        <c:when test='${application.status == "Offered"}'><span class="status-chip status-chip-offered"><c:out value="${application.leadTa ? 'Lead TA offer' : 'Offer made'}" /></span></c:when>
                                        <c:when test='${application.status == "Unsuccessful"}'><span class="status-chip status-chip-unsuccessful">Not selected</span></c:when>
                                        <c:when test='${application.status == "Withdrawn"}'><span class="status-chip status-chip-withdrawn">Cancelled by you</span></c:when>
                                        <c:otherwise><span class="status-chip status-chip-pending">Under review</span></c:otherwise>
                                    </c:choose>
                                    <c:if test='${application.status == "Submitted"}'>
                                        <form method="post" action="${pageContext.request.contextPath}/applicant/cancel" class="inline-form">
                                            <input type="hidden" name="applicationId" value="${application.applicationId}">
                                            <button type="submit" class="btn btn-nav btn-nav-logout btn-cancel-inline">Cancel application</button>
                                        </form>
                                    </c:if>
                                </div>
                            </div>
                            <div class="status-card-notes">
                                <div class="subcard status-note-card"><strong>Review note</strong><p><c:out value="${empty application.reviewNote ? 'No review note has been shared yet.' : application.reviewNote}" /></p></div>
                                <div class="subcard status-note-card"><strong>Optional feedback</strong><p><c:out value="${empty application.optionalFeedback ? 'No optional feedback has been shared yet.' : application.optionalFeedback}" /></p></div>
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
