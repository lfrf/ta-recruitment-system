package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.model.ApplicantProfile;
import com.group27.tarecruitment.model.QuickLoginBinding;
import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.service.ApplicantProfileService;
import com.group27.tarecruitment.service.ApplicationService;
import com.group27.tarecruitment.service.QuickLoginBindingService;
import com.group27.tarecruitment.util.JsonFileUtil;
import com.group27.tarecruitment.util.SessionUtil;
import com.group27.tarecruitment.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@WebServlet("/applicant/profile")
@MultipartConfig(maxFileSize = 10 * 1024 * 1024, maxRequestSize = 12 * 1024 * 1024)
public class ApplicantProfileServlet extends HttpServlet {
    private static final List<String> ALLOWED_CV_EXTENSIONS = List.of(".pdf", ".doc", ".docx");
    private final ApplicantProfileService applicantProfileService = new ApplicantProfileService();
    private final QuickLoginBindingService quickLoginBindingService = new QuickLoginBindingService();
    private final ApplicationService applicationService = new ApplicationService();

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

        ApplicantProfile existing = applicantProfileService.findByApplicantId(currentUser.getUserId()).orElse(null);
        ApplicantProfile profile = buildProfileFromRequest(currentUser, existing, request);

        String uploadError = attachUploadedCv(profile, request.getPart("cvFile"));
        if (uploadError != null) {
            attachProfileAttributes(request, currentUser, profile);
            request.setAttribute("flashError", uploadError);
            request.getRequestDispatcher("/WEB-INF/views/applicant/profile.jsp").forward(request, response);
            return;
        }

        if (ValidationUtil.isBlank(profile.getFullName())
                || ValidationUtil.isBlank(profile.getStudentId())
                || ValidationUtil.isBlank(profile.getEmail())) {
            attachProfileAttributes(request, currentUser, profile);
            request.setAttribute("flashError", "Full name, student ID, and email are required so organisers can identify you quickly.");
            request.getRequestDispatcher("/WEB-INF/views/applicant/profile.jsp").forward(request, response);
            return;
        }

        if (!ValidationUtil.isValidEmail(profile.getEmail())) {
            attachProfileAttributes(request, currentUser, profile);
            request.setAttribute("flashError", "Please enter a valid email address.");
            request.getRequestDispatcher("/WEB-INF/views/applicant/profile.jsp").forward(request, response);
            return;
        }

        if (!ValidationUtil.isBlank(profile.getYearOfStudy())
                && !ValidationUtil.isPositiveInteger(profile.getYearOfStudy())) {
            attachProfileAttributes(request, currentUser, profile);
            request.setAttribute("flashError", "Year of study must be a positive integer when provided.");
            request.getRequestDispatcher("/WEB-INF/views/applicant/profile.jsp").forward(request, response);
            return;
        }

        applicantProfileService.saveProfile(profile);
        String flashMessage = ValidationUtil.isBlank(profile.getCvFileName())
                ? "Basic profile saved. You can apply directly from Browse Jobs now."
                : "Profile and CV saved. You can return to Browse Jobs and apply straight away.";
        SessionUtil.storeFlashMessage(request, flashMessage);
        response.sendRedirect(request.getContextPath() + "/vacancies");
    }

    private void attachProfileAttributes(HttpServletRequest request, UserAccount currentUser, ApplicantProfile profile) {
        request.setAttribute("currentUser", currentUser);
        request.setAttribute("profile", profile);
        request.setAttribute("profileReady", applicantProfileService.isProfileReady(profile));
        request.setAttribute("unreadDecisionCount", applicationService.countUnreadDecisions(currentUser.getUserId()));
        request.setAttribute("relevantCoursesValue", join(profile.getRelevantCourses()));
        request.setAttribute("skillsValue", join(profile.getSkills()));
        Optional<QuickLoginBinding> quickLoginBinding = quickLoginBindingService.getActiveBinding(currentUser.getUserId());
        request.setAttribute("quickLoginBound", quickLoginBinding.isPresent());
        quickLoginBinding.ifPresent(binding -> {
            request.setAttribute("quickLoginDeviceName", binding.getDeviceName());
            request.setAttribute("quickLoginBoundAt", binding.getBoundAt());
        });
        request.setAttribute("flashMessage", SessionUtil.consumeFlashMessage(request));
        request.setAttribute("flashError", SessionUtil.consumeFlashError(request));
    }

    private ApplicantProfile buildProfileFromRequest(UserAccount currentUser, ApplicantProfile existing, HttpServletRequest request) {
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
        if (existing != null) {
            profile.setCvFileName(existing.getCvFileName());
            profile.setCvFilePath(existing.getCvFilePath());
        }
        profile.setBlacklisted(existing != null && existing.isBlacklisted());
        return profile;
    }

    private String attachUploadedCv(ApplicantProfile profile, Part cvFilePart) throws IOException {
        if (cvFilePart == null || cvFilePart.getSize() == 0) {
            return null;
        }

        String originalFileName = extractSubmittedFileName(cvFilePart);
        if (ValidationUtil.isBlank(originalFileName)) {
            return null;
        }

        String extension = extractExtension(originalFileName).toLowerCase();
        if (!ALLOWED_CV_EXTENSIONS.contains(extension)) {
            return "Please upload your CV as a PDF, DOC, or DOCX file.";
        }

        Path uploadsDir = JsonFileUtil.getRuntimeDataDirectory().getParent().resolve("uploads");
        Files.createDirectories(uploadsDir);

        String savedFileName = profile.getApplicantId() + "-cv-" + System.currentTimeMillis() + extension;
        Path targetFile = uploadsDir.resolve(savedFileName);
        try (InputStream inputStream = cvFilePart.getInputStream()) {
            Files.copy(inputStream, targetFile, StandardCopyOption.REPLACE_EXISTING);
        }

        profile.setCvFileName(originalFileName);
        profile.setCvFilePath(targetFile.toString());
        return null;
    }

    private String extractSubmittedFileName(Part part) {
        String submitted = part.getSubmittedFileName();
        if (ValidationUtil.isBlank(submitted)) {
            return "";
        }
        return Path.of(submitted).getFileName().toString();
    }

    private String extractExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex >= 0 ? fileName.substring(dotIndex) : "";
    }

    private String join(List<String> values) {
        return values == null ? "" : String.join(", ", values);
    }
}
