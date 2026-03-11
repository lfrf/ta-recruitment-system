# Team Requirements Findings for Group 27

## Purpose
This document summarises the requirements findings for the **TA Recruitment System**. The purpose of the document is not only to record stakeholder needs, but also to show that the requirements work was carried out collaboratively. In line with Agile practice, the team treated requirement discovery and user story formation as a shared activity. While some members were responsible for coordination or documentation, the understanding of stakeholder needs and the refinement of user stories came from team discussion.

## Evidence Sources
The findings in this document are based on:
- the project brief and lecture material;
- collaborative workshop discussion of the recruitment workflow;
- stakeholder-oriented discussion covering Applicant, Module Organiser, and Admin perspectives;
- draft interview questions and interview records;
- shared review of the first set of user stories and backlog items.

## Team Participation Summary
The requirements process was organised as a collaborative workshop rather than a one-person task.

| Team member | Main contribution |
|---|---|
| Yifu Feng | Confirmed project scope, technical direction, and coordination arrangements |
| Yuzhang Wu | Initiated stakeholder fact-finding and recorded the workshop and interview outputs |
| Chensiyuan Qing | Refined early backlog items and helped structure user stories |
| Fuhe Huang | Checked whether proposed stories were aligned with the intended interface and page flow |
| Tao Li | Integrated requirements findings into the report and supporting appendix |
| Mu Du | Considered implementation and testing implications such as validation and reliability |

All members contributed to the discussion of stakeholder needs and the identification of core stories. Individual members may have drafted or edited specific stories, but the stories themselves were shaped through team discussion and review.

## User Story Co-Creation Approach

After the stakeholder findings were collected, the team collaboratively converted the requirements into user stories. Instead of treating backlog creation as a single-person writing task, the group used the findings as shared evidence and discussed how they should be represented in backlog form.

The co-creation process followed four steps:

1. stakeholder findings were grouped by role, including Applicant, Module Organiser, and Admin;
2. different team members proposed candidate stories from the perspective most closely related to their work;
3. overlapping or similar stories were merged and rewritten into the format “As a..., I want..., so that...”;
4. priorities were then agreed collectively based on business value, feasibility, and importance to the first usable version.

This means that although individual members may have suggested or drafted particular stories first, the final wording, scope, and priority of the stories were shaped through collaborative review. The product backlog therefore reflects shared team understanding rather than isolated authorship.

## Stakeholder Findings

### 1. TA Applicant

#### Main points gathered from discussion
- Applicants usually learn about TA opportunities through teachers, course groups, or direct announcements.
- Before applying, they want to see the module name, responsibilities, required skills, expected workload, and deadline.
- They find repeated form filling inconvenient and prefer to reuse one profile across multiple applications.
- After submission, they want to see a visible application status such as `Submitted`, `Accepted`, or `Rejected`.

#### Main pain points
- job information is not always presented clearly in one place;
- the process feels repetitive when applying for multiple roles;
- application progress is difficult to track after submission.

#### Derived requirements
- The system shall allow an applicant to create and maintain a reusable profile.
- The system shall allow an applicant to upload a CV.
- The system shall present clear vacancy details before application.
- The system shall allow an applicant to apply for more than one role.
- The system shall display application status clearly.

### 2. Module Organiser

#### Main points gathered from discussion
- MOs need to enter job title, module, duties, required skills, workload, and deadline when posting a TA role.
- They rely on profile information, CV content, and relevant skills when reviewing applicants.
- They want a more efficient way to compare applicants and update decisions without switching between multiple files.
- They may also need to know whether an applicant is already heavily committed elsewhere.

#### Main pain points
- applicant review becomes slow when information is split across several places;
- tracking multiple applications manually is inconvenient;
- decisions are harder to trace when they are not recorded consistently.

#### Derived requirements
- The system shall allow an MO to create and publish a TA role.
- The system shall allow an MO to view applicants for a selected role.
- The system shall show the relevant applicant profile and CV reference during review.
- The system shall allow an MO to accept or reject applicants and record the decision.
- The system should allow an MO to view workload information when needed.

### 3. Admin

#### Main points gathered from discussion
- Admin users need to see accepted TA workload across applicants rather than only raw application records.
- They want to identify overload or conflict risks across different jobs or modules.
- They need a simple overview of records and statuses without manually checking multiple spreadsheets.
- In the first version, they value consistency and traceability more than advanced analytics.

#### Main pain points
- workload visibility is weak in the current manual process;
- records are harder to verify when data is distributed across files;
- cross-module conflicts are not easy to identify quickly.

#### Derived requirements
- The system shall provide an overview of TA workload for admin users.
- The system should allow workload filtering by applicant or module.
- The system should highlight possible overload or conflict cases.
- The system shall preserve recruitment records in a consistent file structure.

## Cross-Cutting Functional Requirements
The workshop also identified several requirements that apply across roles rather than to a single stakeholder only:

- The system shall support three user perspectives: Applicant, Module Organiser, and Admin.
- The system shall support the end-to-end recruitment flow from vacancy posting to decision update.
- The system shall keep status values consistent across the applicant and MO views.
- The system shall preserve recruitment information in a structured and traceable way.

## Non-Functional Requirements
The following non-functional requirements were identified during the discussion:

- The system shall be implemented as a stand-alone Java application.
- The system shall use text-based storage only and shall not rely on a database.
- The system shall provide clear validation messages for invalid input.
- The system shall reject duplicate or inconsistent data where appropriate.
- The system shall make recruitment decisions traceable.
- The system should remain simple to use for first-time users.

## Scope Decision

### In scope for the early version
- applicant profile and CV handling;
- job browsing and vacancy details;
- job application and status tracking;
- MO vacancy posting and applicant review;
- admin workload overview.

### Out of scope for the early version
- database integration;
- third-party platform integration;
- advanced AI-assisted decision support;
- complex access-control mechanisms.

## Backlog Implications
These findings support treating the following as the highest-priority backlog items:

- applicant profile management;
- job listing and vacancy details;
- job application submission;
- MO job posting;
- applicant review and decision update;
- admin workload checking.

The team used these findings to shape the initial backlog collaboratively. Some stories were first drafted by individual members, but the final direction, wording, and priority were discussed and reviewed as a group. This is consistent with the Agile principle that what matters most is not who first wrote a user story, but who took part in discussing and refining it.
