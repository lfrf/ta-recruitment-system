# Initial Testing Strategy and Core Test Plan

## 1. Purpose

This document defines the initial testing strategy for the TA Recruitment System. It explains how the planned builds will be tested through unit testing, integration testing, system testing, and acceptance testing. It also identifies the highest-risk cases that should be tested first.

## 2. Testing Objectives

The objectives of this testing plan are to:
- verify that core recruitment functions work correctly;
- detect validation and file-handling errors early;
- ensure consistency across Applicant, MO, and Admin views;
- support later report writing, demo, and acceptance testing.

## 3. Test Levels

### 3.1 Unit Testing
Unit testing will verify individual classes and methods such as input validation, file reading/writing, duplicate checking, and workload calculation.

### 3.2 Integration Testing
Integration testing will verify that components work together correctly, especially between UI actions, business logic, and file persistence.

### 3.3 System Testing
System testing will verify complete user scenarios across the whole application, including Applicant, Module Organiser, and Admin functions.

### 3.4 Acceptance Testing
Acceptance testing will confirm that the implemented system satisfies the expected user stories and project requirements.

## 4. Planned Builds Mapped to Testing Levels

### Build 1
Focus:
- prototype walkthrough;
- requirement consistency check;
- early acceptance-style review.

### Build 2
Focus:
- applicant registration/login;
- profile maintenance;
- browse jobs;
- MO posts jobs.

Testing:
- Unit testing for validation and file operations;
- Integration testing for posting and browsing jobs;
- System testing for basic navigation.

### Build 3
Focus:
- apply for a job;
- review applicants;
- accept/reject applicant;
- applicant status update;
- admin workload overview.

Testing:
- end-to-end workflow testing;
- status consistency testing;
- duplicate prevention testing;
- regression testing for previous functions.

### Build 4
Focus:
- improved validation;
- better error handling;
- filtering functions;
- review notes or workload alerts.

Testing:
- regression testing;
- exception handling;
- non-happy-path scenarios;
- usability checks.

### Build 5
Focus:
- final stable build;
- full regression testing;
- formal acceptance testing;
- demo preparation.

## 5. Highest-Risk Cases to Test First

1. File-based data inconsistency  
2. Broken end-to-end recruitment workflow  
3. Invalid or duplicate input  
4. Status mismatch across Applicant, MO, and Admin views  
5. Workload calculation errors  

## 6. Core High-Priority Test Cases

| TC ID | Test case | Level | Priority | Expected result |
|---|---|---|---|---|
| TC01 | Register applicant with valid data | System | High | Record created successfully |
| TC02 | Submit missing required fields | Unit/System | High | Validation message shown |
| TC03 | MO posts a new job | Integration/System | High | Job saved and visible |
| TC04 | Applicant applies for a job | Integration/System | High | Application saved with correct status |
| TC05 | Duplicate application attempt | Unit/Integration | High | Duplicate blocked |
| TC06 | MO accepts an applicant | Integration/System | High | Status updated correctly |
| TC07 | MO rejects an applicant | Integration/System | High | Status updated correctly |
| TC08 | Applicant checks updated application status | System | High | Correct status displayed |
| TC09 | Admin workload view after acceptance | Integration/System | High | Workload updated correctly |
| TC10 | Reopen saved data files | Integration/System | High | Data remains correct |

## 7. Conclusion

This initial testing strategy prioritises the most critical risks first, especially data consistency, workflow continuity, validation, status synchronisation, and workload correctness. The team will expand testing progressively across later builds.
