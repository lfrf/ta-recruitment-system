package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.model.UserAccount;
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

        SessionUtil.storeUser(request, authenticatedUser.get());
        response.sendRedirect(request.getContextPath() + "/vacancies");
    }
}