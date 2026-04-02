# Extreme Test Plan - TA Recruitment System
## 1. Test Scope
Full-scenario, extreme-case, and boundary testing for all 5 modules. Ensure stable functions, complete workflow, and no crashes under abnormal conditions.

## 2. Test Roles
- Visitor
- Applicant
- MO (Module Organiser)
- Admin

## 3. Module 1: Visitor + Applicant Profile
### Normal Scenarios
1. Visitor can view vacancy list
2. Visitor can view vacancy details
3. Login button displays correctly
4. Applicant can register and save profile
5. Profile data loads back correctly
6. CV upload and display work
7. Page style loads normally

### Extreme / Abnormal Scenarios
1. No vacancies → page shows empty state, no crash
2. Slow network → style not lost, no plain-text page
3. Submit empty required fields → validation blocks
4. Enter ultra-long text → no error, no garbled code
5. Refresh repeatedly → no duplicate/lost data
6. Restart app → data still exists

## 4. Module 2: Validation / Consistency / Traceability
### Normal Scenarios
1. All key forms have required validation
2. Statuses only use: Submitted / Offered / Unsuccessful
3. reviewNote and optionalFeedback save and display correctly
4. Field names are consistent system-wide
5. No old status terms remain

### Extreme / Abnormal Scenarios
1. Input special characters < > ' " & → no crash
2. Empty request / duplicate submit → system stable
3. Manually change status in frontend → blocked by backend
4. Cross-page navigation → status remains consistent
5. Invalid API parameters → no stack trace exposed

## 5. Module 3: Apply + Status
### Normal Scenarios
1. Applicant can apply for a vacancy
2. Status becomes Submitted after application
3. Applicant can view their own applications

### Extreme / Abnormal Scenarios
1. Duplicate application to the same vacancy → blocked
2. Exceed maxWorkload → blocked
3. Blacklisted applicant → cannot apply
4. Apply when no vacancies → friendly message
5. Call apply API without login → blocked
6. Submit during network loss → no dirty data

## 6. Module 4: MO Module
### Normal Scenarios
1. MO can create and post vacancies
2. Posted vacancies are visible to visitors
3. MO can view applicants under a vacancy
4. MO can set status: Offered / Unsuccessful
5. Review notes and feedback save correctly

### Extreme / Abnormal Scenarios
1. Post vacancy with empty required fields → blocked
2. No applicants → list shows empty, no crash
3. Multiple users operate the same applicant → status consistent
4. Ultra-long feedback → saves normally
5. After logout → cannot perform actions

## 7. Module 5: Testing / Demo Data / Regression
### Normal Scenarios
1. users.json accounts can log in
2. vacancies.json loads correctly
3. applications.json shows correct statuses
4. admin_config.json works
5. blacklist.json blocks properly

### Extreme / Abnormal Scenarios
1. Empty JSON files → system does not crash
2. Invalid JSON format → graceful error message
3. Missing fields → no exceptions
4. Quick regression available after every merge

## 8. Delivery Standard
1. Stable demo data
2. Complete and executable test steps
3. Written test results
4. Quick regression after merge:
   - Login
   - Visitor browsing
   - Application
   - MO review
   - Admin configuration
