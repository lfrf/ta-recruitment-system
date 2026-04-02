<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Create Vacancy</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<div class="page">
    <div class="topbar">
        <div class="brand">
            <h1>Create Vacancy</h1>
            <p>Create a new vacancy for your module and publish it to the vacancy list.</p>
        </div>
        <div class="nav-actions">
            <a class="btn" href="${pageContext.request.contextPath}/mo/applicants">Back to MO Review</a>
            <a class="btn" href="${pageContext.request.contextPath}/logout">Log Out</a>
        </div>
    </div>

    <c:if test="${not empty flashMessage}">
        <div class="alert success">${flashMessage}</div>
    </c:if>
    <c:if test="${not empty flashError}">
        <div class="alert error">${flashError}</div>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <div class="alert error">${errorMessage}</div>
    </c:if>

    <div class="card">
        <form method="post" action="${pageContext.request.contextPath}/mo/create-vacancy" class="form-grid">
            <div class="field">
                <label>Module Code</label>
                <input type="text" name="moduleCode" value="${moduleCode}">
            </div>

            <div class="field">
                <label>Module Name</label>
                <input type="text" name="moduleName" value="${moduleName}">
            </div>

            <div class="field field-span-2">
                <label>Title</label>
                <input type="text" name="title" value="${title}">
            </div>

            <div class="field field-span-2">
                <label>Description</label>
                <textarea name="description" rows="4">${description}</textarea>
            </div>

            <div class="field field-span-2">
                <label>Required Skills (comma separated)</label>
                <input type="text" name="requiredSkills" value="${requiredSkills}" placeholder="Java, Communication, Debugging">
            </div>

            <div class="field field-span-2">
                <label>Preferred Background</label>
                <textarea name="preferredBackground" rows="3">${preferredBackground}</textarea>
            </div>

            <div class="field">
                <label>Workload Value</label>
                <input type="number" name="workloadValue" min="1" value="${workloadValue}">
            </div>

            <div class="field">
                <label>Deadline</label>
                <input type="date" name="deadline" value="${deadline}">
            </div>

            <div class="field field-span-2">
                <button class="btn primary" type="submit">Create Vacancy</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>
