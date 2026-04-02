package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.service.ReviewService;
import com.group27.tarecruitment.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/mo/review")
public class ReviewDecisionServlet extends HttpServlet {
    private final ReviewService reviewService = new ReviewService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserAccount currentUser = SessionUtil.getCurrentUser(request);
        if (currentUser == null) {
            SessionUtil.storeFlashError(request, "Please log in before reviewing applicant records.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if (currentUser.getRole() != UserRole.MO) {
            SessionUtil.storeFlashError(request, "Only organiser accounts can update review decisions.");
            response.sendRedirect(request.getContextPath() + "/vacancies");
            return;
        }

        String vacancyId = request.getParameter("vacancyId");
        String applicationId = request.getParameter("applicationId");
        String decision = request.getParameter("decision");
        String reviewNote = request.getParameter("reviewNote");
        String optionalFeedback = request.getParameter("optionalFeedback");

        String error = reviewService.updateDecision(currentUser, vacancyId, applicationId, decision, reviewNote, optionalFeedback);
        if (error != null) {
            SessionUtil.storeFlashError(request, error);
        } else {
            SessionUtil.storeFlashMessage(request, "Application decision updated successfully.");
        }

        response.sendRedirect(request.getContextPath() + "/mo/applicants?vacancyId=" + vacancyId);
    }
}
