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
            <p>Keep the basic details accurate and update them any time, even after you have submitted applications.</p>
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

        <div class="subcard quick-login-binding-card">
            <strong>Phone quick login</strong>
            <div class="hint">Generate a QR code here, scan it on your phone once, and use that phone for later quick login confirmation.</div>
            <c:choose>
                <c:when test="${quickLoginBound}">
                    <div class="upload-summary spacing-top">
                        <strong>Current binding</strong>
                        <span><c:out value="${quickLoginDeviceName}" /></span>
                        <c:if test="${not empty quickLoginBoundAt}">
                            <span class="hint">Bound at: <c:out value="${quickLoginBoundAt}" /></span>
                        </c:if>
                    </div>
                    <form class="spacing-top" method="post" action="${pageContext.request.contextPath}/applicant/quick-login-binding">
                        <input type="hidden" name="action" value="unbind">
                        <button class="btn btn-nav btn-nav-logout" type="submit">Unbind this device</button>
                    </form>
                </c:when>
                <c:otherwise>
                    <div class="quick-login-bind-actions spacing-top">
                        <button id="quick-login-bind-start" type="button" class="btn primary btn-hero-compact">Generate binding QR</button>
                        <span id="quick-login-bind-state" class="hint"></span>
                    </div>
                    <div id="quick-login-bind-qr-wrap" class="quick-login-qr-wrap hidden spacing-top">
                        <img id="quick-login-bind-qr" alt="Phone binding QR code">
                        <a id="quick-login-bind-open" class="btn btn-nav btn-nav-subtle" href="#" target="_blank" rel="noopener">Open bind link</a>
                    </div>
                    <p class="hint spacing-top">Use your phone camera or browser QR scanner to bind this phone. No phone login is required.</p>
                </c:otherwise>
            </c:choose>
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
<c:if test="${not quickLoginBound}">
    <script>
        (() => {
            const contextPath = "${pageContext.request.contextPath}";
            const startButton = document.getElementById("quick-login-bind-start");
            const stateText = document.getElementById("quick-login-bind-state");
            const qrWrap = document.getElementById("quick-login-bind-qr-wrap");
            const qrImage = document.getElementById("quick-login-bind-qr");
            const openLink = document.getElementById("quick-login-bind-open");
            if (!startButton || !stateText || !qrWrap || !qrImage || !openLink) {
                return;
            }

            let pollTimer = null;
            let activeRequestId = null;

            const setState = (text) => {
                stateText.textContent = text || "";
            };

            const stopPolling = () => {
                if (pollTimer) {
                    window.clearInterval(pollTimer);
                    pollTimer = null;
                }
            };

            const pollStatus = async () => {
                if (!activeRequestId) {
                    return;
                }
                const response = await fetch(
                    contextPath + "/applicant/quick-login-binding/poll?request=" + encodeURIComponent(activeRequestId)
                );
                const result = await response.json().catch(() => ({status: "ERROR"}));
                if (!response.ok) {
                    setState("Binding status check failed. Please generate a new QR.");
                    startButton.disabled = false;
                    stopPolling();
                    return;
                }
                if (result.status === "BOUND") {
                    setState("Phone bound successfully. Refreshing profile...");
                    startButton.disabled = true;
                    stopPolling();
                    window.setTimeout(() => window.location.reload(), 700);
                    return;
                }
                if (result.status === "EXPIRED") {
                    setState("This QR has expired. Generate a new one.");
                    startButton.disabled = false;
                    stopPolling();
                }
            };

            startButton.addEventListener("click", async () => {
                stopPolling();
                startButton.disabled = true;
                setState("Generating binding QR...");
                const response = await fetch(contextPath + "/applicant/quick-login-binding/request", {method: "POST"});
                const result = await response.json().catch(() => ({status: "ERROR"}));
                if (!response.ok || result.status !== "PENDING" || !result.requestId || !result.bindUrl) {
                    setState("Unable to generate binding QR. Please try again.");
                    startButton.disabled = false;
                    return;
                }

                activeRequestId = result.requestId;
                qrImage.src = "https://api.qrserver.com/v1/create-qr-code/?size=220x220&data="
                    + encodeURIComponent(result.bindUrl);
                openLink.href = result.bindUrl;
                qrWrap.classList.remove("hidden");
                setState("Scan this QR with your phone to finish binding.");

                pollTimer = window.setInterval(() => {
                    pollStatus().catch(() => {
                        setState("Binding status check failed. Please generate a new QR.");
                        startButton.disabled = false;
                        stopPolling();
                    });
                }, 2000);
            });
        })();
    </script>
</c:if>
</body>
</html>
