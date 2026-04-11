<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Publish Course Job</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<div class="page page-admin">
    <div class="topbar-wide">
        <div class="brand">
            <h1>Publish Course Job</h1>
            <p>Set up one course-based TA team, define the total TA slots, choose the campus, and decide whether one selected TA may later become the lead.</p>
        </div>
        <div class="nav-actions panel-nav">
            <a class="btn btn-nav" href="${pageContext.request.contextPath}/mo/applicants">MO Review</a>
            <a class="btn btn-nav btn-nav-active" href="${pageContext.request.contextPath}/mo/create-vacancy">Publish Course Job</a>
            <a class="btn btn-nav btn-nav-subtle" href="${pageContext.request.contextPath}/vacancies">Browse Jobs</a>
            <a class="btn btn-nav btn-nav-logout" href="${pageContext.request.contextPath}/logout">Log Out</a>
        </div>
    </div>

    <c:if test="${not empty flashMessage}"><div class="alert success">${flashMessage}</div></c:if>
    <c:if test="${not empty flashError}"><div class="alert error">${flashError}</div></c:if>
    <c:if test="${not empty errorMessage}"><div class="alert error">${errorMessage}</div></c:if>

    <div class="card section-stack">
        <div class="subcard">
            <strong>Course-based TA team</strong>
            <div class="hint">Applicants will browse by course first. Keep the module information clear, choose the campus, set the total TA slot count, and only enable a lead appointment if the course really needs one lead TA.</div>
        </div>

        <form method="post" action="${pageContext.request.contextPath}/mo/create-vacancy" class="form-grid">
            <div class="field">
                <label for="moduleCode">Module Code *</label>
                <input id="moduleCode" type="text" name="moduleCode" value="${moduleCode}" placeholder="e.g. EBU6304" required>
            </div>
            <div class="field">
                <label for="moduleName">Module Name *</label>
                <input id="moduleName" type="text" name="moduleName" value="${moduleName}" placeholder="e.g. Software Engineering" required>
            </div>
            <div class="field">
                <label for="campus">Campus *</label>
                <select id="campus" name="campus" required>
                    <option value="">Select campus</option>
                    <option value="Xitucheng Campus" <c:if test="${campus == 'Xitucheng Campus'}">selected</c:if>>Xitucheng Campus</option>
                    <option value="Shahe Campus" <c:if test="${campus == 'Shahe Campus'}">selected</c:if>>Shahe Campus</option>
                </select>
            </div>
            <div class="field">
                <label for="workloadValue">Workload Value *</label>
                <input id="workloadValue" type="number" name="workloadValue" min="1" value="${workloadValue}" placeholder="e.g. 1" required>
            </div>
            <div class="field">
                <label for="positionCount">Total TA Slots *</label>
                <input id="positionCount" type="number" name="positionCount" min="1" value="${positionCount}" placeholder="e.g. 3" required>
            </div>
            <div class="field field-span-2">
                <label for="description">Course Support Summary *</label>
                <textarea id="description" name="description" rows="4" placeholder="Describe what support this course expects from its TA team" required>${description}</textarea>
            </div>
            <div class="field field-span-2">
                <label for="requiredSkills">Required Skills *</label>
                <input id="requiredSkills" type="text" name="requiredSkills" value="${requiredSkills}" placeholder="e.g. Java, communication, debugging" required>
                <p class="field-hint">Separate each required skill with a comma.</p>
            </div>
            <div class="field field-span-2">
                <label for="preferredBackground">Preferred Background</label>
                <textarea id="preferredBackground" name="preferredBackground" rows="3" placeholder="Describe any preferred background, prior modules, or experience">${preferredBackground}</textarea>
            </div>
            <div class="field field-span-2 checkbox-card">
                <label class="checkbox-wrap checkbox-wrap-emphasis" for="leaderRoleAvailable">
                    <input id="leaderRoleAvailable" type="checkbox" name="leaderRoleAvailable" <c:if test="${leaderRoleAvailable}">checked</c:if>>
                    <span>
                        <strong>Allow one selected TA to be appointed as course lead later</strong>
                        <span class="hint">Use this only when the course needs one lead TA to coordinate the rest of the team.</span>
                    </span>
                </label>
            </div>
            <div class="field field-span-2">
                <div class="config-submit-bar">
                    <div class="config-submit-copy">
                        <strong>Publish once the course details are clear</strong>
                        <div class="hint">After publishing, the course job will appear immediately in Browse Jobs and applicants can apply directly from that list.</div>
                    </div>
                    <button class="btn primary btn-hero" type="submit">
                        <span class="btn-hero-text">
                            <span class="btn-hero-title">Publish course job</span>
                            <span class="btn-hero-subtitle">Create the TA team entry for this module</span>
                        </span>
                        <svg class="btn-hero-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false"><path d="M6 12.5l4 4L18 8.75" /></svg>
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>
