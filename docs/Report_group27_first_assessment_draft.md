# EBU6304 Software Engineering Group Project
## First Assessment Brief Report Draft

**Group:** `Group 27`  
**Project:** `TA Recruitment System`  
**Module:** `EBU6304 Software Engineering`

## 1. Project Context and Scope

BUPT International School currently recruits Teaching Assistants through forms and Excel files. This process is slow, difficult to track, and weak in status visibility when several applicants and vacancies are handled at the same time. Our project addresses this problem by developing a simple TA Recruitment System.

The system serves three main stakeholder groups. **TA Applicants** need to create profiles, upload CVs, browse jobs, apply for roles, and check application status. **Module Organisers (MOs)** need to post jobs, review candidates, and record decisions. **Admin users** need to monitor TA workload across jobs and modules.

The planned first release is intentionally limited to the core workflow. In scope are account and profile management, CV upload, job browsing, job application, application status tracking, job posting, applicant review, decision recording, and workload viewing. Out of scope are database integration, external services, complex access-control frameworks, and advanced AI features. This boundary keeps the project aligned with the coursework brief and reduces delivery risk.

To keep implementation risk low, the team plans to develop a **stand-alone Java application** with **text-based storage** such as JSON or CSV. This choice fits the module constraints, avoids unnecessary framework overhead, and keeps attention on software engineering process, design, and testing.

## 2. Fact-Finding Techniques

The team used three fact-finding techniques introduced in the module: **background reading**, **interviewing**, and **document analysis**.

Background reading was used to understand the project brief, mandatory constraints, and the high-level recruitment workflow. This helped the team identify the likely users, core functions, and delivery restrictions before writing any stories.

Interviewing was used in the form of a team requirements workshop and role-based discussion. Semi-structured questions were prepared for applicants, MOs, and Admin users, and all six team members contributed to the discussion and review of the findings. The interviews focused on pain points, required information, important actions, and expected outputs. The main findings were that applicants care most about clear job information and status visibility, MOs care most about efficient review and decision recording, and Admin users care most about workload visibility and consistency.

Document analysis was used to examine the manual workflow and the likely data items required by the system. This helped identify key entities such as applicant profile, vacancy, application, decision, and workload record. It also supported the identification of non-functional concerns, especially consistency, traceability, and validation.

Using the three techniques together allowed the team to move from a high-level problem statement to a more focused and evidence-based backlog.

## 3. Requirements and Backlog Formation

The fact-finding results were converted into functional and non-functional requirements. The main functional requirements are that applicants can register, maintain profiles, upload CVs, browse jobs, apply, and view status; MOs can post jobs, review candidates, and record outcomes; and Admin users can monitor TA workload.

Key non-functional requirements were also identified. The system must comply with the coursework restrictions, use text-based storage, and avoid a database. It should be easy to use, provide clear validation messages, reject invalid or duplicate input, and keep decisions traceable. These requirements are important because a feature-complete system is still weak if it is confusing, inconsistent, or hard to justify.

The team organised the requirements into epics and then decomposed them into user stories. For example, **Applicant Onboarding** includes registering an account, maintaining a profile, and uploading a CV. **MO Recruitment** includes posting a job, viewing applicants, and recording decisions. This structure makes large requirements easier to estimate, prioritise, and allocate across iterations.

Each story was written in the form **"As a..., I want..., so that..."** and supported by acceptance criteria. For example, the job application story requires the application to be stored correctly, the initial status to be set to *Submitted*, and duplicate applications to be prevented. Acceptance criteria reduce ambiguity and create a clear link between requirements, implementation, and later testing.

## 4. Prioritisation and Estimation

The backlog was prioritised using **MoSCoW**. **Must** stories define the minimum system needed to deliver the core recruitment workflow. **Should** stories improve usefulness but can be delayed if time becomes tight. **Could** stories are enhancements and are therefore placed later.

Prioritisation was based on four factors: business value, feasibility, risk, and the staged assessment schedule. Stories such as job posting, job browsing, applying, reviewing, decision recording, and workload viewing were marked as **Must** because they form the end-to-end value of the system. Optional explainable matching features were intentionally delayed because they do not define the basic usefulness of the product.

The team used **story points** for estimation with the scale `1, 2, 3, 5, 8`. Small values were used for simple interface or validation work, while larger values were used for stories that coordinate several components or data files. Relative estimation was preferred to hour-based estimation because uncertainty is still high at this stage.

This approach produced a backlog that is both realistic and traceable. Instead of trying to implement everything at once, the team focused on the minimum set of stories needed to support incremental delivery.

## 5. Iteration Planning

The project is planned across four iterations. **Iteration 1** focuses on fact-finding, backlog definition, prototype production, and preparation of the first assessment deliverables. **Iteration 2** targets the first working version of the core recruitment workflow. **Iteration 3** strengthens quality through error handling, workload improvements, refactoring, and broader testing. **Iteration 4** is reserved for final hardening and optional explainable enhancements if the core system is already stable.

For the first assessment, the team is not attempting to implement the full backlog. The priority is to produce a complete product backlog, a consistent prototype, and a brief report supported by evidence. This is followed by a small but coherent software scope for the next iteration.

GitHub is being used to track issues, milestones, branches, and contribution evidence. Regular meetings, decision logs, and risk logs are used to support Agile project management. Overall, the plan is realistic because it matches the assessment structure, prioritises core value first, and leaves room for refinement after feedback.

## Supporting Materials

The following items should be attached outside the main 5-page limit:

- interview questions;
- interview records or workshop notes;
- workflow and scope notes;
- prototype feedback evidence;
- backlog and GitHub management screenshots.
