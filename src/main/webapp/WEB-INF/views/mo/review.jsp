<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>MO Review</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<div class="page page-admin">
    <div class="topbar-wide">
        <div class="brand">
            <h1>Review Course Applicants</h1>
            <p>Review applicants for <strong>${vacancy.moduleCode} - ${vacancy.moduleName}</strong>, record decisions, and appoint one lead TA if the course allows it.</p>
        </div>
        <div class="nav-actions panel-nav">
            <a href="${pageContext.request.contextPath}/mo/applicants?vacancyId=${vacancy.vacancyId}" class="btn btn-nav btn-nav-active">MO Review</a>
            <a href="${pageContext.request.contextPath}/mo/create-vacancy" class="btn btn-nav">Publish Course Job</a>
            <a href="${pageContext.request.contextPath}/vacancies" class="btn btn-nav btn-nav-subtle">Browse Jobs</a>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-nav btn-nav-logout">Log Out</a>
        </div>
    </div>

    <div class="subcard section-stack">
        <div class="summary-card-header">
            <div>
                <h3>${vacancy.moduleCode} - ${vacancy.moduleName}</h3>
                <p>Course-based TA team</p>
            </div>
            <div class="summary-card-flags">
                <span class="status-chip status-chip-pending">${applications.size()} application<c:if test="${applications.size() != 1}">s</c:if></span>
            </div>
        </div>
        <p>${vacancy.description}</p>
        <div class="meta spacing-top">
            <span class="tag">TA places: ${vacancy.positionCount > 0 ? vacancy.positionCount : 1}</span>
            <c:if test="${vacancy.leaderRoleAvailable}">
                <span class="tag">One lead TA can be appointed</span>
            </c:if>
        </div>
        <div class="detail-actions">
            <a href="${pageContext.request.contextPath}/mo/applicants" class="btn btn-nav">Back to course list</a>
        </div>
    </div>

    <c:if test="${not empty flashMessage}">
        <div class="notice notice-success">${flashMessage}</div>
    </c:if>
    <c:if test="${not empty flashError}">
        <div class="notice notice-error">${flashError}</div>
    </c:if>

    <c:choose>
        <c:when test="${empty applications}">
            <div class="subcard empty-state">
                <h3>No applicants yet</h3>
                <p>This course does not have any submitted applications to review yet.</p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="review-card-grid">
                <c:forEach var="application" items="${applications}">
                    <c:set var="profile" value="${profileByApplicantId[application.applicantId]}" />
                    <c:set var="user" value="${userByApplicantId[application.applicantId]}" />
                    <c:set var="activeCount" value="${activeCountByApplicantId[application.applicantId]}" />
                    <div class="summary-card review-card">
                        <div class="summary-card-header">
                            <div>
                                <h3><c:out value="${not empty profile.fullName ? profile.fullName : user.displayName}" /></h3>
                                <p>
                                    <c:out value="${not empty user.username ? user.username : application.applicantId}" />
                                    <c:if test="${not empty profile.email}"> | <c:out value="${profile.email}" /></c:if>
                                </p>
                            </div>
                            <div class="summary-card-flags">
                                <c:choose>
                                    <c:when test='${application.status == "Offered"}'>
                                        <span class="status-chip status-chip-offered">Offered</span>
                                    </c:when>
                                    <c:when test='${application.status == "Unsuccessful"}'>
                                        <span class="status-chip status-chip-unsuccessful">Unsuccessful</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status-chip status-chip-pending">Submitted</span>
                                    </c:otherwise>
                                </c:choose>
                                <c:if test="${application.leadTa}">
                                    <span class="status-chip status-chip-offered">Lead TA</span>
                                </c:if>
                            </div>
                        </div>

                        <div class="summary-metrics review-metrics">
                            <div class="summary-metric">
                                <span class="summary-metric-label">Student ID</span>
                                <strong><c:out value="${empty profile.studentId ? '-' : profile.studentId}" /></strong>
                            </div>
                            <div class="summary-metric">
                                <span class="summary-metric-label">Current active roles</span>
                                <strong><c:out value="${empty activeCount ? 0 : activeCount}" /></strong>
                            </div>
                            <div class="summary-metric">
                                <span class="summary-metric-label">Submitted at</span>
                                <strong><c:out value="${empty application.submittedAt ? '-' : (fn:length(application.submittedAt) gt 19 ? fn:substring(fn:replace(application.submittedAt, 'T', ' '), 0, 19) : fn:replace(application.submittedAt, 'T', ' '))}" /></strong>
                            </div>
                        </div>

                        <div class="review-body-grid">
                            <div class="subcard review-subcard">
                                <h3>Profile Summary</h3>
                                <dl class="review-detail-list">
                                    <dt>Degree</dt>
                                    <dd><c:out value="${empty profile.degreeProgramme ? '-' : profile.degreeProgramme}" /></dd>
                                    <dt>Year</dt>
                                    <dd><c:out value="${empty profile.yearOfStudy ? '-' : profile.yearOfStudy}" /></dd>
                                    <dt>Relevant Courses</dt>
                                    <dd><c:out value="${empty profile.relevantCourses ? '-' : profile.relevantCourses}" /></dd>
                                    <dt>Skills</dt>
                                    <dd><c:out value="${empty profile.skills ? '-' : profile.skills}" /></dd>
                                    <dt>Availability</dt>
                                    <dd><c:out value="${empty profile.availability ? '-' : profile.availability}" /></dd>
                                    <dt>CV</dt>
                                    <dd>
                                        <c:choose>
                                            <c:when test="${not empty profile.cvFileName}">
                                                <div class="review-cv-actions">
                                                    <span><c:out value="${profile.cvFileName}" /></span>
                                                    <a class="btn btn-nav" href="${pageContext.request.contextPath}/mo/cv?applicantId=${application.applicantId}">Download CV</a>
                                                </div>
                                            </c:when>
                                            <c:otherwise>Not provided</c:otherwise>
                                        </c:choose>
                                    </dd>
                                </dl>
                            </div>

                            <form method="post" action="${pageContext.request.contextPath}/mo/review" class="subcard section-stack review-form-card">
                                <input type="hidden" name="vacancyId" value="${vacancy.vacancyId}">
                                <input type="hidden" name="applicationId" value="${application.applicationId}">

                                <div class="form-grid review-form-grid">
                                    <div class="form-field review-field">
                                        <label for="decision-${application.applicationId}">Decision</label>
                                        <select class="review-select" id="decision-${application.applicationId}" name="decision">
                                            <option value="Offered" <c:if test='${application.status == "Offered"}'>selected</c:if>>Offered</option>
                                            <option value="Unsuccessful" <c:if test='${application.status == "Unsuccessful"}'>selected</c:if>>Unsuccessful</option>
                                        </select>
                                    </div>
                                </div>

                                <c:if test="${vacancy.leaderRoleAvailable}">
                                    <label class="toggle-card compact-toggle review-toggle">
                                        <input type="checkbox" name="appointLeadTa" <c:if test="${application.leadTa}">checked</c:if>>
                                        <span>
                                            <strong>Appoint as lead TA</strong>
                                            <span class="field-hint">Only one offered applicant can hold the lead role for this course.</span>
                                        </span>
                                    </label>
                                </c:if>

                                <div class="form-field review-field">
                                    <label for="reviewNote-${application.applicationId}">Review Note</label>
                                    <textarea class="review-textarea" id="reviewNote-${application.applicationId}" name="reviewNote" rows="4" placeholder="Summarize the decision basis.">${application.reviewNote}</textarea>
                                </div>

                                <div class="form-field review-field">
                                    <label for="optionalFeedback-${application.applicationId}">Optional Feedback</label>
                                    <textarea class="review-textarea" id="optionalFeedback-${application.applicationId}" name="optionalFeedback" rows="4" placeholder="Share concise feedback with the applicant if needed.">${application.optionalFeedback}</textarea>
                                </div>

                                <div class="config-submit-bar">
                                    <div>
                                        <strong>Save review decision</strong>
                                        <p>Updates the applicant status, notes, and optional feedback for this course job.</p>
                                    </div>
                                    <button type="submit" class="btn btn-hero btn-hero-compact">Save decision</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>


