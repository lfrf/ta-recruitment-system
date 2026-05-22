package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.service.QuickLoginBindingService;
import com.group27.tarecruitment.util.SessionUtil;
import com.group27.tarecruitment.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ApplicantQuickLoginBindingServlet class type.
 *
 * <p>Servlet/controller type that handles HTTP input, output, and endpoint orchestration.</p>
 * <p>Package: {@code com.group27.tarecruitment.servlet}</p>
 */
@WebServlet("/applicant/quick-login-binding")
public class ApplicantQuickLoginBindingServlet extends HttpServlet {
    private final QuickLoginBindingService quickLoginBindingService = new QuickLoginBindingService();

    /**
     * Handles the primary HTTP/filter entrypoint workflow for this operation.
     * @param request input parameter of type {@code HttpServletRequest}.
     * @param response input parameter of type {@code HttpServletResponse}.
     * @throws ServletException if this operation cannot complete successfully.
     * @throws IOException if this operation cannot complete successfully.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/applicant/profile");
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
        if (currentUser == null || currentUser.getRole() != UserRole.APPLICANT) {
            SessionUtil.storeFlashError(request, "Please log in as an applicant before managing quick login binding.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = ValidationUtil.trimToEmpty(request.getParameter("action"));
        if ("unbind".equalsIgnoreCase(action)) {
            quickLoginBindingService.unbind(currentUser.getUserId());
            SessionUtil.storeFlashMessage(request, "Quick login binding has been removed for this account.");
            response.sendRedirect(request.getContextPath() + "/applicant/profile");
            return;
        }

        SessionUtil.storeFlashMessage(request, "Use the QR binding button in My Profile to bind your phone.");
        response.sendRedirect(request.getContextPath() + "/applicant/profile");
    }
}
