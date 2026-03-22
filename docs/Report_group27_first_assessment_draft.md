# EBU6304 Software Engineering Group Project
## First Assessment Brief Report Draft

**Group:** `Group 27`  
**Project:** `TA Recruitment System`  
**Module:** `EBU6304 Software Engineering`

## 1. Project Context and Scope

BUPT International School currently recruits Teaching Assistants through forms, spreadsheets, manual records, and follow-up communication outside a single system. Fact-finding showed that vacancy information is fragmented, application progress is hard to track, and workload conflicts are not easy to identify early. The aim of this project is therefore to design a simple TA Recruitment System that supports the core recruitment workflow in a more structured, visible, and reliable way.

The system is intended to support three main stakeholder groups. **TA Applicants** need to browse vacancies easily, view vacancy details before logging in, sign in when they want to use personal features, maintain a reusable profile, upload a CV, apply for suitable roles, and check application status. **Module Organisers (MOs)** need to publish TA roles, review applicants efficiently, compare candidates using structured information, and record outcomes. **Admin users** need to monitor workload, configure a simple role-limit rule through a `max_workload` parameter, and later support blacklist control where necessary.

The first release is intentionally limited to the minimum end-to-end recruitment workflow. In scope are public vacancy browsing before login, a visible login entry, structured applicant profile management, CV upload, vacancy detail viewing, job application, application status tracking, job posting, applicant review, decision recording, workload overview, and an admin-configured `max_workload` parameter. Later iterations may extend this with blacklist support, optional short feedback, applicant-visible application counts, and lightweight explainable matching features. Out of scope for the early version are database integration, third-party services, automated email integration, mandatory detailed rejection feedback for every applicant, and post-recruitment tutorial-support scheduling.

To reduce technical risk, the team plans to implement the system as a **stand-alone Java application** using **text-based storage** such as JSON or CSV. This satisfies the module constraints and keeps the project focused on software engineering process rather than unnecessary infrastructure.

## 2. Fact-Finding Techniques

The team used three complementary fact-finding techniques introduced in the module: **background reading**, **interviewing**, and **document analysis**.

Background reading was used first to understand the coursework brief, required deliverables, technical restrictions, and the likely structure of the recruitment process. This helped the team identify stakeholder groups, system boundaries, and early implementation constraints before drafting backlog items.

Interviewing was carried out through a team requirements workshop supported by semi-structured questions for Applicants, Module Organisers, and Admin users. The team later added a short follow-up interview with an experienced TA to validate the real process. This follow-up clarified that vacancies are often announced through the Information Portal and QR-code forms, module-specific skills matter in practice, successful applicants are commonly informed by email, and later tutorial arrangement happens after recruitment rather than during initial selection. Later teaching-staff feedback also pointed out missing items in the first backlog draft, especially login, configurable workload limits, blacklist support, low-priority AI-assisted features, and the need to let users browse vacancies before login while prompting login only when restricted actions are attempted.

Document analysis was used to examine the current manual workflow and the likely data items required by the proposed system. This also included analysis of a sample TA CV/resume so that the applicant profile could be expanded beyond a minimal name-and-skills form. The document analysis helped the team identify profile structure, key entities, and non-functional concerns such as consistency, validation, and traceability.

## 3. Requirements and Backlog Formation

The findings from fact-finding were converted into both functional and non-functional requirements. The main functional requirements are that visitors can browse vacancies and vacancy details before login, applicants can then log in, maintain detailed profiles, upload CVs, apply for roles, and view application status; MOs can post vacancies, review applicants, and record outcomes; and admin users can review workload, configure a simple `max_workload` parameter, and later support blacklist management.

The sample CV analysis showed that profile data should include structured academic background, relevant courses, technical skills, TA experience, and project or leadership experience rather than relying only on a CV file. The later teaching-staff review also showed that login should not be the entry barrier for browsing vacancies, so the public browsing interface and login-gated actions were added explicitly to the backlog.

The team grouped related requirements into epics and then decomposed them into user stories. **Applicant Profile and Application** includes public vacancy browsing, profile maintenance, CV upload, login, application, and status tracking. **Vacancy Posting and Applicant Review** includes vacancy publication, applicant review, and decision recording. **Admin Workload Monitoring** includes workload overview, limit control, and later blacklist handling. Lower-priority enhancement stories were also added for skill matching, missing-skill identification, and workload-balancing suggestions.

Each story was written using the form **"As a..., I want..., so that..."** and linked to supporting acceptance criteria. Acceptance criteria reduce ambiguity and create a clearer link between requirements, implementation, and later testing.

## 4. Prioritisation and Estimation

The backlog was prioritised using **MoSCoW**. **Must** stories define the minimum system needed to deliver the core recruitment workflow. **Should** stories improve usability, policy control, or management visibility but can be delayed if time becomes limited. **Could** stories are enhancements and are therefore placed later in the plan.

Prioritisation was based on business value, implementation feasibility, delivery risk, and the staged assessment schedule. Stories such as public vacancy browsing, login, structured profile management, vacancy publishing, applying, workload-limit enforcement, applicant review, outcome recording, and workload viewing were marked as **Must** because they define the end-to-end value of the system. Stories such as blacklist handling and optional feedback were kept below the first release because they are useful but not strictly necessary for the earliest usable version. Low-priority AI-assisted features were placed in the `Could` category so they remain visible without threatening the core delivery.

The team used **story points** for estimation with the scale `1, 2, 3, 5, 8`. Lower values were used for relatively simple interface or validation work, while larger values were assigned to stories involving multiple views, file operations, or coordination across components.

## 5. Iteration Planning

The project is planned across four iterations. **Sprint 1** focuses on fact-finding, backlog definition, prototype preparation, and production of the first assessment deliverables. **Sprint 2** targets the first working version of the core recruitment workflow, including public vacancy browsing, login, profile handling, application, organiser review, and configurable workload-limit control. **Sprint 3** strengthens quality through improved administration support, blacklist handling, error handling, refactoring, and broader testing. **Sprint 4** is reserved for final hardening and optional explainable features such as skill matching or workload-balancing suggestions if the core system is already stable.

For the first assessment, the team is not attempting to implement the full backlog. The immediate priority is to produce a coherent set of requirements, a consistent product backlog, an aligned prototype, and a brief report supported by evidence.

## Supporting Materials

The following items should be attached outside the main 5-page limit:
- interview questions;
- interview records or workshop notes;
- workflow and scope notes;
- sample CV analysis or feedback notes used to refine requirements;
- prototype feedback evidence;
- backlog and GitHub management screenshots.
