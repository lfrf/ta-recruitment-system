# Testing Defect and Regression Log

## 1. Purpose

Track representative defects found during integration/testing, record root cause and fix, and lock each defect with a regression check.

## 2. Defect Records

| Defect ID | Date | Area | Symptom | Root Cause | Fix Summary | Regression Check |
|---|---|---|---|---|---|---|
| DF-001 | 2026-05 | Quick login mobile confirm | Mobile scan opened `localhost` and failed with `CONNECTION_REFUSED` | QR/entry URL used local host address not reachable from phone | Unified host to LAN-reachable address and updated quick-login flow guidance | Re-run phone bind -> scan -> confirm -> desktop auto-login |
| DF-002 | 2026-05 | AI CV download URL | Agent returned `TOKEN_USED` when reusing same `cvDownloadUrl` | Download token is designed as one-time token; repeated fetch invalid | Clarified prompt wording and UX to generate a fresh task for a new attempt | Re-run scenario: first fetch success, second fetch blocked |
| DF-003 | 2026-05 | MO applicants AI order link | Clicking AI fit order opened 404 URL with duplicated context path | URL assembly concatenated context path twice in navigation link | Fixed route assembly for MO applicant list AI order mode | Re-run MO applicants page toggle between course/AI order |
| DF-004 | 2026-05 | Profile AI import actions | Apply action was hard to find after callback | Prompt visibility and action grouping reduced discoverability | Split action semantics and adjusted side-panel wording/buttons | Re-run AI callback -> preview -> apply workflow |

## 3. Regression Discipline

For every defect:

1. create a reproducible minimal scenario;
2. verify fix in local branch;
3. add or update automated test where feasible;
4. re-run related smoke checklist before merge.

## 4. Current Regression Coverage Links

- Automated: `src/test/java/com/group27/tarecruitment/service/`
- AI E2E: `test/e2e/ai_import_e2e_checklist.md`
- System E2E batch: `test/e2e/system_flow_batch_2026-05-22.md`
