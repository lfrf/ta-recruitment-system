# Supporting Materials for Group 27

**Source:** `Group27_Report_with_refs_inserted (1).docx`  
**Group:** `Group 27`  
**Project:** `TA Recruitment System`

The following supporting materials provide evidence for the process claims made in the main report. They are presented separately so that the five-page report remains concise while the underlying decision trail, artefact evolution, and collaboration record remain visible.

## Supporting Material A. Evidence to Requirement and Scope Decisions

| Evidence source | Observed need / issue | Team interpretation | Resulting decision |
|---|---|---|---|
| Requirements workshop | Applicants repeated the same information across applications and lacked clear vacancy detail before deciding to apply. | The first release needed a clearer applicant-side flow with reusable profile data and decision-support information. | Translate the applicant requirement into separate but related stories for profile reuse, CV upload, browsing, vacancy details, application, and status visibility. |
| Role-based discussion | MOs found applicant review inefficient when information was scattered across records and pages. | Review work should be treated as a structured workflow rather than a single generic review screen. | Create distinct stories for vacancy publishing, applicant list review, profile/CV review, decision recording, and workload visibility. |
| Follow-up TA interview | Detailed rejection explanations and tutorial scheduling were not central to an early recruitment release. | Not every plausible feature belonged in the MVP. | Keep detailed rejection workflow and post-recruitment scheduling out of the first release. |
| Internal scope review | Advanced ideas such as AI matching and extended balancing risked inflating the system beyond a realistic first iteration. | The team needed to preserve a manageable MVP. | Deprioritise advanced intelligent-support features and focus on the core recruitment route. |

This table supports Section 1 of the main report by showing that requirements were derived through evidence and then narrowed through explicit scope decisions, rather than expanded without control.

## Supporting Material B. Backlog Evolution Across Three Versions

Only the most representative changes are shown below. The purpose is not to reproduce the entire backlog, but to demonstrate how discussion and stakeholder feedback altered specific stories across iterations.

| Feature / story | Version 1 | Version 2 | Version 3 | Why the change mattered |
|---|---|---|---|---|
| US04 Browse vacancies / view details | Applicant-side browsing existed as part of the initial flow. | Browsing remained, but required login and was treated more conservatively within the refined flow. | The story was explicitly extended to browsing available vacancies before login. | This is the clearest evidence that accessibility and entry into the system became a stronger design concern in the later iteration. |
| Applicant count visibility | Applicant count was initially shown as a transparency feature. | The feature was removed from the visible design and lowered in priority after the team observed that many real recruitment systems do not display it. | The feature was reintroduced because it became an explicit client requirement, although its priority remained controlled. | This shows that refinement was not simply additive: features could be introduced, removed, and reintroduced for different reasons. |
| Admin workload scope | Admin concerns were recognised broadly. | Workload-related functionality was narrowed towards accepted assignments and manageable controls. | The structure stabilised around practical workload visibility rather than broad administrative expansion. | This demonstrates scope control and a move away from over-design. |
| Advanced intelligent-support ideas | Ideas such as matching or balancing were discussed conceptually. | They were pushed down the backlog as non-essential for the first release. | They remained low priority in the final planning view. | The team preserved feasibility by distinguishing desirable extensions from MVP essentials. |

Supporting Material B supports Sections 2 and 3 of the main report by showing that backlog refinement involved selection, weakening, postponement, and justified reintroduction rather than one-directional feature growth.

## Supporting Material C. Selected Prototype Changes that Evidence Refinement

The report does not need the full prototype repeated here. Instead, the three most important changes are highlighted to show how backlog decisions altered the interface.

### Figure C1. Simplification from Version 1 to Version 2
The first major revision reduced unnecessary page fragmentation and made the applicant flow more compact. This supports the report's claim that improvement was not measured by adding screens, but by making the workflow easier to follow.

### Figure C2. Applicant entry and browsing refinement
The later prototype introduces a clearer browsing route and supports the revised form of US04, where applicants can inspect opportunities before login. This demonstrates a direct connection between story refinement and interface entry structure.

### Figure C3. Quality and management changes
Validation visibility and workload-oriented pages became more explicit as reliability and admin scope were clarified in the backlog. The screenshots therefore support the argument that quality requirements were made visible rather than left implicit.

## Supporting Material D. Requirement -> Backlog -> Prototype Traceability

| Requirement insight | Backlog expression | Prototype response | Why this evidence matters |
|---|---|---|---|
| Applicants need enough vacancy detail to make an informed decision. | US03 browse vacancies, US04 view vacancy details, US05 apply for vacancy. | Job-list and vacancy-detail pages were revised to expose module, skills, workload, deadline, and the route into application. | This shows that a broad requirement became a structured decision-support interface. |
| Applicants want transparency after submission, but the first release should avoid overdesigned rejection handling. | US07 application status and lighter-weight outcome feedback. | Status pages use concise labels such as Submitted, Offered, and Unsuccessful rather than a heavy rejection workflow. | This demonstrates realistic scoping rather than feature inflation. |
| Organisers need one coherent review workflow. | US10, US11, US12, US13, and US21 split a broad organiser need into visible review actions. | The organiser review page combines profile preview, CV access, notes, workload summary, and decisions. | This evidences how decomposition in the backlog led to more structured prototype pages. |
| Admin needs practical workload visibility rather than full scheduling. | US14, US15, and US16 centre on overview, filtering, and overload/conflict signalling. | The admin/workload page was narrowed accordingly. | This shows that prototype scope followed backlog discipline rather than expanding on its own. |

## Supporting Material E. Collaboration as a Driver of Artefact Change

| Situation | Discussion trigger | Collaborative action | Artefact change |
|---|---|---|---|
| Applicant count was questioned. | Comparison with common recruitment systems suggested that transparency of this kind is not always exposed. | The team discussed realism versus perceived usefulness and agreed to remove the feature and lower its priority in the second version. | Backlog V2 and prototype V2 both reflected the removal. |
| Applicant count returned. | The client later made this an explicit requirement. | The team reintroduced the feature without allowing it to dominate the overall MVP. | Backlog V3 and prototype V3 reflected the reintroduction, but priority remained controlled. |
| Browsing before login was added. | Stakeholder understanding of user entry behaviour improved. | The team refined US04 and aligned page flow accordingly. | Backlog V3 and the later prototype introduced pre-login browsing. |
| Validation became explicit. | Reliability and duplication concerns were raised during testing-oriented discussion. | Validation and inconsistency handling were converted into explicit stories rather than hidden assumptions. | Backlog and prototype both gained visible error and warning logic. |

### Figure E1. Team discussion session during backlog refinement and requirement analysis
This figure provides evidence of collaborative decision-making within the team. During this session, requirements and backlog items were discussed and refined collectively. Key changes, including the refinement of US04 (introduction of pre-login browsing) and the removal and later reintroduction of the applicant count feature, emerged from such discussions. These interactions directly influenced backlog evolution and subsequent prototype modifications. Supporting Material E therefore reinforces the argument that teamwork contributed to artefact change rather than merely task distribution.

## Supporting Material F. Story Clusters, Relative Estimation and Sprint Logic

| Story cluster | Relative effort logic | Sprint | Reason for placement |
|---|---|---|---|
| Login, profile reuse, basic browsing and vacancy detail visibility | Lower-complexity presentation and navigation work, but foundational to later tasks. | Sprint 1 | These items establish the user entry route and the information structure required by the rest of the system. |
| Application submission, multiple-role support, application status, vacancy publishing and applicant review | More coordination-heavy because they combine validation, state change and cross-page dependency. | Sprint 2 | These stories form the core recruitment workflow and therefore define the central implementation phase. |
| Workload filtering, overload flags, duplicate/inconsistent data handling, review notes, optional feedback and applicant counts | Mostly dependent or refinement-oriented stories that build on the core workflow. | Sprint 3 | These features make more sense once the main path already exists and can therefore be layered on in a controlled way. |

Supporting Material F supports the report's estimation and iteration-planning claims by showing that relative effort and dependency were used together, rather than distributing stories evenly across time.
