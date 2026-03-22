# Group 27 Analysis Draft

## 1. Why we wrote this analysis
This analysis is a simple internal description of **what the main parts of the TA Recruitment System are** and **how the core recruitment flow works**.

For the first assessment, analysis is not the main focus of the report, so this document is kept short. Its purpose is to help the team move from requirements and prototype into later design and implementation.

## 2. Main things in the system
At the current stage, the system mainly revolves around five core objects.

| Object | Meaning in this project |
|---|---|
| ApplicantProfile | The reusable profile of a student, including skills, experience, and CV reference |
| Vacancy | A TA job posted for a specific module |
| Application | A student's application for one vacancy |
| ReviewNote | An optional note or short feedback written by the organiser |
| WorkloadRecord | A summary of which roles a student has already been offered or accepted |

These five objects are enough to describe the early recruitment workflow without expanding into later scheduling work.

## 3. How these objects are related
The most important object is **Application**, because it connects applicants, vacancies, review results, and workload.

The main relationships are:
- one applicant can submit multiple applications;
- one vacancy can receive multiple applications;
- each application belongs to one applicant and one vacancy;
- a review note is optional and belongs to one application;
- workload information is derived from application outcomes rather than stored separately as a completely independent process.

This means the system is not just a list of students or a list of jobs. It is mainly a system for managing the relationship between **people**, **vacancies**, and **recruitment outcomes**.

## 4. Main user views in the system
From the current requirements, the system has three main stakeholder views:
- `Applicant`: maintains profile, views vacancies, applies, and checks status;
- `Module Organiser`: posts vacancies, reviews applicants, and records outcomes;
- `Admin`: checks workload across students and modules.

These views help the team understand what information needs to be shown and which parts of the system each role should interact with.

## 5. Core workflow analysis
### 5.1 Applicant applies for a vacancy
1. The applicant maintains a reusable profile.
2. The applicant browses vacancies.
3. The applicant opens vacancy details and checks requirements, deadline, and current applicant count.
4. The system creates an application with status `Submitted`.
5. Later, the applicant checks whether the result is still `Submitted`, has become `Offered`, or is `Unsuccessful`.

### 5.2 Module organiser reviews an application
1. The organiser posts a vacancy.
2. The organiser views applicants for that vacancy.
3. The organiser reviews the applicant's profile and CV reference.
4. The organiser records an outcome.
5. The organiser may also add an optional review note.

### 5.3 Admin checks workload
1. The admin views offered or accepted commitments.
2. The system highlights possible overload or conflict risk.
3. The admin can use this information to identify students who may already be committed to several roles.

## 6. What is outside this analysis
To keep the project realistic, this analysis does **not** include:
- automatic email sending;
- detailed tutorial scheduling after recruitment;
- TA lead task allocation after recruitment;
- complex AI-based ranking.

These may exist in the real process or in later extensions, but they are not part of the early core system.

## 7. Why this analysis is useful
This analysis helps the team in three practical ways:
- it shows that `Application` is the centre of the system;
- it separates early recruitment from later teaching arrangement work;
- it gives a clear basis for later design, implementation, and testing.

In short, the system can be understood as a small recruitment platform built around **profiles, vacancies, applications, outcomes, and workload visibility**.
