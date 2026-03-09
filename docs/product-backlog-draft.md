# Product Backlog Draft

## Planning assumptions
- `Sprint 1`: 2026-03-09 to 2026-03-22
- `Sprint 2`: 2026-03-23 to 2026-04-12
- `Sprint 3`: 2026-04-13 to 2026-05-03
- `Sprint 4`: 2026-05-04 to 2026-05-24
- Priority uses `Must / Should / Could`
- Estimation uses story points: `1, 2, 3, 5, 8`

## Sprint goals
- `Sprint 1`: define scope, collect evidence, produce backlog and prototype, prepare the first assessment package
- `Sprint 2`: deliver the core recruitment workflow from job posting to application review
- `Sprint 3`: improve administration, stability, error handling, and overall quality
- `Sprint 4`: add optional explainable intelligence and prepare final delivery

## Story list
| ID | Story name | Role | Priority | Sprint | SP | Notes |
|---|---|---|---|---|---|---|
| US01 | Register account | Applicant | Must | 1 | 3 | Epic: Applicant onboarding |
| US02 | Log in and log out | Applicant | Must | 1 | 3 | Epic: Applicant onboarding |
| US03 | Maintain applicant profile | Applicant | Must | 1 | 5 | Epic: Applicant onboarding |
| US04 | Upload CV | Applicant | Must | 1 | 3 | Epic: Applicant onboarding |
| US05 | Record skills and availability | Applicant | Should | 2 | 3 | Epic: Applicant onboarding |
| US06 | Browse open jobs | Applicant | Must | 1 | 3 | Epic: Job discovery |
| US07 | View job details | Applicant | Must | 1 | 2 | Epic: Job discovery |
| US08 | Filter and search jobs | Applicant | Should | 2 | 5 | Epic: Job discovery |
| US09 | Apply for a job | Applicant | Must | 2 | 5 | Epic: Application flow |
| US10 | Block duplicate applications | Applicant | Should | 2 | 2 | Epic: Application flow |
| US11 | View application status | Applicant | Must | 2 | 3 | Epic: Application flow |
| US12 | Withdraw a pending application | Applicant | Should | 3 | 3 | Epic: Application flow |
| US13 | Post a job | MO | Must | 2 | 5 | Epic: MO recruitment |
| US14 | Edit or close a job | MO | Should | 3 | 3 | Epic: MO recruitment |
| US15 | View applicants for a job | MO | Must | 2 | 3 | Epic: MO recruitment |
| US16 | Review applicant details and CV | MO | Must | 2 | 3 | Epic: MO recruitment |
| US17 | Accept or reject an applicant | MO | Must | 2 | 5 | Epic: MO recruitment |
| US18 | Add review notes | MO | Should | 3 | 2 | Epic: MO recruitment |
| US19 | View TA workload dashboard | Admin | Must | 2 | 5 | Epic: Workload management |
| US20 | Filter workload data | Admin | Should | 3 | 3 | Epic: Workload management |
| US21 | Highlight overload risk | Admin | Should | 3 | 3 | Epic: Workload management |
| US22 | Validate input data | All roles | Must | 2 | 5 | Epic: Quality and reliability |
| US23 | Handle file errors gracefully | All roles | Should | 3 | 5 | Epic: Quality and reliability |
| US24 | Show explainable skill match | MO | Could | 4 | 5 | Epic: Explainable support |
| US25 | Suggest missing skills | Applicant | Could | 4 | 3 | Epic: Explainable support |

## Notes for the team
- The backlog is larger than Sprint 1 on purpose. Sprint 1 defines the scope; it does not implement every story.
- The first software version should focus on the minimum end-to-end workflow:
  - `US13 Post a job`
  - `US06 Browse open jobs`
  - `US07 View job details`
  - `US09 Apply for a job`
  - `US15 View applicants for a job`
- Optional AI-assisted features are deliberately delayed until Sprint 4 so they do not endanger the core delivery.
