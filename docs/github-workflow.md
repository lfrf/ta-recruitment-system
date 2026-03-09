# GitHub Workflow

## Repository setup
- Create one shared repository for the whole group.
- Add all team members as collaborators.
- Give the assigned teaching assistant access.
- Protect `main` by convention even if formal branch protection is unavailable.

## Branch model
- `main`: latest stable integrated version
- `member-name`: each member's long-lived personal branch if needed
- `feature/usXX-short-name`: story-based working branch
- `release/v1.0`, `release/v2.0`: optional release preparation branches

## Naming rules
- User stories: `US01`, `US02`, `US03`, ...
- Issues: one issue per story or tightly related bug
- Pull requests: use the format `US09 Apply for job`

## Commit rules
- Make small, logical commits.
- Use clear messages, for example:
  - `Add applicant profile file model`
  - `Implement job browsing screen`
  - `Add JUnit tests for workload controller`
- Every member should commit regularly, not in one batch before the deadline.

## Pull request checklist
- Story or task is referenced
- Scope is small and clear
- Code runs locally
- Tests added or updated where relevant
- Screenshots attached for UI changes
- Notes added for any known limitation

## Minimum evidence per member
- At least `2` feature pull requests
- At least `1` review on another member's pull request
- Visible commits across multiple dates
- At least `1` contribution to testing or documentation

## Weekly release expectation
- End every week with one integrated version on `main`.
- Add a short release note in the decision log or release notes.

## Warning signs
- One member doing all merges
- Large end-of-week commit dumps
- Work only visible in local folders, not on GitHub
- Features built without linked backlog items
