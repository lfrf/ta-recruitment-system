# Prototype Plan for Group 27

## Recommended prototype level
- Use `medium-fidelity`.
- Make it clickable if possible.
- Focus on workflow clarity, not visual polish.

## Main user flows to cover
- `MO posts a job -> Applicant browses jobs -> Applicant views details -> Applicant applies -> MO reviews applicants`
- `Applicant checks application status`
- `Admin checks TA workload`

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
  - `US01`
  - `US02`

### P02 Applicant Registration
- Purpose: create a new applicant account
- Main elements:
  - name
  - student ID
  - email
  - password
  - confirm password
  - register button
  - cancel button
- Linked stories:
  - `US01`

### P03 Applicant Dashboard
- Purpose: central navigation page for applicants
- Main elements:
  - profile shortcut
  - uploaded CV status
  - browse jobs button
  - application status button
  - logout button
- Linked stories:
  - `US03`
  - `US04`
  - `US06`
  - `US11`

### P04 Applicant Profile
- Purpose: create or edit applicant information
- Main elements:
  - name
  - programme
  - year of study
  - phone
  - skills
  - availability
  - upload CV button
  - save button
- Linked stories:
  - `US03`
  - `US04`
  - `US05`

### P05 Job List
- Purpose: browse open vacancies
- Main elements:
  - search box
  - filter dropdown
  - job cards or table
  - module name
  - deadline
  - required skills
  - view details button
- Linked stories:
  - `US06`
  - `US08`

### P06 Job Details
- Purpose: allow the applicant to decide whether to apply
- Main elements:
  - job title
  - module
  - description
  - required skills
  - workload
  - deadline
  - apply button
  - back to list button
- Linked stories:
  - `US07`
  - `US09`

### P07 Application Status
- Purpose: show the applicant's application history and current status
- Main elements:
  - application list
  - status labels: `Submitted`, `Accepted`, `Rejected`
  - last updated date
  - optional withdraw button for pending cases
- Linked stories:
  - `US11`
  - `US12`

### P08 MO Dashboard
- Purpose: central page for Module Organisers
- Main elements:
  - post job button
  - current job list
  - applicant review shortcut
  - close or edit job action
  - logout button
- Linked stories:
  - `US13`
  - `US14`
  - `US15`

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
  - save draft or cancel button
- Linked stories:
  - `US13`

### P10 Applicant Review Page
- Purpose: review candidates for one job
- Main elements:
  - job summary
  - applicant list
  - applicant profile preview
  - CV link or view button
  - accept button
  - reject button
  - notes field
- Linked stories:
  - `US15`
  - `US16`
  - `US17`
  - `US18`

### P11 Admin Workload Dashboard
- Purpose: show accepted workload across TAs
- Main elements:
  - TA list
  - accepted jobs count
  - total workload
  - filter by TA or module
  - overload flag
- Linked stories:
  - `US19`
  - `US20`
  - `US21`

### P12 Validation and Error States
- Purpose: demonstrate usability and robustness
- Main elements:
  - empty required field warning
  - duplicate application warning
  - invalid login message
  - file error message
- Linked stories:
  - `US10`
  - `US22`
  - `US23`

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
- Clear navigation
- Required fields and validation
- Visible status changes
- Workload visibility for admin
