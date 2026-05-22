package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.service.ReviewService;
import com.group27.tarecruitment.util.SessionUtil;
import com.group27.tarecruitment.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ReviewDecisionServlet class type.
 *
 * <p>Servlet/controller type that handles HTTP input, output, and endpoint orchestration.</p>
 * <p>Package: {@code com.group27.tarecruitment.servlet}</p>
 */
@WebServlet("/mo/review")
public class ReviewDecisionServlet extends HttpServlet {
    private final ReviewService reviewService = new ReviewService();

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
            SessionUtil.storeFlashError(request, "Please use the staff login page before reviewing applicant records.");
            response.sendRedirect(request.getContextPath() + "/staff/login");
            return;
        }
        if (currentUser.getRole() != UserRole.MO) {
            SessionUtil.storeFlashError(request, "Only organiser accounts can update review decisions.");
            response.sendRedirect(request.getContextPath() + "/vacancies");
            return;
        }

        String vacancyId = ValidationUtil.trimToEmpty(request.getParameter("vacancyId"));
        String applicationId = ValidationUtil.trimToEmpty(request.getParameter("applicationId"));
        String decision = ValidationUtil.trimToEmpty(request.getParameter("decision"));
        String reviewNote = ValidationUtil.trimToEmpty(request.getParameter("reviewNote"));
        String optionalFeedback = ValidationUtil.trimToEmpty(request.getParameter("optionalFeedback"));
        String orderMode = ValidationUtil.trimToEmpty(request.getParameter("orderMode"));
        boolean appointLeadTa = request.getParameter("appointLeadTa") != null;

        String error = reviewService.updateDecision(currentUser, vacancyId, applicationId, decision, reviewNote, optionalFeedback, appointLeadTa);
        if (error != null) {
            SessionUtil.storeFlashError(request, error);
        } else {
            SessionUtil.storeFlashMessage(request, "Application decision updated successfully.");
        }

        StringBuilder redirectUrl = new StringBuilder(request.getContextPath())
                .append("/mo/applicants?vacancyId=")
                .append(vacancyId);
        if ("ai".equalsIgnoreCase(orderMode)) {
            redirectUrl.append("&orderMode=ai");
        }
        response.sendRedirect(redirectUrl.toString());
    }
}
