# Appendix for First Assessment

**Group:** `Group 27`  
**Project:** `TA Recruitment System`

## A1. Stakeholder Summary

| Stakeholder | Main goals | Main concerns |
|---|---|---|
| TA Applicant | Find suitable jobs, submit applications, track progress | Missing information, repeated data entry, unclear status |
| Module Organiser | Post jobs, review applicants, record decisions | Slow screening, weak traceability, inconsistent data |
| Admin | Monitor TA allocation and workload | Over-allocation, poor visibility across jobs, manual checking effort |

## A2. Interview Questions Used

### TA Applicant
1. How do you currently find TA opportunities?
2. Which part of the application process is most inconvenient?
3. What information should appear in an applicant profile?
4. Which job details matter most before you apply?
5. How would you like to check application progress?

### Module Organiser
6. What information do you need when posting a TA role?
7. Which criteria do you use to review applicants?
8. How do you currently track applicant decisions?
9. Do you need to see an applicant's existing workload?
10. What would make the review process more efficient?

### Admin
11. How do you currently monitor TA workload?
12. When do you consider a TA overloaded?
13. What summary information is most useful for management?
14. Which failures in the current process cause the biggest problem?

## A3. Summary of Findings

### Applicant findings
- Applicants need a clear job list with module, skills, and deadline.
- Applicants want one place to store profile and CV information.
- Status visibility is important after submission.

### MO findings
- MOs need a simple way to publish vacancies and compare applicants.
- Decision recording should be traceable and easy to update.
- Reviewing applicants is harder when profile, CV, and status are split across files.

### Admin findings
- Admin users need a quick view of accepted workload, not just raw applications.
- Overload should be visible without checking each module separately.
- Consistent file structure is important because the project cannot use a database.

## A4. Scope Notes

### In scope
- Applicant registration and profile
- CV upload
- Job list and job details
- Job application and status tracking
- MO job posting and applicant review
- Admin workload view

### Out of scope
- Database integration
- External system integration
- Advanced role-permission framework
- Unexplainable AI-based decision support

## A5. Prototype Feedback Record

| Feedback source | Observation | Planned change |
|---|---|---|
| Student reviewer 1 | Job list should show deadline clearly | Add deadline to job cards |
| Student reviewer 2 | Application status needs clearer labels | Use Submitted / Accepted / Rejected consistently |
| Student reviewer 3 | Workload screen should highlight overload | Add visual flag for high workload |

## A6. Backlog and Planning Evidence

- Product backlog created in the course Excel template
- Stories written in the format “As a..., I want..., so that...”
- Acceptance criteria added to each story
- MoSCoW used for prioritisation
- Story points used for estimation
- Stories allocated across four iterations

## A7. GitHub Evidence to Attach

- Screenshot of repository home page
- Screenshot of milestones
- Screenshot of issues for the first assessment
- Screenshot of branch or pull request activity
- Screenshot of meeting notes or decision log committed to the repository

## A8. User Story Collaboration Evidence

The team treated user story creation as a collaborative refinement activity rather than as a single-author task. Stakeholder findings were first gathered from workshop discussion and interview preparation, then converted into backlog items through group discussion.

Different members contributed to the user stories in different ways:
- Yuzhang Wu contributed stakeholder evidence and proposed applicant- and admin-related stories.
- Chensiyuan Qing structured the backlog and converted requirement statements into standard user story wording.
- Fuhe Huang checked whether stories aligned with the intended interface and page flow.
- Mu Du contributed validation, data consistency, reliability, and workload-risk stories from an implementation and testing perspective.
- Tao Li refined wording and alignment with the main report and appendix.
- Yifu Feng reviewed scope, priority, and consistency with overall project coordination.

The final backlog therefore represents shared team understanding. Individual stories may have had different initial proposers, but wording, merging, prioritisation, and backlog structure were agreed collaboratively.
