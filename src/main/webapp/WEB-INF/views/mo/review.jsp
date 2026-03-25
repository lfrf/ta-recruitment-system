<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Review Applicants</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<div class="page">
    <div class="topbar">
        <div class="brand">
            <h1>Review Applicants</h1>
            <p>${vacancy.moduleCode} - ${vacancy.moduleName}: ${vacancy.title}</p>
        </div>
        <div class="nav-actions">
            <a class="btn" href="${pageContext.request.contextPath}/mo/applicants">Back to vacancy list</a>
            <a class="btn" href="${pageContext.request.contextPath}/logout">Log Out</a>
        </div>
    </div>

    <c:if test="${not empty flashMessage}"><div class="alert success">${flashMessage}</div></c:if>
    <c:if test="${not empty flashError}"><div class="alert error">${flashError}</div></c:if>

    <div class="card">
        <c:choose>
            <c:when test="${empty applications}">
                <p class="hint">No applications have been submitted for this vacancy yet.</p>
            </c:when>
            <c:otherwise>
                <table class="table">
                    <thead>
                    <tr>
                        <th>Applicant</th>
                        <th>Profile Summary</th>
                        <th>Current Workload</th>
                        <th>Status</th>
                        <th>Decision Update</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${applications}" var="application">
                        <c:set var="profile" value="${profileByApplicantId[application.applicantId]}" />
                        <c:set var="user" value="${userByApplicantId[application.applicantId]}" />
                        <tr>
                            <td>
                                <strong><c:out value="${profile != null && not empty profile.fullName ? profile.fullName : user.displayName}" /></strong><br>
                                <span class="hint"><c:out value="${profile != null ? profile.studentId : application.applicantId}" /></span><br>
                                <span class="hint"><c:out value="${profile != null && not empty profile.email ? profile.email : user.email}" /></span>
                            </td>
                            <td>
                                <div><strong>Skills:</strong> <c:out value="${profile != null ? profile.skills : '-'}" /></div>
                                <div><strong>CV:</strong> <c:out value="${profile != null && not empty profile.cvFileName ? profile.cvFileName : '-'}" /></div>
                                <div><strong>Availability:</strong> <c:out value="${profile != null && not empty profile.availability ? profile.availability : '-'}" /></div>
                            </td>
                            <td>
                                <c:set var="activeCount" value="${activeCountByApplicantId[application.applicantId]}" />
                                <strong><c:out value="${empty activeCount ? 0 : activeCount}" /></strong> active applications
                            </td>
                            <td><span class="status-badge status-${application.status}">${application.status}</span></td>
                            <td>
                                <form method="post" action="${pageContext.request.contextPath}/mo/review" class="form-grid review-form">
                                    <input type="hidden" name="vacancyId" value="${vacancy.vacancyId}">
                                    <input type="hidden" name="applicationId" value="${application.applicationId}">
                                    <div class="field field-span-2">
                                        <label>Decision</label>
                                        <select name="decision">
                                            <option value="Offered" ${application.status eq 'Offered' ? 'selected' : ''}>Offered</option>
                                            <option value="Unsuccessful" ${application.status eq 'Unsuccessful' ? 'selected' : ''}>Unsuccessful</option>
                                        </select>
                                    </div>
                                    <div class="field field-span-2">
                                        <label>Review Note</label>
                                        <textarea name="reviewNote" rows="2">${application.reviewNote}</textarea>
                                    </div>
                                    <div class="field field-span-2">
                                        <label>Optional Feedback</label>
                                        <textarea name="optionalFeedback" rows="2">${application.optionalFeedback}</textarea>
                                    </div>
                                    <div class="field field-span-2">
                                        <button class="btn primary" type="submit">Save decision</button>
                                    </div>
                                </form>
                            </td>
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
