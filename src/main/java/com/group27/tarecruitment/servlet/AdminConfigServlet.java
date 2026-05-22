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

/**
 * AdminConfigServlet class type.
 *
 * <p>Servlet/controller type that handles HTTP input, output, and endpoint orchestration.</p>
 * <p>Package: {@code com.group27.tarecruitment.servlet}</p>
 */
@WebServlet("/admin/config")
public class AdminConfigServlet extends HttpServlet {
    private final AdminService adminService = new AdminService();

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
            SessionUtil.storeFlashError(request, "Please use the staff login page before managing admin configuration.");
            response.sendRedirect(request.getContextPath() + "/staff/login");
            return;
        }
        if (currentUser.getRole() != UserRole.ADMIN) {
            SessionUtil.storeFlashError(request, "Only admin accounts can access configuration settings.");
            response.sendRedirect(request.getContextPath() + "/staff/login");
            return;
        }

        request.setAttribute("currentUser", currentUser);
        request.setAttribute("config", adminService.getConfig());
        request.setAttribute("flashMessage", SessionUtil.consumeFlashMessage(request));
        request.setAttribute("flashError", SessionUtil.consumeFlashError(request));
        request.getRequestDispatcher("/WEB-INF/views/admin/config.jsp").forward(request, response);
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
        if (currentUser == null || currentUser.getRole() != UserRole.ADMIN) {
            SessionUtil.storeFlashError(request, "Only admin accounts can update configuration. Please use the staff login page.");
            response.sendRedirect(request.getContextPath() + "/staff/login");
            return;
        }

        String error = adminService.updateConfig(request.getParameter("maxWorkload"), request.getParameter("allowVisitorBrowsing") != null);
        if (error != null) {
            SessionUtil.storeFlashError(request, error);
        } else {
            SessionUtil.storeFlashMessage(request, "Admin configuration updated successfully.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/config");
    }
}
