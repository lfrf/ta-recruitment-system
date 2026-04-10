<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Browse Jobs</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<div class="${isAdmin ? 'page page-admin page-browse' : 'page page-browse'}">
    <div class="topbar-wide">
        <div class="brand">
            <h1>Browse Jobs</h1>
            <p>Every course publishes one TA team. Compare the course cards below and apply directly from the list when the basics of your profile are ready.</p>
        </div>
        <c:choose>
            <c:when test="${isAdmin}">
                <div class="nav-actions admin-nav">
                    <a class="btn btn-nav" href="${pageContext.request.contextPath}/admin/config">Config</a>
                    <a class="btn btn-nav" href="${pageContext.request.contextPath}/admin/workload">Workload</a>
                    <a class="btn btn-nav" href="${pageContext.request.contextPath}/admin/blacklist">Blacklist</a>
                    <a class="btn btn-nav btn-nav-active" href="${pageContext.request.contextPath}/vacancies">Browse Vacancies</a>
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

    <div class="browse-layout">
        <div class="browse-main section-stack">
            <div class="card section-stack">
                <form method="get" action="${pageContext.request.contextPath}/vacancies" class="form-grid vacancy-filter-grid">
                    <div class="field">
                        <label for="keyword">Search TA jobs</label>
                        <input id="keyword"
                               name="keyword"
                               value="${keyword}"
                               placeholder="Module code, module name, campus, skill, or keyword">
                    </div>
                    <div class="field">
                        <label for="campus">Campus</label>
                        <select id="campus" name="campus">
                            <option value="">All campuses</option>
                            <c:forEach items="${campusOptions}" var="campus">
                                <option value="${campus}" <c:if test="${selectedCampus == campus}">selected</c:if>>${campus}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="field field-span-2 filter-actions">
                        <button class="btn primary" type="submit">Apply filters</button>
                        <a class="btn btn-nav" href="${pageContext.request.contextPath}/vacancies">Clear filters</a>
                        <span class="hint">Showing ${resultCount} matching job<c:if test="${resultCount != 1}">s</c:if>.</span>
                    </div>
                </form>
            </div>

            <c:choose>
                <c:when test="${empty vacancies}">
                    <div class="card empty-state">
                        <c:choose>
                            <c:when test="${hasBrowseVacancies and filtersApplied}">
                                <h2>No matching jobs</h2>
                                <p class="hint">No course jobs matched your current keyword or campus filter.</p>
                                <div><a class="btn btn-nav" href="${pageContext.request.contextPath}/vacancies">Clear filters</a></div>
                            </c:when>
                            <c:otherwise>
                                <h2>No course jobs available</h2>
                                <p class="hint">There are currently no TA opportunities available to browse.</p>
                                <c:if test="${not loggedIn and not isAdmin}"><div><a class="btn primary" href="${pageContext.request.contextPath}/login">Log In to Apply</a></div></c:if>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="vacancy-grid">
                        <c:forEach items="${vacancies}" var="vacancy">
                            <c:set var="vacancyFull" value="${vacancyFullById[vacancy.vacancyId]}" />
                            <c:set var="vacancyOpen" value="${vacancy.status eq 'OPEN'}" />
                            <c:set var="skillCount" value="${fn:length(vacancy.requiredSkills)}" />
                            <c:set var="campusLower" value="${fn:toLowerCase(empty vacancy.campus ? '' : vacancy.campus)}" />
                            <c:set var="isShaheCampus" value="${fn:contains(campusLower, 'shahe')}" />
                            <div class="vacancy-card ${isShaheCampus ? 'vacancy-card-shahe' : ''}" id="vacancy-${vacancy.vacancyId}">
                                <c:if test="${isShaheCampus}">
                                    <span class="corner-badge corner-badge-shahe">SHAHE</span>
                                </c:if>
                                <div class="card-header">
                                    <div>
                                        <h2>${vacancy.moduleCode} - ${vacancy.moduleName}</h2>
                                    </div>
                                    <div class="vacancy-card-flags">
                                        <c:choose>
                                            <c:when test="${vacancyFull}">
                                                <span class="status-badge status-full">FULL</span>
                                            </c:when>
                                            <c:when test="${vacancyOpen}">
                                                <span class="status-badge status-open">OPEN</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="status-badge status-closed">CLOSED</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                                <p>
                                    ${vacancy.description}
                                    <c:if test="${skillCount > 0 and skillCount <= 4}">
                                        <span class="vacancy-inline-skills">
                                            Skills:
                                            <c:forEach items="${vacancy.requiredSkills}" var="skill" varStatus="skillStatus">
                                                ${skill}<c:if test="${not skillStatus.last}">, </c:if>
                                            </c:forEach>
                                        </span>
                                    </c:if>
                                </p>
                                <div class="meta spacing-top">
                                    <span class="tag">TA places: ${vacancy.positionCount > 0 ? vacancy.positionCount : 1}</span>
                                    <span class="tag">${vacancy.applicantCount} applicants</span>
                                </div>
                                <c:if test="${skillCount > 4}">
                                    <div class="meta spacing-top">
                                        <c:forEach items="${vacancy.requiredSkills}" var="skill"><span class="tag">${skill}</span></c:forEach>
                                    </div>
                                </c:if>
                                <div class="detail-actions spacing-top">
                                    <c:choose>
                                        <c:when test="${isApplicant and not empty applicationStatusByVacancyId[vacancy.vacancyId]}">
                                            <c:set var="applicationStatus" value="${applicationStatusByVacancyId[vacancy.vacancyId]}" />
                                            <c:choose>
                                                <c:when test='${applicationStatus == "Submitted"}'><span class="status-chip status-chip-pending">Under review</span></c:when>
                                                <c:when test='${applicationStatus == "Offered"}'><span class="status-chip status-chip-offered">Offer made</span></c:when>
                                                <c:when test='${applicationStatus == "Unsuccessful"}'><span class="status-chip status-chip-unsuccessful">Not selected</span></c:when>
                                                <c:otherwise><span class="status-chip status-chip-pending">Applied</span></c:otherwise>
                                            </c:choose>
                                            <c:if test='${applicationStatus == "Submitted"}'>
                                                <c:set var="cancelFormId" value="cancel-form-${activeApplicationIdByVacancyId[vacancy.vacancyId]}" />
                                                <form id="${cancelFormId}" method="post" action="${pageContext.request.contextPath}/applicant/cancel" class="inline-form inline-cancel-form">
                                                    <input type="hidden" name="applicationId" value="${activeApplicationIdByVacancyId[vacancy.vacancyId]}">
                                                    <button type="button"
                                                            class="btn btn-nav btn-nav-logout btn-cancel-inline js-open-cancel-modal"
                                                            data-target-form-id="${cancelFormId}"
                                                            data-course-title="${vacancy.moduleCode} - ${vacancy.moduleName}">
                                                        Cancel application
                                                    </button>
                                                </form>
                                            </c:if>
                                            <a class="btn btn-nav" href="${pageContext.request.contextPath}/vacancy?id=${vacancy.vacancyId}">View details</a>
                                        </c:when>
                                        <c:when test="${vacancyFull}">
                                            <span class="status-chip status-chip-unsuccessful">No places left</span>
                                            <a class="btn btn-nav" href="${pageContext.request.contextPath}/vacancy?id=${vacancy.vacancyId}">View details</a>
                                        </c:when>
                                        <c:when test="${isApplicant and not vacancyOpen}">
                                            <span class="status-chip status-chip-unsuccessful">Closed</span>
                                            <a class="btn btn-nav" href="${pageContext.request.contextPath}/vacancy?id=${vacancy.vacancyId}">View details</a>
                                        </c:when>
                                        <c:when test="${isApplicant and vacancyOpen}">
                                            <form method="post" action="${pageContext.request.contextPath}/applicant/apply" class="inline-form">
                                                <input type="hidden" name="vacancyId" value="${vacancy.vacancyId}">
                                                <button class="btn primary" type="submit">Apply now</button>
                                            </form>
                                            <a class="btn btn-nav" href="${pageContext.request.contextPath}/vacancy?id=${vacancy.vacancyId}">View details</a>
                                        </c:when>
                                        <c:when test="${loggedIn and vacancyOpen}">
                                            <a class="btn primary" href="${pageContext.request.contextPath}/vacancy?id=${vacancy.vacancyId}">View details</a>
                                        </c:when>
                                        <c:when test="${loggedIn}">
                                            <span class="status-chip status-chip-unsuccessful">Closed</span>
                                            <a class="btn btn-nav" href="${pageContext.request.contextPath}/vacancy?id=${vacancy.vacancyId}">View details</a>
                                        </c:when>
                                        <c:when test="${vacancyFull or not vacancyOpen}">
                                            <span class="status-chip status-chip-unsuccessful"><c:out value="${vacancyFull ? 'No places left' : 'Closed'}" /></span>
                                            <a class="btn btn-nav" href="${pageContext.request.contextPath}/vacancy?id=${vacancy.vacancyId}">View details</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a class="btn primary" href="${pageContext.request.contextPath}/login">Log In to Apply</a>
                                            <a class="btn btn-nav" href="${pageContext.request.contextPath}/vacancy?id=${vacancy.vacancyId}">View details</a>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <aside class="browse-side">
            <div class="card release-card section-stack">
                <div class="card-header release-card-header">
                    <div>
                        <h2>Release board</h2>
                        <p class="hint">Quick overview of all published TA jobs.</p>
                    </div>
                    <span class="tag">Total: ${releaseTotalCount}</span>
                </div>
                <div class="meta">
                    <span class="tag">Open: ${releaseOpenCount}</span>
                    <span class="tag tag-warning">Full: ${releaseFullCount}</span>
                    <span class="tag">Closed: ${releaseClosedCount}</span>
                </div>
                <c:if test="${releaseShaheCount > 0}">
                    <p class="hint release-legend-text">
                        <span class="legend-chip legend-chip-shahe">SHAHE</span>
                        <span class="tag">Shahe jobs: ${releaseShaheCount}</span>
                        Yellow SHAHE badge means the job is in Shahe Campus.
                    </p>
                </c:if>
                <c:if test="${filtersApplied}">
                    <p class="hint">Filters are active on the left list. Use clear to return to full browse mode.</p>
                    <a class="btn btn-nav" href="${pageContext.request.contextPath}/vacancies">Show full list</a>
                </c:if>
                <div class="release-subsection">
                    <h3>All job releases</h3>
                </div>
                <c:choose>
                    <c:when test="${empty releaseVacancies}">
                        <p class="hint">No jobs published yet.</p>
                    </c:when>
                    <c:otherwise>
                        <div class="release-list">
                            <c:forEach items="${releaseVacancies}" var="vacancy">
                                <c:set var="releaseVacancyFull" value="${vacancyFullById[vacancy.vacancyId]}" />
                                <c:set var="releaseVacancyOpen" value="${vacancy.status eq 'OPEN'}" />
                                <c:set var="releaseCampusLower" value="${fn:toLowerCase(empty vacancy.campus ? '' : vacancy.campus)}" />
                                <c:set var="releaseIsShaheCampus" value="${fn:contains(releaseCampusLower, 'shahe')}" />
                                <a class="release-item ${releaseIsShaheCampus ? 'release-item-shahe' : ''}" href="#vacancy-${vacancy.vacancyId}" title="Jump to card in the list">
                                    <c:if test="${releaseIsShaheCampus}">
                                        <span class="corner-badge corner-badge-shahe corner-badge-compact">SHAHE</span>
                                    </c:if>
                                    <span class="release-item-title">${vacancy.moduleCode} - ${vacancy.moduleName}</span>
                                    <span class="release-item-meta">
                                        <span class="tag">TA places ${vacancy.positionCount > 0 ? vacancy.positionCount : 1}</span>
                                        <c:choose>
                                            <c:when test="${releaseVacancyFull}">
                                                <span class="status-badge status-full release-item-status">FULL</span>
                                            </c:when>
                                            <c:when test="${releaseVacancyOpen}">
                                                <span class="status-badge status-open release-item-status">OPEN</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="status-badge status-closed release-item-status">CLOSED</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </span>
                                </a>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </aside>
    </div>
</div>

<div id="cancel-confirm-modal" class="hidden modal-shell" role="dialog" aria-modal="true" aria-labelledby="cancel-confirm-title">
    <div class="modal-backdrop js-cancel-modal-close"></div>
    <div class="modal-card">
        <h3 id="cancel-confirm-title">Cancel application?</h3>
        <p>You are about to withdraw your application for <strong id="cancel-course-name">this course</strong>. You can apply again later.</p>
        <div class="modal-actions">
            <button type="button" class="btn btn-nav js-cancel-modal-close">Keep application</button>
            <button type="button" id="confirm-cancel-submit" class="btn btn-nav btn-nav-logout btn-cancel-inline">Cancel application</button>
        </div>
    </div>
</div>

<script>
    (function () {
        const modal = document.getElementById('cancel-confirm-modal');
        if (!modal) return;

        const courseName = document.getElementById('cancel-course-name');
        const confirmBtn = document.getElementById('confirm-cancel-submit');
        let activeForm = null;

        function openModal(formId, courseTitle) {
            activeForm = formId ? document.getElementById(formId) : null;
            if (!activeForm) return;
            courseName.textContent = courseTitle || 'this course';
            modal.classList.remove('hidden');
            document.body.classList.add('modal-open');
        }

        function closeModal() {
            modal.classList.add('hidden');
            document.body.classList.remove('modal-open');
            activeForm = null;
        }

        document.querySelectorAll('.js-open-cancel-modal').forEach(function (button) {
            button.addEventListener('click', function () {
                openModal(button.getAttribute('data-target-form-id'), button.getAttribute('data-course-title'));
            });
        });

        modal.querySelectorAll('.js-cancel-modal-close').forEach(function (el) {
            el.addEventListener('click', closeModal);
        });

        confirmBtn.addEventListener('click', function () {
            if (activeForm) {
                activeForm.submit();
            }
        });

        document.addEventListener('keydown', function (event) {
            if (event.key === 'Escape' && !modal.classList.contains('hidden')) {
                closeModal();
            }
        });
    })();
</script>
</body>
</html>

