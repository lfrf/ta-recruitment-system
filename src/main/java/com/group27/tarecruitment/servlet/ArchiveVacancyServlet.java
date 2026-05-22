package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.service.VacancyService;
import com.group27.tarecruitment.util.SessionUtil;
import com.group27.tarecruitment.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ArchiveVacancyServlet class type.
 *
 * <p>Servlet/controller type that handles HTTP input, output, and endpoint orchestration.</p>
 * <p>Package: {@code com.group27.tarecruitment.servlet}</p>
 */
@WebServlet("/mo/vacancy/archive")
public class ArchiveVacancyServlet extends HttpServlet {
    private final VacancyService vacancyService = new VacancyService();

    /**
     * Handles the primary HTTP/filter entrypoint workflow for this operation.
     * @param request input parameter of type {@code HttpServletRequest}.
     * @param response input parameter of type {@code HttpServletResponse}.
     * @throws ServletException if this operation cannot complete successfully.
     * @throws IOException if this operation cannot complete successfully.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserAccount currentUser = SessionUtil.getCurrentUser(request);
        if (currentUser == null) {
            SessionUtil.storeFlashError(request, "Please use the staff login page before archiving a course job.");
            response.sendRedirect(request.getContextPath() + "/staff/login");
            return;
        }
        if (currentUser.getRole() != UserRole.MO) {
            SessionUtil.storeFlashError(request, "Only organiser accounts can archive course jobs.");
            response.sendRedirect(request.getContextPath() + "/vacancies");
            return;
        }

        String vacancyId = ValidationUtil.trimToEmpty(request.getParameter("vacancyId"));
        String error = vacancyService.archiveVacancy(currentUser, vacancyId);
        if (error != null) {
            SessionUtil.storeFlashError(request, error);
        } else {
            SessionUtil.storeFlashMessage(request, "Course job archived. It is now hidden from Browse Jobs.");
        }

        response.sendRedirect(request.getContextPath() + "/mo/applicants");
    }
}
