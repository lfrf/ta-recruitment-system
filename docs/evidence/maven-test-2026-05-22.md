# Maven Test Evidence - 2026-05-22

## 1. Run Metadata

- Date/time: 2026-05-22 16:00:06 +08:00
- Runner: IntelliJ IDEA Maven runner
- Goal: `test`
- Exit code: `0`
- Build result: `BUILD SUCCESS`

## 2. Command Context (IDEA Runtime)

Observed Java/Maven launch context:

- Java: `D:\develop\jdk21\bin\java.exe`
- Maven home: `D:\develop\idea\IntelliJ IDEA 2024.3.3\plugins\maven\lib\maven3`
- Project directory: `C:\Users\FYF\Documents\GitHub\ta-recruitment-system`

## 3. Test Result Summary

From the Maven console output:

- `Running com.group27.tarecruitment.service.AccountSecurityAndAuthServiceIntegrationTest`
  - `Tests run: 5, Failures: 0, Errors: 0, Skipped: 0`
- `Running com.group27.tarecruitment.service.AiProfileImportServiceIntegrationTest`
  - `Tests run: 3, Failures: 0, Errors: 0, Skipped: 0`
- `Running com.group27.tarecruitment.service.ApplicantProfileServiceTest`
  - `Tests run: 2, Failures: 0, Errors: 0, Skipped: 0`
- `Running com.group27.tarecruitment.service.ApplicationServiceIntegrationTest`
  - `Tests run: 6, Failures: 0, Errors: 0, Skipped: 0`
- `Running com.group27.tarecruitment.service.ReviewServiceSortingTest`
  - `Tests run: 2, Failures: 0, Errors: 0, Skipped: 0`
- `Running com.group27.tarecruitment.service.WorkloadServiceIntegrationTest`
  - `Tests run: 2, Failures: 0, Errors: 0, Skipped: 0`
- `Running com.group27.tarecruitment.util.ValidationUtilTest`
  - `Tests run: 4, Failures: 0, Errors: 0, Skipped: 0`

Aggregated:

- `Tests run: 24, Failures: 0, Errors: 0, Skipped: 0`

## 4. Coverage Scope of This Run

This automated run verifies unit/service rule modules:

- input/status validation utilities
- applicant profile readiness gate logic
- application validation and cancellation rules
- MO review ordering logic (default vs AI fit)
- workload summary and filtering rules
- password-change and authentication compatibility rules
- AI callback validation and apply-to-profile workflow

This run does not, by itself, prove full system/E2E coverage. System-level evidence should be recorded separately in:

- `test/e2e/ai_import_e2e_checklist.md`
- `docs/test-execution-record.md` (additional rows for role flows and negative-path scenarios)
- `target/surefire-reports/TEST-*.xml` (machine-readable execution artifacts)
