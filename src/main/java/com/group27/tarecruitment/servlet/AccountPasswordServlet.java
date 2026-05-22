package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.service.AccountSecurityService;
import com.group27.tarecruitment.util.SessionUtil;
import com.group27.tarecruitment.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * AccountPasswordServlet class type.
 *
 * <p>Servlet/controller type that handles HTTP input, output, and endpoint orchestration.</p>
 * <p>Package: {@code com.group27.tarecruitment.servlet}</p>
 */
@WebServlet("/account/password")
public class AccountPasswordServlet extends HttpServlet {
    private final AccountSecurityService accountSecurityService = new AccountSecurityService();

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
            SessionUtil.storeFlashError(request, "Please log in first.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.setAttribute("flashMessage", SessionUtil.consumeFlashMessage(request));
        request.setAttribute("flashError", SessionUtil.consumeFlashError(request));
        request.setAttribute("currentUser", currentUser);
        request.setAttribute("backHref", resolveBackHref(request, currentUser));
        request.getRequestDispatcher("/WEB-INF/views/common/change-password.jsp").forward(request, response);
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
            SessionUtil.storeFlashError(request, "Please log in first.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (ValidationUtil.isBlank(currentPassword)
                || ValidationUtil.isBlank(newPassword)
                || ValidationUtil.isBlank(confirmPassword)) {
            request.setAttribute("errorMessage", "Please complete all password fields.");
            doGet(request, response);
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "New password and confirmation do not match.");
            doGet(request, response);
            return;
        }

        AccountSecurityService.ChangePasswordResult result = accountSecurityService.changePassword(
                currentUser.getUserId(),
                currentPassword,
                newPassword
        );
        if (!result.isOk()) {
            request.setAttribute("errorMessage", result.getMessage());
            doGet(request, response);
            return;
        }

        SessionUtil.storeUser(request, result.getUser());
        SessionUtil.storeFlashMessage(request, "Password updated successfully.");
        response.sendRedirect(request.getContextPath() + "/account/password");
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param request input parameter of type {@code HttpServletRequest}.
     * @param user input parameter of type {@code UserAccount}.
     * @return the computed `String` value for this operation.
     */
    private String resolveBackHref(HttpServletRequest request, UserAccount user) {
        if (user == null || user.getRole() == null) {
            return request.getContextPath() + "/vacancies";
        }
        if (user.getRole() == UserRole.ADMIN) {
            return request.getContextPath() + "/admin/config";
        }
        if (user.getRole() == UserRole.MO) {
            return request.getContextPath() + "/mo/applicants";
        }
        return request.getContextPath() + "/applicant/profile";
    }
}
