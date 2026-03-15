# EBU6304 Software Engineering Group Project
## First Assessment Brief Report Draft

**Group:** `Group 27`  
**Project:** `TA Recruitment System`  
**Module:** `EBU6304 Software Engineering`

## 1. Project Context and Scope

BUPT International School currently recruits Teaching Assistants through forms, spreadsheets, manual records, and follow-up communication outside a single system. Recent fact-finding also showed that vacancies are often announced through the Information Portal or QR-code forms, and successful applicants are later informed by email. Although this approach is workable when the number of vacancies is small, it becomes inefficient when several modules recruit at the same time. Information is fragmented, application progress is difficult to track, and workload conflicts are not easy to identify. This project therefore aims to design and implement a simple TA Recruitment System that supports the core recruitment workflow in a more structured, visible, and reliable way.

The system is intended to support three main stakeholder groups. **TA Applicants** need to create a reusable profile, upload a CV, browse vacancies, apply for suitable roles, and check the status or outcome of their applications. **Module Organisers (MOs)** need to publish TA roles, review applications efficiently, compare candidates using consistent information, and record outcomes. **Admin users** need to monitor TA workload across modules so that overload or conflict risks can be identified early.

The first release is intentionally limited to the minimum end-to-end recruitment workflow. In scope are applicant profile management, CV upload, vacancy browsing, vacancy detail viewing, job application, application status tracking, job posting, applicant review, decision recording, workload overview, and lightweight organiser notes where useful. Out of scope are database integration, third-party services, complex access-control mechanisms, mandatory detailed rejection feedback for every applicant, automated email integration, and post-recruitment tutorial-support scheduling. Defining this boundary early helps the team keep the project realistic and aligned with the coursework brief.

To reduce technical risk, the team plans to implement the system as a **stand-alone Java application** using **text-based storage** such as JSON or CSV. This approach satisfies the module constraints, keeps the architecture lightweight, and ensures that the coursework remains focused on software engineering process, design, backlog management, and testing rather than on unnecessary infrastructure.

## 2. Fact-Finding Techniques

The team used three complementary fact-finding techniques introduced in the module: **background reading**, **interviewing**, and **document analysis**.

Background reading was used first to understand the coursework brief, required deliverables, technical restrictions, and the likely structure of the recruitment process. This helped the team identify probable stakeholder groups, core system boundaries, and early implementation constraints before drafting any backlog items.

Interviewing was carried out through a team requirements workshop supported by semi-structured interview questions. Separate question sets were prepared for **Applicants**, **Module Organisers**, and **Admin users**. The team later added a short follow-up interview with an experienced TA to validate the real process. This follow-up clarified that vacancy announcements often come from the Information Portal and QR-code forms, module-specific skill requirements matter in practice, successful applicants are commonly informed by email, and detailed timetable allocation happens after recruitment rather than during the initial selection stage. However, these observations were treated as evidence about the current process rather than as direct instructions that the proposed system must reproduce every current practice.

Document analysis was used to examine the current manual workflow and the likely data items required by the proposed system. This helped the team identify key entities such as applicant profile, vacancy, application, review decision, and workload record. It also supported the identification of non-functional concerns, particularly consistency, traceability, validation, and usability.

Using these three techniques together allowed the team to move from a broad problem statement to a more focused, evidence-based set of requirements and backlog items.

## Team Collaboration in Requirements Work

The team approached requirement discovery as a collaborative activity. Although responsibilities were allocated across members for coordination and documentation, the requirements were not treated as the work of one individual alone. Stakeholder perspectives were discussed in a shared workshop, and the resulting user stories were reviewed and refined by the group.

Yuzhang Wu was responsible for initiating the stakeholder fact-finding activity and completing the interview records, which provided part of the evidence base for the next stage. However, the interpretation of stakeholder needs, the identification of priorities, and the drafting of backlog items involved the participation of all team members. This approach is consistent with Agile practice, where the value of a user story depends less on who first writes it and more on who contributes to discussing and refining it.

## 3. Requirements and Backlog Formation

The findings from fact-finding were converted into both functional and non-functional requirements. The main functional requirements are that applicants can maintain profiles, upload CVs, browse vacancies, apply for roles, and view application status; MOs can post vacancies, review applicants, and record outcomes; and admin users can review overall TA workload based on accepted assignments. The follow-up TA interview also showed that module-specific skills or prior course knowledge are important in real recruitment, so these details should appear in applicant profiles and vacancy records.

Important non-functional requirements were also identified. The system must comply with the coursework restrictions, use text-based storage, and avoid the use of a database. It should be simple to use, provide clear validation messages, prevent invalid or duplicate input where appropriate, and keep recruitment decisions traceable. The later TA interview also helped the team avoid unnecessary scope, but the exclusions remained team design decisions rather than automatic copies of the current manual process: full personalised rejection explanations, automated email sending, and tutorial-support scheduling were excluded from the first version, while lightweight optional notes were still considered a reasonable improvement.

The team grouped related requirements into epics and then decomposed them into user stories. For example, **Applicant Profile and Application** includes profile maintenance, CV upload, and application status. **Vacancy Posting and Applicant Review** includes vacancy publication, applicant review, and decision recording. This structure makes the backlog easier to estimate, prioritise, and allocate across iterations.

Each story was written using the form **"As a..., I want..., so that..."** and linked to supporting acceptance criteria. For example, the application status story requires the application to be stored correctly, the current status to be shown consistently, and outcome updates made by the MO to appear in the applicant view. Acceptance criteria help reduce ambiguity and create a clearer link between requirements, implementation, and later testing.

## 4. Prioritisation and Estimation

The backlog was prioritised using **MoSCoW**. **Must** stories define the minimum system needed to deliver the core recruitment workflow. **Should** stories improve usability and management visibility but can be delayed if time becomes limited. **Could** stories are enhancements and are therefore placed later in the plan.

Prioritisation was based on four factors: business value, implementation feasibility, delivery risk, and the staged assessment schedule. Stories such as vacancy publishing, vacancy browsing, applying, applicant review, outcome recording, and workload viewing were marked as **Must** because they define the end-to-end value of the system. The follow-up interview reinforced that these core steps reflect the real process, while later-stage features such as tutorial scheduling or automatic email integration are not necessary for the first usable version.

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
