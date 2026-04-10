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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("flashMessage", SessionUtil.consumeFlashMessage(request));
        request.setAttribute("flashError", SessionUtil.consumeFlashError(request));
        request.setAttribute("loginTitle", "Applicant Log In");
        request.setAttribute("loginSubtitle", "Use this page for applicant accounts to browse jobs, apply, and manage profile details.");
        request.setAttribute("submitLabel", "Log In");
        request.setAttribute("formAction", request.getContextPath() + "/login");
        request.setAttribute("backHref", request.getContextPath() + "/home");
        request.setAttribute("backLabel", "Back to jobs");
        request.setAttribute("altLoginHref", request.getContextPath() + "/staff/login");
        request.setAttribute("altLoginLabel", "Staff login");
        request.setAttribute("loginVariant", "applicant");
        request.setAttribute("loginAudience", "Applicant access");
        request.setAttribute("loginNotice", "MO and Admin accounts are not allowed on this page. Use the staff login page instead.");
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
        if (user.getRole() != UserRole.APPLICANT) {
            request.setAttribute("errorMessage", "Staff accounts must use the staff login page.");
            doGet(request, response);
            return;
        }

        SessionUtil.storeUser(request, user);
        response.sendRedirect(request.getContextPath() + "/vacancies");
    }
}
