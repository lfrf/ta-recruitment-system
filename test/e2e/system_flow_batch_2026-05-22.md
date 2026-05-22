# System Flow Test Batch - 2026-05-22

## 1. Batch Context

- Date: 2026-05-22
- Tester: FYF
- Environment: local Tomcat 11.0.14 + browser manual walkthrough
- Scope: Applicant / MO / Admin core workflow and role access

## 2. Execution Checklist

| Batch ID | Linked TC | Scenario | Result | Evidence Placeholder | Notes |
|---|---|---|---|---|---|
| SB-01 | TC07 | Applicant opens Browse Jobs and sees open vacancies list | Pending | `screenshots/TC07_*.png` | Verify card list + filter area render |
| SB-02 | TC09 | Applicant submits valid application from vacancy card | Pending | `screenshots/TC09_*.png` | Verify status created as Submitted |
| SB-03 | TC11 | Applicant opens Application History and sees own records | Pending | `screenshots/TC11_*.png` | Include unread badge state if present |
| SB-04 | TC12 | MO opens applicants list for one vacancy | Pending | `screenshots/TC12_*.png` | Confirm list is scoped to target vacancy |
| SB-05 | TC14 | MO marks applicant as Offered | Pending | `screenshots/TC14_*.png` | Include review note save state |
| SB-06 | TC15 | MO marks another applicant as Unsuccessful | Pending | `screenshots/TC15_*.png` | Verify only valid status transition is allowed |
| SB-07 | TC16 | Applicant sees updated decision after MO action | Pending | `screenshots/TC16_*.png` | Confirm unread decision notification appears |
| SB-08 | TC18 / TC20 | Admin workload page reflects accepted allocations and filters | Pending | `screenshots/TC18_TC20_*.png` | Validate counts and flagged filters |
| SB-09 | TC27 | Role-based page access control check (Applicant/MO/Admin) | Pending | `screenshots/TC27_*.png` | Include forbidden redirect examples |
| SB-10 | TC25 / TC26 | Vacancy search/filter with match and no-match conditions | Pending | `screenshots/TC25_TC26_*.png` | Capture both populated and empty results |

## 3. Completion Rule

Mark a scenario as Pass only when:

1. expected UI result is visible;
2. reload does not break the state;
3. a screenshot/video frame is archived in evidence.

## 4. Post-Batch Actions

- Append final results to `docs/test-execution-record.md`.
- Update status row in `docs/testing-coverage-matrix.md`.
- If any failure occurs, open/update `docs/testing-defect-regression-log.md`.
