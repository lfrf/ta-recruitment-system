# Extreme Test Points (100% Project Validation)
## 1. Testing / Demo Data / Regression
### Delivery Standards
- Stable demo data
- Clear test steps
- Test result records
- Fast regression after every merge:
  - Login
  - Guest browsing
  - Application
  - MO review
  - Admin configuration

### Extreme Test Points
1. Extreme demo data cleanup and reset: Clear all database tables, reimport demo data, verify no conflicts, no broken relations, no duplicate data after repeated imports.
2. Extreme concurrent regression: Execute login, browsing, application, review, and admin operations in multiple tabs simultaneously; verify no session conflicts, no freezes, no duplicate submissions.
3. Cross-environment regression: Test in incognito mode, cleared cache, mobile browsers, and after server restart; ensure full functionality and stability.
4. Dirty data resistance: Test with invalid status, empty fields, and broken associations; ensure no crashes, no data pollution.

---

## 2. Visitor + Applicant Profile
### Delivery Standards
- Guests can browse vacancies stably
- Clear login button in the top-right corner
- Applicant profile can be saved and redisplayed correctly
- Profile fields match job requirements
- Stable page styling with no unstyled plain-text issues

### Extreme Test Points
1. Extreme browsing scenarios: 0 vacancies, 1000+ vacancies, offline/online switching; ensure no errors, no style crashes.
2. Login UI stability: Visible on all screen sizes; correct status display for logged-out, logged-in, and session-expired states.
3. Profile edge cases: Submit empty, ultra-long, special character, or emoji content; verify validation works and no data loss after restart or cache clear.
4. Style robustness: Disable/enable CSS/JS; switch pages rapidly; ensure no layout collapse or style loss.

---

## 3. Validation / Consistency / Traceability
### Delivery Standards
- Basic validation for all key forms
- Only allowed statuses: Submitted, Offered, Unsuccessful
- reviewNote and optionalFeedback saved and displayed correctly
- Consistent field names across pages and servlets
- No legacy status terms in any page

### Extreme Test Points
1. Extreme form validation: Empty submission, over-length input, invalid formats; ensure full blocking and consistent error messages.
2. Status consistency: Global check for only allowed statuses; manual invalid status injection; ensure no system crash or incorrect display.
3. Notes and feedback: Ultra-long text, empty values, repeated edits; ensure correct save and redisplay without truncation.
4. Field name consistency: 100% match between front-end pages and back-end servlets; no mapping failures or null pointers.

---

## 4. Apply + Status
### Responsibilities
- Core applicant application and status viewing flow
- Business rule enforcement during application

### Main Files
ApplyServlet.java, ApplicantStatusServlet.java, ApplicationService.java, ApplicationRepository.java, ApplicationRecord.java, status.jsp, vacancy-detail.jsp

### Not Responsible For
Vacancy publishing, admin configuration, global permission framework

### Delivery Standards
- Applicants can apply for vacancies
- Duplicate applications blocked
- Applications blocked when exceeding max_workload
- Blacklisted applicants cannot apply
- Applicants can view their own application status

### Extreme Test Points
1. Extreme application blocking: 100 repeated applications, max_workload boundary, blacklist toggle, expired vacancies; all rules must block correctly.
2. Data consistency: Immediate status display as Submitted; concurrent applications with no data loss or conflict.
3. Abnormal environment: Disconnect or server restart during application; no dirty data or duplicate applications.
4. Status viewing: 0 or 100 applications; real-time status sync after MO review with no delay.

---

## 5. MO Module
### Responsibilities
- MO-side core business
- Vacancy publishing, applicant review, offer/rejection, notes and feedback

### Main Files
CreateVacancyServlet.java (new), MOApplicantListServlet.java, ReviewDecisionServlet.java, ReviewService.java, VacancyService.java, VacancyRepository.java, create-vacancy.jsp (new), applicant-list.jsp, review.jsp

### Not Responsible For
Admin workload, guest browsing switch, global repository rules

### Delivery Standards
- MO can publish vacancies
- Published vacancies visible to guests
- MO can view applicants per vacancy
- MO can set Offered / Unsuccessful
- MO notes and optional feedback saved correctly

### Extreme Test Points
1. Extreme vacancy publishing: 0/100 requirements, ultra-long content, rapid repeated publishing; no errors or duplicates.
2. Applicant list: 0 or 1000 applicants; smooth pagination, no lag, consistent data for multiple MOs.
3. Review decisions: 100 repeated status changes; final status stable; empty/ultra-long notes saved correctly.
4. Permission boundaries: MO cannot access admin functions or other MO’s data; disabled accounts lose all access.

需要我帮你再压缩成**极简版**、或者**表格版**也可以告诉我！
