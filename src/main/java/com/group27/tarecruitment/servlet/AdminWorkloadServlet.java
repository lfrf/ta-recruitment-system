package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.model.AdminConfig;
import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.service.AdminService;
import com.group27.tarecruitment.service.WorkloadService;
import com.group27.tarecruitment.util.SessionUtil;
import com.group27.tarecruitment.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/workload")
public class AdminWorkloadServlet extends HttpServlet {
    private final AdminService adminService = new AdminService();
    private final WorkloadService workloadService = new WorkloadService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserAccount currentUser = SessionUtil.getCurrentUser(request);
        if (currentUser == null) {
            SessionUtil.storeFlashError(request, "Please use the staff login page before viewing admin workload data.");
            response.sendRedirect(request.getContextPath() + "/staff/login");
            return;
        }
        if (currentUser.getRole() != UserRole.ADMIN) {
            SessionUtil.storeFlashError(request, "Only admin accounts can access workload monitoring.");
            response.sendRedirect(request.getContextPath() + "/staff/login");
            return;
        }

        AdminConfig config = adminService.getConfig();
        String applicantKeyword = ValidationUtil.trimToEmpty(request.getParameter("applicantKeyword"));
        String moduleKeyword = ValidationUtil.trimToEmpty(request.getParameter("moduleKeyword"));
        boolean flaggedOnly = request.getParameter("flaggedOnly") != null;

        request.setAttribute("currentUser", currentUser);
        request.setAttribute("config", config);
        request.setAttribute("summaries", workloadService.filterSummaries(config.getMaxWorkload(), applicantKeyword, moduleKeyword, flaggedOnly));
        request.setAttribute("applicantKeyword", applicantKeyword);
        request.setAttribute("moduleKeyword", moduleKeyword);
        request.setAttribute("flaggedOnly", flaggedOnly);
        request.setAttribute("flashMessage", SessionUtil.consumeFlashMessage(request));
        request.setAttribute("flashError", SessionUtil.consumeFlashError(request));
        request.getRequestDispatcher("/WEB-INF/views/admin/workload.jsp").forward(request, response);
    }
}
