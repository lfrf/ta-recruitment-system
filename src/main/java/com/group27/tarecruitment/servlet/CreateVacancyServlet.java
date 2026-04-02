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

@WebServlet("/mo/create-vacancy")
public class CreateVacancyServlet extends HttpServlet {
    private final VacancyService vacancyService = new VacancyService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserAccount currentUser = SessionUtil.getCurrentUser(request);
        if (currentUser == null) {
            SessionUtil.storeFlashError(request, "Please log in before creating a vacancy.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (currentUser.getRole() != UserRole.MO) {
            SessionUtil.storeFlashError(request, "Only organiser accounts can create vacancies.");
            response.sendRedirect(request.getContextPath() + "/vacancies");
            return;
        }

        request.setAttribute("flashMessage", SessionUtil.consumeFlashMessage(request));
        request.setAttribute("flashError", SessionUtil.consumeFlashError(request));
        request.getRequestDispatcher("/WEB-INF/views/mo/create-vacancy.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserAccount currentUser = SessionUtil.getCurrentUser(request);
        if (currentUser == null) {
            SessionUtil.storeFlashError(request, "Please log in before creating a vacancy.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (currentUser.getRole() != UserRole.MO) {
            SessionUtil.storeFlashError(request, "Only organiser accounts can create vacancies.");
            response.sendRedirect(request.getContextPath() + "/vacancies");
            return;
        }

        String moduleCode = request.getParameter("moduleCode");
        String moduleName = request.getParameter("moduleName");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String requiredSkills = request.getParameter("requiredSkills");
        String preferredBackground = request.getParameter("preferredBackground");
        String workloadValue = request.getParameter("workloadValue");
        String deadline = request.getParameter("deadline");

        String error = vacancyService.createVacancy(
                currentUser,
                moduleCode,
                moduleName,
                title,
                description,
                requiredSkills,
                preferredBackground,
                workloadValue,
                deadline
        );

        if (error != null) {
            request.setAttribute("errorMessage", error);
            request.setAttribute("moduleCode", moduleCode);
            request.setAttribute("moduleName", moduleName);
            request.setAttribute("title", title);
            request.setAttribute("description", description);
            request.setAttribute("requiredSkills", requiredSkills);
            request.setAttribute("preferredBackground", preferredBackground);
            request.setAttribute("workloadValue", workloadValue);
            request.setAttribute("deadline", deadline);
            request.getRequestDispatcher("/WEB-INF/views/mo/create-vacancy.jsp").forward(request, response);
            return;
        }

        SessionUtil.storeFlashMessage(request, "Vacancy created successfully.");
        response.sendRedirect(request.getContextPath() + "/mo/applicants");
    }
}
