# EBU6304 Software Engineering Group Project
## First Assessment Brief Report Draft

**Group:** `Group XXX`  
**Project:** `TA Recruitment System`  
**Module:** `EBU6304 Software Engineering`

## 1. Project Context and Scope

BUPT International School currently recruits Teaching Assistants (TAs) through a largely manual process based on forms and Excel files. This approach makes the workflow difficult to track, time-consuming to manage, and prone to inconsistency when multiple applicants and vacancies are handled at the same time. In response to this problem, our team is developing a TA Recruitment System to support the recruitment workflow in a more structured and transparent way.

The proposed system focuses on three main stakeholder groups. The first stakeholder group is the **TA Applicant**, who needs to create a profile, upload supporting information, browse available jobs, apply for suitable roles, and monitor application progress. The second stakeholder group is the **Module Organiser (MO)**, who needs to create vacancies, review applicants, and record recruitment decisions. The third stakeholder group is the **Admin**, who needs to monitor the overall workload of TAs and identify potential allocation issues.

The scope of the first release is intentionally limited to the core business workflow. In scope are applicant account and profile management, CV upload, job browsing, job application, application status tracking, vacancy posting, applicant review, decision recording, and TA workload viewing. Out of scope for the current project phase are database integration, external platform integration, complex role-permission frameworks, and advanced AI features that cannot be clearly explained or justified. This scope decision was made to keep the project aligned with the module requirements and to ensure that the team can deliver a coherent and testable solution within the assessment timeline.

The system will be developed under the constraints defined in the handout. It will be implemented either as a stand-alone Java application or as a lightweight Java Servlet/JSP web application, depending on the final team decision. In either case, all persistent data will be stored in simple text-based file formats such as JSON or CSV, and no database will be used. These constraints directly influenced our design decisions, backlog planning, and feasibility analysis.

## 2. Fact-Finding Techniques

To identify requirements at an appropriate level of detail, the team used a combination of **background reading**, **interviewing**, and **document analysis**. These techniques were selected because they are consistent with the methods introduced in the module and because they provide complementary perspectives on the problem domain.

Background reading was used first to establish a high-level understanding of the project context. The team reviewed the project handout, the coursework constraints, and the expected recruitment workflow described in the coursework brief. This helped the team understand the purpose of the system, the mandatory technology restrictions, and the likely stakeholder roles. Background reading was also useful for identifying the initial functional areas of the system, such as applicant management, vacancy management, and workload monitoring.

Interviewing was used to capture stakeholder needs and workflow pain points in more detail. Semi-structured questions were prepared for the three main stakeholder roles: TA applicants, Module Organisers, and Admin users. The goal of the interviews was to identify which information each role needs, which actions are most important, and which problems are currently caused by the manual process. For example, applicants are likely to value a clear overview of available jobs and application status, while Module Organisers are more concerned with efficiently reviewing candidates and making traceable decisions. Admin users are more likely to focus on fairness of TA allocation and visibility of workload across modules. The interview results also helped the team identify non-functional requirements such as usability, clarity of validation messages, and the need for transparent decision records.

Document analysis was used to understand the existing structure of the recruitment workflow and the likely data items required by the system. By analysing the current manual process, the team identified key information entities such as applicant details, vacancy details, application status, review outcome, and workload summary. Document analysis also supported the identification of constraints and validation needs, for example the need to keep records consistent across users and the importance of preserving traceable decisions.

These three fact-finding techniques were used together rather than in isolation. Background reading established the context, interviewing revealed stakeholder priorities, and document analysis clarified the data and workflow structure. As a result, the team was able to define a focused project scope and convert high-level needs into a product backlog that reflects both business value and feasibility.

## 3. Requirements and Backlog Formation

The findings from fact-finding were transformed into both functional and non-functional requirements. The main **functional requirements** identified so far are:

- the applicant shall be able to register and maintain a profile;
- the applicant shall be able to upload a CV;
- the applicant shall be able to browse and apply for jobs;
- the applicant shall be able to view application status;
- the MO shall be able to create and manage job posts;
- the MO shall be able to review applicants and record decisions;
- the admin shall be able to monitor TA workload.

In addition to these functional requirements, the team also identified key **non-functional requirements**. First, the system must remain compatible with the coursework restrictions, which means that it must use Java-based implementation and text-based file storage rather than a database. Second, the system should be easy to use for its intended users, with clear navigation and understandable validation messages. Third, the system should preserve data consistency by rejecting invalid or duplicate input. Fourth, the system should provide traceable results, especially when decisions such as acceptance or rejection are made. These non-functional requirements are important because they affect the practical usefulness of the system even when the functional features are present.

After the initial requirements were identified, they were organised into epics and then decomposed into user stories. For example, the epic **Applicant Onboarding** was broken down into stories such as registering an account, maintaining a profile, and uploading a CV. The epic **MO Recruitment** was broken down into posting a job, viewing applicants, and accepting or rejecting an applicant. This breakdown was useful because it allowed the team to transform broad requirements into smaller, implementable items that can be estimated and assigned across iterations.

Each user story was written in the Agile format **“As a…, I want…, so that…”**. Acceptance criteria were then added to each story in order to clarify the expected outcome and reduce ambiguity. For example, a story such as “As a TA applicant, I want to apply for a job so that I can be considered for the role” includes acceptance criteria requiring the application to be stored correctly, the status to start as *Submitted*, and duplicate applications to be handled appropriately. This use of acceptance criteria improves traceability between stakeholder needs, backlog planning, and later testing activities.

## 4. Prioritisation and Estimation

The team used the **MoSCoW** method to prioritise backlog items. This method was selected because it provides a simple but effective way to classify stories according to their business value and delivery importance. Stories classified as **Must** are required for the system to deliver its core value. Stories classified as **Should** are important but can be postponed if necessary. Stories classified as **Could** add value but are not essential for the success of the initial releases.

The prioritisation decision was based on four factors: business value, feasibility, risk, and the staged assessment timeline. For example, stories such as browsing jobs, posting jobs, applying for a job, reviewing applicants, and viewing workload were prioritised as **Must** because together they form the central recruitment workflow. In contrast, explainable skill matching and missing-skill suggestions were deliberately placed in a later iteration as **Could** items, since they are enhancements rather than prerequisites for the system to function.

The team used **story points** to estimate the relative size of backlog items. A simplified Fibonacci-style scale of `1, 2, 3, 5, 8` was adopted. Smaller values were assigned to simple interface or validation tasks, while larger values were used for stories involving coordination across multiple components, such as applying for a job or calculating workload. Story points were preferred over fixed time estimates because they better reflect uncertainty, complexity, and implementation effort at this stage of the project.

This prioritisation and estimation process helped the team create a realistic release plan. Instead of attempting to implement every desired feature immediately, the team focused first on the minimum set of stories needed to demonstrate a complete end-to-end workflow. This is consistent with Agile principles and reduces the risk of over-scoping the project before the first assessment.

## 5. Iteration Planning

The project was divided into four planned iterations. **Iteration 1** focuses on project initiation, fact-finding, backlog definition, prototype production, and preparation of the first assessment package. The main deliverables in this iteration are the product backlog, the prototype, and the brief report. Some implementation preparation is also expected, such as repository setup, early design discussion, and initial project structure.

**Iteration 2** is planned to deliver the core working software. The focus will be on the central recruitment workflow: MOs posting jobs, applicants browsing and applying for jobs, MOs reviewing applications, applicants viewing decisions, and Admin users monitoring workload. This iteration is intended to support the intermediate assessment by ensuring that the project demonstrates visible software progress rather than documentation only.

**Iteration 3** is planned to strengthen quality and maintainability. At this stage, the team expects to improve error handling, extend admin and workload functions, refine the user interface, and expand testing. This iteration also creates time for refactoring, documentation updates, and addressing issues found during earlier demonstrations.

**Iteration 4** is reserved for final hardening and optional enhancements. If the core system is stable, the team may implement an explainable skills-matching feature or missing-skill suggestion function. However, such features will only be included if they can be justified clearly and do not threaten delivery of the core assessment requirements. The main objective of the final iteration is to prepare a reliable, testable, and well-documented final product.

To support iteration planning, the team is using GitHub to track issues, milestones, and contributions. Regular meetings, branch-based development, and documented decisions are being used to provide visible evidence of Agile project management. This process is intended to ensure that progress can be reviewed incrementally and that feedback can be incorporated into later iterations. Overall, the current plan is realistic because it aligns the system scope with the assessment structure, prioritises the most valuable features first, and leaves room for quality improvement before final delivery.

## Supporting Materials

The following materials should be attached outside the main 5-page limit:

- interview questions;
- interview records or workshop notes;
- scope and workflow notes;
- prototype feedback evidence;
- screenshots of backlog planning or GitHub project management evidence.
