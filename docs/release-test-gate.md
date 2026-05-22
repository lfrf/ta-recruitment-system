# Release Test Gate (Pre-Demo / Pre-Submission)

## 1. Gate Purpose

Define a strict release gate so demo builds are reproducible, auditable, and not dependent on ad-hoc verification.

## 2. Mandatory Gate Items

| Gate Item | Pass Condition | Evidence |
|---|---|---|
| G1: Build + unit/integration test | `mvn test` succeeds with zero failures | `target/surefire-reports/`, `docs/test-execution-record.md` |
| G2: Core role flow smoke | Applicant, MO, Admin critical paths verified | `test/e2e/system_flow_batch_2026-05-22.md` |
| G3: AI callback path | Token validation + callback + apply path verified | `test/e2e/ai_import_e2e_checklist.md` |
| G4: Security sanity | Login role boundaries + inactive-account behavior + password change checked | `AccountSecurityAndAuthServiceIntegrationTest`, manual role access evidence |
| G5: Defect closure | High-impact open defects closed or accepted with mitigation | `docs/testing-defect-regression-log.md` |

## 3. Release Decision Rule

Release candidate is accepted only when:

1. all mandatory gate items are Pass;
2. no unresolved blocker defects remain;
3. evidence files are committed and traceable.

## 4. Quick Runbook

1. Run `mvn test`.
2. Execute manual batch checklist (`test/e2e/system_flow_batch_2026-05-22.md`).
3. Execute AI E2E checklist (`test/e2e/ai_import_e2e_checklist.md`).
4. Update `docs/test-execution-record.md` and `docs/testing-coverage-matrix.md`.
5. Confirm gate status and tag demo build.
