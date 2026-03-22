# Requirements Revision Note - 2026-03-20 (updated after 2026-03-21 visitor-access feedback)

## Reason for Revision
Later teaching-staff review, followed by the 2026-03-21 visitor-access feedback, suggested that the requirements and backlog still missed several practical points that should be reflected before the first assessment package is finalised.

## Key Corrections from Later Feedback
- The applicant profile should be more detailed and should not rely only on a CV upload.
- Login should be represented explicitly in the backlog.
- Applicants should not choose unlimited roles and rely only on later admin balancing.
- Admin should be able to configure a simple `max_workload` parameter. The initial default can be `3` roles.
- The system should support blacklist handling for applicants with poor previous performance.
- Public vacancy browsing should remain available before login, with a visible login entry from the interface.
- Restricted actions should prompt login instead of blocking vacancy browsing completely.
- Low-priority AI-assisted features should still be kept as explicit backlog items.

## Implications for Requirements and Backlog
The later review means the first draft needed to be updated in the following ways:
- expanded profile fields;
- added login story;
- revised vacancy-browsing and vacancy-detail stories to work before login;
- revised application flow so login is required when the user tries to apply or access personal data;
- added admin `max_workload` story;
- added admin blacklist story;
- added three `Could` stories for explainable AI-assisted enhancements.

## Scope Reminder
These changes do not mean the project must implement every feature immediately. The aim is to make the backlog more realistic, traceable, and aligned with stakeholder expectations while keeping the first working version manageable.
