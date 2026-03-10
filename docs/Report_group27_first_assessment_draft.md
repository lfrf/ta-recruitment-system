# EBU6304 Software Engineering Group Project
## First Assessment Brief Report Draft

**Group:** `Group 27`  
**Project:** `TA Recruitment System`  
**Module:** `EBU6304 Software Engineering`

## 1. Project Context and Scope

At present, BUPT International School manages Teaching Assistant recruitment mainly through forms and Excel files. This process works at a basic level, but it becomes inefficient when there are many applicants and vacancies at the same time. It is also difficult to track application progress clearly. Our project aims to improve this situation by developing a simple TA Recruitment System.

The system is designed for three main stakeholder groups. **TA Applicants** need to create profiles, upload CVs, browse available jobs, apply for roles, and check their application status. **Module Organisers (MOs)** need to post vacancies, review applicants, and record recruitment decisions. **Admin users** need to monitor TA workload across different jobs and modules.

For the first release, the team decided to focus only on the core workflow. The main in-scope functions are account and profile management, CV upload, job browsing, job application, status tracking, job posting, applicant review, decision recording, and workload viewing. Out-of-scope items include database integration, third-party services, complex access-control mechanisms, and advanced AI-based features. Keeping the scope limited in this way makes the project more realistic and reduces the risk of overcommitting too early.

To keep implementation simple and manageable, the team plans to develop a **stand-alone Java application** with **text-based storage**, such as JSON or CSV files. This fits the coursework constraints and allows the team to focus more on software engineering process, design, and testing rather than external frameworks.

## 2. Fact-Finding Techniques

The team used three fact-finding techniques introduced in the module: **background reading**, **interviewing**, and **document analysis**.

Background reading was used first to understand the coursework brief, required constraints, and the general recruitment process. This helped the team identify the likely users, the core system functions, and the technical limits that had to be respected before starting backlog work.

Interviewing was used to gather role-specific requirements from the three main stakeholder groups: applicants, Module Organisers, and Admin users. Semi-structured interview questions were prepared for each group. The main purpose was to understand their current problems, the information they need, the actions they need to perform, and what they expect from the system. From this early review, it became clear that applicants mainly want clear job information and a simple way to check their status, MOs want a more efficient way to review applicants and record decisions, and Admin users need better visibility of workload and possible conflicts.

Document analysis was also used to look at the manual workflow and the kinds of data the system would need to handle. This helped the team identify key entities such as applicant profile, vacancy, application, decision, and workload record. It also helped the team think about non-functional issues, especially consistency, traceability, and validation.

By using these three techniques together, the team was able to move from a general problem description to a more focused backlog supported by evidence.

## 3. Requirements and Backlog Formation

The findings from fact-finding were turned into both functional and non-functional requirements. The main functional requirements are that applicants can register, manage their profiles, upload CVs, browse jobs, apply for positions, and check status updates. MOs can post jobs, review applicants, and record outcomes. Admin users can monitor TA workload.

The team also identified several important non-functional requirements. The system must follow the coursework restrictions, use text-based storage, and avoid using a database. It should also be easy to use, provide clear validation messages, reject invalid or duplicate input, and keep decisions traceable. These requirements are important because even if the system has the right features, it still needs to be usable, consistent, and reliable.

To organise the requirements, the team grouped them into epics and then broke them down into user stories. For example, **Applicant Onboarding** includes account registration, profile management, and CV upload. **MO Recruitment** includes job posting, viewing applicants, and recording decisions. This structure helps the team manage the backlog more clearly and makes the system easier to plan across iterations.

Each story was written in the form **“As a..., I want..., so that...”** and supported by acceptance criteria. For example, for the job application story, the application should be stored correctly, the initial status should be set to *Submitted*, and duplicate applications should not be allowed. Writing acceptance criteria in this way reduces ambiguity and creates a clearer link between requirements, implementation, and testing.

## 4. Prioritisation and Estimation

The team used **MoSCoW** to prioritise the backlog. **Must** stories represent the minimum set of features needed to support the core recruitment workflow. **Should** stories improve the usefulness of the system but could be delayed if necessary. **Could** stories are additional enhancements and are therefore planned for later.

Prioritisation was based on business value, feasibility, risk, and the staged structure of the assessments. Stories such as job posting, job browsing, applying for jobs, reviewing applicants, recording decisions, and workload viewing were treated as **Must** because together they form the main value of the system. More advanced explainable matching features were deliberately given lower priority because they are not necessary for the first usable version.

For estimation, the team used **story points** with the scale `1, 2, 3, 5, 8`. Smaller values were used for simpler interface or validation tasks, while larger values were used for stories that involve several components or more coordination between files and functions. Relative estimation was chosen instead of hour-based estimation because there is still uncertainty at this stage of the project.

Overall, this approach helped the team create a backlog that is realistic, prioritised, and easier to justify.

## 5. Iteration Planning

The project is planned across four iterations. **Iteration 1** focuses on fact-finding, backlog definition, prototype preparation, and the first assessment deliverables. **Iteration 2** focuses on building the first working version of the core recruitment workflow. **Iteration 3** will improve quality through better error handling, workload-related improvements, refactoring, and broader testing. **Iteration 4** is mainly reserved for final improvements and optional explainable features if the core system is already stable.

For the first assessment, the team is not trying to implement the full system. Instead, the current priority is to produce a complete product backlog, a consistent prototype, and a short report supported by evidence. This gives the team a clearer foundation before moving into implementation.

GitHub is being used to manage issues, branches, milestones, and contribution evidence. Regular meetings, decision logs, and risk logs are also being used to support project management. Overall, the current plan is realistic because it follows the assessment structure, focuses on core features first, and leaves space for later refinement.

## Supporting Materials

The following items should be attached outside the main 5-page limit:

- interview questions;
- interview records or workshop notes;
- workflow and scope notes;
- prototype feedback evidence;
- backlog and GitHub management screenshots.
