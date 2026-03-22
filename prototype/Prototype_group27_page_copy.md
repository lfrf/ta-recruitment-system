# Prototype Page Copy for Group 27

## P01 Login
- Title: `TA Recruitment System`
- Subtitle: `Sign in to continue`
- Buttons:
  - `Log In`
  - `Create Applicant Account`
- Error copy:
  - `Invalid email or password. Please try again.`

## P02 Applicant Registration
- Title: `Create Applicant Account`
- Helper text: `Please complete your basic information before applying for jobs.`
- Field labels:
  - `Full Name`
  - `Student ID`
  - `Email`
  - `Password`
  - `Confirm Password`
- Buttons:
  - `Register`
  - `Cancel`
- Validation copy:
  - `Student ID is required.`
  - `Email is already registered.`
  - `Passwords do not match.`

## P03 Applicant Dashboard
- Title: `Applicant Dashboard`
- Cards or menu items:
  - `My Profile`
  - `CV Uploaded`
  - `Browse Jobs`
  - `My Applications`
- Small status text:
  - `Welcome back, [Applicant Name].`
  - `You are using 2 of 3 allowed applications.`
  - `Current application limit: 3`

## P04 Applicant Profile
- Title: `My Profile`
- Field labels:
  - `Full Name`
  - `Student ID`
  - `Email`
  - `Phone Number`
  - `Degree or Programme`
  - `Year of Study`
  - `Relevant Modules or Grades`
  - `Technical Skills and Tools`
  - `Previous TA Experience`
  - `Project or Leadership Experience`
  - `Availability`
- CV area:
  - `Upload CV`
  - `Current CV: applicant_cv.pdf`
- Buttons:
  - `Save Changes`
  - `Back`
- Validation copy:
  - `Please complete all required profile fields before saving.`
  - `Student ID, email, degree/programme, and availability are required.`

## P05 Visitor Job List
- Title: `Visitor Interface`
- Subtitle: `Please log in first to access personal features`
- Top-right action:
  - `Log In`
- Example card text:
  - `Teaching Assistant`
  - `Assist with lab sessions, grade assignments, and provide student support for the introductory programming course.`
  - `Required Skills: Python, Communication, Problem Solving`
  - `12 applicants`
  - `Open`
- Buttons:
  - `View Details`

## P06 Job Details
- Title: `Job Details`
- Top-right action:
  - `Log In`
- Fields:
  - `Job Title`
  - `Module`
  - `Description`
  - `Required Skills`
  - `Expected Workload`
  - `Application Deadline`
  - `Current Applicants`
  - `Current Application Limit`
- Example transparency text:
  - `12 applicants have already applied for this role.`
  - `You can apply for up to 3 roles under the current setting.`
- Buttons:
  - `Apply for This Job`
  - `Back to Job List`
- Success copy:
  - `Your application has been submitted successfully.`
- Warning copy:
  - `Please log in to apply for this vacancy.`
  - `You have already applied for this job.`
  - `You have reached the current maximum of 3 applications.`

## P07 Application Status
- Title: `My Applications`
- Column labels:
  - `Job Title`
  - `Module`
  - `Submitted On`
  - `Status`
  - `Optional Feedback`
  - `Last Updated`
- Example statuses:
  - `Submitted`
  - `Offered`
  - `Unsuccessful`
- Example optional feedback:
  - `Good overall fit for the module.`
  - `No additional comment provided.`

## P08 MO Dashboard
- Title: `Module Organiser Dashboard`
- Sections:
  - `Post New Job`
  - `Current Vacancies`
  - `Review Applicants`
- Buttons:
  - `Post Job`
  - `View Applicants`

## P09 Post Job Form
- Title: `Post a New TA Job`
- Field labels:
  - `Job Title`
  - `Module`
  - `Description`
  - `Required Skills`
  - `Expected Workload`
  - `Deadline`
- Buttons:
  - `Post Job`
  - `Cancel`
- Validation copy:
  - `Please complete all required job information before posting.`

## P10 Applicant Review Page
- Title: `Applicants for [Job Title]`
- Left panel labels:
  - `Applicant Name`
  - `Student ID`
  - `Degree or Programme`
  - `Relevant Modules or Grades`
  - `Technical Skills and Tools`
  - `Previous TA Experience`
  - `Project or Leadership Experience`
  - `Availability`
- Right panel labels:
  - `CV`
  - `Current Role Count`
  - `Applicant Workload`
  - `Blacklist Status`
  - `Review Notes`
  - `Decision`
- Buttons:
  - `Offer`
  - `Mark Unsuccessful`
  - `Save Notes`
- Success copy:
  - `Application outcome updated successfully.`
- Warning copy:
  - `This applicant has been flagged on the blacklist and needs admin review before selection.`

## P11 Admin Workload Dashboard
- Title: `TA Workload Overview`
- Column labels:
  - `TA Name`
  - `Offered Roles`
  - `Total Workload`
  - `Risk Flag`
  - `Blacklist Status`
- Filter labels:
  - `Filter by TA`
  - `Filter by Module`
- Admin controls:
  - `Current Max Workload`
  - `Update Limit`
  - `Add to Blacklist`
  - `Remove from Blacklist`
- Example risk text:
  - `Overload Risk`
  - `Normal`

## P12 Validation and Error States
- Example messages:
  - `This field cannot be empty.`
  - `Duplicate application detected.`
  - `Invalid email or password. Please try again.`
  - `Please log in to apply for this vacancy.`
  - `Please log in to view your profile or application status.`
  - `You have reached the current maximum of 3 applications.`
  - `This applicant is currently blacklisted for future recruitment.`
  - `The selected file could not be read. Please try again.`
  - `No jobs match the current data condition.`
  - `This action is not available because the vacancy is closed or unavailable.`

## Consistency rules
- Use the same status terms everywhere:
  - `Submitted`
  - `Offered`
  - `Unsuccessful`
- Use the same role names everywhere:
  - `Visitor`
  - `Applicant`
  - `Module Organiser`
  - `Admin`
- Keep button labels short and action-based.
