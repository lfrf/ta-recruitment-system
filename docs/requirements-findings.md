# Team Requirements Findings for Group 27

## Purpose
This document records the latest requirements findings for the **TA Recruitment System** after the earlier workshop, the 2026-03-13 follow-up TA interview, the later teaching-staff review, the 2026-03-21 visitor-access feedback, and the analysis of a sample TA CV/resume. The new feedback was treated as supporting evidence to refine the backlog, not as a direct specification that must be copied blindly.

## Evidence Sources
- project brief and lecture material;
- team workshop discussion;
- stakeholder-oriented questions for Applicant, MO, and Admin;
- follow-up interview with an experienced TA on 2026-03-13;
- teaching-staff backlog review on 2026-03-20;
- teaching-staff interface feedback on 2026-03-21;
- document analysis of a sample TA CV/resume.

## Team Participation Summary
| Team member | Main contribution |
|---|---|
| Yifu Feng | revised scope, backlog direction, and later corrections |
| Yuzhang Wu | organised requirement-finding and interview recording |
| Chensiyuan Qing | refined user stories and backlog structure |
| Fuhe Huang | checked alignment with UI and page flow |
| Tao Li | integrated findings into report and appendix |
| Mu Du | reviewed validation, workload, and reliability implications |

## Stakeholder Findings

### TA Applicant
Main findings:
- Applicants need clear vacancy details: module, duties, skills, workload, deadline, and optionally current applicant count.
- Applicants should be able to browse vacancy information before logging in. A visible login entry should remain available from the public browsing interface.
- The applicant profile should be more structured than a simple name-and-skills form. Based on the sample CV, it should include:
  - full name, student ID, email, and phone number;
  - degree/programme and year of study;
  - relevant modules or grades where useful;
  - technical skills and tools;
  - previous TA experience;
  - project or leadership experience;
  - availability;
  - CV reference.
- Applicants need login for restricted actions so that their profile, applications, and status records are linked to the correct account.
- Applicants may apply for more than one role, but the later review suggested the system should show and enforce a configurable role limit from the start. The initial default can be `3`, controlled by an admin parameter.
- Applicants want clear application status and may benefit from optional short feedback.

Derived requirements:
- The system shall support detailed reusable applicant profiles.
- The system shall support CV upload.
- The system shall allow public browsing of vacancy lists and vacancy details before login.
- The system shall provide a visible login entry from the public vacancy interface.
- The system shall require login before profile, application, or status actions are allowed.
- The system shall show clear vacancy details.
- The system shall allow multiple applications only within the current `max_workload` limit.
- The system shall show application status clearly.
- The system should show applicant count for a vacancy when transparency is enabled.

### Module Organiser
Main findings:
- MOs need to publish vacancies with structured module-specific requirements.
- MOs need to review applicant profile information and CV together.
- MOs may need to see workload-limit information before making decisions.
- Blacklist checking is currently manual and should be supported later.
- Later tutorial support scheduling should remain outside the early system.

Derived requirements:
- The system shall allow vacancy creation and publishing.
- The system shall allow viewing applicants for a vacancy.
- The system shall show profile and CV together during review.
- The system shall allow organisers to record `Offered` or `Unsuccessful` outcomes.
- The system should allow organisers to view workload and optional review notes.
- The system should expose blacklist information where it affects review.

### Admin
Main findings:
- Admin need a workload overview across applicants and modules.
- Admin should configure a simple `max_workload` parameter instead of relying only on later manual balancing.
- The initial default can be `3`, but the value should be editable.
- Admin also need blacklist support because unsuitable TAs are currently screened manually.

Derived requirements:
- The system shall provide workload overview and filtering.
- The system shall allow admin to configure the `max_workload` parameter.
- The system shall use the same parameter when checking applications and overload warnings.
- The system should allow admin to maintain a blacklist of unsuitable applicants.

## Cross-Cutting Functional Requirements
- The system shall support three roles: Applicant, Module Organiser, and Admin.
- The system shall support role-based login.
- The system shall allow public access to vacancy browsing while protecting personal or restricted actions behind login.
- The system shall keep status values consistent across views.
- The system shall preserve records in a structured and traceable form.
- The system shall enforce the current admin-defined `max_workload` rule consistently.
- The system should support blacklist flagging or blocking where policy requires it.

## Non-Functional Requirements
- The system shall be implemented as a stand-alone Java application.
- The system shall use text-based storage only and shall not rely on a database.
- The system shall provide clear validation messages.
- The system shall reject duplicate or inconsistent data where appropriate.
- The system shall keep recruitment decisions traceable.
- Any later AI-assisted features should remain lightweight, explainable, and optional.

## Scope Decision
### In scope for the early version
- public vacancy browsing before login;
- login and protected applicant actions;
- detailed applicant profile and CV handling;
- vacancy details and application flow;
- workload overview and configurable `max_workload` control;
- organiser review and decision recording.

### In scope for later iterations
- blacklist support;
- optional short outcome feedback;
- applicant-visible application counts;
- explainable low-priority AI-assisted features.

### Out of scope for the early version
- database integration;
- automated email sending;
- tutorial-support scheduling and TA lead task allocation;
- complex black-box AI decision support.

## Backlog Implications
The latest refinement adds or strengthens the following requirement themes:
- detailed applicant profile fields;
- public vacancy browsing with a visible login entry;
- protected actions that require login;
- configurable `max_workload` limit;
- blacklist support;
- low-priority AI-assisted features such as skill matching, missing-skill identification, and workload balancing suggestions.
