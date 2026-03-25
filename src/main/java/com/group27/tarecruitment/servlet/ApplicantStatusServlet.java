package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.model.ApplicationRecord;
import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.model.Vacancy;
import com.group27.tarecruitment.service.ApplicationService;
import com.group27.tarecruitment.service.VacancyService;
import com.group27.tarecruitment.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/applicant/status")
public class ApplicantStatusServlet extends HttpServlet {
    private final ApplicationService applicationService = new ApplicationService();
    private final VacancyService vacancyService = new VacancyService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserAccount currentUser = SessionUtil.getCurrentUser(request);
        if (currentUser == null) {
            SessionUtil.storeFlashError(request, "Please log in before viewing your application status.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if (currentUser.getRole() != UserRole.APPLICANT) {
            SessionUtil.storeFlashError(request, "Only applicant accounts can view applicant status pages.");
            response.sendRedirect(request.getContextPath() + "/vacancies");
            return;
        }

        List<ApplicationRecord> applications = applicationService.getApplicationsByApplicant(currentUser.getUserId());
        Map<String, Vacancy> vacancyById = new LinkedHashMap<>();
        for (Vacancy vacancy : vacancyService.getAllVacancies()) {
            vacancyById.put(vacancy.getVacancyId(), vacancy);
        }

        request.setAttribute("currentUser", currentUser);
        request.setAttribute("applications", applications);
        request.setAttribute("vacancyById", vacancyById);
        request.setAttribute("flashMessage", SessionUtil.consumeFlashMessage(request));
        request.setAttribute("flashError", SessionUtil.consumeFlashError(request));
        request.getRequestDispatcher("/WEB-INF/views/applicant/status.jsp").forward(request, response);
    }
}
