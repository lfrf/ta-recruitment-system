package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.service.ApplicantProfileService;
import com.group27.tarecruitment.service.ApplicationService;
import com.group27.tarecruitment.util.SessionUtil;
import com.group27.tarecruitment.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/applicant/apply")
public class ApplyServlet extends HttpServlet {
    private final ApplicantProfileService applicantProfileService = new ApplicantProfileService();
    private final ApplicationService applicationService = new ApplicationService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserAccount currentUser = SessionUtil.getCurrentUser(request);
        String vacancyId = ValidationUtil.trimToEmpty(request.getParameter("vacancyId"));

        if (currentUser == null) {
            SessionUtil.storeFlashError(request, "Please log in before applying for a vacancy.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if (currentUser.getRole() != UserRole.APPLICANT) {
            SessionUtil.storeFlashError(request, "Only applicant accounts can submit vacancy applications.");
            response.sendRedirect(request.getContextPath() + "/vacancies");
            return;
        }
        if (ValidationUtil.isBlank(vacancyId)) {
            SessionUtil.storeFlashError(request, "Vacancy ID is missing.");
            response.sendRedirect(request.getContextPath() + "/vacancies");
            return;
        }
        if (!applicantProfileService.hasProfile(currentUser.getUserId())) {
            SessionUtil.storeFlashError(request, "Please complete and save your applicant profile before applying.");
            response.sendRedirect(request.getContextPath() + "/applicant/profile");
            return;
        }

        String validationError = applicationService.validateApplication(currentUser, vacancyId);
        if (validationError != null) {
            SessionUtil.storeFlashError(request, validationError);
            response.sendRedirect(request.getContextPath() + "/vacancy?id=" + vacancyId);
            return;
        }

        applicationService.submitApplication(currentUser, vacancyId);
        SessionUtil.storeFlashMessage(request, "Application submitted successfully.");
        response.sendRedirect(request.getContextPath() + "/applicant/status");
    }
}
