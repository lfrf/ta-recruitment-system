package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.service.AdminService;
import com.group27.tarecruitment.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/home")
public class HomeRedirectServlet extends HttpServlet {
    private final AdminService adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserAccount currentUser = SessionUtil.getCurrentUser(request);
        boolean allowVisitorBrowsing = adminService.getConfig().isAllowVisitorBrowsing();
        if (currentUser == null) {
            if (!allowVisitorBrowsing) {
                SessionUtil.storeFlashError(request, "Visitor browsing is currently disabled. Please log in to continue.");
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
            response.sendRedirect(request.getContextPath() + "/vacancies");
            return;
        }

        if (currentUser.getRole() == UserRole.ADMIN) {
            response.sendRedirect(request.getContextPath() + "/admin/config");
            return;
        }
        if (currentUser.getRole() == UserRole.MO) {
            response.sendRedirect(request.getContextPath() + "/mo/applicants");
            return;
        }
        response.sendRedirect(request.getContextPath() + "/vacancies");
    }
}