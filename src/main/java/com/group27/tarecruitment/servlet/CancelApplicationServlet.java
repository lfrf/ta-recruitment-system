package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.service.ApplicationService;
import com.group27.tarecruitment.util.SessionUtil;
import com.group27.tarecruitment.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/applicant/cancel")
public class CancelApplicationServlet extends HttpServlet {
    private final ApplicationService applicationService = new ApplicationService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserAccount currentUser = SessionUtil.getCurrentUser(request);
        if (currentUser == null) {
            SessionUtil.storeFlashError(request, "Please log in before cancelling an application.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if (currentUser.getRole() != UserRole.APPLICANT) {
            SessionUtil.storeFlashError(request, "Only applicant accounts can cancel applications.");
            response.sendRedirect(request.getContextPath() + "/vacancies");
            return;
        }

        String applicationId = ValidationUtil.trimToEmpty(request.getParameter("applicationId"));
        String error = applicationService.cancelApplication(currentUser, applicationId);
        if (error != null) {
            SessionUtil.storeFlashError(request, error);
        } else {
            SessionUtil.storeFlashMessage(request, "Application cancelled successfully. You can update your profile and apply again later.");
        }
        response.sendRedirect(request.getContextPath() + "/applicant/status");
    }
}
