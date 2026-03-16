# Team Requirements Findings for Group 27

## Purpose
This document summarises the requirements findings for the **TA Recruitment System**. Its purpose is to record stakeholder needs, show how those needs were gathered, and explain how the team refined the backlog after a follow-up interview with an experienced TA. The document also demonstrates that requirements work was carried out collaboratively rather than by one individual alone.

The follow-up interview was used as supporting evidence rather than as a direct specification. The real process helped the team validate assumptions, identify pain points, and refine scope boundaries, but the final requirements were still decided by the team after considering stakeholder value, coursework constraints, and implementation feasibility.

## Evidence Sources
The findings in this document are based on:
- the project brief and lecture material;
- collaborative workshop discussion of the recruitment workflow;
- stakeholder-oriented discussion covering Applicant, Module Organiser, and Admin perspectives;
- draft interview questions and interview records;
- a follow-up interview on 2026-03-13 with an experienced TA about the real recruitment workflow;
- shared review of the first set of user stories and backlog items.

## Team Participation Summary
The requirements process was organised as a collaborative activity rather than a one-person task.

| Team member | Main contribution |
|---|---|
| Yifu Feng | Confirmed project scope, technical direction, coordination arrangements and Modify and correct the recorded results |
| Yuzhang Wu | Initiated stakeholder fact-finding and recorded the workshop and interview outputs |
| Chensiyuan Qing | Refined early backlog items and helped structure user stories |
| Fuhe Huang | Checked whether proposed stories were aligned with the intended interface and page flow |
| Tao Li | Integrated requirements findings into the report and supporting appendix |
| Mu Du | Considered implementation and testing implications such as validation and reliability |

All members contributed to the discussion of stakeholder needs and the identification of core stories. Individual members may have drafted or edited specific stories, but the stories themselves were shaped through team discussion and review.

## Stakeholder Findings

### 1. TA Applicant

#### Main points gathered from discussion and follow-up interview
- Applicants usually learn about TA opportunities through the Information Portal, QR-code forms, teacher notices, or course groups.
- Before applying, they want to see the module name, responsibilities, required skills, preferred background, expected workload, and deadline.
- Current application forms often ask for module-related skills or prior course knowledge, so applicants need a profile that can capture reusable skill information.
- They find repeated form filling inconvenient and prefer to reuse one profile across multiple applications.
- After submission, they want to know whether the application is still under review or whether an offer has been made.
- The follow-up discussion also suggested that seeing how many people have applied for a role could be a useful transparency feature for applicants.

#### Main pain points
- job information is not always presented clearly in one place;
- the process feels repetitive when applying for multiple roles;
- application progress is difficult to track after submission;
- the current process gives little transparency beyond the final outcome.

#### Derived requirements
- The system shall allow an applicant to create and maintain a reusable profile that includes relevant skills or experience.
- The system shall allow an applicant to upload a CV.
- The system shall present clear vacancy details before application.
- The system shall allow an applicant to apply for more than one role.
- The system shall display application status or outcome clearly.
- The early version should support clear outcome visibility and may support lightweight feedback when an organiser chooses to add it. Full personalised rejection explanations are not required.
- The system should show the number of applicants for a vacancy when that transparency information is intended to be visible to applicants.

### 2. Module Organiser

#### Main points gathered from discussion and follow-up interview
- MOs need to enter job title, module, duties, required skills, workload, and deadline when posting a TA role.
- The real process often includes module-specific skill expectations, so vacancy records should make these requirements visible and structured.
- They rely on profile information, CV content, and relevant skills when reviewing applicants.
- Final tutorial-support arrangements and day-to-day task allocation are typically handled later by the MO or TA lead, after recruitment is already complete.
- They may also need to know whether an applicant is already heavily committed elsewhere.

#### Main pain points
- applicant review becomes slow when information is split across several places;
- tracking multiple applications manually is inconvenient;
- decisions are harder to trace when they are not recorded consistently;
- early recruitment information is mixed with later teaching-arrangement details.

#### Derived requirements
- The system shall allow an MO to create and publish a TA role.
- The system shall allow an MO to view applicants for a selected role.
- The system shall show the relevant applicant profile and CV reference during review.
- The system shall allow an MO to record whether an offer is made or the application is unsuccessful.
- The system should allow an MO to view workload information when needed.
- The system should allow an MO to add optional review notes or short outcome feedback.
- The early version does not need to manage post-recruitment timetable allocation or tutorial-support scheduling.

### 3. Admin

#### Main points gathered from discussion and follow-up interview
- Admin users need to see accepted TA workload across applicants rather than only raw application records.
- In practice, workload visibility mainly means knowing which modules a TA has already been accepted for, so that overload or conflict risks can be noticed early.
- They need a simple overview of records and statuses without manually checking multiple spreadsheets.
- In the first version, they value consistency and traceability more than advanced analytics.

#### Main pain points
- workload visibility is weak in the current manual process;
- records are harder to verify when data is distributed across files;
- cross-module conflicts are not easy to identify quickly.

#### Derived requirements
- The system shall provide an overview of accepted TA assignments for admin users.
- The system should allow workload filtering by applicant or module.
- The system should highlight possible overload or conflict cases.
- The system shall preserve recruitment records in a consistent file structure.

## Cross-Cutting Functional Requirements
The workshop and follow-up interview also identified several requirements that apply across roles rather than to a single stakeholder only:

- The system shall support three user perspectives: Applicant, Module Organiser, and Admin.
- The system shall support the end-to-end recruitment flow from vacancy posting to decision update.
- The system shall keep status values consistent across the applicant and MO views.
- The system shall preserve recruitment information in a structured and traceable way.
- The system shall make module-specific skill requirements and applicant skill information visible enough to support selection.

## Non-Functional Requirements
The following non-functional requirements were identified during the discussion:

- The system shall be implemented as a stand-alone Java application.
- The system shall use text-based storage only and shall not rely on a database.
- The system shall provide clear validation messages for invalid input.
- The system shall reject duplicate or inconsistent data where appropriate.
- The system shall make recruitment decisions traceable.
- The system should remain simple to use for first-time users.
- The first release should avoid unnecessary infrastructure such as automated email integration.

## Scope Decision

### In scope for the early version
- applicant profile and CV handling;
- module-related skills and vacancy details;
- job application and status tracking;
- MO vacancy posting and applicant review;
- admin workload overview based on accepted assignments.

### Out of scope for the early version
- database integration;
- third-party platform integration;
- automated email sending;
- mandatory detailed personalised rejection feedback to all applicants;
- post-recruitment tutorial-support scheduling and TA lead task allocation;
- advanced AI-assisted decision support.

## Backlog Implications
These findings support treating the following as the highest-priority backlog items:

- applicant profile management with reusable skill information;
- job listing and vacancy details with module-specific requirements;
- job application submission;
- visible application outcome tracking;
- applicant-side vacancy application count visibility;
- optional organiser notes or lightweight feedback;
- MO job posting;
- applicant review and decision update;
- admin workload checking based on accepted assignments.

The team used these findings to shape the backlog collaboratively. Some stories were first drafted by individual members, but the final direction, wording, and priority were discussed and reviewed as a group. The follow-up TA interview did not determine the backlog on its own. Instead, it clarified the real workflow and helped the team remove unnecessary scope, especially detailed rejection handling and post-recruitment scheduling.
