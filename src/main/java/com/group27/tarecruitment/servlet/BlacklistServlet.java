package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.model.ApplicantProfile;
import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.repository.ApplicantProfileRepository;
import com.group27.tarecruitment.repository.UserRepository;
import com.group27.tarecruitment.service.AdminService;
import com.group27.tarecruitment.util.SessionUtil;
import com.group27.tarecruitment.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * BlacklistServlet class type.
 *
 * <p>Servlet/controller type that handles HTTP input, output, and endpoint orchestration.</p>
 * <p>Package: {@code com.group27.tarecruitment.servlet}</p>
 */
@WebServlet("/admin/blacklist")
public class BlacklistServlet extends HttpServlet {
    private final AdminService adminService = new AdminService();
    private final ApplicantProfileRepository applicantProfileRepository = new ApplicantProfileRepository();
    private final UserRepository userRepository = new UserRepository();

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
            SessionUtil.storeFlashError(request, "Please use the staff login page before managing the blacklist.");
            response.sendRedirect(request.getContextPath() + "/staff/login");
            return;
        }
        if (currentUser.getRole() != UserRole.ADMIN) {
            SessionUtil.storeFlashError(request, "Only admin accounts can manage the blacklist.");
            response.sendRedirect(request.getContextPath() + "/staff/login");
            return;
        }

        Map<String, ApplicantProfile> profileByApplicantId = new LinkedHashMap<>();
        for (ApplicantProfile profile : applicantProfileRepository.findAll()) {
            profileByApplicantId.put(profile.getApplicantId(), profile);
        }

        Map<String, UserAccount> userByApplicantId = new LinkedHashMap<>();
        for (UserAccount user : userRepository.findAll()) {
            userByApplicantId.put(user.getUserId(), user);
        }

        request.setAttribute("currentUser", currentUser);
        request.setAttribute("summaries", adminService.getBlacklistSummaries());
        request.setAttribute("applicants", adminService.findApplicantAccounts());
        request.setAttribute("profileByApplicantId", profileByApplicantId);
        request.setAttribute("userByApplicantId", userByApplicantId);
        request.setAttribute("flashMessage", SessionUtil.consumeFlashMessage(request));
        request.setAttribute("flashError", SessionUtil.consumeFlashError(request));
        request.getRequestDispatcher("/WEB-INF/views/admin/blacklist.jsp").forward(request, response);
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
            SessionUtil.storeFlashError(request, "Only admin accounts can update the blacklist. Please use the staff login page.");
            response.sendRedirect(request.getContextPath() + "/staff/login");
            return;
        }

        String action = ValidationUtil.trimToEmpty(request.getParameter("action"));
        String error;
        if ("deactivate".equalsIgnoreCase(action)) {
            error = adminService.deactivateBlacklistEntry(request.getParameter("entryId"));
            if (error == null) {
                SessionUtil.storeFlashMessage(request, "Blacklist entry deactivated.");
            }
        } else {
            boolean confirmed = "on".equalsIgnoreCase(request.getParameter("confirmSelection"))
                    || "true".equalsIgnoreCase(request.getParameter("confirmSelection"));
            error = adminService.addBlacklistEntry(
                    currentUser,
                    request.getParameter("applicantId"),
                    request.getParameter("reason"),
                    confirmed
            );
            if (error == null) {
                SessionUtil.storeFlashMessage(request, "Blacklist entry added.");
            }
        }

        if (error != null) {
            SessionUtil.storeFlashError(request, error);
        }

        response.sendRedirect(request.getContextPath() + "/admin/blacklist");
    }
}
