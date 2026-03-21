# Appendix for First Assessment

**Group:** `Group 27`  
**Project:** `TA Recruitment System`

## A1. Stakeholder Summary

| Stakeholder | Main goals | Main concerns |
|---|---|---|
| TA Applicant | Browse vacancies easily, log in only when needed, keep one reusable profile, apply efficiently, track progress | Missing information, repeated data entry, unclear outcome visibility, unclear role-limit rules |
| Module Organiser | Post jobs, review applicants, record decisions | Slow screening, weak traceability, inconsistent data, manual workload and blacklist checking |
| Admin | Monitor workload, control policy parameters, later manage blacklist | Over-allocation, poor visibility across jobs, manual checking effort |

## A2. Interview Questions and Review Points Used

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

### Later Teaching-Staff Feedback and CV Review
15. Which profile fields should be structured instead of left only in the CV?
16. Should login be represented explicitly in the backlog?
17. Should the system enforce a configurable maximum number of roles per applicant from the start?
18. Should blacklist support be included for unsuitable applicants?
19. Which low-priority AI-assisted features should still be recorded in the backlog?
20. Should applicants be able to browse vacancies before login with a visible login entry?

## A3. Summary of Findings

### Applicant findings
- Applicants need a clear job list with module, skills, and deadline.
- Applicants should be able to browse vacancies and vacancy details before logging in.
- The public browsing interface should keep a visible `Log In` entry so the user can move into authenticated features when needed.
- After reviewing a sample CV, the team expanded the profile to include identity, contact details, academic background, relevant courses, technical skills, TA experience, project or leadership experience, availability, and CV reference.
- Applicants also need login so that their profile and applications remain linked to the correct account.
- Applicants should be able to apply for multiple roles only within the current admin-defined limit.
- Status visibility is important after submission, and applicant-visible application counts may improve transparency.

### MO findings
- MOs need a simple way to publish vacancies and compare applicants.
- Vacancy records should include module-specific skills or prior-course expectations.
- Decision recording should be traceable and easy to update.
- Workload and blacklist information should be visible where they affect review decisions.

### Admin findings
- Admin users need a quick view of workload, not just raw applications.
- A configurable `max_workload` parameter is preferable to relying only on later manual balancing; the initial default can be `3` roles.
- Blacklist handling is a real requirement because unsuitable applicants are currently screened manually.
- Consistent file structure is important because the project cannot use a database.

### Later feedback findings
- Low-priority future ideas should still be captured in the backlog, including skill matching, missing-skill identification, and workload-balancing suggestions.
- The browsing interface should not force login too early; login should be prompted only when the user attempts restricted actions such as applying or opening personal features.
- These points were used to refine the proposed system scope and priorities, not to force the system to reproduce the current process unchanged.

## A4. Scope Notes

### In scope for the early version
- public vacancy browsing before login
- login and protected applicant actions
- detailed applicant profile and CV handling
- job list and vacancy details
- job application and status tracking
- MO job posting and applicant review
- admin workload view and configurable `max_workload` limit

### Later but still in overall project scope
- blacklist support
- optional short outcome feedback
- applicant-visible application counts
- explainable low-priority AI-assisted features

### Out of scope for the early version
- database integration
- external system integration
- automated email sending
- tutorial-support scheduling after recruitment
- complex black-box AI-assisted decision support

## A5. Prototype Feedback Record

| Feedback source | Observation | Planned change |
|---|---|---|
| Student reviewer 1 | Job list should show deadline clearly | Add deadline to job cards |
| Student reviewer 2 | Application status needs clearer labels | Use Submitted / Offered / Unsuccessful consistently |
| Student reviewer 3 | Workload screen should highlight overload | Add visual flag for high workload |
| Teaching-staff review | Profile is too vague and should be more structured | Expand reusable profile fields in requirements and backlog |
| Teaching-staff review | Login and max workload control are missing from the backlog | Add explicit stories for login and admin-configurable `max_workload` |
| Teaching-staff review | Applicants should browse vacancies before login and log in only when trying to act | Add visitor-style vacancy browsing with a visible login entry and login prompts for restricted actions |

## A6. Backlog and Planning Evidence

- Product backlog created in the course Excel template
- Stories written in the format “As a..., I want..., so that...”
- Acceptance criteria added to each story
- MoSCoW used for prioritisation
- Story points used for estimation
- Stories revised after the follow-up TA interview, later teaching-staff feedback, and sample CV analysis
- Low-priority AI-assisted ideas recorded explicitly as `Could` stories rather than left undocumented

## A7. GitHub Evidence to Attach

- Screenshot of repository home page
- Screenshot of milestones
- Screenshot of issues for the first assessment
- Screenshot of branch or pull request activity
- Screenshot of meeting notes or decision log committed to the repository
- Screenshot or note of the later requirements review feedback
