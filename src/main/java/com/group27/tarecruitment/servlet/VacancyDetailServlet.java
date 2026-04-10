package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.model.Vacancy;
import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.service.AdminService;
import com.group27.tarecruitment.service.ApplicantProfileService;
import com.group27.tarecruitment.service.ApplicationService;
import com.group27.tarecruitment.service.VacancyService;
import com.group27.tarecruitment.util.SessionUtil;
import com.group27.tarecruitment.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/vacancy")
public class VacancyDetailServlet extends HttpServlet {
    private final VacancyService vacancyService = new VacancyService();
    private final ApplicationService applicationService = new ApplicationService();
    private final AdminService adminService = new AdminService();
    private final ApplicantProfileService applicantProfileService = new ApplicantProfileService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String vacancyId = request.getParameter("id");
        UserAccount currentUser = SessionUtil.getCurrentUser(request);
        boolean allowVisitorBrowsing = adminService.getConfig().isAllowVisitorBrowsing();
        if (currentUser == null && !allowVisitorBrowsing) {
            SessionUtil.storeFlashError(request, "Visitor browsing is currently disabled. Please log in to continue.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        boolean isApplicant = currentUser != null && currentUser.getRole() == UserRole.APPLICANT;
        Vacancy vacancy = vacancyService.getVacancy(vacancyId).orElse(null);
        boolean vacancyFull = applicationService.isVacancyFull(vacancy);
        int offeredCount = vacancy == null ? 0 : applicationService.countOfferedApplications(vacancy.getVacancyId());
        boolean profileReady = isApplicant && applicantProfileService.isProfileReady(currentUser.getUserId());
        boolean alreadyApplied = false;
        if (isApplicant) {
            alreadyApplied = applicationService.getApplicationsByApplicant(currentUser.getUserId()).stream()
                    .filter(application -> !ValidationUtil.STATUS_WITHDRAWN.equalsIgnoreCase(
                            ValidationUtil.normalizeApplicationStatus(application.getStatus())))
                    .anyMatch(application -> vacancyId != null && vacancyId.equals(application.getVacancyId()));
        }

        request.setAttribute("vacancy", vacancy);
        request.setAttribute("vacancyFull", vacancyFull);
        request.setAttribute("offeredCount", offeredCount);
        request.setAttribute("loggedIn", currentUser != null);
        request.setAttribute("currentUser", currentUser);
        request.setAttribute("isApplicant", isApplicant);
        request.setAttribute("isMO", currentUser != null && currentUser.getRole() == UserRole.MO);
        request.setAttribute("isAdmin", currentUser != null && currentUser.getRole() == UserRole.ADMIN);
        request.setAttribute("profileReady", profileReady);
        request.setAttribute("alreadyApplied", alreadyApplied);
        request.setAttribute("adminConfig", applicationService.getAdminConfig());
        request.setAttribute("flashMessage", SessionUtil.consumeFlashMessage(request));
        request.setAttribute("flashError", SessionUtil.consumeFlashError(request));
        request.getRequestDispatcher("/WEB-INF/views/visitor/vacancy-detail.jsp").forward(request, response);
    }
}
