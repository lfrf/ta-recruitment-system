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

@WebServlet({"/staff/login", "/admin/login"})
public class AdminLoginServlet extends HttpServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserAccount currentUser = SessionUtil.getCurrentUser(request);
        if (currentUser != null) {
            if (currentUser.getRole() == UserRole.ADMIN) {
                response.sendRedirect(request.getContextPath() + "/admin/config");
                return;
            }
            if (currentUser.getRole() == UserRole.MO) {
                response.sendRedirect(request.getContextPath() + "/mo/applicants");
                return;
            }
        }

        request.setAttribute("flashMessage", SessionUtil.consumeFlashMessage(request));
        request.setAttribute("flashError", SessionUtil.consumeFlashError(request));
        request.setAttribute("loginTitle", "Staff Log In");
        request.setAttribute("loginSubtitle", "Use this page for MO and Admin accounts.");
        request.setAttribute("submitLabel", "Staff Log In");
        request.setAttribute("formAction", request.getContextPath() + "/staff/login");
        request.setAttribute("backHref", request.getContextPath() + "/home");
        request.setAttribute("backLabel", "Back to vacancies");
        request.setAttribute("altLoginHref", request.getContextPath() + "/login");
        request.setAttribute("altLoginLabel", "Applicant login");
        request.setAttribute("loginVariant", "staff");
        request.setAttribute("loginAudience", "Staff access");
        request.setAttribute("loginNotice", "Applicant accounts should use the applicant login page. This page is for MO and Admin accounts.");
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
        if (user.getRole() == UserRole.APPLICANT) {
            request.setAttribute("errorMessage", "Applicant accounts must use the applicant login page.");
            doGet(request, response);
            return;
        }

        SessionUtil.storeUser(request, user);
        if (user.getRole() == UserRole.ADMIN) {
            response.sendRedirect(request.getContextPath() + "/admin/config");
            return;
        }
        response.sendRedirect(request.getContextPath() + "/mo/applicants");
    }
}
