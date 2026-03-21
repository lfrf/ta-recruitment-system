# Prototype Plan for Group 27

## Recommended prototype level
- Use `medium-fidelity`.
- Make it clickable if possible.
- Focus on workflow clarity, not visual polish.

## Main user flows to cover
- `Applicant logs in -> browses jobs -> views details -> applies within the current limit -> checks status`
- `MO posts a job -> reviews applicants -> sees profile, CV, and workload context -> records outcome`
- `Admin reviews workload -> updates max_workload -> manages blacklist entries`

## Page list

### P01 Login
- Purpose: entry point for all users
- Main elements:
  - email or username field
  - password field
  - login button
  - register button
  - error message area
- Linked stories:
  - `US24`

### P02 Applicant Registration
- Purpose: create a new applicant account
- Main elements:
  - full name
  - student ID
  - email
  - password
  - confirm password
  - register button
  - cancel button
- Linked stories:
  - `US01`
  - `US24`

### P03 Applicant Dashboard
- Purpose: central navigation page for applicants
- Main elements:
  - profile shortcut
  - uploaded CV status
  - current application count
  - current max_workload limit indicator
  - browse jobs button
  - application status button
  - logout button
- Linked stories:
  - `US01`
  - `US02`
  - `US03`
  - `US06`
  - `US07`
  - `US24`

### P04 Applicant Profile
- Purpose: create or edit applicant information
- Main elements:
  - full name
  - student ID
  - email
  - phone number
  - degree or programme
  - year of study
  - relevant modules or grades
  - technical skills and tools
  - previous TA experience
  - project or leadership experience
  - availability
  - upload CV button
  - save button
- Linked stories:
  - `US01`
  - `US02`

### P05 Job List
- Purpose: browse open vacancies
- Main elements:
  - job cards or table
  - module name
  - deadline
  - required skills
  - expected workload
  - view details button
- Linked stories:
  - `US03`
  - `US06`

### P06 Job Details
- Purpose: allow the applicant to decide whether to apply
- Main elements:
  - job title
  - module
  - description
  - required skills
  - workload
  - deadline
  - current applicant count
  - current application-limit note
  - apply button
  - back to list button
- Linked stories:
  - `US04`
  - `US05`
  - `US06`
  - `US23`

### P07 Application Status
- Purpose: show the applicant's application history and current status
- Main elements:
  - application list
  - status labels: `Submitted`, `Offered`, `Unsuccessful`
  - optional short feedback area
  - last updated date
- Linked stories:
  - `US07`
  - `US22`

### P08 MO Dashboard
- Purpose: central page for Module Organisers
- Main elements:
  - post job button
  - current job list
  - applicant review shortcut
  - logout button
- Linked stories:
  - `US09`
  - `US10`

### P09 Post Job Form
- Purpose: create a new TA vacancy
- Main elements:
  - job title
  - module
  - description
  - required skills
  - expected workload
  - deadline
  - post button
  - cancel button
- Linked stories:
  - `US09`

### P10 Applicant Review Page
- Purpose: review candidates for one job
- Main elements:
  - job summary
  - applicant list
  - applicant profile preview
  - CV link or view button
  - current role count and workload summary
  - optional blacklist warning
  - offer button
  - mark unsuccessful button
  - notes field
- Linked stories:
  - `US10`
  - `US11`
  - `US12`
  - `US13`
  - `US21`

### P11 Admin Workload Dashboard
- Purpose: show accepted workload across TAs and control workload policy
- Main elements:
  - TA list
  - offered roles count
  - total workload
  - filter by TA or module
  - overload flag
  - current `max_workload` value
  - update `max_workload` control
  - blacklist list and manage-blacklist action
- Linked stories:
  - `US14`
  - `US15`
  - `US16`
  - `US25`
  - `US26`

### P12 Validation and Error States
- Purpose: demonstrate usability and robustness
- Main elements:
  - empty required field warning
  - duplicate application warning
  - invalid login message
  - max-workload-reached message
  - blacklist-related restriction message
  - file error message
  - no matching or unavailable action message
- Linked stories:
  - `US08`
  - `US17`
  - `US18`
  - `US24`
  - `US25`
  - `US26`

## Navigation summary
- `P01 -> P02` for new applicant registration
- `P01 -> P03` for applicant login
- `P03 -> P04 -> P03`
- `P03 -> P05 -> P06 -> P07`
- `P01 -> P08 -> P09 -> P08`
- `P08 -> P10`
- `P01 -> P11`

## Recommended export order for the PDF
1. Login
2. Registration
3. Applicant dashboard
4. Applicant profile
5. Job list
6. Job details
7. Application status
8. MO dashboard
9. Post job form
10. Applicant review
11. Admin workload dashboard
12. Validation and error states

## What to show during marking
- Consistent role-based flows
- Login before role-specific access
- Detailed applicant profile structure, not only CV upload
- Required fields and validation
- Visible status changes
- Optional transparency features such as applicant count and lightweight feedback
- Workload visibility and configurable `max_workload` control
- Blacklist support shown as a later admin-oriented control
