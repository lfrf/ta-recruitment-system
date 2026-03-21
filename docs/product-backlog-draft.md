# Product Backlog Draft

## Planning Assumptions
- `Sprint 1`: 2026-03-09 to 2026-03-22
- `Sprint 2`: 2026-03-23 to 2026-04-12
- `Sprint 3`: 2026-04-13 to 2026-05-03
- `Sprint 4`: 2026-05-04 to 2026-05-24
- Priority uses `Must / Should / Could`
- Estimation uses story points: `1, 2, 3, 5, 8`

## Sprint Goals
- `Sprint 1`: refine requirements, prototype, and first assessment deliverables
- `Sprint 2`: deliver the core workflow from public vacancy browsing and login to application review and workload-limit control
- `Sprint 3`: improve admin support, blacklist handling, validation, and traceability
- `Sprint 4`: add optional explainable support features and prepare the final delivery

## Backlog Refinement from Later Feedback
Later teaching-staff feedback, visitor-access feedback, and sample CV analysis led to several requirement changes:
- the applicant profile should be more structured and should not rely only on CV upload;
- login must appear explicitly in the backlog;
- public vacancy browsing should remain available before login, with a visible login entry;
- restricted actions should prompt login rather than blocking vacancy browsing entirely;
- applicants should not choose unlimited roles and rely only on later admin balancing;
- admin should configure a simple `max_workload` parameter, with `3` as the initial default;
- blacklist support should be included as a later feature;
- low-priority AI-assisted ideas should still be recorded as `Could` stories.

## Story List

| ID | Story name | Role | Priority | Sprint | SP | Notes |
|---|---|---|---|---|---|---|
| US01 | Maintain detailed reusable applicant profile | Applicant | Must | 1 | 8 | Epic: Applicant profile and application |
| US02 | Upload CV | Applicant | Must | 1 | 3 | Epic: Applicant profile and application |
| US03 | Browse vacancies before login | Visitor | Must | 1 | 3 | Epic: Applicant profile and application |
| US04 | View vacancy details before login | Visitor | Must | 1 | 2 | Epic: Applicant profile and application |
| US05 | Apply for vacancy | Applicant | Must | 2 | 5 | Epic: Applicant profile and application |
| US06 | Apply within configurable role limit | Applicant | Must | 2 | 3 | Epic: Applicant profile and application |
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
| US23 | Show applicant count for a vacancy | Applicant | Could | 3 | 2 | Epic: Applicant profile and application |
| US24 | Log in from the public interface | All roles | Must | 2 | 3 | Epic: Applicant profile and application |
| US25 | Configure max_workload parameter | Admin | Must | 2 | 3 | Epic: Admin workload monitoring |
| US26 | Manage blacklist for unsuitable applicants | Admin | Should | 3 | 3 | Epic: Admin workload monitoring |
| US27 | Match skills between jobs and applicants | MO | Could | 4 | 5 | Epic: Explainable and AI-assisted enhancements |
| US28 | Identify missing skills for applicants | Applicant | Could | 4 | 3 | Epic: Explainable and AI-assisted enhancements |
| US29 | Suggest workload balancing | Admin | Could | 4 | 5 | Epic: Explainable and AI-assisted enhancements |

## Notes for the Team
- The first working version should focus on `US03`, `US04`, `US24`, `US01`, `US09`, `US05`, `US06`, `US10`, `US12`, and `US25`.
- The initial default assumption is `max_workload = 3`, but the value must remain configurable.
- Public browsing should remain available even when the user is not logged in.
- Blacklist support and AI-assisted features belong to later iterations rather than the first working version.
- The backlog should remain a collaborative team artefact rather than a single-author document.
