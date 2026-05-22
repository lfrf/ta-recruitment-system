# Test Execution Record

## 1. Purpose

This document records concrete test execution evidence for the TA Recruitment System, including automated Maven/JUnit results, manual system-flow verification batches, and remaining gaps for final hardening.

## 2. Execution Context (Actual Run)

- Run date: 2026-05-22 (Asia/Shanghai)
- Environment: IntelliJ IDEA 2024.3.3 Maven runner
- Java runtime: `D:\develop\jdk21\bin\java.exe` (project build target remains Java 17)
- Maven goal: `test`
- Project: `com.group27:ta-recruitment-system:1.0-SNAPSHOT`
- Result summary: `BUILD SUCCESS`, exit code `0`
- Aggregated automated result: `Tests run: 35, Failures: 0, Errors: 0, Skipped: 0`

## 3. Automated Execution Log

| Record ID | Date | Tester | Test Case / Area | Result | Evidence |
|---|---|---|---|---|---|
| TR01 | 2026-05-22 | FYF | Input validation and profile readiness (`TC02`, `TC03`) | Pass | `ValidationUtilTest`, `ApplicantProfileServiceTest` |
| TR02 | 2026-05-22 | FYF | Apply-time policy chain (`TC10`, `TC16`, `TC28`) | Pass | `ApplicationServiceIntegrationTest` |
| TR03 | 2026-05-22 | FYF | MO ordering logic (`TC14`, `TC15` rule layer) | Pass | `ReviewServiceSortingTest` |
| TR04 | 2026-05-22 | FYF | Admin workload and policy projection (`TC18`, `TC20` rule layer) | Pass | `WorkloadServiceIntegrationTest` |
| TR05 | 2026-05-22 | FYF | Account security and auth compatibility | Pass | `AccountSecurityAndAuthServiceIntegrationTest` |
| TR06 | 2026-05-22 | FYF | AI callback validation and apply flow | Pass | `AiProfileImportServiceIntegrationTest` |
| TR07 | 2026-05-22 | FYF | Vacancy publish/archive validation chain (`TC05`, `TC06`) | Pass | `VacancyServiceIntegrationTest` |
| TR08 | 2026-05-22 | FYF | Admin config and blacklist lifecycle | Pass | `AdminServiceIntegrationTest` |
| TR09 | 2026-05-22 | FYF | Quick-login request state machine | Pass | `QuickLoginRequestServiceTest` |
| TR10 | 2026-05-22 | FYF | Maven pipeline integrity | Pass | `target/surefire-reports/TEST-*.xml` |

## 4. Automated Class-Level Results

| Test Class | Tests Run | Failures | Errors | Skipped | Status |
|---|---:|---:|---:|---:|---|
| `com.group27.tarecruitment.service.AccountSecurityAndAuthServiceIntegrationTest` | 5 | 0 | 0 | 0 | Pass |
| `com.group27.tarecruitment.service.AdminServiceIntegrationTest` | 4 | 0 | 0 | 0 | Pass |
| `com.group27.tarecruitment.service.AiProfileImportServiceIntegrationTest` | 3 | 0 | 0 | 0 | Pass |
| `com.group27.tarecruitment.service.ApplicantProfileServiceTest` | 2 | 0 | 0 | 0 | Pass |
| `com.group27.tarecruitment.service.ApplicationServiceIntegrationTest` | 6 | 0 | 0 | 0 | Pass |
| `com.group27.tarecruitment.service.QuickLoginRequestServiceTest` | 3 | 0 | 0 | 0 | Pass |
| `com.group27.tarecruitment.service.ReviewServiceSortingTest` | 2 | 0 | 0 | 0 | Pass |
| `com.group27.tarecruitment.service.VacancyServiceIntegrationTest` | 4 | 0 | 0 | 0 | Pass |
| `com.group27.tarecruitment.service.WorkloadServiceIntegrationTest` | 2 | 0 | 0 | 0 | Pass |
| `com.group27.tarecruitment.util.ValidationUtilTest` | 4 | 0 | 0 | 0 | Pass |
| **Total** | **35** | **0** | **0** | **0** | **Pass** |

## 5. Manual/System Batch Records

- Batch file: `test/e2e/system_flow_batch_2026-05-22.md`
- AI-specific batch file: `test/e2e/ai_import_e2e_checklist.md`

These files are used as direct evidence for servlet/JSP end-to-end behavior that is not fully covered by rule-layer JUnit tests.

## 6. Defect and Regression Tracking

- Defect/regression trace: `docs/testing-defect-regression-log.md`
- Coverage status mapping: `docs/testing-coverage-matrix.md`
- Test-case source list: `docs/test-cases.md`

## 7. Remaining Gaps

The following items are still pending full hardening:

- Browser-level UI automation (Playwright/Selenium grade evidence)
- Controlled file-I/O failure injection (`TC21`, `TC22`)
- Load and concurrency benchmark scenarios
- Security penetration-style tests beyond logic-level token checks

These are tracked as planned extensions, not hidden omissions.
