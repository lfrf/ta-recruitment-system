<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Course Job Detail</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<div class="${isAdmin ? 'page page-admin' : 'page'}">
    <div class="topbar-wide">
        <div class="brand">
            <h1>Course Job Detail</h1>
            <p>Check the course summary, total TA slots, and required skills, then apply only if the course fits your strengths.</p>
        </div>
        <c:choose>
            <c:when test="${isAdmin}">
                <div class="nav-actions admin-nav">
                    <a class="btn btn-nav" href="${pageContext.request.contextPath}/admin/config">Config</a>
                    <a class="btn btn-nav" href="${pageContext.request.contextPath}/admin/workload">Workload</a>
                    <a class="btn btn-nav" href="${pageContext.request.contextPath}/admin/blacklist">Blacklist</a>
                    <a class="btn btn-nav btn-nav-active" href="${pageContext.request.contextPath}/vacancies">Browse Vacancies</a>
                    <a class="btn btn-nav" href="${pageContext.request.contextPath}/account/password">Change Password</a>
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
                            <a class="btn btn-nav" href="${pageContext.request.contextPath}/account/password">Change Password</a>
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
        <c:when test="${vacancy == null}">
            <div class="card empty-state">
                <h2>Course job not found</h2>
                <p class="hint">The selected course job may no longer be available.</p>
                <div><a class="btn primary" href="${pageContext.request.contextPath}/vacancies">Return to Browse Jobs</a></div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="detail-panel">
                <div>
                    <h2>${vacancy.moduleCode} - ${vacancy.moduleName}</h2>
                    <p class="hint">
                        This course publishes one TA team.
                        <c:if test="${vacancy.leaderRoleAvailable and leadTaAssigned}"> Lead TA has already been appointed.</c:if>
                        <c:if test="${vacancy.leaderRoleAvailable and not leadTaAssigned}"> Organisers may later appoint one selected TA as leader if that option is enabled.</c:if>
                    </p>
                </div>
                <div><strong>Campus:</strong> ${empty vacancy.campus ? 'To be confirmed' : vacancy.campus}</div>
                <div><strong>Course support summary:</strong> ${vacancy.description}</div>
                <div><strong>Preferred background:</strong> ${vacancy.preferredBackground}</div>
                <div><strong>Workload value:</strong> ${vacancy.workloadValue}</div>
                <div><strong>Total TA slots:</strong> ${vacancy.positionCount > 0 ? vacancy.positionCount : 1}</div>
                <div><strong>Current offers:</strong> ${offeredCount}</div>
                <div><strong>Current applicants:</strong> ${vacancy.applicantCount}</div>
                <c:if test="${vacancy.leaderRoleAvailable}">
                    <div>
                        <strong>Leader appointment:</strong>
                        <c:choose>
                            <c:when test="${leadTaAssigned}">Lead TA has already been appointed for this course.</c:when>
                            <c:otherwise>One selected TA may later be appointed as the course lead.</c:otherwise>
                        </c:choose>
                    </div>
                </c:if>
                <div>
                    <strong>Required skills:</strong>
                    <div class="meta spacing-top"><c:forEach items="${vacancy.requiredSkills}" var="skill"><span class="tag">${skill}</span></c:forEach></div>
                </div>
                <c:if test="${vacancyFull}">
                    <div class="warning">This course is currently full. New applications are temporarily closed.</div>
                </c:if>

                <c:choose>
                    <c:when test="${loggedIn and isApplicant and alreadyApplied}">
                        <div class="subcard">
                            <strong>Application already recorded</strong>
                            <div class="hint">You have already applied for this course job. Use Application History only when you want to review the latest decision or note.</div>
                            <div class="detail-actions spacing-top">
                                <a class="btn primary" href="${pageContext.request.contextPath}/applicant/status">Application History</a>
                                <a class="btn" href="${pageContext.request.contextPath}/vacancies">Back to Browse Jobs</a>
                            </div>
                        </div>
                    </c:when>
                    <c:when test="${loggedIn and isApplicant and vacancyFull}">
                        <div class="subcard">
                            <strong>No TA slots left</strong>
                            <div class="hint">All available places for this course have already been offered. If a place opens again, you can apply from Browse Jobs.</div>
                            <div class="detail-actions spacing-top">
                                <a class="btn btn-nav" href="${pageContext.request.contextPath}/vacancies">Back to Browse Jobs</a>
                            </div>
                        </div>
                    </c:when>
                    <c:when test="${loggedIn and isApplicant and profileReady}">
                        <div class="subcard">
                            <strong>Apply for this course job</strong>
                            <div class="hint">Apply in one step from here. The page stays focused on the course and the submit action.</div>
                            <div class="detail-actions spacing-top">
                                <form method="post" action="${pageContext.request.contextPath}/applicant/apply" class="inline-form"><input type="hidden" name="vacancyId" value="${vacancy.vacancyId}"><button class="btn primary" type="submit">Apply now</button></form>
                                <a class="btn" href="${pageContext.request.contextPath}/vacancies">Back to Browse Jobs</a>
                            </div>
                        </div>
                    </c:when>
                    <c:when test="${loggedIn and isApplicant}">
                        <div class="subcard">
                            <strong>Complete basic profile first</strong>
                            <div class="hint">Before applying, please complete your full name, student ID, and email in My Profile. Then you can return here and apply directly.</div>
                            <div class="detail-actions spacing-top">
                                <a class="btn primary" href="${pageContext.request.contextPath}/applicant/profile">Complete profile to apply</a>
                                <a class="btn" href="${pageContext.request.contextPath}/vacancies">Back to Browse Jobs</a>
                            </div>
                        </div>
                    </c:when>
                    <c:when test="${loggedIn and isMO}">
                        <div class="subcard">
                            <strong>Organiser action</strong>
                            <div class="hint">Open the applicant review queue for this course and record decisions with notes.</div>
                            <div class="detail-actions spacing-top"><a class="btn primary" href="${pageContext.request.contextPath}/mo/applicants?vacancyId=${vacancy.vacancyId}">Review applicants for this course</a></div>
                        </div>
                    </c:when>
                    <c:when test="${loggedIn and isAdmin}"><div class="warning">Admin accounts manage workload rules, blacklist records, and workload monitoring from the admin area.</div></c:when>
                    <c:when test="${loggedIn}"><div class="warning">This logged-in account does not have applicant permissions for course job applications.</div></c:when>
                    <c:otherwise>
                        <div class="subcard">
                            <strong>Protected action</strong>
                            <div class="hint">Log in before applying, checking your saved applications, or editing your profile.</div>
                            <div class="detail-actions spacing-top"><a class="btn primary" href="${pageContext.request.contextPath}/login">Log In to Apply</a></div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>

