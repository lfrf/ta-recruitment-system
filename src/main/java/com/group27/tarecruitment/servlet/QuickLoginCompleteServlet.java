package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.repository.UserRepository;
import com.group27.tarecruitment.service.QuickLoginRequestService;
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
 * QuickLoginCompleteServlet class type.
 *
 * <p>Servlet/controller type that handles HTTP input, output, and endpoint orchestration.</p>
 * <p>Package: {@code com.group27.tarecruitment.servlet}</p>
 */
@WebServlet("/quick-login/complete")
public class QuickLoginCompleteServlet extends HttpServlet {
    private final QuickLoginRequestService quickLoginRequestService = new QuickLoginRequestService();
    private final UserRepository userRepository = new UserRepository();

    /**
     * Handles the primary HTTP/filter entrypoint workflow for this operation.
     * @param request input parameter of type {@code HttpServletRequest}.
     * @param response input parameter of type {@code HttpServletResponse}.
     * @throws ServletException if this operation cannot complete successfully.
     * @throws IOException if this operation cannot complete successfully.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestId = ValidationUtil.trimToEmpty(request.getParameter("request"));
        if (ValidationUtil.isBlank(requestId)) {
            writeJson(response, HttpServletResponse.SC_BAD_REQUEST, "{\"status\":\"INVALID_REQUEST\"}");
            return;
        }

        Optional<String> confirmedUserId = quickLoginRequestService.consumeConfirmedRequest(requestId);
        if (confirmedUserId.isEmpty()) {
            writeJson(response, HttpServletResponse.SC_CONFLICT, "{\"status\":\"NOT_READY\"}");
            return;
        }

        Optional<UserAccount> userAccount = userRepository.findByUserId(confirmedUserId.get())
                .filter(UserAccount::isActive)
                .filter(user -> user.getRole() == UserRole.APPLICANT);
        if (userAccount.isEmpty()) {
            writeJson(response, HttpServletResponse.SC_FORBIDDEN, "{\"status\":\"INVALID_ACCOUNT\"}");
            return;
        }

        SessionUtil.storeUser(request, userAccount.get());
        String redirect = request.getContextPath() + "/vacancies";
        writeJson(response, HttpServletResponse.SC_OK, "{\"status\":\"SUCCESS\",\"redirect\":\"" + escapeJson(redirect) + "\"}");
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param response input parameter of type {@code HttpServletResponse}.
     * @param statusCode input parameter of type {@code int}.
     * @param body input parameter of type {@code String}.
     * @throws IOException if this operation cannot complete successfully.
     */
    private void writeJson(HttpServletResponse response, int statusCode, String body) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(body);
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param value input parameter of type {@code String}.
     * @return the computed `String` value for this operation.
     */
    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}
