<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<div class="page page-admin">
    <div class="topbar-wide">
        <div class="brand">
            <h1>My Profile</h1>
            <p>Keep the basic details accurate, upload your CV if you have it ready, then return straight to Browse Jobs.</p>
        </div>
        <div class="nav-actions panel-nav">
            <a class="btn btn-nav btn-nav-subtle" href="${pageContext.request.contextPath}/vacancies">Browse Jobs</a>
            <a class="btn btn-nav btn-nav-active" href="${pageContext.request.contextPath}/applicant/profile">My Profile</a>
            <a class="btn btn-nav" href="${pageContext.request.contextPath}/applicant/status">Application History</a>
            <a class="btn btn-nav btn-nav-logout" href="${pageContext.request.contextPath}/logout">Log Out</a>
        </div>
    </div>

    <c:if test="${not empty flashMessage}"><div class="alert success">${flashMessage}</div></c:if>
    <c:if test="${not empty flashError}"><div class="alert error">${flashError}</div></c:if>

    <div class="card section-stack">
        <div class="subcard applicant-profile-intro">
            <strong>Basic applicant profile</strong>
            <div class="hint">Only <span class="inline-emphasis">full name</span>, <span class="inline-emphasis">student ID</span>, and <span class="inline-emphasis">email</span> are required before your first application. Everything else can be added later.</div>
            <div class="detail-actions spacing-top">
                <a class="btn btn-nav" href="${pageContext.request.contextPath}/vacancies">Browse Jobs</a>
                <a class="btn btn-nav" href="${pageContext.request.contextPath}/applicant/status">Check application history</a>
            </div>
        </div>

        <form class="form-grid" method="post" action="${pageContext.request.contextPath}/applicant/profile" enctype="multipart/form-data">
            <div class="field"><label for="fullName">Full Name *</label><input id="fullName" name="fullName" value="${profile.fullName}" placeholder="Enter your full name" required></div>
            <div class="field"><label for="studentId">Student ID *</label><input id="studentId" name="studentId" value="${profile.studentId}" placeholder="Enter your student ID" required></div>
            <div class="field"><label for="email">Email *</label><input id="email" name="email" value="${profile.email}" placeholder="Enter your university email" required></div>
            <div class="field"><label for="phone">Phone</label><input id="phone" name="phone" value="${profile.phone}" placeholder="Enter your phone number"></div>
            <div class="field"><label for="degreeProgramme">Degree Programme</label><input id="degreeProgramme" name="degreeProgramme" value="${profile.degreeProgramme}" placeholder="e.g. BSc Computer Science"></div>
            <div class="field"><label for="yearOfStudy">Year of Study</label><input id="yearOfStudy" name="yearOfStudy" value="${profile.yearOfStudy}" placeholder="e.g. 2"></div>
            <div class="field field-span-2"><label for="relevantCourses">Relevant Courses and Grades</label><input id="relevantCourses" name="relevantCourses" value="${relevantCoursesValue}" placeholder="e.g. EBU6304 A, EBU4211 A-"><p class="field-hint">List relevant modules and grades, separated by commas.</p></div>
            <div class="field field-span-2"><label for="skills">Skills and Tools</label><input id="skills" name="skills" value="${skillsValue}" placeholder="e.g. Java, communication, debugging"><p class="field-hint">Separate each skill or tool with a comma so reviewers can scan them quickly.</p></div>
            <div class="field field-span-2"><label for="taExperience">TA Experience</label><textarea id="taExperience" name="taExperience" rows="3" placeholder="Describe any previous TA, tutoring, or teaching support experience">${profile.taExperience}</textarea></div>
            <div class="field field-span-2"><label for="projectExperience">Project or Leadership Experience</label><textarea id="projectExperience" name="projectExperience" rows="3" placeholder="Describe project work, teamwork, leadership, or other relevant experience">${profile.projectOrLeadershipExperience}</textarea></div>
            <div class="field field-span-2"><label for="availability">Availability</label><textarea id="availability" name="availability" rows="3" placeholder="State your general availability during the semester">${profile.availability}</textarea></div>
            <div class="field field-span-2 applicant-upload-field">
                <label for="cvFile">Upload CV</label>
                <input id="cvFile" name="cvFile" type="file" accept=".pdf,.doc,.docx">
                <p class="field-hint">Upload a PDF, DOC, or DOCX file. You can replace it at any time.</p>
                <c:if test="${not empty profile.cvFileName}">
                    <div class="upload-summary">
                        <strong>Current CV</strong>
                        <span><c:out value="${profile.cvFileName}" /></span>
                    </div>
                </c:if>
            </div>
            <div class="field field-span-2">
                <div class="config-submit-bar">
                    <div class="config-submit-copy"><strong><c:out value="${profileReady ? 'Profile ready to apply' : 'Save the essentials first'}" /></strong><div class="hint">Once the essentials are saved, Browse Jobs stays as your main starting page. Application history remains available from the top navigation whenever you need it.</div></div>
                    <button class="btn primary btn-hero" type="submit"><span class="btn-hero-text"><span class="btn-hero-title">Save profile</span><span class="btn-hero-subtitle">Keep the essentials ready for direct apply</span></span><svg class="btn-hero-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false"><path d="M6 12.5l4 4L18 8.75" /></svg></button>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>