package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.service.AdminService;
import com.group27.tarecruitment.service.VacancyService;
import com.group27.tarecruitment.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/vacancies")
public class PublicVacancyListServlet extends HttpServlet {
    private final VacancyService vacancyService = new VacancyService();
    private final AdminService adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserAccount currentUser = SessionUtil.getCurrentUser(request);
        boolean allowVisitorBrowsing = adminService.getConfig().isAllowVisitorBrowsing();
        if (currentUser == null && !allowVisitorBrowsing) {
            SessionUtil.storeFlashError(request, "Visitor browsing is currently disabled. Please log in to continue.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.setAttribute("vacancies", vacancyService.getOpenVacancies());
        request.setAttribute("loggedIn", currentUser != null);
        request.setAttribute("isApplicant", currentUser != null && currentUser.getRole() == UserRole.APPLICANT);
        request.setAttribute("isMO", currentUser != null && currentUser.getRole() == UserRole.MO);
        request.setAttribute("isAdmin", currentUser != null && currentUser.getRole() == UserRole.ADMIN);
        request.setAttribute("currentUser", currentUser);
        request.setAttribute("flashMessage", SessionUtil.consumeFlashMessage(request));
        request.setAttribute("flashError", SessionUtil.consumeFlashError(request));
        request.getRequestDispatcher("/WEB-INF/views/visitor/vacancy-list.jsp").forward(request, response);
    }
}