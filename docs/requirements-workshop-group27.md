# Requirements Workshop Notes - Group 27

**Project:** TA Recruitment System  
**Date:** 2026-03-10  
**Participants:** Yifu Feng, Yuzhang Wu, Chensiyuan Qing, Fuhe Huang, Tao Li, Mu Du  
**Workshop format:** Team-based role discussion  
**Notes recorded by:** Yuzhang Wu

## Workshop Purpose
The purpose of this workshop was to collect early requirements evidence for the TA Recruitment System through a collaborative team discussion. Rather than assigning requirement discovery to only one person, the whole team participated in analysing the needs of the three main stakeholder groups: **Applicant**, **Module Organiser**, and **Admin**. This approach helped ensure that the resulting user stories and backlog items were shaped through shared discussion rather than individual interpretation.

## How the workshop was conducted
The team discussed the recruitment process from three stakeholder perspectives. Different members contributed examples, questions, and candidate feature ideas based on the project brief and the expected workflow. Yuzhang Wu was responsible for initiating the stakeholder fact-finding activity and recording the discussion outcomes, while all team members contributed to identifying pain points, clarifying priorities, and refining the initial user story ideas.

The workshop focused on:
- the current manual recruitment workflow;
- the main difficulties faced by each stakeholder group;
- the most useful features for an early software version;
- the boundaries between essential functionality and optional future enhancements.

## Stakeholder 1: Applicant

### Key discussion points
- Applicants need clear vacancy information before deciding whether to apply.
- Important vacancy details include module name, job responsibilities, required skills, workload, and deadline.
- Applicants do not want to repeatedly enter the same personal information for different applications.
- Applicants want confirmation after submission and visible status updates later.

### Main needs identified
- reusable applicant profile;
- CV upload;
- vacancy browsing;
- clear job details;
- visible application status.

## Stakeholder 2: Module Organiser

### Key discussion points
- MOs need a simple way to create and publish TA vacancies.
- They need to review applicants using consistent information rather than scattered files.
- Applicant profile information and CV content are both important during review.
- MOs need to record decisions clearly and consistently.

### Main needs identified
- vacancy creation and publishing;
- applicant list for each vacancy;
- applicant detail review;
- decision recording;
- access to workload information when needed.

## Stakeholder 3: Admin

### Key discussion points
- Admin users need to monitor TA workload across modules.
- They want to identify overload or conflicts without checking multiple spreadsheets manually.
- Record consistency is more important than advanced analytics in the first version.
- A simple overview of accepted workload is sufficient for the early release.

### Main needs identified
- workload overview;
- basic filtering;
- overload/conflict visibility;
- consistent recruitment records.

## Common Findings from Team Discussion
Across all stakeholder perspectives, the team identified the following recurring needs:

- the workflow should be easier to track than the current manual process;
- information should be stored in a more consistent and structured way;
- status values should be clear and visible;
- the first version should focus on the core recruitment flow rather than advanced optional features.

## Later Refinement from the Follow-Up TA Interview
On 2026-03-13, the team added a follow-up interview with an experienced TA to validate the real workflow. This later evidence clarified that:

- vacancies are often announced through the Information Portal or QR-code forms;
- module-specific skills and prior course knowledge matter in practice;
- successful applicants are commonly informed by email;
- detailed rejection reasons are usually not provided;
- tutorial-support scheduling happens later and should not expand the early system scope.

This follow-up did not replace the workshop results. Instead, it refined the scope and helped the team align the backlog with the real process. The interview evidence was treated as one important input to team decision-making rather than as a complete definition of what the final system must do.

## Early Scope Decision
The team agreed that the first version should focus on a realistic minimum end-to-end workflow.

### In scope
- applicant profile and CV handling;
- browsing and applying for vacancies;
- MO vacancy posting and applicant review;
- decision recording;
- admin workload overview.

### Out of scope
- database integration;
- third-party platform integration;
- automated email integration;
- advanced AI recommendation or ranking;
- complex role and permission frameworks;
- post-recruitment tutorial-support scheduling.

## Workshop Outcome
The workshop results were used as a shared evidence base for:
- drafting stakeholder interview records;
- producing the requirements findings summary;
- shaping the initial product backlog;
- writing the first assessment brief report.

The discussion also informed the first set of user stories. Although different team members may draft individual stories, the team agreed that user stories should reflect collaborative discussion and shared understanding rather than isolated authorship.
