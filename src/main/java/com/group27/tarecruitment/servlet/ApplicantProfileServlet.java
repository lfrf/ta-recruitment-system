package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.model.ApplicantProfile;
import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.service.ApplicantProfileService;
import com.group27.tarecruitment.util.SessionUtil;
import com.group27.tarecruitment.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/applicant/profile")
public class ApplicantProfileServlet extends HttpServlet {
    private final ApplicantProfileService applicantProfileService = new ApplicantProfileService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserAccount currentUser = SessionUtil.getCurrentUser(request);
        if (currentUser == null) {
            SessionUtil.storeFlashError(request, "Please log in before updating your applicant profile.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if (currentUser.getRole() != UserRole.APPLICANT) {
            SessionUtil.storeFlashError(request, "Only applicant accounts can access the applicant profile page.");
            response.sendRedirect(request.getContextPath() + "/vacancies");
            return;
        }

        ApplicantProfile profile = applicantProfileService.getOrCreateProfile(currentUser);
        attachProfileAttributes(request, currentUser, profile);
        request.getRequestDispatcher("/WEB-INF/views/applicant/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserAccount currentUser = SessionUtil.getCurrentUser(request);
        if (currentUser == null || currentUser.getRole() != UserRole.APPLICANT) {
            SessionUtil.storeFlashError(request, "Please log in as an applicant before saving your profile.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String fullName = request.getParameter("fullName");
        String studentId = request.getParameter("studentId");
        String email = request.getParameter("email");
        String degreeProgramme = request.getParameter("degreeProgramme");
        String yearOfStudy = request.getParameter("yearOfStudy");

        ApplicantProfile profile = buildProfileFromRequest(currentUser, request);
        if (ValidationUtil.isBlank(fullName)
                || ValidationUtil.isBlank(studentId)
                || ValidationUtil.isBlank(email)
                || ValidationUtil.isBlank(degreeProgramme)
                || ValidationUtil.isBlank(yearOfStudy)) {
            attachProfileAttributes(request, currentUser, profile);
            request.setAttribute("flashError", "Full name, student ID, email, degree programme, and year of study are required.");
            request.getRequestDispatcher("/WEB-INF/views/applicant/profile.jsp").forward(request, response);
            return;
        }

        if (!ValidationUtil.isValidEmail(profile.getEmail())) {
            attachProfileAttributes(request, currentUser, profile);
            request.setAttribute("flashError", "Please enter a valid email address.");
            request.getRequestDispatcher("/WEB-INF/views/applicant/profile.jsp").forward(request, response);
            return;
        }

        if (!ValidationUtil.isPositiveInteger(profile.getYearOfStudy())) {
            attachProfileAttributes(request, currentUser, profile);
            request.setAttribute("flashError", "Year of study must be a positive integer.");
            request.getRequestDispatcher("/WEB-INF/views/applicant/profile.jsp").forward(request, response);
            return;
        }

        applicantProfileService.saveProfile(profile);
        SessionUtil.storeFlashMessage(request, "Applicant profile saved successfully.");
        response.sendRedirect(request.getContextPath() + "/applicant/profile");
    }

    private void attachProfileAttributes(HttpServletRequest request, UserAccount currentUser, ApplicantProfile profile) {
        request.setAttribute("currentUser", currentUser);
        request.setAttribute("profile", profile);
        request.setAttribute("relevantCoursesValue", join(profile.getRelevantCourses()));
        request.setAttribute("skillsValue", join(profile.getSkills()));
        request.setAttribute("flashMessage", SessionUtil.consumeFlashMessage(request));
        request.setAttribute("flashError", SessionUtil.consumeFlashError(request));
    }

    private ApplicantProfile buildProfileFromRequest(UserAccount currentUser, HttpServletRequest request) {
        ApplicantProfile existing = applicantProfileService.findByApplicantId(currentUser.getUserId()).orElse(null);
        ApplicantProfile profile = new ApplicantProfile();
        profile.setApplicantId(currentUser.getUserId());
        profile.setStudentId(ValidationUtil.trimToEmpty(request.getParameter("studentId")));
        profile.setFullName(ValidationUtil.trimToEmpty(request.getParameter("fullName")));
        profile.setEmail(ValidationUtil.trimToEmpty(request.getParameter("email")));
        profile.setPhone(ValidationUtil.trimToEmpty(request.getParameter("phone")));
        profile.setDegreeProgramme(ValidationUtil.trimToEmpty(request.getParameter("degreeProgramme")));
        profile.setYearOfStudy(ValidationUtil.trimToEmpty(request.getParameter("yearOfStudy")));
        profile.setRelevantCourses(ValidationUtil.splitCsv(request.getParameter("relevantCourses")));
        profile.setSkills(ValidationUtil.splitCsv(request.getParameter("skills")));
        profile.setTaExperience(ValidationUtil.trimToEmpty(request.getParameter("taExperience")));
        profile.setProjectOrLeadershipExperience(ValidationUtil.trimToEmpty(request.getParameter("projectExperience")));
        profile.setAvailability(ValidationUtil.trimToEmpty(request.getParameter("availability")));
        profile.setCvFileName(ValidationUtil.trimToEmpty(request.getParameter("cvFileName")));
        profile.setCvFilePath(ValidationUtil.trimToEmpty(request.getParameter("cvFilePath")));
        profile.setBlacklisted(existing != null && existing.isBlacklisted());
        return profile;
    }

    private String join(List<String> values) {
        return values == null ? "" : String.join(", ", values);
    }
}
