# Product Backlog Draft

## Planning Assumptions
- `Sprint 1`: 2026-03-09 to 2026-03-22
- `Sprint 2`: 2026-03-23 to 2026-04-12
- `Sprint 3`: 2026-04-13 to 2026-05-03
- `Sprint 4`: 2026-05-04 to 2026-05-24
- Priority uses `Must / Should / Could`
- Estimation uses story points: `1, 2, 3, 5, 8`

## Sprint Goals
- `Sprint 1`: define scope, collect evidence, prepare the backlog and prototype, and complete the first assessment package
- `Sprint 2`: deliver the core recruitment workflow from vacancy posting to application review
- `Sprint 3`: improve administration support, stability, validation, and overall quality
- `Sprint 4`: final refinement, stretch improvements, and assessment preparation

## Story List

| ID | Epic | User Story | Priority | Sprint | SP | Source requirement | Contributors |
|---|---|---|---|---|---|---|---|
| US01 | Applicant Profile and Application | As an applicant, I want to create and maintain a reusable profile so that I do not need to re-enter the same information every time I apply for a TA role. | Must | 1 | 5 | Applicant requirement: reusable profile | Yuzhang Wu, Chensiyuan Qing |
| US02 | Applicant Profile and Application | As an applicant, I want to upload my CV so that module organisers can review my qualifications efficiently. | Must | 1 | 3 | Applicant requirement: upload CV | Yuzhang Wu, Chensiyuan Qing |
| US03 | Applicant Profile and Application | As an applicant, I want to browse available TA vacancies so that I can find suitable roles. | Must | 1 | 3 | Applicant requirement: browse vacancies | Fuhe Huang, Yuzhang Wu |
| US04 | Applicant Profile and Application | As an applicant, I want to view full vacancy details, including module name, duties, required skills, workload, and deadline, so that I can decide whether to apply. | Must | 1 | 2 | Applicant requirement: clear vacancy details | Fuhe Huang, Tao Li |
| US05 | Applicant Profile and Application | As an applicant, I want to apply for a TA vacancy using my saved profile and CV so that the application process is faster and simpler. | Must | 2 | 5 | Applicant requirement: apply for more than one role | Yuzhang Wu, Chensiyuan Qing, Mu Du |
| US06 | Applicant Profile and Application | As an applicant, I want to apply for more than one TA role so that I can increase my chances of being selected. | Must | 2 | 3 | Applicant requirement: multiple applications | Yuzhang Wu, Tao Li |
| US07 | Applicant Profile and Application | As an applicant, I want to view the status of each application, such as Submitted, Accepted, or Rejected, so that I can track progress clearly. | Must | 2 | 3 | Applicant requirement: visible application status | Tao Li, Yuzhang Wu |
| US08 | Applicant Profile and Application | As an applicant, I want the system to prevent duplicate applications for the same role so that my records remain correct and consistent. | Should | 2 | 2 | Cross-cutting requirement: avoid duplicate or invalid input | Mu Du, Chensiyuan Qing |
| US09 | Vacancy Posting and Applicant Review | As a module organiser, I want to create and publish a TA vacancy with title, module, duties, required skills, workload, and deadline so that applicants can see complete job information. | Must | 2 | 5 | MO requirement: create and publish TA role | Chensiyuan Qing, Yifu Feng |
| US10 | Vacancy Posting and Applicant Review | As a module organiser, I want to view all applicants for a selected vacancy so that I can review candidates efficiently. | Must | 2 | 3 | MO requirement: view applicants for a selected role | Chensiyuan Qing, Mu Du |
| US11 | Vacancy Posting and Applicant Review | As a module organiser, I want to see each applicant’s profile and CV together during review so that I do not need to switch between multiple files. | Must | 2 | 3 | MO requirement: show profile and CV during review | Fuhe Huang, Chensiyuan Qing, Tao Li |
| US12 | Vacancy Posting and Applicant Review | As a module organiser, I want to accept or reject applicants and record the decision in the system so that recruitment decisions are consistent and traceable. | Must | 2 | 5 | MO requirement: accept/reject and record decision | Chensiyuan Qing, Yifu Feng |
| US13 | Vacancy Posting and Applicant Review | As a module organiser, I want to view workload information when needed so that I can avoid selecting someone who is already over-committed. | Should | 3 | 3 | MO requirement: workload visibility when needed | Yifu Feng, Mu Du |
| US14 | Admin Workload Monitoring | As an admin user, I want to see an overview of TA workload across applicants so that I can monitor commitments across modules. | Must | 2 | 5 | Admin requirement: workload overview | Mu Du, Yuzhang Wu |
| US15 | Admin Workload Monitoring | As an admin user, I want to filter workload records by applicant or module so that I can check workload information more easily. | Should | 3 | 3 | Admin requirement: workload filtering | Mu Du, Tao Li |
| US16 | Admin Workload Monitoring | As an admin user, I want the system to highlight potential overload or conflict cases so that I can identify risks quickly. | Should | 3 | 3 | Admin requirement: highlight overload/conflict | Mu Du, Yifu Feng |
| US17 | System Quality and Data Reliability | As any user, I want clear validation messages for invalid input so that I can correct errors easily. | Must | 2 | 3 | Non-functional requirement: usability and validation | Mu Du, Chensiyuan Qing |
| US18 | System Quality and Data Reliability | As any user, I want the system to reject duplicate or inconsistent data where appropriate so that the stored information remains reliable. | Must | 3 | 5 | Cross-cutting requirement: structured and reliable records | Mu Du, Yifu Feng, Tao Li |
| US19 | System Quality and Data Reliability | As any user, I want application status values to stay consistent across applicant and organiser views so that everyone sees the same recruitment progress. | Must | 3 | 3 | Cross-cutting requirement: consistent status values | Tao Li, Chensiyuan Qing |
| US20 | System Quality and Data Reliability | As a stakeholder, I want recruitment decisions to be traceable so that the process is transparent and auditable. | Must | 3 | 3 | Cross-cutting requirement: traceable records | Yifu Feng, Tao Li |

## Notes for the Team
- The backlog has been derived from the requirements findings and rewritten as Agile user stories.
- The highest-priority items support the minimum end-to-end recruitment workflow:
  - `US09 Create and publish vacancy`
  - `US03 Browse vacancies`
  - `US04 View vacancy details`
  - `US05 Apply for vacancy`
  - `US10 View applicants for a vacancy`
  - `US12 Accept or reject applicants`
  - `US14 View workload overview`
- Stories were prioritised using `Must / Should / Could` based on value, feasibility, and assessment timing.
- Some generic features such as login or advanced explainable support were not prioritised here because they were not central in the current requirements findings.

## Team User Story Creation Note
- The backlog should be treated as a collaborative team draft rather than a single-author document.
- Requirements evidence came from shared discussion of Applicant, Module Organiser, and Admin needs.
- Initial user stories were proposed by different team members according to stakeholder focus, then merged, refined, and prioritised through group discussion.
- This reflects Agile practice, where user stories emerge from collaborative understanding rather than isolated authorship.
