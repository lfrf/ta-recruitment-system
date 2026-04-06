<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Applicant Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<div class="page">
    <div class="topbar">
        <div class="brand">
            <h1>Applicant Profile</h1>
            <p>Complete and save your reusable applicant profile before applying for vacancies.</p>
        </div>
        <div class="nav-actions">
            <a class="btn" href="${pageContext.request.contextPath}/vacancies">Browse Vacancies</a>
            <a class="btn" href="${pageContext.request.contextPath}/applicant/status">My Status</a>
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
        <p class="hint">
            Fields marked with <strong>*</strong> are required. Please keep your profile accurate and up to date before submitting any application.
        </p>

        <form class="form-grid" method="post" action="${pageContext.request.contextPath}/applicant/profile">
            <div class="field">
                <label for="fullName">Full Name *</label>
                <input id="fullName" name="fullName" value="${profile.fullName}" placeholder="Enter your full name" required>
            </div>
            <div class="field">
                <label for="studentId">Student ID *</label>
                <input id="studentId" name="studentId" value="${profile.studentId}" placeholder="Enter your student ID" required>
            </div>
            <div class="field">
                <label for="email">Email *</label>
                <input id="email" name="email" value="${profile.email}" placeholder="Enter your university email" required>
            </div>
            <div class="field">
                <label for="phone">Phone</label>
                <input id="phone" name="phone" value="${profile.phone}" placeholder="Enter your phone number">
            </div>
            <div class="field">
                <label for="degreeProgramme">Degree Programme *</label>
                <input id="degreeProgramme" name="degreeProgramme" value="${profile.degreeProgramme}" placeholder="e.g. BSc Computer Science" required>
            </div>
            <div class="field">
                <label for="yearOfStudy">Year of Study *</label>
                <input id="yearOfStudy" name="yearOfStudy" value="${profile.yearOfStudy}" placeholder="e.g. Year 2" required>
            </div>
            <div class="field field-span-2">
                <label for="relevantCourses">Relevant Courses and Grades</label>
                <input id="relevantCourses" name="relevantCourses" value="${relevantCoursesValue}" placeholder="e.g. EBU6304 A, EBU6305 B+">
                <p class="hint">List course names or codes and grades, separated by commas.</p>
            </div>
            <div class="field field-span-2">
                <label for="skills">Skills and Tools</label>
                <input id="skills" name="skills" value="${skillsValue}" placeholder="e.g. Java, Servlet, JSP, Python">
                <p class="hint">Separate different skills or tools with commas.</p>
            </div>
            <div class="field field-span-2">
                <label for="taExperience">TA Experience</label>
                <textarea id="taExperience" name="taExperience" rows="3" placeholder="Describe any previous TA, tutoring, or teaching support experience">${profile.taExperience}</textarea>
            </div>
            <div class="field field-span-2">
                <label for="projectExperience">Project or Leadership Experience</label>
                <textarea id="projectExperience" name="projectExperience" rows="3" placeholder="Describe project work, teamwork, leadership, or other relevant experience">${profile.projectOrLeadershipExperience}</textarea>
            </div>
            <div class="field field-span-2">
                <label for="availability">Availability</label>
                <textarea id="availability" name="availability" rows="3" placeholder="State your general availability during the semester">${profile.availability}</textarea>
            </div>
            <div class="field">
                <label for="cvFileName">CV File Name</label>
                <input id="cvFileName" name="cvFileName" value="${profile.cvFileName}" placeholder="e.g. resume.pdf">
            </div>
            <div class="field">
                <label for="cvFilePath">CV File Path</label>
                <input id="cvFilePath" name="cvFilePath" value="${profile.cvFilePath}" placeholder="e.g. /uploads/resume.pdf">
            </div>
            <div class="field field-span-2">
                <button class="btn primary" type="submit">Save Profile</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>
