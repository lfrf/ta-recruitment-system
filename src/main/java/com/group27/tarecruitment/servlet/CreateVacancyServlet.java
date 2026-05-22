package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.service.VacancyService;
import com.group27.tarecruitment.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * CreateVacancyServlet class type.
 *
 * <p>Servlet/controller type that handles HTTP input, output, and endpoint orchestration.</p>
 * <p>Package: {@code com.group27.tarecruitment.servlet}</p>
 */
@WebServlet("/mo/create-vacancy")
public class CreateVacancyServlet extends HttpServlet {
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
            SessionUtil.storeFlashError(request, "Please use the staff login page before publishing a course job.");
            response.sendRedirect(request.getContextPath() + "/staff/login");
            return;
        }

        if (currentUser.getRole() != UserRole.MO) {
            SessionUtil.storeFlashError(request, "Only organiser accounts can publish course jobs.");
            response.sendRedirect(request.getContextPath() + "/vacancies");
            return;
        }

        request.setAttribute("flashMessage", SessionUtil.consumeFlashMessage(request));
        request.setAttribute("flashError", SessionUtil.consumeFlashError(request));
        request.getRequestDispatcher("/WEB-INF/views/mo/create-vacancy.jsp").forward(request, response);
    }

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
            SessionUtil.storeFlashError(request, "Please use the staff login page before publishing a course job.");
            response.sendRedirect(request.getContextPath() + "/staff/login");
            return;
        }

        if (currentUser.getRole() != UserRole.MO) {
            SessionUtil.storeFlashError(request, "Only organiser accounts can publish course jobs.");
            response.sendRedirect(request.getContextPath() + "/vacancies");
            return;
        }

        String moduleCode = request.getParameter("moduleCode");
        String moduleName = request.getParameter("moduleName");
        String campus = request.getParameter("campus");
        String description = request.getParameter("description");
        String requiredSkills = request.getParameter("requiredSkills");
        String preferredBackground = request.getParameter("preferredBackground");
        String workloadValue = request.getParameter("workloadValue");
        String positionCount = request.getParameter("positionCount");
        boolean leaderRoleAvailable = request.getParameter("leaderRoleAvailable") != null;

        String error = vacancyService.validateNewVacancy(
                currentUser,
                moduleCode,
                moduleName,
                campus,
                description,
                requiredSkills,
                preferredBackground,
                workloadValue,
                positionCount
        );

        if (error != null) {
            request.setAttribute("errorMessage", error);
            request.setAttribute("moduleCode", moduleCode);
            request.setAttribute("moduleName", moduleName);
            request.setAttribute("campus", campus);
            request.setAttribute("description", description);
            request.setAttribute("requiredSkills", requiredSkills);
            request.setAttribute("preferredBackground", preferredBackground);
            request.setAttribute("workloadValue", workloadValue);
            request.setAttribute("positionCount", positionCount);
            request.setAttribute("leaderRoleAvailable", leaderRoleAvailable);
            request.getRequestDispatcher("/WEB-INF/views/mo/create-vacancy.jsp").forward(request, response);
            return;
        }

        vacancyService.createVacancy(
                currentUser,
                moduleCode,
                moduleName,
                campus,
                description,
                requiredSkills,
                preferredBackground,
                workloadValue,
                positionCount,
                leaderRoleAvailable
        );

        SessionUtil.storeFlashMessage(request, "Course job published successfully.");
        response.sendRedirect(request.getContextPath() + "/mo/applicants");
    }
}
