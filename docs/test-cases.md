# Test Cases

## 1. Purpose

This document lists the initial functional test cases for the TA Recruitment System. It is intended to support the implementation of the testing strategy, provide a clear basis for test execution, and help the team prepare evidence for later iterations, reporting, and final acceptance testing.

## 2. Scope

The initial test cases focus on the core workflow of the system, especially the interactions between Applicants, Module Organisers (MO), and Admin users. The main areas covered in this version are:

- applicant registration and profile handling;
- job posting and job browsing;
- job application submission;
- applicant review and decision update;
- status consistency across views;
- workload update and checking;
- validation and duplicate prevention;
- file-based data persistence.

## 3. Test Case Priority Definition

- **High**: core workflow or high-risk function; failure would seriously affect the system.
- **Medium**: important supporting function; failure would reduce usability or reliability.
- **Low**: secondary or enhancement-related function.

## 4. Test Cases

| TC ID | Feature | Precondition | Test Steps | Expected Result | Level | Priority |
|---|---|---|---|---|---|---|
| TC01 | Applicant registration with valid data | User is on the applicant registration page | 1. Enter valid applicant details. 2. Submit the form. | Registration succeeds and applicant record is saved correctly. | System | High |
| TC02 | Applicant registration with missing required fields | User is on the applicant registration page | 1. Leave one or more required fields empty. 2. Submit the form. | Validation message is displayed and no invalid record is saved. | Unit / System | High |
| TC03 | Applicant registration with invalid input format | User is on the applicant registration page | 1. Enter invalid data such as incorrect email or empty numeric field. 2. Submit the form. | Validation message is displayed and data is rejected. | Unit / System | High |
| TC04 | Applicant profile persistence after save and reload | Applicant has already created a profile | 1. Save applicant profile. 2. Close the application or return to main menu. 3. Reopen the profile. | Previously saved profile data is displayed correctly. | Integration / System | High |
| TC05 | MO posts a new job with valid data | MO account is available and user is on job posting page | 1. Enter valid job details. 2. Submit the form. | Job is saved successfully and appears in the available job list. | Integration / System | High |
| TC06 | MO posts a job with missing required fields | MO account is available and user is on job posting page | 1. Leave required fields blank. 2. Submit the form. | Validation message is displayed and job is not saved. | Unit / System | High |
| TC07 | Applicant browses available jobs | At least one job exists in the system | 1. Log in as applicant. 2. Open the job list page. | Available jobs are displayed correctly. | System | High |
| TC08 | Applicant views job details | At least one job exists in the system | 1. Open a job from the job list. | Correct job information is shown, including module, duties, deadline, and workload if applicable. | System | High |
| TC09 | Applicant submits a valid job application | Applicant account exists and at least one job exists | 1. Open a job. 2. Submit an application with valid details. | Application is saved successfully with the correct initial status, such as Submitted. | Integration / System | High |
| TC10 | Applicant attempts duplicate application to the same job | Applicant has already applied for the selected job | 1. Open the same job again. 2. Submit another application. | Duplicate application is blocked and warning message is shown. | Unit / Integration | High |
| TC11 | Applicant views submitted applications | Applicant has submitted at least one application | 1. Log in as applicant. 2. Open application history or status page. | Submitted applications are displayed correctly. | System | High |
| TC12 | MO views applicants for a selected job | A job exists with at least one submitted application | 1. Log in as MO. 2. Open the applicant list for a job. | Correct applicant records are displayed for that job. | Integration / System | High |
| TC13 | MO reviews applicant details | A job exists with at least one submitted application | 1. Open one applicant record from the list. | Applicant details are displayed correctly and completely. | System | High |
| TC14 | MO accepts an applicant | A job exists with at least one submitted application | 1. Select one applicant. 2. Choose Accept. 3. Save or confirm the action. | Applicant status changes to Accepted and the updated result is saved correctly. | Integration / System | High |
| TC15 | MO rejects an applicant | A job exists with at least one submitted application | 1. Select one applicant. 2. Choose Reject. 3. Save or confirm the action. | Applicant status changes to Rejected and the updated result is saved correctly. | Integration / System | High |
| TC16 | Applicant checks updated status after MO decision | A submitted application has already been accepted or rejected | 1. Log in as applicant. 2. Open application status page. | The applicant sees the updated decision status correctly. | System | High |
| TC17 | Status consistency across MO and Applicant views | A decision has been made by MO | 1. Check status in MO view. 2. Check status in applicant view. | Both views display the same application status. | Integration / System | High |
| TC18 | Admin views workload after applicant acceptance | At least one applicant has been accepted | 1. Log in as admin. 2. Open workload overview page. | Accepted allocation is reflected correctly in workload view. | Integration / System | High |
| TC19 | Rejected applicant does not affect workload totals | At least one application has been rejected | 1. Log in as admin. 2. Open workload overview page. | Rejected applications are not counted in workload totals. | Integration / System | High |
| TC20 | Workload update after multiple accepted assignments | Multiple accepted assignments exist in the system | 1. Open workload page as admin. | Workload totals are calculated correctly across relevant records. | Integration / System | High |
| TC21 | File read error handling | System attempts to read missing or invalid data file | 1. Trigger a read operation where the data file is unavailable or invalid. | Clear error handling is shown and the system does not crash unexpectedly. | Unit / Integration | Medium |
| TC22 | File write error handling | System attempts to save data but write operation fails | 1. Trigger a save operation in a controlled failure scenario. | Error is handled properly and user is informed if needed. | Unit / Integration | Medium |
| TC23 | Saved job data remains correct after reload | At least one job has been posted | 1. Save job data. 2. Reopen the job list or restart the application. | Posted job still exists and details remain correct. | Integration / System | High |
| TC24 | Saved application data remains correct after reload | At least one application has been submitted | 1. Save application data. 2. Reopen the system or refresh the workflow. | Submitted application remains visible and status remains correct. | Integration / System | High |
| TC25 | Search or filter jobs with matching input | Multiple jobs exist in the system | 1. Enter a valid search term or filter condition. 2. Run the search/filter. | Matching jobs are displayed correctly. | Unit / System | Medium |
| TC26 | Search or filter jobs with no matching result | Multiple jobs exist in the system | 1. Enter a search term with no matches. 2. Run the search/filter. | Empty result is shown correctly without error. | Unit / System | Medium |
| TC27 | Access correct role-specific function pages | Accounts for different roles are available | 1. Log in as Applicant, MO, and Admin separately. 2. Navigate to role-specific pages. | Each role can access the correct functions and views. | System | Medium |
| TC28 | Invalid direct operation or wrong sequence handling | User attempts an action without satisfying workflow requirements | 1. Try to review applicants before any application exists, or check status before submission. | System handles the case correctly and displays appropriate message or empty state. | System | Medium |

## 5. High-Priority Execution Order

The following test cases should be executed first because they cover the highest-risk parts of the system:

1. **TC05** - MO posts a new job with valid data  
2. **TC07** - Applicant browses available jobs  
3. **TC09** - Applicant submits a valid job application  
4. **TC12** - MO views applicants for a selected job  
5. **TC14** - MO accepts an applicant  
6. **TC16** - Applicant checks updated status after MO decision  
7. **TC18** - Admin views workload after applicant acceptance  
8. **TC10** - Applicant attempts duplicate application to the same job  
9. **TC17** - Status consistency across MO and Applicant views  
10. **TC24** - Saved application data remains correct after reload  

These cases should be prioritised because they test the main end-to-end recruitment workflow and the most important risks: data persistence, workflow continuity, duplicate prevention, decision update, and workload consistency.

## 6. Notes for Later Iterations

This version is the initial test case set. In later iterations, the team should extend it by adding:

- actual test execution dates;
- tester name;
- pass/fail result;
- defect or issue reference;
- screenshot or evidence link;
- regression test status.

## 7. Conclusion

These test cases provide an initial basis for structured testing of the TA Recruitment System. They are designed to support early validation of the most critical system functions and to give the team a clear starting point for future test execution and evidence collection.

## 8. Current Execution Status Mapping

For the latest execution state and evidence mapping, use:

- `docs/testing-coverage-matrix.md` (TC01-TC28 status: Auto-Pass / Manual-Pass / Partial / Pending)
- `docs/test-execution-record.md` (actual run records and test totals)
- `test/e2e/system_flow_batch_2026-05-22.md` (manual system-flow checklist)
- `test/e2e/ai_import_e2e_checklist.md` (AI callback end-to-end checklist)
