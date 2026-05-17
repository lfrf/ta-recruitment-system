# AI Import E2E Checklist

This checklist verifies the "0-token AI import" flow end-to-end for four scenarios:

1. CV available: one-time `cvDownloadUrl` works.
2. No CV uploaded: task still works with manual CV upload to agent.
3. One-time token reuse is rejected.
4. Expired token is rejected.

## Preconditions

- App is running and reachable, e.g. `http://10.228.76.161:8081/ta_recruitment_system`.
- You are logged in as an applicant.
- Browser open at `My Profile` page.

---

## Scenario A: CV available (happy path)

1. Upload any CV in profile and save once.
2. In `0-token AI assistant import`, click `Generate prompt task`.
3. Confirm status message says task created with one-time CV download URL.
4. Copy prompt and send to your agent.
5. Agent should:
   - download CV from `.../ai/cv-download?taskId=...&token=...`
   - parse it
   - callback to `.../ai/callback?taskId=...` with header `X-Callback-Token`.
6. In profile page, wait until status shows validated.
7. Check `Extracted fields preview` is visible.
8. Click `Apply to profile form`.
9. Confirm form fields are updated and after refresh values remain.

Expected:
- Main flow reaches `VALIDATED -> APPLIED`.
- Form values are persisted.

---

## Scenario B: No CV uploaded (fallback path)

1. Remove/unset CV (new test account is easiest), then click `Generate prompt task`.
2. Confirm status says no uploaded CV found and asks manual CV upload to agent.
3. Manually upload CV file to your agent chat and send prompt.
4. Wait for validated status and apply.

Expected:
- Task still succeeds without `cvDownloadUrl`.
- Callback/validation/apply path remains normal.

---

## Scenario C: One-time cvDownload token reuse

After one successful agent run from Scenario A:

1. In browser history or copied prompt, find the same `cvDownloadUrl`.
2. Open it again (second attempt), or use PowerShell:

```powershell
Invoke-WebRequest "http://10.228.76.161:8081/ta_recruitment_system/ai/cv-download?taskId=<TASK_ID>&token=<CV_TOKEN>" -Method Get
```

Expected:
- Second attempt returns conflict/error (`TOKEN_USED` semantics), not file bytes.

---

## Scenario D: cvDownload token expiry

Default TTL is 10 minutes.

1. Generate a fresh task with CV uploaded.
2. Do not let agent download immediately. Wait 11+ minutes.
3. Access the same `cvDownloadUrl`:

```powershell
Invoke-WebRequest "http://10.228.76.161:8081/ta_recruitment_system/ai/cv-download?taskId=<TASK_ID>&token=<CV_TOKEN>" -Method Get
```

Expected:
- Request fails with token expired behavior (`TOKEN_EXPIRED` semantics).

---

## Optional API-level callback verification

Use this only if you want to test callback directly.

```powershell
$payload = @'
{
  "schemaVersion": "profile-import-v1",
  "profile": {
    "fullName": "Alice Zhang",
    "studentId": "S1234567",
    "email": "alice.zhang@example.com",
    "phone": "+86 138-0013-8000",
    "degreeProgramme": "BSc Computer Science",
    "yearOfStudy": "2",
    "relevantCourses": ["EBU6304 Software Engineering", "EBU4211 Programming"],
    "skills": ["Java", "SQL"],
    "taExperience": "Lab assistant experience",
    "projectOrLeadershipExperience": "Team project leadership",
    "availability": "Tue/Thu afternoons"
  }
}
'@

Invoke-RestMethod `
  -Uri "http://10.228.76.161:8081/ta_recruitment_system/ai/callback?taskId=<TASK_ID>" `
  -Method Post `
  -Headers @{ "X-Callback-Token" = "<CALLBACK_TOKEN>" } `
  -ContentType "application/json" `
  -Body $payload
```

Expected:
- Response status `OK`, profile task becomes `VALIDATED`.

---

## Pass/Fail quick rubric

- Pass: All four scenarios behave as expected.
- Fail: Any of these happen:
  - Task cannot proceed without CV link.
  - Reused/expired download token still returns file.
  - Valid callback cannot reach validated state.
  - Apply does not persist profile values.
