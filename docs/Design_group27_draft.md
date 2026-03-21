# Group 27 Design Draft

## 1. Why we wrote this design
This document explains **how the current TA Recruitment System is planned to be built**.

For the first assessment, design is not the main report requirement, so this is only a lightweight draft. Its purpose is to help the team prepare for later coding by making the planned structure clear and easy to understand.

## 2. Main design choice
The team plans to build the system as a **stand-alone Java application** with **text-based file storage**.

This choice fits the coursework constraints and keeps the project simple. It avoids database setup and lets the team focus on software engineering process, structure, and incremental delivery.

## 3. Proposed system structure
The simplest useful structure for this project is:

```text
UI layer
  -> screens or pages for Applicant, MO, and Admin

Controller layer
  -> handles actions such as apply, review, publish vacancy, and check workload

Model layer
  -> stores core objects such as profile, vacancy, application, and note

File storage layer
  -> reads and writes JSON or CSV files
```

This structure is suitable because:
- the interface can change without rewriting all business logic;
- file handling is kept away from the UI;
- the system stays modular and easier to test.

## 4. Main modules we expect to build
| Module | What it should do |
|---|---|
| Profile module | create and update applicant profiles |
| Vacancy module | create, publish, list, and view vacancies |
| Application module | submit applications and display status |
| Review module | let organisers inspect applications and record outcomes |
| Workload module | calculate and display offered or accepted commitments |
| Validation module | check missing fields, duplicate applications, and invalid data |
| File storage module | save and load the system data |

## 5. Data files we expect to use
A simple design is to keep the main data in three files:
- `applicants.json`
- `vacancies.json`
- `applications.json`

These files are enough for the current scope.

Their responsibilities are:
- applicant file: profile, skills, experience, CV reference;
- vacancy file: module, duties, requirements, deadline, workload;
- application file: applicant-vacancy link, status, and optional note or feedback.

Workload should be calculated from application outcomes rather than stored as a completely separate manual table.

## 6. Important design decisions
### 6.1 Consistent status values
The same status words should be used everywhere:
- `Submitted`
- `Offered`
- `Unsuccessful`

This avoids confusion between prototype, report, data files, and code.

### 6.2 Duplicate application rule
A student should not be able to apply for the same vacancy twice.

The system can check this using the pair:
- `applicantId`
- `vacancyId`

### 6.3 Optional transparency features
The design also allows some lightweight improvements:
- optional review notes from organisers;
- optional outcome feedback shown to applicants;
- applicant-visible number of current applications for a vacancy.

These are useful additions, but they should not make the system unnecessarily complex.

## 7. Why this design is reasonable
This design follows the ideas from the course in a practical way:
- it meets the current requirements;
- it keeps the system modular;
- it is easy to explain and implement;
- it stays within the no-database constraint;
- it keeps early recruitment work separate from later scheduling work.

In short, it is a small and realistic design that gives the team a clear starting point for implementation.

## 8. Simple implementation skeleton
A practical folder structure could be:

```text
src/
  model/
  controller/
  repository/
  ui/
  validation/
  util/
data/
```

This is enough to begin coding without over-designing the project.
