package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.model.ApplicationRecord;
import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.model.Vacancy;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ApplicantStatusServlet class type.
 *
 * <p>Servlet/controller type that handles HTTP input, output, and endpoint orchestration.</p>
 * <p>Package: {@code com.group27.tarecruitment.servlet}</p>
 */
@WebServlet({"/applicant/status", "/applicant/dashboard"})
public class ApplicantStatusServlet extends HttpServlet {
    private static final DateTimeFormatter DISPLAY_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final ApplicationService applicationService = new ApplicationService();
    private final VacancyService vacancyService = new VacancyService();

    /**
     * Handles the primary HTTP/filter entrypoint workflow for this operation.
     * @param request input parameter of type {@code HttpServletRequest}.
     * @param response input parameter of type {@code HttpServletResponse}.
     * @throws ServletException if this operation cannot complete successfully.
     * @throws IOException if this operation cannot complete successfully.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserAccount currentUser = SessionUtil.getCurrentUser(request);
        if (currentUser == null) {
            SessionUtil.storeFlashError(request, "Please log in before viewing your saved applications.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if (currentUser.getRole() != UserRole.APPLICANT) {
            SessionUtil.storeFlashError(request, "Only applicant accounts can view saved applications.");
            response.sendRedirect(request.getContextPath() + "/vacancies");
            return;
        }

        if (request.getRequestURI().endsWith("/applicant/dashboard")) {
            response.sendRedirect(request.getContextPath() + "/vacancies");
            return;
        }

        List<ApplicationRecord> applications = applicationService.getApplicationsByApplicant(currentUser.getUserId());
        Map<String, Vacancy> vacancyById = new LinkedHashMap<>();
        for (Vacancy vacancy : vacancyService.getAllVacancies()) {
            vacancyById.put(vacancy.getVacancyId(), vacancy);
        }

        List<ApplicationRecord> originalOrder = new ArrayList<>(applications);
        Map<String, Integer> originalIndexById = new LinkedHashMap<>();
        for (int i = 0; i < originalOrder.size(); i++) {
            originalIndexById.put(originalOrder.get(i).getApplicationId(), i);
        }
        applications = new ArrayList<>(applications);
        applications.sort(buildHistoryComparator(originalIndexById));

        Map<String, Boolean> unreadDecisionByApplicationId = new LinkedHashMap<>();
        for (ApplicationRecord application : applications) {
            unreadDecisionByApplicationId.put(application.getApplicationId(), isUnreadDecision(application));
            application.setStatus(ValidationUtil.normalizeApplicationStatus(application.getStatus()));
            application.setSubmittedAt(formatTimestamp(application.getSubmittedAt()));
            application.setDecisionUpdatedAt(formatTimestamp(application.getDecisionUpdatedAt()));
        }

        int unreadDecisionCount = applicationService.countUnreadDecisions(currentUser.getUserId());
        request.setAttribute("currentUser", currentUser);
        request.setAttribute("applications", applications);
        request.setAttribute("unreadDecisionCount", unreadDecisionCount);
        request.setAttribute("unreadDecisionByApplicationId", unreadDecisionByApplicationId);
        request.setAttribute("vacancyById", vacancyById);
        request.setAttribute("flashMessage", SessionUtil.consumeFlashMessage(request));
        request.setAttribute("flashError", SessionUtil.consumeFlashError(request));
        request.getRequestDispatcher("/WEB-INF/views/applicant/status.jsp").forward(request, response);
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param originalIndexById input parameter of type {@code Map<String, Integer>}.
     * @return the computed `Comparator<ApplicationRecord>` value for this operation.
     */
    private Comparator<ApplicationRecord> buildHistoryComparator(Map<String, Integer> originalIndexById) {
        return Comparator
                .comparingInt((ApplicationRecord item) -> isUnreadDecision(item) ? 0 : 1)
                .thenComparingLong((ApplicationRecord item) -> isUnreadDecision(item)
                        ? -parseEpochMillis(item.getDecisionUpdatedAt())
                        : 0L)
                .thenComparingInt(item -> originalIndexById.getOrDefault(item.getApplicationId(), Integer.MAX_VALUE));
    }

    /**
     * Applies review or decision outcomes and related status changes.
     * @param application input parameter of type {@code ApplicationRecord}.
     * @return true when the condition is met; otherwise false.
     */
    private boolean isUnreadDecision(ApplicationRecord application) {
        String status = ValidationUtil.normalizeApplicationStatus(application.getStatus());
        if (!ValidationUtil.STATUS_OFFERED.equals(status)
                && !ValidationUtil.STATUS_UNSUCCESSFUL.equals(status)) {
            return false;
        }
        if (ValidationUtil.isBlank(application.getDecisionUpdatedAt())) {
            return false;
        }
        return !Boolean.TRUE.equals(application.getDecisionRead());
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param value input parameter of type {@code String}.
     * @return the computed `long` value for this operation.
     */
    private long parseEpochMillis(String value) {
        if (ValidationUtil.isBlank(value)) {
            return 0L;
        }
        try {
            return LocalDateTime.parse(value).atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
        } catch (DateTimeParseException ignored) {
            return 0L;
        }
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param submittedAt input parameter of type {@code String}.
     * @return the computed `String` value for this operation.
     */
    private String formatTimestamp(String submittedAt) {
        if (ValidationUtil.isBlank(submittedAt)) {
            return "-";
        }
        try {
            return LocalDateTime.parse(submittedAt).format(DISPLAY_TIME);
        } catch (Exception ignored) {
            return submittedAt;
        }
    }
}
