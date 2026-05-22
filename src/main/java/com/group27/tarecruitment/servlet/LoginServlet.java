package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.service.AuthService;
import com.group27.tarecruitment.util.SessionUtil;
import com.group27.tarecruitment.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * LoginServlet class type.
 *
 * <p>Servlet/controller type that handles HTTP input, output, and endpoint orchestration.</p>
 * <p>Package: {@code com.group27.tarecruitment.servlet}</p>
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final AuthService authService = new AuthService();

    /**
     * Handles the primary HTTP/filter entrypoint workflow for this operation.
     * @param request input parameter of type {@code HttpServletRequest}.
     * @param response input parameter of type {@code HttpServletResponse}.
     * @throws ServletException if this operation cannot complete successfully.
     * @throws IOException if this operation cannot complete successfully.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String returnTo = request.getAttribute("returnTo") == null
                ? sanitizeReturnTo(request.getParameter("returnTo"))
                : sanitizeReturnTo(request.getAttribute("returnTo").toString());
        request.setAttribute("flashMessage", SessionUtil.consumeFlashMessage(request));
        request.setAttribute("flashError", SessionUtil.consumeFlashError(request));
        request.setAttribute("loginTitle", "Applicant Log In");
        request.setAttribute("loginSubtitle", "Use this page for applicant accounts to browse jobs, apply, and manage profile details.");
        request.setAttribute("submitLabel", "Log In");
        request.setAttribute("formAction", request.getContextPath() + "/login");
        request.setAttribute("returnTo", returnTo);
        request.setAttribute("backHref", request.getContextPath() + "/home");
        request.setAttribute("backLabel", "Back to jobs");
        request.setAttribute("altLoginHref", request.getContextPath() + "/staff/login");
        request.setAttribute("altLoginLabel", "Staff login");
        request.setAttribute("loginVariant", "applicant");
        request.setAttribute("loginAudience", "Applicant access");
        request.setAttribute("loginNotice", "MO and Admin accounts are not allowed on this page. Use the staff login page instead.");
        request.getRequestDispatcher("/WEB-INF/views/common/login.jsp").forward(request, response);
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
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String returnTo = sanitizeReturnTo(request.getParameter("returnTo"));
        request.setAttribute("returnTo", returnTo);

        if (ValidationUtil.isBlank(username) || ValidationUtil.isBlank(password)) {
            request.setAttribute("errorMessage", "Please enter both username and password.");
            doGet(request, response);
            return;
        }

        Optional<UserAccount> authenticatedUser = authService.authenticate(username, password);
        if (authenticatedUser.isEmpty()) {
            request.setAttribute("errorMessage", "Invalid username or password.");
            doGet(request, response);
            return;
        }

        UserAccount user = authenticatedUser.get();
        if (user.getRole() != UserRole.APPLICANT) {
            request.setAttribute("errorMessage", "Staff accounts must use the staff login page.");
            doGet(request, response);
            return;
        }

        SessionUtil.storeUser(request, user);
        if (!ValidationUtil.isBlank(returnTo)) {
            response.sendRedirect(request.getContextPath() + returnTo);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/vacancies");
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param returnTo input parameter of type {@code String}.
     * @return the computed `String` value for this operation.
     */
    private String sanitizeReturnTo(String returnTo) {
        if (ValidationUtil.isBlank(returnTo)) {
            return null;
        }
        String trimmed = returnTo.trim();
        if (!trimmed.startsWith("/")) {
            return null;
        }
        if (trimmed.startsWith("//") || trimmed.contains("://")) {
            return null;
        }
        return trimmed;
    }
}
