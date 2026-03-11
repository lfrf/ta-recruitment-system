# Team Requirements Findings for Group 27

## Purpose
This document summarises the current requirements findings for the TA Recruitment System. It is intended to show that the requirements work was not treated as a single-person task. Instead, the team used a combination of background reading, role-based discussion, and shared review to identify stakeholder needs and convert them into backlog items.

## Evidence Sources
- project handout and course lecture material
- group discussion on the current recruitment workflow
- role-based requirements workshop covering Applicant, Module Organiser, and Admin perspectives
- draft interview questions and discussion records
- backlog review based on the first set of candidate stories

## Team Participation Summary
The team used a shared workshop format so that all six members contributed to the requirements stage.

| Team role | Contribution to requirements work |
|---|---|
| Leader and GitHub coordination | confirmed project scope, technical constraints, and iteration boundaries |
| Requirements and evidence coordinator | collected questions, notes, and findings from the team discussion |
| Product backlog and prioritisation coordinator | converted agreed needs into user stories, priorities, and sprint plans |
| Prototype and UX coordinator | checked that the early UI flow matched the identified user needs |
| Report and appendix coordinator | integrated the findings into the brief report and supporting materials |
| Testing and implementation preparation coordinator | identified validation, consistency, and reliability requirements for later implementation |

## Stakeholder Findings

### TA Applicant
#### Main answers gathered from discussion
- Applicants usually hear about TA opportunities through teachers, course groups, or direct announcements.
- Before applying, they want to see the module name, responsibilities, required skills, workload, and deadline.
- They find repeated form filling inconvenient and want to reuse one profile across multiple applications.
- They want a visible status after submission, such as `Submitted`, `Accepted`, or `Rejected`.

#### Main pain points
- job information is not always presented clearly in one place
- the process can feel repetitive when applying for multiple roles
- application progress is difficult to track

#### Derived requirements
- The system shall allow an applicant to create and maintain a reusable profile.
- The system shall allow an applicant to upload a CV.
- The system shall present clear job details before application.
- The system shall allow an applicant to apply for more than one role.
- The system shall display application status clearly.

### Module Organiser
#### Main answers gathered from discussion
- MOs need job title, module, duties, required skills, workload, and deadline when posting a TA role.
- They rely on profile information, CV content, and relevant skills when reviewing candidates.
- They want a clearer way to compare applicants and update decisions without switching between multiple files.
- They often need to know whether an applicant is already heavily committed elsewhere.

#### Main pain points
- applicant review is slow when information is split across several places
- tracking multiple applications manually is inconvenient
- decisions are harder to trace when they are not recorded consistently

#### Derived requirements
- The system shall allow an MO to create and publish a TA role.
- The system shall allow an MO to view applicants for a selected role.
- The system shall show the relevant applicant profile and CV reference during review.
- The system shall allow an MO to accept or reject applicants and record the decision.
- The system should allow an MO to view workload information when needed.

### Admin
#### Main answers gathered from discussion
- Admin users need to see accepted workload across applicants rather than only raw applications.
- They want to identify overload or conflicts across different jobs or modules.
- They need a simple overview of records and statuses without manually checking multiple spreadsheets.
- They value consistency and traceability more than advanced analytics in the first version.

#### Main pain points
- workload visibility is weak in the current manual process
- records are harder to check when data is distributed across files
- cross-module conflicts are not easy to identify quickly

#### Derived requirements
- The system shall provide an overview of TA workload for Admin users.
- The system should allow workload filtering by applicant or module.
- The system should flag possible overload or conflict cases.
- The system shall preserve recruitment records in a consistent file structure.

## Cross-Cutting Functional Requirements
- The system shall support three user perspectives: Applicant, Module Organiser, and Admin.
- The system shall support the end-to-end recruitment flow from vacancy posting to decision update.
- The system shall keep status values consistent across the applicant and MO views.

## Non-Functional Requirements
- The system shall be implemented as a stand-alone Java application.
- The system shall use text-based storage only and shall not use a database.
- The system shall provide clear validation messages for invalid input.
- The system shall reject duplicate or inconsistent data where appropriate.
- The system shall make recruitment decisions traceable.
- The system should remain simple to use for first-time users.

## Scope Decision
### In scope for the early version
- applicant profile and CV handling
- job browsing and job details
- job application and status tracking
- MO vacancy posting and applicant review
- admin workload overview

### Out of scope for the early version
- database integration
- third-party platform integration
- advanced AI decision support
- complex access-control mechanisms

## Backlog Implications
These findings support the current decision to treat the following as the highest-priority backlog items:
- applicant profile
- job listing and job details
- job application submission
- MO job posting
- applicant review and decision update
- admin workload checking

The findings also support delaying advanced matching features until the core workflow is stable.
