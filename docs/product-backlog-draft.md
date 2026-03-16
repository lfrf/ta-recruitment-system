# Product Backlog Draft

## Planning Assumptions
- `Sprint 1`: 2026-03-09 to 2026-03-22
- `Sprint 2`: 2026-03-23 to 2026-04-12
- `Sprint 3`: 2026-04-13 to 2026-05-03
- `Sprint 4`: 2026-05-04 to 2026-05-24
- Priority uses `Must / Should / Could`
- Estimation uses story points: `1, 2, 3, 5, 8`

## Sprint Goals
- `Sprint 1`: define scope, collect evidence, refine requirements, prepare the backlog and prototype, and complete the first assessment package
- `Sprint 2`: deliver the core recruitment workflow from vacancy posting to application review
- `Sprint 3`: improve administration support, validation, traceability, and overall quality
- `Sprint 4`: add optional explainable support features and prepare the final delivery

## Backlog Refinement from the Follow-Up TA Interview
The follow-up TA interview on 2026-03-13 did not change the core direction of the backlog, but it clarified several important scope decisions. These points were treated as evidence about the current process and used to refine priorities and boundaries rather than copied mechanically as mandatory system behaviour:

- vacancies are currently announced through the Information Portal, QR-code forms, or course groups, so vacancy details must be especially clear;
- module-specific skills and prior course knowledge matter in the real process, so applicant profiles and vacancy records should capture them;
- successful applicants are usually informed by email, but automated email sending is not necessary for the early version;
- detailed rejection reasons are not usually provided, so they are not required in the early backlog;
- tutorial-support scheduling and TA lead task allocation happen after recruitment and should stay out of early scope;
- workload visibility mainly means knowing which modules a TA has already been accepted for, not building a full scheduling system;
- applicant-side visibility of how many people have applied for a vacancy can be a useful transparency feature.

## Story List

| ID | Story name | Role | Priority | Sprint | SP | Notes |
|---|---|---|---|---|---|---|
| US01 | Maintain reusable applicant profile | Applicant | Must | 1 | 5 | Epic: Applicant profile and application |
| US02 | Upload CV | Applicant | Must | 1 | 3 | Epic: Applicant profile and application |
| US03 | Browse available vacancies | Applicant | Must | 1 | 3 | Epic: Applicant profile and application |
| US04 | View vacancy details | Applicant | Must | 1 | 2 | Epic: Applicant profile and application |
| US05 | Apply for vacancy | Applicant | Must | 2 | 5 | Epic: Applicant profile and application |
| US06 | Apply for multiple roles | Applicant | Must | 2 | 3 | Epic: Applicant profile and application |
| US07 | View application status | Applicant | Must | 2 | 3 | Epic: Applicant profile and application |
| US08 | Prevent duplicate applications | Applicant | Should | 2 | 2 | Epic: Applicant profile and application |
| US09 | Create and publish vacancy | MO | Must | 2 | 5 | Epic: Vacancy posting and applicant review |
| US10 | View applicants for a vacancy | MO | Must | 2 | 3 | Epic: Vacancy posting and applicant review |
| US11 | Review applicant profile and CV | MO | Must | 2 | 3 | Epic: Vacancy posting and applicant review |
| US12 | Record offer or rejection outcome | MO | Must | 2 | 5 | Epic: Vacancy posting and applicant review |
| US13 | View workload when needed | MO | Should | 3 | 3 | Epic: Vacancy posting and applicant review |
| US14 | View workload overview | Admin | Must | 2 | 5 | Epic: Admin workload monitoring |
| US15 | Filter workload records | Admin | Should | 3 | 3 | Epic: Admin workload monitoring |
| US16 | Highlight overload or conflict risk | Admin | Should | 3 | 3 | Epic: Admin workload monitoring |
| US17 | Show validation messages | All roles | Must | 2 | 3 | Epic: System quality and data reliability |
| US18 | Reject duplicate or inconsistent data | All roles | Must | 3 | 5 | Epic: System quality and data reliability |
| US19 | Keep status values consistent | All roles | Must | 3 | 3 | Epic: System quality and data reliability |
| US20 | Keep decisions traceable | Stakeholder | Must | 3 | 3 | Epic: System quality and data reliability |
| US21 | Add review notes | MO | Should | 3 | 2 | Epic: Vacancy posting and applicant review |
| US22 | Show optional outcome feedback | Applicant | Should | 3 | 2 | Epic: Applicant profile and application |
| US23 | Show applicant count for a vacancy | Applicant | Should | 3 | 2 | Epic: Applicant profile and application |

## Notes for the Team
- The backlog is intentionally larger than the work planned for Sprint 1. Sprint 1 is mainly for refining requirements, preparing the prototype, and producing the first assessment materials.
- The first implementable software version should focus on the minimum end-to-end workflow:
  - `US09 Create and publish vacancy`
  - `US03 Browse available vacancies`
  - `US04 View vacancy details`
  - `US05 Apply for vacancy`
  - `US10 View applicants for a vacancy`
  - `US12 Record offer or rejection outcome`
- The early system should stop at recruitment outcome management. Lightweight organiser notes, optional outcome feedback, and applicant-visible vacancy application counts can be included, but post-recruitment tutorial scheduling, TA lead coordination, and automated email integration are outside the first release.
- Optional AI-assisted or explainable features are deliberately delayed until the core workflow is stable.

## Team Requirements Workshop Note
- The backlog should be treated as a collaborative team draft rather than a single-author document.
- The requirements evidence comes from shared discussion of Applicant, Module Organiser, and Admin needs, plus a follow-up interview with an experienced TA.
- Individual user stories may have been initially drafted by different members, but the backlog priorities and story direction were refined through team discussion.
- This reflects the Agile expectation that user stories should emerge from collaborative understanding rather than isolated authorship.
