<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String loginTitle = request.getAttribute("loginTitle") != null ? request.getAttribute("loginTitle").toString() : "Log In";
    String loginSubtitle = request.getAttribute("loginSubtitle") != null ? request.getAttribute("loginSubtitle").toString() : "Sign in to continue.";
    String submitLabel = request.getAttribute("submitLabel") != null ? request.getAttribute("submitLabel").toString() : "Log In";
    String formAction = request.getAttribute("formAction") != null ? request.getAttribute("formAction").toString() : request.getContextPath() + "/login";
    String backHref = request.getAttribute("backHref") != null ? request.getAttribute("backHref").toString() : request.getContextPath() + "/home";
    String backLabel = request.getAttribute("backLabel") != null ? request.getAttribute("backLabel").toString() : "Back";
    String altLoginHref = request.getAttribute("altLoginHref") != null ? request.getAttribute("altLoginHref").toString() : null;
    String altLoginLabel = request.getAttribute("altLoginLabel") != null ? request.getAttribute("altLoginLabel").toString() : null;
    String loginVariant = request.getAttribute("loginVariant") != null ? request.getAttribute("loginVariant").toString() : "applicant";
    String loginAudience = request.getAttribute("loginAudience") != null ? request.getAttribute("loginAudience").toString() : "Access";
    String loginNotice = request.getAttribute("loginNotice") != null ? request.getAttribute("loginNotice").toString() : null;
    String returnTo = request.getAttribute("returnTo") != null ? request.getAttribute("returnTo").toString() : null;
    String returnToEscaped = returnTo == null ? null : returnTo
            .replace("&", "&amp;")
            .replace("\"", "&quot;")
            .replace("<", "&lt;")
            .replace(">", "&gt;");
    boolean applicantVariant = !"admin".equals(loginVariant) && !"staff".equals(loginVariant);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= loginTitle %></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="login-body <%= ("admin".equals(loginVariant) || "staff".equals(loginVariant)) ? "admin-login-body" : "applicant-login-body" %>">
<div class="page login-page">
    <div class="topbar login-topbar">
        <div class="brand">
            <span class="login-badge <%= ("admin".equals(loginVariant) || "staff".equals(loginVariant)) ? "login-badge-admin" : "login-badge-applicant" %>"><%= loginAudience %></span>
            <h1><%= loginTitle %></h1>
            <p><%= loginSubtitle %></p>
        </div>
        <div class="nav-actions">
            <a class="btn" href="<%= backHref %>"><%= backLabel %></a>
            <% if (altLoginHref != null && altLoginLabel != null) { %>
                <a class="btn" href="<%= altLoginHref %>"><%= altLoginLabel %></a>
            <% } %>
        </div>
    </div>

    <% if (request.getAttribute("flashMessage") != null) { %>
        <div class="alert success"><%= request.getAttribute("flashMessage") %></div>
    <% } %>
    <% if (request.getAttribute("flashError") != null) { %>
        <div class="alert error"><%= request.getAttribute("flashError") %></div>
    <% } %>

    <div class="login-shell <%= ("admin".equals(loginVariant) || "staff".equals(loginVariant)) ? "login-shell-admin" : "login-shell-applicant" %>">
        <div class="login-copy card">
            <h2><%= ("admin".equals(loginVariant) || "staff".equals(loginVariant)) ? "Protected staff access" : "Applicant account access" %></h2>
            <p class="hint"><%= loginNotice != null ? loginNotice : "Use the correct account type for this page." %></p>
            <div class="login-points">
                <% if ("admin".equals(loginVariant) || "staff".equals(loginVariant)) { %>
                    <div class="login-point">Use this page for MO review workflows and admin management functions.</div>
                    <div class="login-point">Applicant accounts should switch back to the applicant login page.</div>
                <% } else { %>
                    <div class="login-point">Use this page to browse jobs, apply, and maintain applicant profile details.</div>
                    <div class="login-point">MO and Admin accounts must switch to the staff login page.</div>
                <% } %>
            </div>
        </div>

        <div class="card login-form-card">
            <% if (request.getAttribute("errorMessage") != null) { %>
                <div class="warning"><%= request.getAttribute("errorMessage") %></div>
                <br>
            <% } %>
            <form method="post" action="<%= formAction %>">
                <% if (returnTo != null && !returnTo.isBlank()) { %>
                    <input type="hidden" name="returnTo" value="<%= returnToEscaped %>">
                <% } %>
                <div class="field">
                    <label>Username</label>
                    <input type="text" name="username" placeholder="e.g. applicant01">
                </div>
                <div class="field">
                    <label>Password</label>
                    <input type="password" name="password" placeholder="Enter password">
                </div>
                <button class="btn primary" type="submit"><%= submitLabel %></button>
            </form>
            <% if (applicantVariant) { %>
                <div class="quick-login-divider"></div>
                <div class="quick-login-panel">
                    <h3>Quick login with phone confirmation</h3>
                    <p class="hint">Bind your phone once in <strong>My Profile</strong>, then scan and confirm here for faster login.</p>
                    <div class="quick-login-actions">
                        <button id="quick-login-start" type="button" class="btn">Generate QR request</button>
                        <span id="quick-login-state" class="hint"></span>
                    </div>
                    <div id="quick-login-qr-wrap" class="quick-login-qr-wrap hidden">
                        <img id="quick-login-qr" alt="Quick login QR code">
                        <a id="quick-login-open" class="btn btn-nav btn-nav-subtle" href="#" target="_blank" rel="noopener">Open confirmation link</a>
                    </div>
                </div>
                <script>
                    (() => {
                        const contextPath = "<%= request.getContextPath() %>";
                        const startButton = document.getElementById("quick-login-start");
                        const stateText = document.getElementById("quick-login-state");
                        const qrWrap = document.getElementById("quick-login-qr-wrap");
                        const qrImage = document.getElementById("quick-login-qr");
                        const openLink = document.getElementById("quick-login-open");
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

                        const finishLogin = async () => {
                            if (!activeRequestId) {
                                return;
                            }
                            const body = new URLSearchParams();
                            body.set("request", activeRequestId);
                            const response = await fetch(contextPath + "/quick-login/complete", {
                                method: "POST",
                                headers: {"Content-Type": "application/x-www-form-urlencoded"},
                                body: body.toString()
                            });
                            const result = await response.json();
                            if (result.status === "SUCCESS" && result.redirect) {
                                window.location.href = result.redirect;
                                return;
                            }
                            setState("Login confirmation received, but completion failed. Please retry.");
                            startButton.disabled = false;
                            stopPolling();
                        };

                        const pollStatus = async () => {
                            if (!activeRequestId) {
                                return;
                            }
                            const response = await fetch(contextPath + "/quick-login/poll?request=" + encodeURIComponent(activeRequestId));
                            const result = await response.json().catch(() => ({status: "ERROR"}));
                            if (!response.ok && result.status !== "NOT_FOUND") {
                                setState("Quick login check failed. Please retry.");
                                startButton.disabled = false;
                                stopPolling();
                                return;
                            }
                            if (result.status === "CONFIRMED") {
                                setState("Phone confirmed. Finishing login...");
                                stopPolling();
                                await finishLogin();
                                return;
                            }
                            if (result.status === "EXPIRED" || result.status === "USED" || result.status === "NOT_FOUND") {
                                setState("This QR request expired. Generate a new one.");
                                startButton.disabled = false;
                                stopPolling();
                            }
                        };

                        startButton.addEventListener("click", async () => {
                            stopPolling();
                            startButton.disabled = true;
                            setState("Generating quick login request...");
                            const response = await fetch(contextPath + "/quick-login/request", {method: "POST"});
                            const result = await response.json().catch(() => ({status: "ERROR"}));
                            if (!response.ok || result.status !== "PENDING" || !result.requestId || !result.confirmUrl) {
                                setState("Unable to generate QR request. Please try again.");
                                startButton.disabled = false;
                                return;
                            }
                            activeRequestId = result.requestId;
                            const qrSource = "https://api.qrserver.com/v1/create-qr-code/?size=220x220&data="
                                + encodeURIComponent(result.confirmUrl);
                            qrImage.src = qrSource;
                            openLink.href = result.confirmUrl;
                            qrWrap.classList.remove("hidden");
                            setState("Scan with your bound phone, then tap Confirm on the phone page.");
                            pollTimer = window.setInterval(() => {
                                pollStatus().catch(() => {
                                    setState("Quick login check failed. Please retry.");
                                    startButton.disabled = false;
                                    stopPolling();
                                });
                            }, 2000);
                        });
                    })();
                </script>
            <% } %>
        </div>
    </div>
</div>
</body>
</html>
