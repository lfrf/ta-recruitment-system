# Requirements Refinement Meeting Notes

**Project:** TA Recruitment System  
**Date:** 2026-03-20  
**Meeting type:** Backlog and requirements refinement after teaching-staff review  
**Participants:** Yifu Feng, Yuzhang Wu, Chensiyuan Qing, Fuhe Huang, Tao Li, Mu Du  
**Chair:** Yifu Feng  
**Recorded by:** Tao Li

## Purpose
This meeting was held to refine the first backlog draft after later teaching-staff comments. The aim was to identify which practical points were still missing and to update the requirements, backlog wording, and prototype direction before finalising the first assessment package.

## Main Feedback Discussed
- The applicant profile was too vague and needed more structured fields.
- Login needed to appear explicitly in the backlog rather than remain implicit.
- Applicants should not choose unlimited roles and rely only on later admin balancing.
- Admin should be able to configure a simple `max_workload` parameter, with `3` used as the initial default.
- Blacklist support should be represented as a later administrative feature.
- Low-priority AI-assisted ideas should still be visible in the backlog as controlled `Could` stories.

## Decisions Made
- Expand applicant profile requirements using structured fields derived from sample CV analysis.
- Add an explicit login story to the backlog.
- Replace the earlier "apply for multiple roles" wording with a configurable role-limit rule.
- Add an admin story for configuring `max_workload`.
- Add blacklist support as a later story rather than a first-version core feature.
- Record explainable AI-assisted features as low-priority enhancements instead of removing them entirely.

## Artefacts Affected
- requirements findings document;
- backlog markdown draft;
- Excel backlog;
- report wording;
- appendix evidence notes.

## Outcome
The team agreed that these changes made the backlog more realistic, more traceable to teaching-staff feedback, and better aligned with a practical first-release scope.
