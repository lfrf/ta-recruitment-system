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

@WebServlet("/admin/login")
public class AdminLoginServlet extends HttpServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserAccount currentUser = SessionUtil.getCurrentUser(request);
        if (currentUser != null && currentUser.getRole() == UserRole.ADMIN) {
            response.sendRedirect(request.getContextPath() + "/admin/config");
            return;
        }

        request.setAttribute("flashMessage", SessionUtil.consumeFlashMessage(request));
        request.setAttribute("flashError", SessionUtil.consumeFlashError(request));
        request.setAttribute("loginTitle", "Admin Log In");
        request.setAttribute("loginSubtitle", "Use the dedicated admin login window to manage configuration, workload, and blacklist controls.");
        request.setAttribute("submitLabel", "Admin Log In");
        request.setAttribute("formAction", request.getContextPath() + "/admin/login");
        request.setAttribute("backHref", request.getContextPath() + "/home");
        request.setAttribute("backLabel", "Back to vacancies");
        request.setAttribute("altLoginHref", request.getContextPath() + "/login");
        request.setAttribute("altLoginLabel", "Applicant / organiser login");
        request.setAttribute("loginVariant", "admin");
        request.setAttribute("loginAudience", "Administrator access only");
        request.setAttribute("loginNotice", "Applicant and organiser accounts should not use this page. This window is reserved for admin-only actions.");
        request.getRequestDispatcher("/WEB-INF/views/common/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

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
        if (user.getRole() != UserRole.ADMIN) {
            request.setAttribute("errorMessage", "This login page is reserved for administrator accounts only.");
            doGet(request, response);
            return;
        }

        SessionUtil.storeUser(request, user);
        response.sendRedirect(request.getContextPath() + "/admin/config");
    }
}