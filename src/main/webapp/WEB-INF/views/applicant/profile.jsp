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
            <a class="btn btn-nav btn-nav-with-badge" href="${pageContext.request.contextPath}/applicant/status">
                <span>Application History</span>
                <c:if test="${unreadDecisionCount > 0}">
                    <span class="nav-unread-badge" aria-label="${unreadDecisionCount} unread decisions">${unreadDecisionCount}</span>
                </c:if>
            </a>
            <a class="btn btn-nav" href="${pageContext.request.contextPath}/account/password">Change Password</a>
            <a class="btn btn-nav btn-nav-logout" href="${pageContext.request.contextPath}/logout">Log Out</a>
        </div>
    </div>

    <c:if test="${not empty flashMessage}"><div class="alert success">${flashMessage}</div></c:if>
    <c:if test="${not empty flashError}"><div class="alert error">${flashError}</div></c:if>
    <c:if test="${unreadDecisionCount > 0}">
        <div class="warning">You have ${unreadDecisionCount} unread application decision<c:if test="${unreadDecisionCount != 1}">s</c:if>. Open Application History to review them.</div>
    </c:if>

    <div class="card">
        <div class="profile-layout">
            <div class="profile-main">
                <form id="applicant-profile-form" class="section-stack" method="post" action="${pageContext.request.contextPath}/applicant/profile" enctype="multipart/form-data">
                    <div class="subcard applicant-upload-card">
                        <strong>Upload CV first (optional)</strong>
                        <div class="hint">Upload your CV if you want. You can do it now or later. If uploaded, AI import can help pre-fill profile fields and generate vacancy-fit ranking.</div>
                        <div class="field spacing-top applicant-upload-field">
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
                    </div>

                    <div class="subcard">
                        <strong>Basic applicant details</strong>
                        <div class="form-grid spacing-top">
                            <div class="field"><label for="fullName">Full Name *</label><input id="fullName" name="fullName" value="${profile.fullName}" placeholder="Enter your full name" required></div>
                            <div class="field"><label for="studentId">Student ID *</label><input id="studentId" name="studentId" value="${profile.studentId}" placeholder="Enter your student ID" required></div>
                            <div class="field"><label for="email">Email *</label><input id="email" name="email" value="${profile.email}" placeholder="Enter your university email" required></div>
                            <div class="field"><label for="phone">Phone</label><input id="phone" name="phone" value="${profile.phone}" placeholder="Enter your phone number"></div>
                            <div class="field"><label for="degreeProgramme">Degree Programme</label><input id="degreeProgramme" name="degreeProgramme" value="${profile.degreeProgramme}" placeholder="e.g. BSc Computer Science"></div>
                            <div class="field"><label for="yearOfStudy">Year of Study</label><input id="yearOfStudy" name="yearOfStudy" value="${profile.yearOfStudy}" placeholder="e.g. 2"></div>
                        </div>
                    </div>

                    <div class="subcard">
                        <strong>Academic and skills</strong>
                        <div class="field spacing-top"><label for="relevantCourses">Relevant Courses and Grades</label><input id="relevantCourses" name="relevantCourses" value="${relevantCoursesValue}" placeholder="e.g. EBU6304 A, EBU4211 A-"><p class="field-hint">List relevant modules and grades, separated by commas.</p></div>
                        <div class="field"><label for="skills">Skills and Tools</label><input id="skills" name="skills" value="${skillsValue}" placeholder="e.g. Java, communication, debugging"><p class="field-hint">Separate each skill or tool with a comma so reviewers can scan them quickly.</p></div>
                    </div>

                    <div class="subcard">
                        <strong>Experience and availability</strong>
                        <div class="field spacing-top"><label for="taExperience">TA Experience</label><textarea id="taExperience" name="taExperience" rows="3" placeholder="Describe any previous TA, tutoring, or teaching support experience">${profile.taExperience}</textarea></div>
                        <div class="field"><label for="projectExperience">Project or Leadership Experience</label><textarea id="projectExperience" name="projectExperience" rows="3" placeholder="Describe project work, teamwork, leadership, or other relevant experience">${profile.projectOrLeadershipExperience}</textarea></div>
                        <div class="field"><label for="availability">Availability</label><textarea id="availability" name="availability" rows="3" placeholder="State your general availability during the semester">${profile.availability}</textarea></div>
                    </div>
                </form>
            </div>

            <aside class="profile-side section-stack">
                <div class="subcard applicant-profile-intro">
                    <strong>Basic applicant profile</strong>
                    <div class="hint">Only <span class="inline-emphasis">full name</span>, <span class="inline-emphasis">student ID</span>, and <span class="inline-emphasis">email</span> are required before your first application. Everything else can be added later.</div>
                    <div class="detail-actions spacing-top">
                        <a class="btn btn-nav" href="${pageContext.request.contextPath}/vacancies">Browse Jobs</a>
                        <a class="btn btn-nav" href="${pageContext.request.contextPath}/applicant/status">Check application history</a>
                    </div>
                </div>

                <div class="subcard ai-import-card">
                    <strong>0-token AI assistant import</strong>
                    <div class="hint">Generate one task prompt, paste it into your own agent, and let the agent call back automatically with both structured profile fields and vacancy-fit ranking.</div>
                    <div class="ai-import-actions spacing-top">
                        <button id="ai-import-generate" type="button" class="btn btn-nav">Generate prompt task</button>
                        <button id="ai-import-copy" type="button" class="btn btn-nav btn-nav-subtle hidden">Copy prompt</button>
                    </div>
                    <p id="ai-import-status" class="hint spacing-top">No active AI import task.</p>
                    <p id="ai-import-summary" class="hint ai-import-summary hidden"></p>
                    <textarea id="ai-import-prompt" class="ai-import-prompt hidden" rows="7" readonly></textarea>
                    <div id="ai-import-preview" class="selection-preview hidden">
                        <strong>Extracted fields preview</strong>
                        <pre id="ai-import-preview-lines" class="ai-import-preview-lines"></pre>
                        <div class="ai-import-actions spacing-top">
                            <button id="ai-import-apply" type="button" class="btn primary hidden">Apply extracted data</button>
                        </div>
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

                <div class="subcard profile-submit-card">
                    <strong><c:out value="${profileReady ? 'Profile ready to apply' : 'Save the essentials first'}" /></strong>
                    <div class="hint">Once the essentials are saved, Browse Jobs stays as your main starting page. Application history remains available from the top navigation whenever you need it.</div>
                    <button class="btn primary btn-hero spacing-top" type="submit" form="applicant-profile-form"><span class="btn-hero-text"><span class="btn-hero-title">Save profile</span><span class="btn-hero-subtitle">Keep the essentials ready for direct apply</span></span><svg class="btn-hero-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false"><path d="M6 12.5l4 4L18 8.75" /></svg></button>
                </div>
            </aside>
        </div>
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
<script>
    (() => {
        const contextPath = "${pageContext.request.contextPath}";
        const generateButton = document.getElementById("ai-import-generate");
        const copyButton = document.getElementById("ai-import-copy");
        const applyButton = document.getElementById("ai-import-apply");
        const statusText = document.getElementById("ai-import-status");
        const summaryText = document.getElementById("ai-import-summary");
        const promptBox = document.getElementById("ai-import-prompt");
        const previewBox = document.getElementById("ai-import-preview");
        const previewLines = document.getElementById("ai-import-preview-lines");
        if (!generateButton || !copyButton || !applyButton
            || !statusText || !summaryText || !promptBox || !previewBox || !previewLines) {
            return;
        }

        let currentTaskId = "";
        let pollTimer = null;
        let latestSuggestion = null;
        let latestRankingReady = false;
        let hasAppliedProfile = false;

        const setStatus = (text) => {
            statusText.textContent = text || "";
        };

        const stopPolling = () => {
            if (pollTimer) {
                window.clearInterval(pollTimer);
                pollTimer = null;
            }
        };

        const setSummary = (text) => {
            const normalized = text || "";
            summaryText.textContent = normalized;
            summaryText.classList.toggle("hidden", normalized.length === 0);
        };

        const updateSummary = () => {
            const parts = [];
            if (hasAppliedProfile) {
                parts.push("Profile fields were applied.");
            }
            if (latestRankingReady) {
                parts.push("Vacancy fit ranking is ready in Browse Jobs (AI fit order).");
            }
            setSummary(parts.join(" "));
        };

        const joinList = (values) => {
            if (!Array.isArray(values) || values.length === 0) {
                return "";
            }
            return values.join(", ");
        };

        const renderSuggestionPreview = (suggestion) => {
            latestSuggestion = suggestion || null;
            if (!latestSuggestion) {
                previewBox.classList.add("hidden");
                previewLines.textContent = "";
                applyButton.classList.add("hidden");
                return;
            }

            const lines = [];
            const pushLine = (label, value) => {
                if (!value) {
                    return;
                }
                lines.push(label + ": " + value);
            };

            pushLine("Full Name", latestSuggestion.fullName || "");
            pushLine("Student ID", latestSuggestion.studentId || "");
            pushLine("Email", latestSuggestion.email || "");
            pushLine("Phone", latestSuggestion.phone || "");
            pushLine("Degree Programme", latestSuggestion.degreeProgramme || "");
            pushLine("Year of Study", latestSuggestion.yearOfStudy || "");
            pushLine("Relevant Courses", joinList(latestSuggestion.relevantCourses));
            pushLine("Skills", joinList(latestSuggestion.skills));
            pushLine("TA Experience", latestSuggestion.taExperience || "");
            pushLine("Project/Leadership", latestSuggestion.projectOrLeadershipExperience || "");
            pushLine("Availability", latestSuggestion.availability || "");

            previewLines.textContent = lines.length > 0 ? lines.join("\n") : "No non-empty fields returned.";
            previewBox.classList.remove("hidden");
        };

        const resetTaskUi = () => {
            currentTaskId = "";
            latestSuggestion = null;
            latestRankingReady = false;
            hasAppliedProfile = false;
            promptBox.value = "";
            promptBox.classList.add("hidden");
            copyButton.classList.add("hidden");
            applyButton.classList.add("hidden");
            applyButton.disabled = false;
            previewBox.classList.add("hidden");
            previewLines.textContent = "";
            setSummary("");
        };

        const applyProfileValues = (profile) => {
            if (!profile) {
                return;
            }
            const setValue = (id, value) => {
                const input = document.getElementById(id);
                if (!input) {
                    return;
                }
                input.value = value || "";
            };

            setValue("fullName", profile.fullName);
            setValue("studentId", profile.studentId);
            setValue("email", profile.email);
            setValue("phone", profile.phone);
            setValue("degreeProgramme", profile.degreeProgramme);
            setValue("yearOfStudy", profile.yearOfStudy);
            setValue("relevantCourses", joinList(profile.relevantCourses));
            setValue("skills", joinList(profile.skills));
            setValue("taExperience", profile.taExperience);
            setValue("projectExperience", profile.projectOrLeadershipExperience);
            setValue("availability", profile.availability);
        };

        const pollTaskStatus = async () => {
            if (!currentTaskId) {
                return;
            }
            const response = await fetch(
                contextPath + "/applicant/ai/tasks/status?taskId=" + encodeURIComponent(currentTaskId)
            );
            const result = await response.json().catch(() => ({status: "ERROR"}));
            if (!response.ok || result.status !== "OK") {
                setStatus("Unable to query AI task status. Please generate a new task.");
                stopPolling();
                return;
            }
            const taskStatus = (result.taskStatus || "").toUpperCase();
            const profileStatus = (result.profileStatus || "").toUpperCase();
            const rankingStatus = (result.rankingStatus || "").toUpperCase();
            latestRankingReady = rankingStatus === "VALIDATED";
            if (profileStatus === "VALIDATED" || taskStatus === "VALIDATED") {
                renderSuggestionPreview(result.profile || null);
                applyButton.classList.remove("hidden");
                applyButton.disabled = false;
                hasAppliedProfile = false;
                if (latestRankingReady) {
                    setStatus("AI result validated. Review profile fields and click Apply extracted data. Vacancy ranking is already ready in Browse Jobs.");
                } else if (rankingStatus === "FAILED") {
                    setStatus("AI profile fields validated. Vacancy ranking failed this round, but you can still apply profile fields now.");
                } else {
                    setStatus("AI result validated. Review extracted fields, then click Apply extracted data.");
                }
                updateSummary();
                stopPolling();
                return;
            }
            if (taskStatus === "APPLIED") {
                renderSuggestionPreview(result.profile || latestSuggestion);
                setStatus("AI suggestion already applied.");
                applyButton.classList.add("hidden");
                updateSummary();
                stopPolling();
                return;
            }
            if (profileStatus === "FAILED" && rankingStatus === "VALIDATED") {
                const details = Array.isArray(result.profileValidationErrors) && result.profileValidationErrors.length > 0
                    ? " (" + result.profileValidationErrors[0] + ")"
                    : "";
                setStatus("Profile extraction failed" + details + ". Vacancy ranking is still ready in Browse Jobs.");
                renderSuggestionPreview(null);
                applyButton.classList.add("hidden");
                updateSummary();
                stopPolling();
                return;
            }
            if (taskStatus === "FAILED") {
                const details = Array.isArray(result.validationErrors) && result.validationErrors.length > 0
                    ? " (" + result.validationErrors[0] + ")"
                    : "";
                setStatus("AI result failed validation" + details);
                renderSuggestionPreview(null);
                applyButton.classList.add("hidden");
                updateSummary();
                stopPolling();
                return;
            }
            if (taskStatus === "EXPIRED") {
                setStatus("AI task expired. Click Retry import to create a fresh task.");
                renderSuggestionPreview(null);
                applyButton.classList.add("hidden");
                updateSummary();
                stopPolling();
                return;
            }
            if (taskStatus === "RECEIVED") {
                setStatus("AI callback received. Validating payload...");
                return;
            }
            setStatus("Waiting for your agent callback...");
        };

        const startImport = async () => {
            stopPolling();
            resetTaskUi();
            generateButton.disabled = true;
            setStatus("Generating AI prompt task...");
            const response = await fetch(contextPath + "/applicant/ai/tasks", {method: "POST"});
            const result = await response.json().catch(() => ({status: "ERROR"}));
            if (!response.ok || result.status !== "OK" || !result.taskId || !result.promptTemplate) {
                setStatus("Unable to generate AI prompt task. Please retry.");
                generateButton.disabled = false;
                return;
            }

            currentTaskId = result.taskId;
            promptBox.value = result.promptTemplate;
            promptBox.classList.remove("hidden");
            copyButton.classList.remove("hidden");
            generateButton.disabled = false;
            if (result.hasCvDownload) {
                setStatus("Prompt task created with short-lived CV download URL. Send prompt to your agent and wait for callback.");
            } else {
                setStatus("Prompt task created, but no uploaded CV was found. Upload CV to your agent manually before running extraction.");
            }

            pollTimer = window.setInterval(() => {
                pollTaskStatus().catch(() => {
                    setStatus("Status polling failed. Please refresh and check again.");
                    stopPolling();
                    generateButton.disabled = false;
                });
            }, 3000);
        };

        copyButton.addEventListener("click", async () => {
            try {
                await navigator.clipboard.writeText(promptBox.value);
                setStatus("Prompt copied. Send it to your own agent and keep this page open for status updates.");
            } catch (error) {
                promptBox.focus();
                promptBox.select();
                setStatus("Clipboard unavailable. Prompt selected, please copy manually.");
            }
        });

        const applyCurrentTask = async () => {
            if (!currentTaskId) {
                setStatus("No active task. Generate a prompt task first.");
                return;
            }
            applyButton.disabled = true;
            setStatus("Applying validated AI fields to your profile...");
            const response = await fetch(contextPath + "/applicant/ai/tasks/apply", {
                method: "POST",
                headers: {"Content-Type": "application/x-www-form-urlencoded"},
                body: new URLSearchParams({taskId: currentTaskId})
            });
            const result = await response.json().catch(() => ({status: "ERROR"}));
            if (!response.ok || result.status !== "OK") {
                setStatus("Apply failed. Please try again.");
                applyButton.disabled = false;
                return;
            }
            applyProfileValues(result.profile || latestSuggestion);
            setStatus("Applied. Profile form has been updated and saved from validated AI data.");
            applyButton.classList.add("hidden");
            hasAppliedProfile = true;
            updateSummary();
            stopPolling();
        };

        generateButton.addEventListener("click", () => {
            startImport().catch(() => {
                setStatus("Unable to generate AI prompt task. Please retry.");
                generateButton.disabled = false;
            });
        });

        applyButton.addEventListener("click", () => {
            applyCurrentTask().catch(() => {
                setStatus("Apply failed. Please try again.");
                applyButton.disabled = false;
            });
        });

        resetTaskUi();
    })();
</script>
</body>
</html>
