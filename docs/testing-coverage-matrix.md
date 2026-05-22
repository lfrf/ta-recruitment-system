# Testing Coverage Matrix

## 1. Automated Baseline

- Date: 2026-05-22
- Execution: Maven Surefire (`mvn test`)
- Result: `BUILD SUCCESS`
- Total: `Tests run: 35, Failures: 0, Errors: 0, Skipped: 0`
- Raw evidence: `target/surefire-reports/TEST-*.xml`

## 2. Test Classes and Coverage Focus

| Test Class | Coverage Focus |
|---|---|
| `ValidationUtilTest` | Input parsing, e-mail validation, CSV normalization, status normalization |
| `ApplicantProfileServiceTest` | Required profile fields and readiness gate |
| `ApplicationServiceIntegrationTest` | Duplicate apply, blacklist, full vacancy, workload cap, cancel, decision-read update |
| `ReviewServiceSortingTest` | MO default ordering and AI ordering fallback |
| `WorkloadServiceIntegrationTest` | Workload aggregation, overload/blacklist flags, filtering |
| `AccountSecurityAndAuthServiceIntegrationTest` | Password change constraints, plain-to-hash migration, active/inactive auth behavior |
| `AiProfileImportServiceIntegrationTest` | AI callback token/schema checks and apply-to-profile flow |
| `VacancyServiceIntegrationTest` | Vacancy publish validation, duplicate prevention, campus/skills checks, archive permissions |
| `AdminServiceIntegrationTest` | Admin config validation, blacklist add/deactivate lifecycle, profile blacklist projection |
| `QuickLoginRequestServiceTest` | Quick-login request state transitions (pending/confirmed/used) |

## 3. TC01-TC28 Coverage Status

Status definition:
- `Auto-Pass`: covered by automated tests and currently green.
- `Manual-Pass`: covered by manual E2E checklist record.
- `Partial`: rule logic is covered, but full UI/servlet path still requires or benefits from manual evidence.
- `Pending`: planned but not yet fully evidenced.

| TC | Status | Evidence |
|---|---|---|
| TC01 | Pending | No dedicated registration test (current flow is login + profile path) |
| TC02 | Auto-Pass | `ValidationUtilTest`, `ApplicantProfileServiceTest` |
| TC03 | Auto-Pass | `ValidationUtilTest`, `ApplicantProfileServiceTest` |
| TC04 | Pending | Needs explicit manual persistence walkthrough evidence |
| TC05 | Auto-Pass | `VacancyServiceIntegrationTest` |
| TC06 | Auto-Pass | `VacancyServiceIntegrationTest` |
| TC07 | Pending | Needs browser flow evidence |
| TC08 | Pending | Needs browser flow evidence |
| TC09 | Partial | Rule-level apply chain in `ApplicationServiceIntegrationTest`; full servlet path pending |
| TC10 | Auto-Pass | `ApplicationServiceIntegrationTest` |
| TC11 | Pending | Needs browser history page evidence |
| TC12 | Pending | Needs MO page evidence |
| TC13 | Pending | Needs MO applicant-details evidence |
| TC14 | Partial | Decision/sorting rules covered (`ReviewServiceSortingTest` + `ApplicationServiceIntegrationTest`), full MO UI path pending |
| TC15 | Partial | Decision/sorting rules covered (`ReviewServiceSortingTest` + `ApplicationServiceIntegrationTest`), full MO UI path pending |
| TC16 | Partial | Decision-read logic covered in `ApplicationServiceIntegrationTest`; full UI trace pending |
| TC17 | Pending | Needs cross-page consistency screenshots/log |
| TC18 | Partial | Workload rule layer covered in `WorkloadServiceIntegrationTest`; full admin page trace pending |
| TC19 | Pending | Needs explicit rejection-count exclusion evidence on admin page |
| TC20 | Partial | Workload aggregation covered in `WorkloadServiceIntegrationTest`; full admin page trace pending |
| TC21 | Pending | Controlled file-read failure injection not executed |
| TC22 | Pending | Controlled file-write failure injection not executed |
| TC23 | Pending | Needs manual persistence evidence |
| TC24 | Pending | Needs manual persistence evidence |
| TC25 | Pending | Needs UI search/filter evidence |
| TC26 | Pending | Needs UI empty-result evidence |
| TC27 | Pending | Needs role-access walkthrough evidence |
| TC28 | Partial | Invalid flow rules partly covered (`ApplicationServiceIntegrationTest`, `ValidationUtilTest`) |

## 4. Manual Coverage Artifacts

- `test/e2e/ai_import_e2e_checklist.md` (AI task/callback/token-path checks)
- `test/e2e/system_flow_batch_2026-05-22.md` (cross-role E2E checklist and result recording)

## 5. Remaining High-Value Additions

- Browser automation for role routing, apply, review, and history flows
- File I/O failure injection harness for `TC21`/`TC22`
- Load and concurrency benchmark
- Security penetration-style checklist (session, callback endpoint abuse patterns)
