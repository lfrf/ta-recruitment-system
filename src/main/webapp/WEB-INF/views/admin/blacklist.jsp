<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Blacklist</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<div class="page page-admin">
    <div class="topbar topbar-admin">
        <div class="brand">
            <h1>Admin Blacklist</h1>
            <p>Manage applicants who should not be allowed to submit future applications.</p>
        </div>
        <div class="nav-actions admin-nav">
            <a class="btn btn-nav" href="${pageContext.request.contextPath}/admin/config">Config</a>
            <a class="btn btn-nav" href="${pageContext.request.contextPath}/admin/workload">Workload</a>
            <a class="btn btn-nav btn-nav-active" href="${pageContext.request.contextPath}/admin/blacklist">Blacklist</a>
            <a class="btn btn-nav btn-nav-subtle" href="${pageContext.request.contextPath}/vacancies">Browse Vacancies</a>
            <a class="btn btn-nav btn-nav-logout" href="${pageContext.request.contextPath}/logout">Log Out</a>
        </div>
    </div>

    <c:if test="${not empty flashMessage}"><div class="alert success">${flashMessage}</div></c:if>
    <c:if test="${not empty flashError}"><div class="alert error">${flashError}</div></c:if>

    <div class="card spacing-top">
        <div class="card-header">
            <div>
                <h2>Add active blacklist entry</h2>
                <p class="hint">Search in the applicant field below. Matching candidates will appear in the dropdown suggestion list.</p>
            </div>
            <span class="tag">Available applicants: ${applicants.size()}</span>
        </div>

        <form method="post" action="${pageContext.request.contextPath}/admin/blacklist" class="form-grid" id="blacklistAddForm">
            <input type="hidden" name="action" value="add">
            <input type="hidden" id="applicantId" name="applicantId">
            <div class="field field-span-2 suggestion-field">
                <label for="applicantSearch">Applicant</label>
                <input id="applicantSearch" placeholder="Type name, username, email, or student ID" autocomplete="off" required>
                <div id="applicantSuggestionBox" class="suggestion-box hidden"></div>
                <div class="hint" id="selectedApplicantHint">Start typing and choose one applicant from the dropdown suggestions.</div>
                <div id="selectedApplicantCard" class="selection-preview hidden">
                    <strong id="selectedApplicantName">No applicant selected</strong>
                    <div class="hint" id="selectedApplicantMeta"></div>
                </div>
            </div>
            <div class="field field-span-2">
                <div class="confirmation-panel">
                    <div class="confirmation-copy">
                        <strong>Final confirmation</strong>
                        <div class="hint">After checking the selected applicant details above, tick the confirmation on the right to unlock the blacklist action.</div>
                    </div>
                    <label class="confirmation-toggle" for="confirmSelection">
                        <input type="checkbox" id="confirmSelection" name="confirmSelection" disabled>
                        <span>Confirm blacklist action</span>
                    </label>
                </div>
            </div>
            <div class="field field-span-2">
                <label for="reason">Reason</label>
                <textarea id="reason" name="reason" rows="3" required></textarea>
            </div>
            <div class="field field-span-2">
                <button class="btn primary" type="submit" id="addBlacklistButton" disabled>Add blacklist entry</button>
            </div>
        </form>
    </div>

    <div class="card spacing-top">
        <div class="card-header">
            <div>
                <h2>Current and previous entries</h2>
                <p class="hint">Each applicant appears only once. The count shows how many times the applicant has been blacklisted.</p>
            </div>
            <div class="search-summary">
                <span class="tag tag-dark">1 time = black</span>
                <span class="tag tag-warning">2 times = yellow</span>
                <span class="tag tag-danger">3+ times = red</span>
            </div>
        </div>

        <div class="form-grid spacing-top">
            <div class="field field-span-2 suggestion-field">
                <label for="historySearch">Search current and previous blacklist entries</label>
                <input id="historySearch" placeholder="Type name, username, email, or student ID" autocomplete="off">
                <div id="historySuggestionBox" class="suggestion-box hidden"></div>
                <div class="hint">This search also uses a dropdown list. Type a keyword and choose one blacklisted applicant.</div>
            </div>
        </div>

        <c:choose>
            <c:when test="${empty summaries}">
                <div class="warning">There are no blacklist entries yet.</div>
            </c:when>
            <c:otherwise>
                <table class="table" id="blacklistTable">
                    <thead>
                    <tr>
                        <th>Applicant</th>
                        <th>Student ID</th>
                        <th>Email</th>
                        <th>Listed Times</th>
                        <th>Latest Reason</th>
                        <th>Latest Created At</th>
                        <th>Latest Created By</th>
                        <th>Status</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${summaries}" var="summary">
                        <c:set var="profile" value="${profileByApplicantId[summary.applicantId]}" />
                        <c:set var="applicantUser" value="${userByApplicantId[summary.applicantId]}" />
                        <tr class="${summary.active ? 'row-blacklisted' : ''} blacklist-summary-row"
                            data-search="${profile.fullName} ${applicantUser.displayName} ${applicantUser.username} ${profile.studentId} ${profile.email} ${applicantUser.email}"
                            data-label="${not empty profile.fullName ? profile.fullName : applicantUser.displayName} (${not empty applicantUser.username ? applicantUser.username : summary.applicantId})${not empty profile.studentId ? ' - ' : ''}${profile.studentId}">
                            <td>
                                <strong><c:out value="${not empty profile.fullName ? profile.fullName : applicantUser.displayName}" /></strong>
                                <div class="hint"><c:out value="${not empty applicantUser.username ? applicantUser.username : summary.applicantId}" /></div>
                            </td>
                            <td><c:out value="${empty profile.studentId ? '-' : profile.studentId}" /></td>
                            <td><c:out value="${empty profile.email ? applicantUser.email : profile.email}" /></td>
                            <td>
                                <c:choose>
                                    <c:when test="${summary.listedTimes >= 3}">
                                        <span class="tag tag-danger">${summary.listedTimes} times</span>
                                    </c:when>
                                    <c:when test="${summary.listedTimes == 2}">
                                        <span class="tag tag-warning">${summary.listedTimes} times</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="tag tag-dark">1 time</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td><c:out value="${summary.latestReason}" /></td>
                            <td><span class="js-datetime" data-iso="${summary.latestCreatedAt}"><c:out value="${summary.latestCreatedAt}" /></span></td>
                            <td><c:out value="${summary.latestCreatedBy}" /></td>
                            <td>
                                <c:choose>
                                    <c:when test="${summary.active}"><span class="tag tag-danger">Active</span></c:when>
                                    <c:otherwise><span class="tag">Inactive</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:if test="${summary.active}">
                                    <form method="post" action="${pageContext.request.contextPath}/admin/blacklist" class="inline-form">
                                        <input type="hidden" name="action" value="deactivate">
                                        <input type="hidden" name="entryId" value="${summary.activeEntryId}">
                                        <button class="btn" type="submit">Deactivate</button>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <div class="warning spacing-top hidden" id="blacklistEmptyState" style="display:none;">No blacklist entries matched the current search.</div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<script>
(() => {
    const applicantInput = document.getElementById('applicantSearch');
    const applicantIdInput = document.getElementById('applicantId');
    const applicantSuggestionBox = document.getElementById('applicantSuggestionBox');
    const selectedApplicantHint = document.getElementById('selectedApplicantHint');
    const selectedApplicantCard = document.getElementById('selectedApplicantCard');
    const selectedApplicantName = document.getElementById('selectedApplicantName');
    const selectedApplicantMeta = document.getElementById('selectedApplicantMeta');
    const confirmSelection = document.getElementById('confirmSelection');
    const addBlacklistButton = document.getElementById('addBlacklistButton');
    const addForm = document.getElementById('blacklistAddForm');
    const historySearch = document.getElementById('historySearch');
    const historySuggestionBox = document.getElementById('historySuggestionBox');
    const rows = Array.from(document.querySelectorAll('.blacklist-summary-row'));
    const emptyState = document.getElementById('blacklistEmptyState');

    const applicants = [
        <c:forEach items="${applicants}" var="applicant" varStatus="status">
            <c:set var="profile" value="${profileByApplicantId[applicant.userId]}" />
            {
                id: '${applicant.userId}',
                name: '${not empty profile.fullName ? profile.fullName : applicant.displayName}',
                label: '${not empty profile.fullName ? profile.fullName : applicant.displayName} (${applicant.username})<c:if test="${not empty profile.studentId}"> - ${profile.studentId}</c:if>',
                meta: '${applicant.username}<c:if test="${not empty profile.email or not empty applicant.email}"> | ${empty profile.email ? applicant.email : profile.email}</c:if><c:if test="${not empty profile.studentId}"> | ${profile.studentId}</c:if>',
                search: '${profile.fullName} ${applicant.displayName} ${applicant.username} ${profile.studentId} ${profile.email} ${applicant.email}'.toLowerCase()
            }<c:if test="${not status.last}">,</c:if>
        </c:forEach>
    ];

    const historyEntries = rows.map(row => ({
        label: row.dataset.label,
        search: (row.dataset.search || '').toLowerCase(),
        row
    }));

    const formatIsoDateTime = (value) => {
        if (!value) {
            return '-';
        }
        const text = String(value).replace('T', ' ');
        return text.replace(/\..*$/, '').slice(0, 16);
    };

    document.querySelectorAll('.js-datetime').forEach(node => {
        node.textContent = formatIsoDateTime(node.dataset.iso);
    });

    const updateAddButtonState = () => {
        addBlacklistButton.disabled = !applicantIdInput.value || !confirmSelection.checked;
    };

    const clearApplicantSelection = (resetHint) => {
        applicantIdInput.value = '';
        confirmSelection.checked = false;
        confirmSelection.disabled = true;
        updateAddButtonState();
        selectedApplicantCard.classList.add('hidden');
        selectedApplicantName.textContent = 'No applicant selected';
        selectedApplicantMeta.textContent = '';
        if (resetHint) {
            selectedApplicantHint.textContent = 'Start typing and choose one applicant from the dropdown suggestions.';
        }
    };

    const renderSuggestions = (container, items, onSelect) => {
        container.innerHTML = '';
        if (items.length === 0) {
            container.classList.add('hidden');
            return;
        }
        items.forEach(item => {
            const button = document.createElement('button');
            button.type = 'button';
            button.className = 'suggestion-item';
            button.textContent = item.label;
            button.addEventListener('click', () => onSelect(item));
            container.appendChild(button);
        });
        container.classList.remove('hidden');
    };

    const hideSuggestions = (container) => {
        if (container) {
            container.classList.add('hidden');
        }
    };

    const filterApplicants = () => {
        const keyword = applicantInput.value.trim().toLowerCase();
        clearApplicantSelection(false);
        const matches = applicants.filter(item => !keyword || item.search.includes(keyword)).slice(0, 8);
        renderSuggestions(applicantSuggestionBox, matches, item => {
            applicantInput.value = item.label;
            applicantIdInput.value = item.id;
            selectedApplicantName.textContent = item.name;
            selectedApplicantMeta.textContent = item.meta;
            selectedApplicantCard.classList.remove('hidden');
            confirmSelection.disabled = false;
            confirmSelection.checked = false;
            updateAddButtonState();
            selectedApplicantHint.textContent = 'Selected applicant is ready to be added to the blacklist.';
            hideSuggestions(applicantSuggestionBox);
        });
        if (!keyword) {
            selectedApplicantHint.textContent = 'Start typing and choose one applicant from the dropdown suggestions.';
        } else if (matches.length === 0) {
            selectedApplicantHint.textContent = 'No applicant matched the current keyword.';
        } else {
            selectedApplicantHint.textContent = 'Select one applicant from the dropdown suggestions.';
        }
    };

    const applyHistoryFilter = (selectedRow = null) => {
        const keyword = historySearch.value.trim().toLowerCase();
        let visibleCount = 0;
        rows.forEach(row => {
            const haystack = (row.dataset.search || '').toLowerCase();
            const visible = selectedRow ? row === selectedRow : (!keyword || haystack.includes(keyword));
            row.classList.toggle('hidden', !visible);
            if (visible) {
                visibleCount += 1;
            }
        });
        if (emptyState) {
            const shouldShow = keyword.length > 0 && visibleCount === 0;
            emptyState.style.display = shouldShow ? 'block' : 'none';
            emptyState.classList.toggle('hidden', !shouldShow);
        }
    };

    const filterHistory = () => {
        const keyword = historySearch.value.trim().toLowerCase();
        const matches = historyEntries.filter(item => !keyword || item.search.includes(keyword)).slice(0, 8);
        renderSuggestions(historySuggestionBox, matches, item => {
            historySearch.value = item.label;
            applyHistoryFilter(item.row);
            hideSuggestions(historySuggestionBox);
        });
        applyHistoryFilter();
    };

    applicantInput.addEventListener('input', filterApplicants);
    applicantInput.addEventListener('focus', filterApplicants);
    applicantInput.addEventListener('blur', () => setTimeout(() => hideSuggestions(applicantSuggestionBox), 160));
    confirmSelection.addEventListener('change', updateAddButtonState);

    addForm.addEventListener('submit', event => {
        if (!applicantIdInput.value) {
            event.preventDefault();
            selectedApplicantHint.textContent = 'Please choose one applicant from the dropdown suggestions before submitting.';
            return;
        }
        if (!confirmSelection.checked) {
            event.preventDefault();
            selectedApplicantHint.textContent = 'Please confirm the selected applicant before submitting.';
        }
    });

    historySearch.addEventListener('input', filterHistory);
    historySearch.addEventListener('focus', filterHistory);
    historySearch.addEventListener('blur', () => setTimeout(() => hideSuggestions(historySuggestionBox), 160));

    if (emptyState) {
        emptyState.style.display = 'none';
        emptyState.classList.add('hidden');
    }
    clearApplicantSelection(true);
    applyHistoryFilter();
})();
</script>
</body>
</html>
