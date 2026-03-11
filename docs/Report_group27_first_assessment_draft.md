# EBU6304 Software Engineering Group Project
## First Assessment Brief Report Draft

**Group:** `Group 27`  
**Project:** `TA Recruitment System`  
**Module:** `EBU6304 Software Engineering`

## 1. Project Context and Scope

BUPT International School currently recruits Teaching Assistants through forms, spreadsheets, and manually maintained records. Although this approach is manageable when the number of vacancies is small, it becomes inefficient when several modules recruit at the same time. Information is fragmented, application progress is difficult to track, and workload conflicts are not easy to identify. This project therefore aims to design and implement a simple TA Recruitment System that supports the core recruitment workflow in a more structured, visible, and reliable way.

The system is intended to support three main stakeholder groups. **TA Applicants** need to create a reusable profile, upload a CV, browse vacancies, apply for suitable roles, and check the status of their applications. **Module Organisers (MOs)** need to publish TA roles, review applications efficiently, compare candidates using consistent information, and record outcomes. **Admin users** need to monitor TA workload across modules so that overload or conflict risks can be identified early.

The first release is intentionally limited to the minimum end-to-end workflow. In scope are account and profile management, CV upload, vacancy browsing, job application, application status tracking, job posting, applicant review, decision recording, and workload overview. Out of scope are database integration, third-party services, complex access-control mechanisms, and advanced AI-assisted decision support. Defining this boundary early helps the team keep the project realistic and aligned with the coursework brief.

To reduce technical risk, the team plans to implement the system as a **stand-alone Java application** using **text-based storage** such as JSON or CSV. This approach satisfies the module constraints, keeps the architecture lightweight, and ensures that the coursework remains focused on software engineering process, design, backlog management, and testing rather than on unnecessary infrastructure.

## 2. Fact-Finding Techniques

The team used three complementary fact-finding techniques introduced in the module: **background reading**, **interviewing**, and **document analysis**.

Background reading was used first to understand the coursework brief, required deliverables, technical restrictions, and the likely structure of the recruitment process. This helped the team identify probable stakeholder groups, core system boundaries, and early implementation constraints before drafting any backlog items.

Interviewing was carried out through a team requirements workshop supported by semi-structured interview questions. Separate question sets were prepared for **Applicants**, **Module Organisers**, and **Admin users**. The discussion focused on current pain points, important information needs, key tasks, and expected outputs. The main findings were that applicants value clear vacancy information and visible status updates, MOs value efficient applicant review and traceable decision recording, and admin users value workload visibility and consistent records.

Document analysis was used to examine the current manual workflow and the likely data items required by the proposed system. This helped the team identify key entities such as applicant profile, vacancy, application, review decision, and workload record. It also supported the identification of non-functional concerns, particularly consistency, traceability, validation, and usability.

Using these three techniques together allowed the team to move from a broad problem statement to a more focused, evidence-based set of requirements and backlog items.

## 3. Requirements and Backlog Formation

The findings from fact-finding were converted into both functional and non-functional requirements. The main functional requirements are that applicants can register, maintain profiles, upload CVs, browse vacancies, apply for roles, and view application status; MOs can post vacancies, review applicants, and record decisions; and admin users can review overall TA workload.

Important non-functional requirements were also identified. The system must comply with the coursework restrictions, use text-based storage, and avoid the use of a database. It should be simple to use, provide clear validation messages, prevent invalid or duplicate input where appropriate, and keep recruitment decisions traceable. These requirements matter because a feature-complete system is still weak if it is inconsistent, difficult to understand, or hard to justify during testing.

The team grouped related requirements into epics and then decomposed them into user stories. For example, **Applicant Onboarding** includes account creation, profile maintenance, and CV upload. **MO Recruitment** includes job posting, applicant review, and decision recording. This structure makes the backlog easier to estimate, prioritise, and allocate across iterations.

Each story was written using the form **“As a…, I want…, so that…”** and linked to supporting acceptance criteria. For example, the job application story requires the application to be stored correctly, the initial status to be set to *Submitted*, and duplicate applications to be prevented. Acceptance criteria help reduce ambiguity and create a clearer link between requirements, implementation, and later testing.

## 4. Prioritisation and Estimation

The backlog was prioritised using **MoSCoW**. **Must** stories define the minimum system needed to deliver the core recruitment workflow. **Should** stories improve usability and management visibility but can be delayed if time becomes limited. **Could** stories are enhancements and are therefore placed later in the plan.

Prioritisation was based on four factors: business value, implementation feasibility, delivery risk, and the staged assessment schedule. Stories such as job posting, job browsing, applying, applicant review, decision recording, and workload viewing were marked as **Must** because they define the end-to-end value of the system. More advanced explainable or intelligent support features were intentionally delayed because they do not determine whether the first usable version works.

The team used **story points** for estimation with the scale `1, 2, 3, 5, 8`. Lower values were used for relatively simple interface or validation work, while larger values were assigned to stories involving multiple views, file operations, or coordination across components. Relative estimation was preferred over hour-based estimation because uncertainty is still relatively high at this stage of the project.

This produced a backlog that is realistic, incremental, and traceable. Rather than trying to deliver everything at once, the team focused on the minimum set of stories needed to support an initial working version and then planned refinements in later iterations.

## 5. Iteration Planning

The project is planned across four iterations. **Sprint 1** focuses on fact-finding, backlog definition, prototype preparation, and production of the first assessment deliverables. **Sprint 2** targets the first working version of the core recruitment workflow. **Sprint 3** strengthens quality through improved administration support, error handling, refactoring, and broader testing. **Sprint 4** is reserved for final hardening and optional explainable features if the core system is already stable.

For the first assessment, the team is not attempting to implement the full backlog. The immediate priority is to produce a coherent set of requirements, a consistent product backlog, an aligned prototype, and a brief report supported by evidence. This provides a solid foundation for the next implementation-focused stage.

GitHub is being used to manage issues, milestones, branches, and contribution evidence. Regular team meetings, decision notes, and project records support Agile coordination and accountability. Overall, the plan is realistic because it aligns with the assessment structure, prioritises core value first, and leaves room for revision after feedback.

## Supporting Materials

The following items should be attached outside the main 5-page limit:

- interview questions;
- interview records or workshop notes;
- workflow and scope notes;
- prototype feedback evidence;
- backlog and GitHub management screenshots.
