# Visitor Access Feedback Notes

**Project:** TA Recruitment System  
**Date:** 2026-03-21  
**Feedback source:** Teaching-staff interface feedback  
**Recorded by:** Yifu Feng  
**Discussed by:** Group 27

## Purpose
This note records the later teaching-staff feedback that changed the applicant entry flow. The earlier backlog assumed that applicants would log in before vacancy browsing. The new feedback clarified that browsing should remain public and that login should only be required when the user attempts a protected action.

## Feedback Summary
- Applicants should be able to browse vacancies before login.
- Vacancy details should also remain visible before login.
- The interface should include a visible `Log In` entry in the top-right area or another clearly accessible location.
- The system should prompt login only when the user wants to apply, view their profile, or access application status.

## Team Interpretation
- The entry flow should begin with a visitor-facing vacancy list rather than a forced login screen.
- Login is still necessary, but only for protected or personal actions.
- This change improves convenience and better matches stakeholder expectations without expanding technical complexity.

## Decisions Made
- Revise the backlog to make vacancy browsing and vacancy details available before login.
- Keep a dedicated login story because protected actions still require authentication.
- Update the prototype so the browsing page includes a visible `Log In` option.
- Add login-required prompts to protected actions such as application submission and profile access.

## Artefacts Affected
- requirements findings;
- backlog markdown and Excel backlog;
- prototype page plan;
- prototype page copy;
- report and appendix wording.

## Outcome
The team agreed that public browsing before login should become part of the refined applicant flow for the first assessment materials.
