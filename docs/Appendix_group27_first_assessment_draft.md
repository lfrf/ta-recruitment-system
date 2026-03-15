# Appendix for First Assessment

**Group:** `Group 27`  
**Project:** `TA Recruitment System`

## A1. Stakeholder Summary

| Stakeholder | Main goals | Main concerns |
|---|---|---|
| TA Applicant | Find suitable jobs, submit applications, track progress | Missing information, repeated data entry, unclear outcome visibility |
| Module Organiser | Post jobs, review applicants, record decisions | Slow screening, weak traceability, inconsistent data |
| Admin | Monitor accepted TA allocation and workload | Over-allocation, poor visibility across jobs, manual checking effort |

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

### Follow-Up TA Interview
15. How are TA vacancies usually announced to students?
16. What happens after a student submits an application?
17. Are detailed rejection reasons usually provided?
18. Which information is most important for deciding whether a TA is suitable?
19. Which parts of the later teaching arrangement should stay outside the early system scope?

## A3. Summary of Findings

### Applicant findings
- Applicants need a clear job list with module, skills, workload, and deadline.
- Applicants want one place to store profile and CV information.
- Status visibility is important after submission, and lightweight optional feedback may be useful even though detailed rejection reasons are not essential for the early version.
- Seeing how many people have applied for a vacancy may also improve transparency for applicants.

### MO findings
- MOs need a simple way to publish vacancies and compare applicants.
- Vacancy records should include module-specific skills or prior-course expectations.
- Decision recording should be traceable and easy to update, and optional review notes may improve transparency.

### Admin findings
- Admin users need a quick view of accepted workload, not just raw applications.
- Overload should be visible without checking each module separately.
- Consistent file structure is important because the project cannot use a database.

### Follow-up TA interview findings
- Vacancies are commonly announced through the Information Portal, QR-code forms, or course groups.
- Successful applicants are commonly informed by email, but automated email sending is not required for the early system.
- Detailed rejection reasons are usually not given in the current process.
- Recruitment usually finishes before the semester starts, while later tutorial-support arrangements are handled by the MO or TA lead.
- A useful definition of workload is visibility of which modules or roles a TA has already accepted.
- These points were used to refine the proposed system scope and priorities, not to force the system to reproduce the current process unchanged.
- The interview also suggested that applicant-visible application counts could be a useful transparency feature.

## A4. Scope Notes

### In scope
- Applicant profile with reusable skills information
- CV upload
- Job list and vacancy details
- Job application and status tracking
- MO job posting and applicant review
- Admin workload view based on accepted assignments

### Out of scope
- Database integration
- External system integration
- Automated email sending
- Mandatory detailed rejection feedback to every applicant
- Tutorial-support scheduling after recruitment
- Advanced AI-based decision support

## A5. Prototype Feedback Record

| Feedback source | Observation | Planned change |
|---|---|---|
| Student reviewer 1 | Job list should show deadline clearly | Add deadline to job cards |
| Student reviewer 2 | Application status needs clearer labels | Use Submitted / Offered / Unsuccessful consistently |
| Student reviewer 3 | Workload screen should highlight overload | Add visual flag for high workload |

## A6. Backlog and Planning Evidence

- Product backlog created in the course Excel template
- Stories written in the format "As a..., I want..., so that..."
- Acceptance criteria added to each story
- MoSCoW used for prioritisation
- Story points used for estimation
- Stories revised after the follow-up TA interview to remove unnecessary scope and clarify real workflow assumptions

## A7. GitHub Evidence to Attach

- Screenshot of repository home page
- Screenshot of milestones
- Screenshot of issues for the first assessment
- Screenshot of branch or pull request activity
- Screenshot of meeting notes or decision log committed to the repository
- Screenshot or record of the follow-up TA interview evidence
