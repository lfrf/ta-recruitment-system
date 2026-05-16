# TA Recruitment System

Software Engineering group project for `EBU6304`.

## Team
- Norman-Ou: 190898878 (Support TA)
- lfrf: 231222017 (Lead)
- TsingChen: 231220806 (Member)
- fuhehuang111: 231221054 (Member)
- SIMON48623: 231222729 (Member)
- dm742: 231220297 (Member)
- 2023213300: 231221582 (Member)

## Project scope
- Support applicants to browse course-based TA jobs, complete profile, apply, and track status.
- Support module organisers (MO) to publish jobs, review applicants, and appoint lead TA.
- Support admins to configure workload rules and manage blacklist/workload overview.

## Current implemented features
- Visitor and applicant job board with keyword and campus filtering.
- Release-style right panel summary (total/open/full/closed and job list).
- Full jobs remain visible, marked as `FULL`, sorted below open jobs.
- Login separation: applicant uses `/login`, staff (MO/Admin) uses `/staff/login`.
- Applicant side: profile editing, CV metadata, apply/cancel-under-review, application history, and lead-TA offer status display.
- MO side: managed vacancy/applicant list, decision (`Offered`/`Unsuccessful`), optional lead-TA appointment, publish/archive vacancy, and CV download in review flow.
- Admin side: global config (`maxWorkload`, `allowVisitorBrowsing`), workload overview filters, and blacklist add/remove with history.
- QR-assisted quick login: bind phone in profile, scan login QR, confirm on phone, desktop completes login automatically.

## Tech stack
- Java 17
- Maven
- Jakarta Servlet/JSP + JSTL
- Jackson JSON storage
- Apache Tomcat 11

## Local run
1. Build the project.

```bash
mvn clean package
```

2. Deploy `target/ta-recruitment-system.war` to Tomcat 11.
3. Use context path `/ta-recruitment-system`.
4. Open home or jobs:
- `http://localhost:8081/ta-recruitment-system/home`
- `http://localhost:8081/ta-recruitment-system/vacancies`

## Demo accounts
- Applicant: `applicant01 / pass123`
- Applicant: `applicant02 / pass123`
- Applicant: `applicant03 / pass123`
- Applicant: `applicant04 / pass123`
- MO: `mo01 / pass123`
- Admin: `admin01 / pass123`

## Data storage
- Seed data is stored in `src/main/resources/data/*.json`.
- Runtime writes are stored in `%USERPROFILE%\.ta-recruitment-system\data` by default.
- You can override data directory with JVM option:

```text
-Dta.recruitment.dataDir=<your_data_dir>
```

- Runtime files include:
- `users.json`, `vacancies.json`, `applications.json`
- `applicant_profiles.json`, `blacklist.json`, `admin_config.json`
- `quick_login_bindings.json`

## Quick login flow (QR-assisted)
1. Applicant logs in on desktop and opens `My Profile`.
2. Click the quick-login bind button to generate a QR.
3. Scan on phone and complete binding on the phone page.
4. Next time, on applicant login page click quick login to generate QR.
5. Scan and confirm on phone.
6. Desktop polling completes session login automatically.

## Common troubleshooting
- 404 after deploy: ensure browser URL context path matches Tomcat deployment context exactly.
- 500 on quick-login binding JSON read/write: check `%USERPROFILE%\.ta-recruitment-system\data\quick_login_bindings.json`; if corrupted, back it up and reset to `[]`.
- LocalDateTime JSON serialization error: ensure `jackson-datatype-jsr310` is present and redeploy after `mvn clean package`.

## Repository structure
- `docs/`: planning, meeting records, risk/decision logs.
- `prototype/`: prototype materials.
- `src/main/java/`: Servlet, service, repository, model, filter code.
- `src/main/resources/data/`: seed JSON data.
- `src/main/webapp/`: JSP views and CSS assets.
- `test/`: test code and test assets.
