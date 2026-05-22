package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.service.ApplicationService;
import com.group27.tarecruitment.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * ApplicantDecisionReadServlet class type.
 *
 * <p>Servlet/controller type that handles HTTP input, output, and endpoint orchestration.</p>
 * <p>Package: {@code com.group27.tarecruitment.servlet}</p>
 */
@WebServlet("/applicant/status/read")
public class ApplicantDecisionReadServlet extends HttpServlet {
    private final ApplicationService applicationService = new ApplicationService();

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
            SessionUtil.storeFlashError(request, "Please log in as an applicant first.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int updated = applicationService.markAllDecisionsAsRead(currentUser.getUserId());
        if (updated > 0) {
            SessionUtil.storeFlashMessage(request, updated + " decision notification"
                    + (updated == 1 ? " was " : "s were ") + "marked as read.");
        } else {
            SessionUtil.storeFlashMessage(request, "No unread decision notifications.");
        }
        response.sendRedirect(request.getContextPath() + "/applicant/status");
    }
}
