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
        request.setAttribute("loginTitle", "Applicant / Organiser Log In");
        request.setAttribute("loginSubtitle", "Use this window for applicant and organiser accounts when you want to browse course jobs, apply, update your profile, or review applicants.");
        request.setAttribute("submitLabel", "Log In");
        request.setAttribute("formAction", request.getContextPath() + "/login");
        request.setAttribute("backHref", request.getContextPath() + "/home");
        request.setAttribute("backLabel", "Back to jobs");
        request.setAttribute("altLoginHref", request.getContextPath() + "/admin/login");
        request.setAttribute("altLoginLabel", "Admin login");
        request.setAttribute("loginVariant", "applicant");
        request.setAttribute("loginAudience", "Applicant / Organiser access");
        request.setAttribute("loginNotice", "Administrator accounts are not allowed on this page. Use the dedicated admin login window instead.");
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
        if (user.getRole() == UserRole.ADMIN) {
            request.setAttribute("errorMessage", "Administrator accounts must use the dedicated admin login page.");
            doGet(request, response);
            return;
        }

        SessionUtil.storeUser(request, user);
        if (user.getRole() == UserRole.APPLICANT) {
            response.sendRedirect(request.getContextPath() + "/vacancies");
            return;
        }
        if (user.getRole() == UserRole.MO) {
            response.sendRedirect(request.getContextPath() + "/mo/applicants");
            return;
        }
        response.sendRedirect(request.getContextPath() + "/vacancies");
    }
}